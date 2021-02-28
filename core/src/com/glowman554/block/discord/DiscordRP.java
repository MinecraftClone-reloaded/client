package com.glowman554.block.discord;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;
import net.arikia.dev.drpc.DiscordUser;
import net.arikia.dev.drpc.callbacks.ReadyCallback;

public class DiscordRP {
    private boolean running = true;
    private long created = 0;
    private String client_id = "806882188673941505";

    private static DiscordRP discordRP = new DiscordRP();

    public static DiscordRP getDiscordRP() {
        return discordRP;
    }

    public void start() {
        this.created = System.currentTimeMillis();

        DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(new ReadyCallback() {
            @Override
            public void apply(DiscordUser discordUser) {
                System.out.println("DiscordRP Websome " + discordUser.username + "#" + discordUser.discriminator);
                update("Starting...", "");
            }
        }).build();

        DiscordRPC.discordInitialize(client_id, handlers, true);

        new Thread("Discord RP Callback") {
            @Override
            public void run() {
                while (running) {
                    DiscordRPC.discordRunCallbacks();
                }
            }
        }.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public synchronized void start() {
                shutdown();
            }
        });
    }

    public void shutdown() {
        running = false;
        DiscordRPC.discordShutdown();
    }

    public void update(String firstLine, String secondLine) {
        DiscordRichPresence.Builder rich = new DiscordRichPresence.Builder(secondLine);
        rich.setBigImage("large", "");
        rich.setDetails(firstLine);
        rich.setStartTimestamps(created);
        DiscordRPC.discordUpdatePresence(rich.build());
    }
}
