# Large CSV Sorter

The CSV File Sorter is a command-line application that allows you to sort CSV files efficiently using a multi-way merge approach. The program takes an input CSV file, sorts it by first column integer value, and produces a sorted output CSV file. It also provides an option to specify the maximum memory size for sorting to optimize performance.

<h3>**Program only works with CSV files that have integer values in first column!**</h3>

CSV File Example
Here is an example portion of the CSV file formatted as a table:

Year	Make	Model	Trim	Body	Transmission	VIN	State	Condition	Odometer	Color	Interior	Seller	MMR	Selling Price	Sale Date
2015	Kia	Sorento	LX	SUV	Automatic	5xyktca69fg566472	CA	5	16639	White	Black	Kia Motors America Inc	20500	21500	Tue Dec 16 2014 12:30:00 GMT-0800 (PST)
2015	Kia	Sorento	LX	SUV	Automatic	5xyktca69fg561319	CA	5	9393	White	Beige	Kia Motors America Inc	20800	21500	Tue Dec 16 2014 12:30:00 GMT-0800 (PST)
2014	BMW	3 Series	328i SULEV	Sedan	Automatic	wba3c1c51ek116351	CA	45	1331	Gray	Black	Financial Services Remarketing (Lease)	31900	30000	Thu Jan 15 2015 04:30:00 GMT-0800 (PST)

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

- **`org.example.FileHelper`**: Provides methods for file handling and sorting of records.
- **`org.example.helper.SorterHelper`**: Contains helper functions for file operations such as reading headers, extracting first fields, and deleting temporary files.
- **`org.example.LineReader`**: Handles reading lines from temporary files and adding them to a priority queue.
- **`org.example.Application`**: The main class for sorting CSV files using a multi-way merge approach.