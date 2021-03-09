package com.glowman554.block.mod;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModEvent {
    static HashMap<String, List<ScriptObjectMirror>> events = new HashMap<>();

    public static int[] data = new int[8];
    public static boolean continue_action = true;

    public static void registerEvent(String name, ScriptObjectMirror executor) {
        events.computeIfAbsent(name, k -> new ArrayList<>());

        events.get(name).add(executor);
    }

    public static void callEvent(String name) {
        if(events.get(name) != null) {
            events.get(name).forEach(executor -> {
                executor.call(executor);
            });
        }
    }
}
