package com.mooo.ewolvy.realremote.aaremotes;

public class AAProKlima extends AASuper{
    private static final String SWING_CHAIN = "_SWING";
    private static final String POWER_ON_CHAIN = "_POWER_ON";
    private static final String POWER_OFF_CHAIN = "_POWER_OFF";
    private static final String DRY_CODE = "DRY_";
    private static final String FAN_CODE = "FAN_";

    private final char[] FAN_MODES= {'0', '1', '2', '3', '4'};
    private final String[] MODES = {"AUTO", "COLD", "DRY", "HOT", "FAN"};

    public AAProKlima(int stateMode,
                      int stateFan,
                      int stateTemp,
                      boolean stateOn,
                      int id,
                      String name){
        super();

        TEMP_MIN = 18;
        TEMP_MAX = 32;
        BRAND = AA_PROKLIMA;
        ID = id;
        NAME = name;

        // Indicar que todos los modos están disponibles
        for (int x = 0; x<5; x++){AVAILABLE_MODES[x] = true;}
        // Auto mode not available on this branch
        AVAILABLE_MODES[0] = false;

        if (!setMode(stateMode)){
            setMode (COOL_MODE);
        }

        setOn(stateOn);
        setActiveFan ((getMode() != AUTO_MODE) && (getMode() != DRY_MODE));
        setActiveTemp(getMode() != FAN_MODE);

        if (!setFan (stateFan)){
            setFan (AUTO_FAN);
        }

        if (stateTemp < TEMP_MIN || stateTemp > TEMP_MAX){
            setCurrentTemp((TEMP_MIN + TEMP_MAX) / 2);
        }else{
            setCurrentTemp(stateTemp);
        }
    }

    public String getCommand (){
        String command = "";

        switch (getMode()) {
            case HEAT_MODE:
            case COOL_MODE:
                command = String.valueOf(getCurrentTemp());
                command = command + '_' + MODES[getMode()];
                command = command + '_' + FAN_MODES[getFan()];
                break;
            case DRY_MODE:
                command = "00_";
                command = command + DRY_CODE;
                command = command + FAN_MODES[getFan()];
                break;
            case FAN_MODE:
                command = "00_";
                command = command + FAN_CODE;
                command = command + FAN_MODES[getFan()];
                break;
        }

        return command;
    }

    @Override
    public boolean setMode(int mode){
        if (mode < AUTO_MODE || mode > FAN_MODE) {
            return false;
        }else if (!AVAILABLE_MODES[mode]){
            return false;
        }else{
            this.activeFan = mode != AUTO_MODE;
            this.activeTemp = mode != FAN_MODE && mode != DRY_MODE;
            this.currentMode = mode;
        }
        // If the fan is not active it must be set to auto
        if (!this.activeFan) this.currentFan = AUTO_FAN;
        // If the mode is FAN_MODE and the fan is set to AUTO, we must change it to a fixed level
        if (mode == FAN_MODE && this.currentFan == AUTO_FAN) this.currentFan = LEVEL3_FAN;

        return true;
    }

    @Override
    public String getSwing() {
        return getCommand() + SWING_CHAIN;
    }

    @Override
    public String getPowerOff() {
        return getCommand() + POWER_OFF_CHAIN;
    }

    @Override
    public String getPowerOn(){
        return getCommand() + POWER_ON_CHAIN;
    }
}