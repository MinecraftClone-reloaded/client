package com.glowman554.block.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class LeavesBlock extends Block {
    private static Texture t = new Texture(Gdx.files.internal("texture/Leaves.PNG"));

    public LeavesBlock() {
        super(t, t, t, t, t, t, Type.LeavesBlock);
    }
}