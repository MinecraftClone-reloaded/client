package com.glowman554.block.mod;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ModEvent {
    public static boolean continue_action = true;
    static HashMap<String, List<ScriptObjectMirror>> events = new HashMap<>();

    public static void registerEvent(String name, ScriptObjectMirror executor) {
        events.computeIfAbsent(name, k -> new ArrayList<>());

        events.get(name).add(executor);
    }

    public static boolean callEvent(String name, Object data) {
        if (events.get(name) != null) {
            events.get(name).forEach(executor -> {
                executor.call(executor, data);
            });
        }

        boolean temp = continue_action;
        continue_action = true;

        return temp;
    }
}
