package com.glowman554.block.block;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GlassBlock extends Block {
    private static Texture t = new Texture(Gdx.files.internal("texture/Glass.PNG"));

    public GlassBlock() {
        super(t, t, t, t, t, t, Type.GlassBlock);
    }
}