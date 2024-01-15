package com.zaku_desktop.actuators;

import javafx.scene.control.Slider;

import java.util.HashMap;

public class DCmotor extends Actuator
{
    static private int motorCounter = 0;
    public DCmotor()
    {
        super(new HashMap<>(){{ put("speed",new String[] {"NaN","dcSpeed"+motorCounter});put("direction",new String[] {"NaN","dcDirection"+motorCounter});}},"dcMotorIcon.png");
        actuatorControls = new HashMap<>(){{put("maxSpeed"+motorCounter,new Slider(0,255,255){{setShowTickLabels(true);setShowTickMarks(true);}});}};
        actuatorID = motorCounter;
        motorCounter++;
    }
    public String getSpeed()
    {
        return actuatorParams.get("speed")[0];
    }
    public String getDirection()
    {
        return actuatorParams.get("direction")[0];
    }
}
