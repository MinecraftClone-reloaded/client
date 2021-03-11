var api = Java.type("com.glowman554.block.mod.ModAPI");

function load() {
}

function enable() {
    api.addCommand("uwu", "get a uwu", function(event) {
        event.sendMessage("UwU")
    });
}

function disable() {
}