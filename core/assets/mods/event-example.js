var event = Java.type("com.glowman554.block.mod.ModEvent");

function load() {
}

function enable() {
    event.registerEvent("test", function(data) {
        print("hello world from event");
    });

    event.registerEvent("timerEvent", function(data) {
        print("Timer UwU");
    });

    event.callEvent("test", null);
}

function disable() {
}