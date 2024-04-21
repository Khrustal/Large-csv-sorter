package org.example.test;

import static org.junit.Assert.*;

import org.example.helper.FileHelper;
import org.junit.Test;

import java.io.*;
import java.nio.file.*;
import java.util.List;

public class FileHelperTest {

    @Test
    public void testSplitAndSortInputFile() throws IOException {
        // Create a test input CSV file with a header
        Path inputFilePath = Paths.get("test_input.csv");
        try (BufferedWriter writer = Files.newBufferedWriter(inputFilePath)) {
            writer.write("Header1,Header2\n"); // Header line
            writer.write("3, data3\n1, data1\n2, data2\n");
        }

        // Call splitAndSortInputFile
        List<Path> tempFiles = FileHelper.splitAndSortInputFile(inputFilePath.toString(), 1);

        // Verify the number of temporary files created
        assertEquals(3, tempFiles.size());

        // Read and verify the contents of the first temp file
        List<String> lines1 = Files.readAllLines(tempFiles.get(0));
        assertArrayEquals(new String[]{"3, data3"}, lines1.toArray());

        // Read and verify the contents of the second temp file
        List<String> lines2 = Files.readAllLines(tempFiles.get(1));
        assertArrayEquals(new String[]{"1, data1"}, lines2.toArray());

        // Read and verify the contents of the third temp file
        List<String> lines3 = Files.readAllLines(tempFiles.get(2));
        assertArrayEquals(new String[]{"2, data2"}, lines3.toArray());

        // Clean up temporary files and input file
        Files.deleteIfExists(inputFilePath);
        for (Path tempFile : tempFiles) {
            Files.deleteIfExists(tempFile);
        }
    }

    @Test
    public void testMergeSortedFiles() throws IOException {
        // Create sample sorted temporary files with data
        Path tempFile1 = Files.createTempFile("temp1_", ".csv");
        Files.write(tempFile1, List.of("1, data1\n2, data2"));

        Path tempFile2 = Files.createTempFile("temp2_", ".csv");
        Files.write(tempFile2, List.of("3, data3\n4, data4"));

        // Create a list of temp file paths
        List<Path> tempFiles = List.of(tempFile1, tempFile2);

        // Output file path
        Path outputFilePath = Paths.get("test_output.csv");

        // Call mergeSortedFiles
        String header = "Header1,Header2";
        FileHelper.mergeSortedFiles(tempFiles, header, outputFilePath.toString());

        // Read and verify the output file contains the expected sorted data
        List<String> outputLines = Files.readAllLines(outputFilePath);
        assertEquals(header, outputLines.get(0)); // Check header
        assertArrayEquals(new String[]{"1, data1", "2, data2", "3, data3", "4, data4"}, outputLines.subList(1, outputLines.size()).toArray());

        // Clean up temporary files and output file
        Files.deleteIfExists(tempFile1);
        Files.deleteIfExists(tempFile2);
        Files.deleteIfExists(outputFilePath);
    }
}

