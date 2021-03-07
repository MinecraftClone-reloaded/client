package com.glowman554.block.mod;

import com.glowman554.block.utils.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ModLoader {

    List<Mod> mods = new ArrayList<>();

    public ModLoader() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("mods/mod-list.txt");
        String data = FileUtils.readFromInputStream(inputStream);

        for (String mod : data.split("\n")) {
            this.mods.add(new Mod(mod));
        }
    }


    public void enableAll() {
        this.mods.forEach(Mod::enable);
    }

    public void disableAll() {
        this.mods.forEach(Mod::disable);
    }
}
