package org.example.reader;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import static org.example.helper.SorterHelper.getFirstFieldAsInt;

/**
 * The LineReader class reads lines from a temporary file and adds them to a PriorityQueue.
 * It handles reading lines from a given file and provides access to the current line being read.
 */
public class LineReader implements Closeable {

    private final BufferedReader reader;
    private String currentLine;

    /**
     * Constructs a LineReader object to read lines from a temporary file.
     *
     * @param tempFilePath the path of the temporary file to read lines from.
     * @throws IOException if an I/O error occurs while opening the file for reading.
     */
    public LineReader(Path tempFilePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(tempFilePath.toFile()));
    }

    /**
     * Reads the next line from the file and returns true if a line was successfully read.
     * Updates the current line being read.
     *
     * @return true if a line was successfully read, false otherwise.
     * @throws IOException if an I/O error occurs while reading the line.
     */
    public boolean readLine() throws IOException {
        currentLine = reader.readLine();
        return currentLine != null;
    }

    /**
     * Returns the first field of the current line as an integer.
     *
     * @return the first field of the current line as an integer.
     */
    public int getFirstField() {
        return getFirstFieldAsInt(currentLine);
    }

    /**
     * Closes the reader and releases any system resources associated with it.
     *
     * @throws IOException if an I/O error occurs while closing the reader.
     */
    @Override
    public void close() throws IOException {
        reader.close();
    }

    /**
     * Returns the current line being read.
     *
     * @return the current line being read.
     */
    public String getCurrentLine() {
        return currentLine;
    }
}
