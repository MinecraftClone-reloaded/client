package com.glowman554.block.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.glowman554.block.BlockGame;

import java.util.ArrayList;
import java.util.List;

public class ChatRenderer {
    public List<String> chat = new ArrayList<>();

    public ChatRenderer() {
        new Thread("Chat remover") {
            @Override
            public void run() {
                while (true) {
                    if (chat.size() != 0) {
                        chat.remove(0);
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void render(SpriteBatch batch, int base) {

        final int[] index = {0};

        chat.forEach(element -> {
            BlockGame.game.font.draw(batch, element, 10, Gdx.graphics.getHeight() - (base + index[0]) * 30);
            index[0]++;
        });
    }
}
