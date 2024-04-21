package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

// Класс работы с файлами и сортировки записей
public class FileHelper {

    // Чтение файла и разбиение на чанки, сортировка и сохранение во временные файлы
    public static List<Path> splitAndSortInputFile(String inputFilePath, int maxMemorySize) throws IOException {
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

    // Сортировка и записть во временные файлы
    public static void sortChunk(List<Path> tempFiles, List<String> chunk) throws IOException {
        chunk.sort(Comparator.comparingInt(SorterHelper::getFirstFieldAsInt));
        Path tempFilePath = Files.createTempFile("sorted_chunk_", ".csv");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFilePath.toFile()))) {
            for (String sortedLine : chunk) {
                writer.write(sortedLine);
                writer.newLine();
            }
        }
        tempFiles.add(tempFilePath);
    }

    // Многопутевое слияние временных файлов в конечный отсортированный файл
    public static void mergeSortedFiles(List<Path> tempFiles, String header, String outputFilePath) throws IOException {
        PriorityQueue<LineReader> pq = new PriorityQueue<>(Comparator.comparingInt(LineReader::getFirstField));
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {

            // Возвращаем заголовок
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
                writer.write(minLineReader.getCurrentLine());
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
}
