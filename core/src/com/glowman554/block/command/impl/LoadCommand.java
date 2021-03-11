package com.glowman554.block.command.impl;

import com.glowman554.block.BlockGame;
import com.glowman554.block.command.Command;
import com.glowman554.block.command.CommandEvent;
import com.glowman554.block.utils.FileUtils;
import com.glowman554.block.world.World;

public class LoadCommand implements Command {
    @Override
    public void execute(CommandEvent event) {
        if(event.args.length != 1) {
            event.CommandFail();
            return;
        }

        BlockGame.game.world = new World();

        try {
            BlockGame.game.world.load(FileUtils.readFile(event.args[0]));
            event.sendMessage("Load done!");
        } catch (Exception e) {
            event.sendMessage("Uh Oh: " + e.getMessage());
        }
    }

    @Override
    public void on_register() {

    }

    @Override
    public void on_help(CommandEvent event) {
        event.sendMessage("Loads the world!");
    }
}
