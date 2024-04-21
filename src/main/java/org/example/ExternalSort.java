package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class ExternalSort {

    public static void main(String[] args) throws IOException {
        String inputFilePath; // Путь к исходному CSV-файлу
        String outputFilePath; // Путь к выходному отсортированному CSV-файлу
        int maxMemorySize = 10000; // Максимальное количество строк, которое можно держать в памяти для сортировки

        if (args.length < 2) {
            System.out.println("No file paths specified");
            return;
        } else {
            inputFilePath = args[0];
            outputFilePath = args[1];
            if (args.length >= 3) {
                Integer t = null;
                try {
                    t = Integer.parseInt(args[2]);
                } catch (Exception e) {
                    System.out.println("Couldn't parse mem size argument, setting default");
                }
                if (t != null) maxMemorySize = t;
            }
        }

        System.out.println("Input file path: " + inputFilePath);
        System.out.println("Output file path: " + outputFilePath);
        System.out.println("Max Memory Size: " + maxMemorySize + " lines");

        String header = getHeader(inputFilePath);
        if (header == null || header.length() == 0) {
            System.out.println("File seems to be empty. Aborting process");
            return;
        }

        List<Path> tempFiles = splitAndSortInputFile(inputFilePath, maxMemorySize);
        mergeSortedFiles(tempFiles, header, outputFilePath);
        deleteTempFiles(tempFiles);
    }

    //Читаем заголовок из файла, в конце ставим его обратно
    private static String getHeader(String inputFilePath) throws IOException {
        String header = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            header = reader.readLine();
        }
        return header;
    }

    // Чтение файла и разбиение на чанки, сортировка и сохранение во временные файлы
    private static List<Path> splitAndSortInputFile(String inputFilePath, int maxMemorySize) throws IOException {
        List<Path> tempFiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            List<String> chunk = new ArrayList<>();
            String line;
            reader.readLine(); // Пропускаем заголовок
            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() >= maxMemorySize) {
                    // Сортируем чанк по первому полю
                    sortChunk(tempFiles, chunk);
                    chunk.clear();
                }
            }
            // Сортируем и сохраняем оставшийся чанк, если он есть
            if (!chunk.isEmpty()) {
                sortChunk(tempFiles, chunk);
            }
        }
        return tempFiles;
    }

    private static void sortChunk(List<Path> tempFiles, List<String> chunk) throws IOException {
        chunk.sort(Comparator.comparingInt(ExternalSort::getFirstFieldAsInt));
        Path tempFilePath = Files.createTempFile("sorted_chunk_", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath.toFile()))) {
            for (String sortedLine : chunk) {
                writer.write(sortedLine);
                writer.newLine();
            }
        }
        tempFiles.add(tempFilePath);
    }

    // Функция для извлечения первого поля в виде целого числа
    private static int getFirstFieldAsInt(String line) {
        return Integer.parseInt(line.split(",")[0]);
    }

    // Многопутевое слияние временных файлов в конечный отсортированный файл
    private static void mergeSortedFiles(List<Path> tempFiles, String header, String outputFilePath) throws IOException {
        PriorityQueue<LineReader> pq = new PriorityQueue<>(Comparator.comparingInt(LineReader::getFirstField));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            writer.write(header);
            writer.newLine();

            // Открываем все временные файлы и добавляем их в PriorityQueue
            for (Path tempFilePath : tempFiles) {
                LineReader lineReader = new LineReader(tempFilePath);
                if (lineReader.readLine()) {
                    pq.add(lineReader);
                }
            }
            // Слияние всех временных файлов
            while (!pq.isEmpty()) {
                LineReader minLineReader = pq.poll();
                writer.write(minLineReader.currentLine);
                writer.newLine();
                if (minLineReader.readLine()) {
                    pq.add(minLineReader);
                } else {
                    // Закрываем файл, если строк больше нет
                    minLineReader.close();
                }
            }
        }
    }

    // Удаляет временные файлы
    private static void deleteTempFiles(List<Path> tempFiles) throws IOException {
        for (Path tempFilePath : tempFiles) {
            Files.deleteIfExists(tempFilePath);
        }
    }

    // Класс для чтения строк из временного файла и добавления их в PriorityQueue
    private static class LineReader implements Closeable {
        private final BufferedReader reader;
        private String currentLine;

        public LineReader(Path tempFilePath) throws IOException {
            this.reader = new BufferedReader(new FileReader(tempFilePath.toFile()));
        }

        // Читает следующую строку и возвращает true, если удалось прочитать
        public boolean readLine() throws IOException {
            currentLine = reader.readLine();
            return currentLine != null;
        }

        // Возвращает первое поле текущей строки в виде целого числа
        public int getFirstField() {
            return getFirstFieldAsInt(currentLine);
        }

        @Override
        public void close() throws IOException {
            reader.close();
        }
    }
}
