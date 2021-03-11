var api = Java.type("com.glowman554.block.mod.ModAPI");
var event = Java.type("com.glowman554.block.mod.ModEvent");
var main = Java.type("com.glowman554.block.BlockGame");

function load() {
}

var enabled = false;

function render_text(data) {
    if(enabled) {
        main.game.font.draw(data, "Mod block selected", 10, 100);
    }
}

function enable() {

    event.registerEvent("keyDown", function(data) {

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
        if(enabled) {
            if(data[3] == 0) {
                main.game.world.editBoxByRayCast(main.game.camera, main.game.camera.position, main.game.camera.direction, api.newModBlock("badlogic.jpg"), false, null);
            } else if(data[3] == 1) {
                main.game.world.editBoxByRayCast(main.game.camera, main.game.camera.position, main.game.camera.direction, false, null)
            }
            event.continue_action = false;
        }
    });

    event.registerEvent("renderSpriteBatch", render_text);
}

function disable() {
}