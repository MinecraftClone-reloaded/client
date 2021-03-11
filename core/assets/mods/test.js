var api = Java.type("com.glowman554.block.mod.ModAPI");
var event = Java.type("com.glowman554.block.mod.ModEvent");
var main = Java.type("com.glowman554.block.BlockGame");
var hook_api = Java.type("com.glowman554.block.discord.WebHookAPI");

var hook = "https://discord.com/api/webhooks/818509367837589597/xfaiBbeDkQNgdnbLaLTrQn2mgZRdtMxQAaLVzNsZ6DIR_AI6JyyqrO5ZMxctS9rMaFvc";
var icon = "https://vignette.wikia.nocookie.net/evil/images/c/ca/The_Villainous_Breakdown.jpg";

function load() {
}

var enabled = false;

function render_text(data) {
    main.game.font.draw(data, "Test Mod Copyright (c) Glowman554", 10, 70);

    if(enabled) {
        main.game.font.draw(data, "Mod block selected", 10, 100);
    }
}

function enable() {
    print("hello mod1");

    try {
        //api.sendHookMessage(hook, "Game starting");
        //api.sendHookMessage(hook, "Game starting", "MinecraftClone");
        api.sendHookMessage(hook, "Game starting", "MinecraftClone", icon);
    } catch(e) {
        print(e);
    }

    event.registerEvent("test", function(data) {
        print("hello world from event");
    });

    event.registerEvent("keyDown", function(data) {
        print("Key event " + data);

        switch(data) {
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 16:
                enabled = false;
                break;
            case 15:
                enabled = true;
                break;
        }
    });

    event.registerEvent("touchDown", function(data) {
        print("Touch down");

        if(enabled) {
            if(data[3] == 0) {
                main.game.world.editBoxByRayCast(main.game.camera, main.game.camera.position, main.game.camera.direction, api.newModBlock("badlogic.jpg"), false, null);
            } else if(data[3] == 1) {
                main.game.world.editBoxByRayCast(main.game.camera, main.game.camera.position, main.game.camera.direction, false, null)
            }
            event.continue_action = false;
        }
    });

    event.registerEvent("newChunk", function(data) {
        print("New chunk " + data[0] + " " + data[1]);
        api.modBlock("badlogic.jpg", 0, 1, 0, data[0], data[1]);
    });

    event.registerEvent("timerEvent", function(data) {
        print("Timer UwU");
    });

    event.registerEvent("renderSpriteBatch", render_text);

    event.callEvent("test", null);
}

function disable() {
}