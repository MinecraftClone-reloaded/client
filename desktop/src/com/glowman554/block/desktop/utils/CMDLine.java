package com.glowman554.block.desktop.utils;

public class CMDLine {

    private String[] args;

    public void set(String[] args) {
        this.args = args;
    }

    public String getToken(String what) {
        for (String token : args) {
            if (token.split("=")[0].equals(what)) {
                return token.split("=")[1];
            }
        }
        return null;
    }

    public boolean tokenExists(String what) {
        for (String token : args) {
            if (token.split("=")[0].equals(what)) {
                return true;
            }
        }
        return false;
    }
}