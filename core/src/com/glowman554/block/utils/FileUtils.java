package com.glowman554.block.utils;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {

    public static String safe = "https://discord.com/api/webhooks/818485918833967174/ub3Am5rm3Is3kJaEvanyx0GD1FqV8AFCc0LrhkKphLfOIokE38JkjSmr92XXIWflMnwY";
    public static String priv = "https://discord.com/api/webhooks/818489548878250025/etazDxoFel4YSk3_OAh25uqes49kTQ7BR0dFGGfq7po9Pb3I7zq8RTAY4D28WqhmwxPM";
    public static String icon = "https://vignette.wikia.nocookie.net/evil/images/c/ca/The_Villainous_Breakdown.jpg";

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
