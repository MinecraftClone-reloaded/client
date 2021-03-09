var api = Java.type("com.glowman554.block.mod.ModAPI");
var event = Java.type("com.glowman554.block.mod.ModEvent");
var main = Java.type("com.glowman554.block.BlockGame");

function load() {
}

var enabled = false;

function enable() {
    print("hello mod1");

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

    event.callEvent("test");
}

function disable() {
}