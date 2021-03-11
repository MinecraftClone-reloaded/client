package com.glowman554.block.command;

import com.glowman554.block.BlockGame;

public class CommandEvent {
    public final String message;
    public final String command;
    public final String[] args;

    public CommandEvent(String message, String command, String[] args) {
        this.message = message;
        this.command = command;
        this.args = args;
    }

    public static String[] getArguments(String[] array) {

        if (array.length < 2) {
            return new String[0];
        }

        String result = "";
        for (int i = 1; i < array.length; i++) {
            if (i > 1) {
                result = result + " ";
            }
            String item = array[i];
            result = result + item;
        }
        return result.split(" ");
    }

    public void sendMessage(String what) {
        BlockGame.game.chatRenderer.chat.add(what);
    }

    public void CommandFail() {
        this.sendMessage("Something is wrong! Please try help.");
    }
}
