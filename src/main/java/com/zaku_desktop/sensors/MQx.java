package com.zaku_desktop.sensors;

import com.zaku_desktop.utilities.MiscUtils;
import com.zaku_desktop.utilities.MqttManager;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.HashMap;

public class MQx extends Sensor
{
    static int sensorCount = 0;
    private int sensorModel;
    public MQx(int sensorModel)
    {
        super( new HashMap<>()
        {{
            /*Parameter Label, Parameter config entry, Parameter default value, Parameter associated sensor value*/
            put(new String[] {"Gas level topic","mq"+sensorModel+"_"+ sensorCount+"_topic","mq"+ sensorModel +"_"+sensorCount,"gaslvl"},new TextField("mq"+ sensorModel +"_"+sensorCount));
        }},"MQ"+ sensorModel +"_"+sensorCount);
        this.sensorModel = sensorModel;

        try {

            this.sensorIcons = new Image[]{MiscUtils.getImage("mqIcon.png")};
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        sensorValues = new HashMap<>(){{put("gaslvl","NaN");}};
        sensorCount++;
    }
    @Override
    public Node generateTile() {
        FlowPane sensorPane = new FlowPane();
        sensorPane.setOrientation(Orientation.HORIZONTAL);
        sensorPane.setHgap(10);
        sensorPane.getChildren().add(new ImageView(sensorIcons[0]));
        sensorPane.getChildren().add(new Separator());
        sensorPane.getChildren().add(new Label("NaN"));
        sensorContainer = sensorPane;
        sensorPane.setMaxWidth(150);
        return sensorPane;
    }

    @Override
    public void updateTile(MqttManager manager)
    {
        updateSensorValues(manager);
        ((Label) ((FlowPane) sensorContainer).getChildren().getLast()).setText(sensorValues.get("gaslvl"));
    }
}
