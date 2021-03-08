package com.glowman554.block.block;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class BerryBlock extends Block {
    private static Texture t = new Texture(Gdx.files.internal("texture/Berry.png"));

    public BerryBlock() {
        super(t, t, t, t, t, t, Type.BerryBlock);
    }

    public static String getIp() {
        try {
            URL whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            String ip = bufferedReader.readLine();
            bufferedReader.close();
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
