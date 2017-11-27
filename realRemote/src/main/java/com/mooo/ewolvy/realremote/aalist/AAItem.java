package com.mooo.ewolvy.realremote.aalist;

public class AAItem {
    private int _id;
    private String name;
    private int brand;
    private String server;
    private int port;
    private String username;
    private String password;
    private String certificate;
    private String alias;
    private int position;
    private int temperature;
    private int mode;
    private int fan;
    private boolean is_on;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getTemperature() {
        return temperature;
    }

    void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getMode() {
        return mode;
    }

    void setMode(int mode) {
        this.mode = mode;
    }

    public int getFan() {
        return fan;
    }

    public void setFan(int fan) {
        this.fan = fan;
    }

    public boolean getIs_on() {
        return is_on;
    }

    void setIs_on(boolean is_on) {
        this.is_on = is_on;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBrand() {
        return brand;
    }

    public void setBrand(int brand) {
        this.brand = brand;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getPosition() {
        return position;
    }

    void setPosition(int position) {
        this.position = position;
    }
}
