package com.glowman554.block.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    public static String readFile(String read_file) throws IOException {
        FileReader fileReader = new FileReader(read_file);

        String file = "";

        int i;
        while((i = fileReader.read()) != -1) {
            file += (char) i;
        }
        return file;
    }

    public static void writeFile(String what, String write_file) throws IOException {
        FileWriter fileWriter = new FileWriter(write_file);
        fileWriter.write(what);
        fileWriter.flush();
    }
}
