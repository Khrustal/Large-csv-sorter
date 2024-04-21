package org.example.constants;

/**
 * The Constants class holds constant messages that are used throughout the application.
 * This class serves as a central repository for message constants, making them easy to maintain and manage.
 */
public final class Constants {

    /**
     * Usage message that explains how to use the application.
     * This message is displayed when the application is run without enough arguments.
     */
    public static final String USAGE_MESSAGE = "Usage: java org.example.Application <inputFilePath> <outputFilePath> [maxMemorySize]";

    /**
     * Message displayed when the memory size argument is invalid.
     * The application will use the default memory size if the provided argument cannot be parsed.
     */
    public static final String INVALID_MEMORY_SIZE_MESSAGE = "Invalid memory size argument. Using default memory size.";

    /**
     * Message displayed when the input file is empty.
     * If the input file is empty, the application will abort the process and display this message.
     */
    public static final String FILE_EMPTY_MESSAGE = "File seems to be empty. Aborting process";

    /**
     * Message template for displaying the input file path.
     * Uses a placeholder (%s) for the file path value.
     */
    public static final String INPUT_FILE_PATH_MESSAGE = "Input file path: %s";

    /**
     * Message template for displaying the output file path.
     * Uses a placeholder (%s) for the file path value.
     */
    public static final String OUTPUT_FILE_PATH_MESSAGE = "Output file path: %s";

    /**
     * Message template for displaying the max memory size.
     * Uses a placeholder (%s) for the memory size value.
     */
    public static final String MAX_MEMORY_SIZE_MESSAGE = "Max Memory Size: %d lines";

    // Private constructor to prevent instantiation of the Constants class
    private Constants() {
    }
}
