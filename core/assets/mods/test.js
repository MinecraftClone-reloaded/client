var api = Java.type("com.glowman554.block.mod.ModAPI");
var event = Java.type("com.glowman554.block.mod.ModEvent");

function load() {
}

function enable() {
    print("hello mod1");

    event.registerEvent("test", function() {
        print("hello world from event");
    });

    event.registerEvent("key", function() {
        print("Key event " + event.data[0]);
    });

    event.registerEvent("newChunk", function() {
        print("New chunk " + event.data[0] + " " + event.data[1]);
        api.modBlock("badlogic.jpg", 0, 1, 0, event.data[0], event.data[1]);
    });

    event.callEvent("test");
}

function disable() {
}