package org.example.helper;

import org.example.utility.LineReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The FileHelper class provides methods for file handling and sorting of records.
 * It allows splitting and sorting input files, and merging temporary sorted files into a final sorted file.
 */
public class FileHelper {

    /**
     * Splits an input file into chunks, sorts them, and saves them as temporary files.
     * The input file is read line by line, ignoring the header, and split into chunks of maximum size
     * specified by the maxMemorySize parameter.
     *
     * @param inputFilePath the path of the input file to be read.
     * @param maxMemorySize the maximum size of a chunk in lines.
     * @return a list of paths to the temporary files containing sorted chunks.
     * @throws IOException if an I/O error occurs while reading or writing files.
     */
    public static List<Path> splitAndSortInputFile(String inputFilePath, int maxMemorySize) throws IOException {
        List<Path> tempFiles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            List<String> chunk = new ArrayList<>();
            String line;
            reader.readLine(); // Skip the header

            while ((line = reader.readLine()) != null) {
                chunk.add(line);
                if (chunk.size() >= maxMemorySize) {
                    // Sort and save the chunk to a temporary file
                    sortChunk(tempFiles, chunk);
                    chunk.clear();
                }
            }

            // Sort and save any remaining chunk if it exists
            if (!chunk.isEmpty()) {
                sortChunk(tempFiles, chunk);
            }

        }
        return tempFiles;
    }

    /**
     * Sorts a chunk of lines and saves them to a temporary file.
     * The lines in the chunk are sorted based on the first field and then saved to a temporary file.
     *
     * @param tempFiles the list to which the path of the temporary file will be added.
     * @param chunk the list of lines to be sorted and saved.
     * @throws IOException if an I/O error occurs while writing to the file.
     */
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

    /**
     * Merges temporary sorted files into a final sorted file using a multi-way merge approach.
     * The temporary files are opened and added to a priority queue for merging.
     *
     * @param tempFiles the list of paths to the temporary sorted files.
     * @param header the header to be written to the output file.
     * @param outputFilePath the path of the output file to be written.
     * @throws IOException if an I/O error occurs while reading or writing files.
     */
    public static void mergeSortedFiles(List<Path> tempFiles, String header, String outputFilePath) throws IOException {
        PriorityQueue<LineReader> pq = new PriorityQueue<>(Comparator.comparingInt(LineReader::getFirstField));

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Write the header to the output file
            writer.write(header);
            writer.newLine();
            // Open all temporary files and add them to the priority queue
            for (Path tempFilePath : tempFiles) {
                LineReader lineReader = new LineReader(tempFilePath);
                if (lineReader.readLine()) {
                    pq.add(lineReader);
                }
            }
            // Merge all temporary files
            while (!pq.isEmpty()) {
                LineReader minLineReader = pq.poll();
                writer.write(minLineReader.getCurrentLine());
                writer.newLine();
                if (minLineReader.readLine()) {
                    pq.add(minLineReader);
                } else {
                    // Close the file if there are no more lines
                    minLineReader.close();
                }
            }
        }
    }
}
