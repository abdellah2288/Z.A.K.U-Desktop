package com.zaku_desktop.actuators;

import javafx.scene.control.Slider;

import java.util.HashMap;

public class Servo  extends Actuator
{
    static private int servoCounter = 0;
    public Servo()
    {
        super(new HashMap<>(){{ put("angle",new String[] {"NaN","dcSpeed"+servoCounter});}},"servoIcon.png");
        actuatorControls = new HashMap<>(){{put("servoAngle"+servoCounter,new Slider(0,180,0){{setShowTickLabels(true);setShowTickMarks(true);}});}};
        actuatorID = servoCounter;
        servoCounter++;
    }
    public String getAngle()
    {
        return actuatorParams.get("angle")[0];
    }
}
