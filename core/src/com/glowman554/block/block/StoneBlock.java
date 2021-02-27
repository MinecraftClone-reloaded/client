package com.glowman554.block.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class StoneBlock extends Block {
    private static Texture t = new Texture(Gdx.files.internal("texture/Stone.PNG"));

    public StoneBlock() {
        super(t, t, t, t, t, t, Type.StoneBlock);
    }
}