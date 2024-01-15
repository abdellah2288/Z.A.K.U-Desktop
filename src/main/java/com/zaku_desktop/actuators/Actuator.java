package com.zaku_desktop.actuators;

import com.zaku_desktop.utilities.MiscUtils;
import com.zaku_desktop.utilities.MqttManager;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Actuator
{
    protected HashMap<String, String[]> actuatorParams;
    protected HashMap<String, Slider> actuatorControls;
    protected Image actuatorIcon;
    protected FlowPane actuatorFP;
    protected int actuatorID;
    public Actuator(HashMap<String, String[]> actuatorParams,String iconName)
    {
        this.actuatorParams = actuatorParams;
        try
        {
            this.actuatorIcon = MiscUtils.getImage(iconName);
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    public Node generateTile()
    {
        actuatorFP = new FlowPane();
        actuatorFP.setOrientation(Orientation.HORIZONTAL);
        actuatorFP.setHgap(15);
        actuatorFP.getChildren().add(new ImageView(actuatorIcon));
        actuatorFP.getChildren().add(new Separator());
        for(Map.Entry<String,String[]> entry : actuatorParams.entrySet().stream().sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey())).toList())
        {
            actuatorFP.getChildren().add(new Label(entry.getValue()[0]));

        }
        return actuatorFP;
    }
    public void updateActuatorTile()
    {
        int counter = 2;
        for(Map.Entry<String,String[]> entry : actuatorParams.entrySet().stream().sorted((e1,e2) -> e1.getKey().compareTo(e2.getKey())).toList())
        {
            ((Label) actuatorFP.getChildren().get(counter)).setText(entry.getValue()[0]);
            counter++;
        }
    }
    public HashMap<String,String[]> getActuatorParams()
    {
        return actuatorParams;
    }
    public void updateActuatorData(MqttManager manager)
    {
        for(Map.Entry<String,String[]> entry : actuatorParams.entrySet())
        {
            actuatorParams.put(entry.getKey(), new String[] {manager.getTopicData(entry.getValue()[1]),entry.getValue()[1]});
        }
    }
    public String[] getControls()
    {
        return actuatorControls.keySet().toArray(new String[actuatorControls.keySet().size()]);
    }
    public void publishControls(MqttManager manager)
    {
        for(Map.Entry<String,Slider> entry : actuatorControls.entrySet())
        {
            manager.publish(entry.getKey(),Double.toString(entry.getValue().getValue()));
        }
    }
    public Node[] generateControls()
    {
        ArrayList<Node> actuatorControlList = new ArrayList<>();
        for(Map.Entry<String,Slider> entry : actuatorControls.entrySet())
        {
            actuatorControlList.add(new Label(entry.getKey()));
            actuatorControlList.add(entry.getValue());
            entry.getValue().setMaxWidth(350);
        }
        return actuatorControlList.toArray(new Node[0]);
    }
    public int getActuatorID()
    {
        return actuatorID;
    }
}
