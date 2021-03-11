package com.glowman554.block.mod;

import com.glowman554.block.command.Command;
import com.glowman554.block.command.CommandEvent;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

public class ModCommand implements Command {

    private final String help;
    private final ScriptObjectMirror executor;

    public ModCommand(String help, ScriptObjectMirror executor) {
        this.help = help;
        this.executor = executor;
    }

    @Override
    public void execute(CommandEvent event) {
        this.executor.call(this.executor, event);
    }

    @Override
    public void on_register() {

    }

    @Override
    public void on_help(CommandEvent event) {
        event.sendMessage(this.help);
    }
}
