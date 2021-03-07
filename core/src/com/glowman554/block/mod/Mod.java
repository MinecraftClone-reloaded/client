package com.glowman554.block.mod;

import com.glowman554.block.utils.FileUtils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.io.InputStream;

public class Mod {
    private final String mod;
    private ScriptEngine scriptEngine;

    public Mod(String mod) {
        this.mod = mod;

        System.out.println("Loading mod " + this.mod);

        this.scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");

        try {

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("mods/" + this.mod);
            String data = FileUtils.readFromInputStream(inputStream);

            this.scriptEngine.eval(data);

            Invocable invocable = (Invocable) this.scriptEngine;

            invocable.invokeFunction("load");
        } catch (ScriptException | NoSuchMethodException | IOException e) {
            System.out.println("Mod load fail: " + e.getMessage());
        }
    }

    public void enable() {
        try {
            Invocable invocable = (Invocable) this.scriptEngine;
            invocable.invokeFunction("enable");
        } catch (ScriptException | NoSuchMethodException e) {
            System.out.println("Mod enable fail: " + e.getMessage());
        }
    }

    public void disable() {
        try {
            Invocable invocable = (Invocable) this.scriptEngine;
            invocable.invokeFunction("disable");
        } catch (ScriptException | NoSuchMethodException e) {
            System.out.println("Mod disable fail: " + e.getMessage());
        }
    }
}
