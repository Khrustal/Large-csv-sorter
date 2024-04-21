package org.example.helper;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * The SorterHelper class provides small helper functions for file sorting and handling operations.
 */
public class SorterHelper {

    /**
     * Reads the header from a file and returns it as a string.
     *
     * @param inputFilePath the path of the input file to read the header from.
     * @return the header of the file as a string.
     * @throws IOException if an I/O error occurs while reading the file.
     */
    public static String getHeader(String inputFilePath) throws IOException {
        String header = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            header = reader.readLine();
        }
        return header;
    }

    /**
     * Extracts the first field from a line as an integer.
     *
     * @param line the line of text to extract the first field from.
     * @return the first field of the line as an integer.
     */
    public static int getFirstFieldAsInt(String line) {
        return Integer.parseInt(line.split(",")[0]);
    }

    /**
     * Deletes temporary files from the file system.
     *
     * @param tempFiles the list of paths to the temporary files to be deleted.
     * @throws IOException if an I/O error occurs while deleting the files.
     */
    public static void deleteTempFiles(List<Path> tempFiles) throws IOException {
        for (Path tempFilePath : tempFiles) {
            Files.deleteIfExists(tempFilePath);
        }
    }
}
