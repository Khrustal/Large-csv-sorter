# Large CSV Sorter

The CSV File Sorter is a command-line application that allows you to sort CSV files efficiently using a multi-way merge approach. The program takes an input CSV file, sorts it by first column integer value, and produces a sorted output CSV file. It also provides an option to specify the maximum memory size for sorting to optimize performance.

<h3>Program only works with CSV files that have integer values in first column!</h3>

## Features

- **Sorting**: Splits the input CSV file into chunks, sorts them, and then merges the sorted chunks into a final sorted output file.
- **Customizable Memory Usage**: You can specify the maximum memory size in lines to control the program's memory usage during sorting.
- **Command-line Interface**: The application accepts input file path, output file path, and optional memory size as command-line arguments.

## Prerequisites

- Java 8 or later

## Usage

To use the CSV File Sorter, run the following command:

```sh
java -cp path/to/jar org.example.Application <input_file_path> <output_file_path> [max_memory_size]
```

- `<input_file_path>`: The path of the input CSV file that you want to sort.
- `<output_file_path>`: The path where the sorted CSV file will be saved.
- `[max_memory_size]`: (Optional) The maximum number of lines that can be held in memory during sorting. Defaults to 10,000 lines if not specified.

## Example

For example, to sort a CSV file located at `/path/to/input.csv` and save the sorted file to `/path/to/output.csv` with a memory size of 5,000 lines, run:

```sh
java -cp path/to/jar org.example.Application /path/to/input.csv /path/to/output.csv 5000
```

## Project Structure

The project contains the following classes:

- **`org.example.helper.FileHelper`**: Provides methods for file handling and sorting of records.
- **`org.example.helper.SorterHelper`**: Contains helper functions for file operations such as reading headers, extracting first fields, and deleting temporary files.
- **`org.example.reader.LineReader`**: Handles reading lines from temporary files and adding them to a priority queue.
- **`org.example.constants.Constants`**: Contains different messages for input params error cases.
- **`org.example.Application`**: The main class for sorting CSV files using a multi-way merge approach.