package com.glowman554.block.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.glowman554.block.BlockGame;
import com.glowman554.block.desktop.utils.CMDLine;

public class DesktopLauncher {

    public static String name;
    public static String host;
    public static int port;
    public static boolean online;

    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 1000;
        config.height = 500;

        CMDLine cli = new CMDLine();
        cli.set(arg);

        if (cli.tokenExists("name")) {
            name = cli.getToken("name");
        } else {
            name = "defaultuser0";
        }

        if (cli.tokenExists("host")) {
            host = cli.getToken("host");
        } else {
            host = "";
        }

        if (cli.tokenExists("port")) {
            port = Integer.parseInt(cli.getToken("port"));
        } else {
            port = 90;
        }

        if (cli.tokenExists("online")) {
            online = true;
        } else {
            online = false;
        }

        new LwjglApplication(new BlockGame(name, online, host, port), config);
    }
}
