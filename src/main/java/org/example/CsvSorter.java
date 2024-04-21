package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.example.helper.FileHelper.mergeSortedFiles;
import static org.example.helper.FileHelper.splitAndSortInputFile;
import static org.example.helper.SorterHelper.*;

/**
 * The CsvSorter class serves as the main class to sort a CSV file using a multi-way merge approach.
 * It provides a command-line interface for specifying input and output file paths, as well as an optional memory size.
 */
public class CsvSorter {

    public static final int DEFAULT_MAX_MEMORY_SIZE = 10000;

    /**
     * The main method serves as the entry point for the CSV sorting application.
     * It takes the file paths as arguments, along with an optional memory size for sorting, and performs the sorting.
     *
     * @param args the command-line arguments specifying file paths and optional memory size.
     * @throws IOException if an I/O error occurs during file handling.
     */
    public static void main(String[] args) throws IOException {
        String inputFilePath; // Path to the input CSV file
        String outputFilePath; // Path to the output sorted CSV file
        int maxMemorySize = DEFAULT_MAX_MEMORY_SIZE; // Maximum number of lines that can be held in memory for sorting

        // Check if the input and output file paths are specified
        if (args.length < 2) {
            System.out.println("No file paths specified");
            return;
        }
        inputFilePath = args[0];
        outputFilePath = args[1];
        // Optional argument for specifying max memory size
        if (args.length >= 3) {
            Integer t = null;
            try {
                t = Integer.parseInt(args[2]);
            } catch (Exception e) {
                System.out.println("Couldn't parse memory size argument, setting default");
            }
            if (t != null) {
                maxMemorySize = t;
            }
        }

        System.out.println("Input file path: " + inputFilePath);
        System.out.println("Output file path: " + outputFilePath);
        System.out.println("Max Memory Size: " + maxMemorySize + " lines");

        // Read the header from the input file
        String header = getHeader(inputFilePath);
        if (header == null || header.isEmpty()) {
            System.out.println("File seems to be empty. Aborting process");
            return;
        }

        // Split and sort the input file
        List<Path> tempFiles = splitAndSortInputFile(inputFilePath, maxMemorySize);
        // Merge sorted temporary files into the final output file
        mergeSortedFiles(tempFiles, header, outputFilePath);
        // Delete temporary files
        deleteTempFiles(tempFiles);
    }
}
