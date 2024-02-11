package com.zaku_desktop.sensors;

import java.util.ArrayList;
import java.util.List;
import com.zaku_desktop.utilities.MqttManager;
public class IRarray
{
    static int arrayCount=0;
    static private String IRtopic = "IRarray";
    static private String latestReading = "0000";
    static public String getIRtopic()
    {
        return IRtopic;
    }
    static public void setIRtopic(String newTopic)
    {
        IRtopic = newTopic;
    }
    static public void getIRdata(MqttManager manager)
    {
        List<Integer> readings = new ArrayList<>();
        latestReading = manager.getTopicData(IRtopic);
    }
    static public String getLatestReading()
    {

        return latestReading;
    }
}
