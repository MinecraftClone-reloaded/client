package com.glowman554.block.command;

public interface Command {
    public void execute(CommandEvent event);

    public void on_register();

    public void on_help(CommandEvent event);
}
