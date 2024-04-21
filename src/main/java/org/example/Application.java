package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.example.helper.FileHelper.mergeSortedFiles;
import static org.example.helper.FileHelper.splitAndSortInputFile;
import static org.example.helper.SorterHelper.*;
import static org.example.constants.Constants.*;

/**
 * The Application class serves as the main class to sort a CSV file using a multi-way merge approach.
 * It provides a command-line interface for specifying input and output file paths, as well as an optional memory size.
 */
public class Application {

    private static final int DEFAULT_MAX_MEMORY_SIZE = 10000;

    /**
     * The main method serves as the entry point for the CSV sorting application.
     * It takes the file paths as arguments, along with an optional memory size for sorting, and performs the sorting.
     *
     * @param args the command-line arguments specifying file paths and optional memory size.
     * @throws IOException if an I/O error occurs during file handling.
     */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println(USAGE_MESSAGE);
            return;
        }

        String inputFilePath = args[0];
        String outputFilePath = args[1];
        int maxMemorySize = parseMaxMemorySize(args);

        processFiles(inputFilePath, outputFilePath, maxMemorySize);
    }

    /**
     * Parses the maximum memory size from command-line arguments.
     * @param args the command-line arguments.
     * @return the maximum memory size for sorting.
     */
    private static int parseMaxMemorySize(String[] args) {
        int maxMemorySize = DEFAULT_MAX_MEMORY_SIZE;
        if (args.length >= 3) {
            try {
                maxMemorySize = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.out.println(INVALID_MEMORY_SIZE_MESSAGE);
            }
        }
        return maxMemorySize;
    }

    /**
     * Processes the input and output files, performing sorting operations.
     *
     * @param inputFilePath the path to the input CSV file.
     * @param outputFilePath the path to the output sorted CSV file.
     * @param maxMemorySize the maximum memory size for sorting.
     * @throws IOException if an I/O error occurs during file handling.
     */
    private static void processFiles(String inputFilePath, String outputFilePath, int maxMemorySize) throws IOException {
        // Output the input file path
        System.out.println(String.format(INPUT_FILE_PATH_MESSAGE, inputFilePath));

        // Output the output file path
        System.out.println(String.format(OUTPUT_FILE_PATH_MESSAGE, outputFilePath));

        // Output the max memory size
        System.out.println(String.format(MAX_MEMORY_SIZE_MESSAGE, maxMemorySize));

        String header = getHeader(inputFilePath);
        if (header == null || header.isEmpty()) {
            System.out.println(FILE_EMPTY_MESSAGE);
            return;
        }

        List<Path> tempFiles = splitAndSortInputFile(inputFilePath, maxMemorySize);
        mergeSortedFiles(tempFiles, header, outputFilePath);
        deleteTempFiles(tempFiles);
    }
}
