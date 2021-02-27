package com.glowman554.block.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class BerryBlock extends Block {
    private static Texture t = new Texture(Gdx.files.internal("texture/Berry.png"));

    public BerryBlock() {
        super(t, t, t, t, t, t, Type.BerryBlock);
    }
}
