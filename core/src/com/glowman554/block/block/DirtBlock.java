package com.glowman554.block.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class DirtBlock extends Block {
    private static Texture t = new Texture(Gdx.files.internal("texture/Dirt.PNG"));

    public DirtBlock() {
        super(t, t, t, t, t, t, Type.DirtBlock);
    }
}
