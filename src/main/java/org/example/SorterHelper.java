package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Класс с небольшими вспомогательными функциями
public class SorterHelper {

    //Читаем заголовок из файла, в конце ставим его обратно
    public static String getHeader(String inputFilePath) throws IOException {
        String header = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            header = reader.readLine();
        }
        return header;
    }

    // Функция для извлечения первого поля в виде целого числа
    public static int getFirstFieldAsInt(String line) {
        return Integer.parseInt(line.split(",")[0]);
    }

    // Удаляет временные файлы
    public static void deleteTempFiles(List<Path> tempFiles) throws IOException {
        for (Path tempFilePath : tempFiles) {
            Files.deleteIfExists(tempFilePath);
        }
    }
}
