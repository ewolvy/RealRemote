package com.mooo.ewolvy.realremote;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mooo.ewolvy.realremote.aalist.AAData;
import com.mooo.ewolvy.realremote.aalist.AAItem;
import com.mooo.ewolvy.realremote.aaremotes.*;
import com.mooo.ewolvy.realremote.database.AirConditionersContract;

public class ControlsFragment extends Fragment {
    AASuper state;
    SSLServer myServer;
    View fragView;
    AAItem item;

    public ControlsFragment(){}

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public static ControlsFragment newInstance(int sectionNumber) {
        ControlsFragment fragment = new ControlsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragView = inflater.inflate(R.layout.fragment_main, container, false);

        // Set onClickListeners
        fragView.findViewById(R.id.modeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modeClick();
            }
        });
        fragView.findViewById(R.id.fanButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fanClick();
            }
        });
        fragView.findViewById(R.id.offButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                offClick();
            }
        });
        fragView.findViewById(R.id.tempMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempMinusClick();
            }
        });
        fragView.findViewById(R.id.tempPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempPlusClick();
            }
        });
        fragView.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClick();
            }
        });
        fragView.findViewById(R.id.swingButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swingClick();
            }
        });

        // Get the item representation
        Bundle args = this.getArguments();
        if (args != null) {
            int position = args.getInt(ARG_SECTION_NUMBER);
            item = AAData.getListData(getContext()).get(position);
        }

        // Create AA object to manage the AA
        switch (item.getBrand()){
            case AirConditionersContract.AA_KAYSUN:
                state = new AAKaysun(item.getMode(),
                        item.getFan(),
                        item.getTemperature(),
                        item.getIs_on(),
                        "AAKaysun",
                        item.getCertificate());
                break;
            case AirConditionersContract.AA_PROKLIMA:
                state = new AAProKlima(item.getMode(),
                        item.getFan(),
                        item.getTemperature(),
                        item.getIs_on(),
                        "AAProKlima",
                        item.getCertificate());
                break;
        }

        return fragView;
    }

    @Override
    public void onPause() {
        Log.v("CONTROLS_FRAGMENT", "Fragment paused: " + item.getPosition());
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v("CONTROLS_FRAGMENT", "Fragment resumed: " + item.getPosition());
    }

    public void modeClick() {
        View modeView;
        int actualMode = state.getMode();
        int nextMode = state.getNextMode(actualMode);

        if (nextMode != AASuper.NONEXISTENT_MODE) {
            switch (actualMode) {
                case AASuper.AUTO_MODE:
                    modeView = fragView.findViewById(R.id.autoMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.COOL_MODE:
                    modeView = fragView.findViewById(R.id.coolMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.DRY_MODE:
                    modeView = fragView.findViewById(R.id.dryMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.HEAT_MODE:
                    modeView = fragView.findViewById(R.id.heatMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;

                case AASuper.FAN_MODE:
                    modeView = fragView.findViewById(R.id.fanMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
            switch (nextMode) {
                case AASuper.AUTO_MODE:
                    modeView = fragView.findViewById(R.id.autoMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.COOL_MODE:
                    modeView = fragView.findViewById(R.id.coolMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.DRY_MODE:
                    modeView = fragView.findViewById(R.id.dryMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.HEAT_MODE:
                    modeView = fragView.findViewById(R.id.heatMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
                case AASuper.FAN_MODE:
                    modeView = fragView.findViewById(R.id.fanMode);
                    if (modeView != null) {
                        modeView.setVisibility(View.VISIBLE);
                    }
                    break;
            }
            state.setMode(nextMode);
            item.setMode(nextMode);
        }
    }

    public void fanClick() {
        if (!state.isActiveFan()){return;}
        TextView fanView;
        switch (state.getFan()) {
            case AASuper.AUTO_FAN:
                fanView = (TextView) fragView.findViewById(R.id.fanLevelAuto);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) fragView.findViewById(R.id.fanLevel1);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.LEVEL1_FAN);
                    item.setFan(AASuper.LEVEL1_FAN);
                }
                break;

            case AASuper.LEVEL1_FAN:
                fanView = (TextView) fragView.findViewById(R.id.fanLevel2);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.LEVEL2_FAN);
                    item.setFan(AASuper.LEVEL2_FAN);
                }
                break;

            case AASuper.LEVEL2_FAN:
                fanView = (TextView) fragView.findViewById(R.id.fanLevel3);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.LEVEL3_FAN);
                    item.setFan(AASuper.LEVEL3_FAN);
                }
                break;

            case AASuper.LEVEL3_FAN:
                fanView = (TextView) fragView.findViewById(R.id.fanLevel1);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) fragView.findViewById(R.id.fanLevel2);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) fragView.findViewById(R.id.fanLevel3);
                if (fanView != null) {
                    fanView.setVisibility(View.INVISIBLE);
                }
                fanView = (TextView) fragView.findViewById(R.id.fanLevelAuto);
                if (fanView != null) {
                    fanView.setVisibility(View.VISIBLE);
                    state.setFan(AASuper.AUTO_FAN);
                    item.setFan(AASuper.AUTO_FAN);
                }
                break;
        }
    }

    public void tempMinusClick() {
        if (state.getCurrentTemp() > state.TEMP_MIN && state.isActiveTemp()){
            state.setMinusTemp();
            item.setTemperature(state.getCurrentTemp());
            TextView tempView = (TextView) fragView.findViewById(R.id.tempView);
            String temperature = Integer.toString(state.getCurrentTemp());
            if (tempView != null) tempView.setText(temperature);
        }
    }

    public void tempPlusClick() {
        if (state.getCurrentTemp() < state.TEMP_MAX && state.isActiveTemp()){
            state.setPlusTemp();
            item.setTemperature(state.getCurrentTemp());
            TextView tempView = (TextView) fragView.findViewById(R.id.tempView);
            String temperature = Integer.toString(state.getCurrentTemp());
            if (tempView != null) tempView.setText(temperature);
        }
    }

    public void offClick() {
        if (myServer != null){
            // Send the PowerOff code.
            myServer.sendCode (state.getPowerOff(), getActivity().getApplicationContext(), state, (ImageView) fragView.findViewById(R.id.onOffSign));
            item.setIs_on(false);
        }else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void sendClick() {
        if (myServer != null){      // If the options were set, send the command, else ask the user to fullfill the settings
            myServer.sendCode (state.getCommand(), getActivity().getApplicationContext(), state, (ImageView) fragView.findViewById(R.id.onOffSign));
        }else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void swingClick() {
        if (state.getIsOn()) {      // If the system is on check to send command, else warn the user to switch on before activating / deactivating swing
            if (myServer != null){  // If the options were set, send the swing command, else ask the user to fullfill the settings
                myServer.sendCode (state.getSwing(), getActivity().getApplicationContext(), state, null);
            }else{
                Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.server_data_missing), Toast.LENGTH_LONG);
                toast.show();
            }
        }else{
            Toast toast = Toast.makeText(getActivity().getApplicationContext(), getString(R.string.is_off_message), Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void updateView(){

        // Put all modes invisible, then set visible the active one
        fragView.findViewById(R.id.autoMode).setVisibility(View.INVISIBLE);
        fragView.findViewById(R.id.coolMode).setVisibility(View.INVISIBLE);
        fragView.findViewById(R.id.dryMode).setVisibility(View.INVISIBLE);
        fragView.findViewById(R.id.heatMode).setVisibility(View.INVISIBLE);
        fragView.findViewById(R.id.fanMode).setVisibility(View.INVISIBLE);
        switch (state.getMode()){
            case AASuper.AUTO_MODE:
                fragView.findViewById(R.id.autoMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.COOL_MODE:
                fragView.findViewById(R.id.coolMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.DRY_MODE:
                fragView.findViewById(R.id.dryMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.HEAT_MODE:
                fragView.findViewById(R.id.heatMode).setVisibility(View.VISIBLE);
                break;
            case AASuper.FAN_MODE:
                fragView.findViewById(R.id.fanMode).setVisibility(View.VISIBLE);
                break;
        }

        // Set on/off sign visible if it's on, invisible if not
        if (state.getIsOn()) {
            fragView.findViewById(R.id.onOffSign).setVisibility(View.VISIBLE);
        }else{
            fragView.findViewById(R.id.onOffSign).setVisibility(View.INVISIBLE);
        }

        // Set temperature text
        TextView tempView = (TextView) fragView.findViewById(R.id.tempView);
        String temperature = Integer.toString(state.getCurrentTemp());
        if (tempView != null) tempView.setText (temperature);

        // Put all fan levels invisible, then set visible the active one(s)
        fragView.findViewById(R.id.fanLevel1).setVisibility(View.INVISIBLE);
        fragView.findViewById(R.id.fanLevel2).setVisibility(View.INVISIBLE);
        fragView.findViewById(R.id.fanLevel3).setVisibility(View.INVISIBLE);
        fragView.findViewById(R.id.fanLevelAuto).setVisibility(View.INVISIBLE);
        switch (state.getFan()){
            case AASuper.AUTO_FAN:
                fragView.findViewById(R.id.fanLevelAuto).setVisibility(View.VISIBLE);
                break;
            case AASuper.LEVEL3_FAN:
                fragView.findViewById(R.id.fanLevel3).setVisibility(View.VISIBLE);
            case AASuper.LEVEL2_FAN:
                fragView.findViewById(R.id.fanLevel2).setVisibility(View.VISIBLE);
            case AASuper.LEVEL1_FAN:
                fragView.findViewById(R.id.fanLevel1).setVisibility(View.VISIBLE);
        }
    }
}
