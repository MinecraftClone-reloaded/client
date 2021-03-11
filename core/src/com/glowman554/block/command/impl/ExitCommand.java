package com.glowman554.block.command.impl;


import com.badlogic.gdx.Gdx;
import com.glowman554.block.command.Command;
import com.glowman554.block.command.CommandEvent;

public class ExitCommand implements Command {
    @Override
    public void execute(CommandEvent event) {
        if (event.args.length != 0) {
            event.CommandFail();
            return;
        }
        Gdx.app.exit();
    }

    @Override
    public void on_register() {

    }

    @Override
    public void on_help(CommandEvent event) {
        event.sendMessage("What do i need to say about exit");
    }
}
