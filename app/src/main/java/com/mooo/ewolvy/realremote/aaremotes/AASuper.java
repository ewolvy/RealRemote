package com.mooo.ewolvy.realremote.aaremotes;

public abstract class AASuper {
    // Mode constants
    public static final int AUTO_MODE = 0;
    public static final int COOL_MODE = 1;
    public static final int DRY_MODE = 2;
    public static final int HEAT_MODE = 3;
    public static final int FAN_MODE = 4;
    public static final int NONEXSISTING_MODE = 99;

    // Fan constants
    public static final int AUTO_FAN = 0;
    public static final int LEVEL1_FAN = 1;
    public static final int LEVEL2_FAN = 2;
    public static final int LEVEL3_FAN = 3;
    public static final int SPECIAL_FAN = 4;

    // Temperature constants and variables (for inheritance on specific devices)
    public int TEMP_MIN;
    public int TEMP_MAX;
    public static final int NONEXISTING_TEMP = 99;

    // Array of available modes to be defined on child classes
    boolean AVAILABLE_MODES[] = new boolean[5];

    // Variables
    private String serverPath;
    private String certificateFile;
    private boolean isOn;
    private int currentMode;
    private int currentFan;
    private boolean activeFan;
    private boolean activeTemp;
    private int currentTemp;

    // Constructor
    AASuper (){
    }

    // Getters methods for variables //
    public String getServerPath(){
        return serverPath;
    }
    public String getCertificateFile(){
        return certificateFile;
    }
    public boolean getIsOn(){return isOn;}
    public int getMode() {
        return currentMode;
    }
    public boolean isActiveFan() {return activeFan;}
    public boolean isActiveTemp() {return activeTemp;}
    public int getFan() {
        return currentFan;
    }
    public int getCurrentTemp() {
        return currentTemp;
    }

    // Setters methods for variables //
    public void setServerPath(String serverPath){
        this.serverPath = serverPath;
    }
    public void setCertificateFile(String certificateFile){
        this.certificateFile = certificateFile;
    }
    public void setOn (boolean on){this.isOn = on;}
    public boolean setMode(int mode) {
        if (mode < AUTO_MODE || mode > FAN_MODE) {
            return false;
        }else if (!AVAILABLE_MODES[mode]){
            return false;
        }else{
            this.activeFan = mode != AUTO_MODE && mode != DRY_MODE;
            this.activeTemp = mode != FAN_MODE;
            this.currentMode = mode;
        }
        return true;
    }
    public boolean setFan(int fan) {
        if (fan < AUTO_FAN || fan > LEVEL3_FAN || !this.activeFan){
            return false;
        }else{
            this.currentFan = fan;
            return true;
        }
    }
    public void setActiveFan(boolean activeFan){
        this.activeFan = activeFan;
    }
    public void setActiveTemp(boolean activeTemp){
        this.activeTemp = activeTemp;
    }
    public void setCurrentTemp(int currentTemp){
        this.currentTemp = currentTemp;
    }
    // End of setters and getters //

    // Plus 1 and minus 1 degrees
    public boolean setPlusTemp(){
        if (currentTemp != TEMP_MAX && isActiveTemp()){
            currentTemp++;
            return true;
        }else{
            return false;
        }
    }

    public boolean setMinusTemp(){
        if (currentTemp != TEMP_MIN && isActiveTemp()){
            currentTemp--;
            return true;
        }else{
            return false;
        }
    }

    // Know next mode available
    public int getNextMode(int fromMode){
        int nextMode;
        switch (fromMode) {
            case AUTO_MODE:
            case COOL_MODE:
            case DRY_MODE:
            case HEAT_MODE:
                nextMode = fromMode + 1;
                if (!AVAILABLE_MODES[nextMode]){
                    nextMode = getNextMode(nextMode);
                }
                break;
            case FAN_MODE:
                nextMode = AUTO_MODE;
                if (!AVAILABLE_MODES[nextMode]){
                    nextMode = getNextMode(nextMode);
                }
                break;
            default:
                nextMode = NONEXSISTING_MODE;
        }
        return nextMode;
    }

    // Commands methods
    public abstract String getCommand();

    public abstract String getSwing();

    public abstract String getPowerOff();
}