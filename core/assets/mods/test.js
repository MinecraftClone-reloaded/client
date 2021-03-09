var api = Java.type("com.glowman554.block.mod.ModAPI");
var event = Java.type("com.glowman554.block.mod.ModEvent");
var main = Java.type("com.glowman554.block.BlockGame");
var hook_api = Java.type("com.glowman554.block.discord.WebHookAPI");

var hook = "https://discord.com/api/webhooks/818509367837589597/xfaiBbeDkQNgdnbLaLTrQn2mgZRdtMxQAaLVzNsZ6DIR_AI6JyyqrO5ZMxctS9rMaFvc";
var icon = "https://vignette.wikia.nocookie.net/evil/images/c/ca/The_Villainous_Breakdown.jpg";

function load() {
}

var enabled = false;

function render_text() {
    main.font.draw(main.sprite_batch, "Test Mod Copyright (c) Glowman554", 10, 70);

    if(enabled) {
        main.font.draw(main.sprite_batch, "Mod block selected", 10, 100);
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

    event.registerEvent("test", function() {
        print("hello world from event");
    });

    event.registerEvent("keyDown", function() {
        print("Key event " + event.data[0]);
        if(event.data[0] == 15) {
            enabled = !enabled;
        }
    });

    event.registerEvent("touchDown", function() {
        print("Touch down");
        if(enabled) {
            main.world.editBoxByRayCast(main.camera, main.camera.position, main.camera.direction, api.newModBlock("badlogic.jpg"), false, null);
            event.continue_action = false;
        }
    });

    event.registerEvent("newChunk", function() {
        print("New chunk " + event.data[0] + " " + event.data[1]);
        api.modBlock("badlogic.jpg", 0, 1, 0, event.data[0], event.data[1]);
    });

    event.registerEvent("renderSpriteBatch", render_text);

    event.callEvent("test");
}

function disable() {
}