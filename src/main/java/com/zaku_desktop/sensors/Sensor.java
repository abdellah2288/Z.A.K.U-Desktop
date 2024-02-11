package com.zaku_desktop.sensors;

import com.zaku_desktop.utilities.MqttManager;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.util.*;

public abstract class Sensor
{
    protected Image[] sensorIcons;
    protected HashMap<String[], Node> sensorKeyParams;
    protected HashMap<String, String> sensorValues;
    protected Node sensorContainer;
    public String sensorName = "";
    public Sensor()
    {
        sensorKeyParams = new HashMap<>();
    }
    public Sensor(HashMap<String[],Node> sensorKeyParams)
    {
        this.sensorKeyParams = sensorKeyParams;
    }

    public Sensor(HashMap<String[], Node> sensorKeyParams, String sensorName)
    {
        this.sensorKeyParams = sensorKeyParams;
        this.sensorName = sensorName;
    }
    public HashMap<String[], Node> getSensorKeyParams()
    {
        return sensorKeyParams;
    }
    public String getSensorName()
    {
        return this.sensorName;
    }
    public void updateSensorConfig(String entryKey, String entryVal)
    {
        for(String[] key : sensorKeyParams.keySet()) { if(entryKey.toLowerCase().equals(key[1]))  { key[2] = entryVal; break; } }
    }
    public void updateSensorValues(MqttManager manager)
    {
        for(String[] key : sensorKeyParams.keySet())
        {
            sensorValues.put(key[3], manager.getTopicData(key[2]));
        }
    }
    public Map<String, String> getSensorValues()
    {

        return sensorValues;
    }

    public abstract Node generateTile();
    public abstract void updateTile(MqttManager manager);
    static public Node generateCluster(int clusterWidth,Object[] sensorArray)
    {
        assert sensorArray[0] instanceof Sensor : "Object is not a sensor";
        FlowPane clusterFP = new FlowPane();
        clusterFP.setOrientation(Orientation.HORIZONTAL);
        clusterFP.setHgap(10);
        clusterFP.setVgap(10);
        clusterFP.setPrefWrapLength(300);
        for(Object sensor : sensorArray)
        {
            clusterFP.getChildren().add(((Sensor) sensor).generateTile());
        }
        return clusterFP;
    }

}
