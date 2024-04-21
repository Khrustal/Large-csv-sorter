package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import static org.example.FileHelper.mergeSortedFiles;
import static org.example.FileHelper.splitAndSortInputFile;
import static org.example.SorterHelper.*;

public class CsvSorter {

    public static final int DEFAULT_MAX_MEMORY_SIZE = 10000;

    public static void main(String[] args) throws IOException {
        String inputFilePath; // Путь к исходному CSV-файлу
        String outputFilePath; // Путь к выходному отсортированному CSV-файлу
        int maxMemorySize = DEFAULT_MAX_MEMORY_SIZE; // Максимальное количество строк, которое можно держать в памяти для сортировки

        if (args.length < 2) {
            System.out.println("No file paths specified");
            return;
        }
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
}
