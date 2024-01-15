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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.Flow;

import static com.zaku_desktop.utilities.MiscUtils.getImage;

public class USsensor extends Sensor
{
    static private int sensorCount = 0;
    public final int id = sensorCount;
    public USsensor()
    {
        super( new HashMap<>()
        {{
            /*Parameter Label, Parameter config entry, Parameter default value, Parameter associated sensor value*/
            put(new String[] {"Distance topic:","us_distance_topic" + sensorCount,"us"+sensorCount,"distance"},new TextField("us"+sensorCount));
        }},"Proximity sensor " + sensorCount);
        sensorCount++;
        sensorValues = new HashMap<>(){{put("distance","NaN");}};
        try { this.sensorIcons = new Image[]{getImage("usIcon.png")}; }
        catch(Exception e){System.out.println(e.getMessage());}
    }
    public String getDistance()
    {
        return sensorValues.get("distance");
    }
    @Override
    public Node generateTile()
    {
        FlowPane sensorPane = new FlowPane();
        sensorPane.setOrientation(Orientation.HORIZONTAL);
        sensorPane.setHgap(10);
        sensorPane.getChildren().add(new ImageView(sensorIcons[0]));
        sensorPane.getChildren().add(new Separator());
        sensorPane.getChildren().add(new Label("test"));
        sensorContainer = sensorPane;
        sensorPane.setMaxWidth(150);
        return sensorPane;
    }
    @Override
    public void updateTile(MqttManager manager)
    {
        updateSensorValues(manager);
        ((Label) ((FlowPane) sensorContainer).getChildren().getLast()).setText(sensorValues.get("distance"));
    }

}
