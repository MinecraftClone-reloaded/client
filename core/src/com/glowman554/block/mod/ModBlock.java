package com.glowman554.block.mod;

import com.badlogic.gdx.graphics.Texture;
import com.glowman554.block.block.Block;

public class ModBlock extends Block {

    public ModBlock(String path) {
        super(new Texture(path), new Texture(path), new Texture(path), new Texture(path), new Texture(path), new Texture(path), Type.ModBlock);
    }
}
