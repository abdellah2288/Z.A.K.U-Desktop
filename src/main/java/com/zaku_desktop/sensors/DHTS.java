package com.zaku_desktop.sensors;

import com.zaku_desktop.utilities.MqttManager;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import java.util.HashMap;

import static com.zaku_desktop.utilities.MiscUtils.getImage;

public class DHTS extends Sensor
{
    static int sensorCount = 0;
    public DHTS()
    {
        super(new HashMap<>()
        {{
            /*Parameter Label, Parameter config entry, Parameter default value, Parameter associated sensor value*/
            put(new String[] {"MQTT humdity topic:","dht11_humidity_topic" + sensorCount,"d11h"+sensorCount,"humidity"},new TextField("d11h"+sensorCount));
            put(new String[] {"MQTT temperature topic","dht11_temperature_topic" + sensorCount,"d11t"+sensorCount,"temperature"},new TextField("d11t"+sensorCount));
        }},"Temperature and Humidity sensor " + sensorCount);
        this.sensorValues = new HashMap<>(){{
            put("temperature","NaN");
            put("humidity","NaN");
        }};
        try { this.sensorIcons = new Image[]{getImage("tempIcon.png"),getImage("humIcon.png")}; }
        catch(Exception e)  { System.out.println(e.getMessage()); }
        sensorCount++;
    }
    @Override
    public Node generateTile()
    {
        FlowPane sensorPane = new FlowPane();
        sensorPane.setOrientation(Orientation.HORIZONTAL);
        sensorPane.setHgap(15);
        int counter = 0;
        for(String[] keyList : sensorKeyParams.keySet())
        {
            sensorPane.getChildren().add(new ImageView(sensorIcons[counter]));
            sensorPane.getChildren().add(new Separator());

            sensorPane.getChildren().add(new Label("NaN"));
            sensorPane.getChildren().add(new Separator());
            counter++;
        }
        sensorContainer = sensorPane;
        return sensorPane;
    }
    @Override
    public void updateTile(MqttManager manager)
    {
        updateSensorValues(manager);
        ((Label)((FlowPane) sensorContainer).getChildren().get(2)).setText(sensorValues.get("temperature"));
        ((Label)((FlowPane) sensorContainer).getChildren().get(6)).setText(sensorValues.get("humidity"));
    }
}
