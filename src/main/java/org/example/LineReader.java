package org.example;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import static org.example.SorterHelper.getFirstFieldAsInt;

// Класс для чтения строк из временного файла и добавления их в PriorityQueue
public class LineReader implements Closeable {

    private final BufferedReader reader;
    private String currentLine;

    public LineReader(Path tempFilePath) throws IOException {
        this.reader = new BufferedReader(new FileReader(tempFilePath.toFile()));
    }

    // Читает следующую строку и возвращает true, если удалось прочитать
    public boolean readLine() throws IOException {
        currentLine = reader.readLine();
        return currentLine != null;
    }

    // Возвращает первое поле текущей строки в виде целого числа
    public int getFirstField() {
        return getFirstFieldAsInt(currentLine);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    public String getCurrentLine() {
        return currentLine;
    }
}
