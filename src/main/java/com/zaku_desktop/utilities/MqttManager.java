package com.zaku_desktop.utilities;

import com.zaku_desktop.actuators.Actuator;
import com.zaku_desktop.sensors.Sensor;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import org.eclipse.paho.client.mqttv3.*;

import java.util.HashMap;

public class MqttManager implements MqttCallback
{
    private final int maxAttempts = 10;
    private Sensor[] sensorList;
    private Actuator[] actuatorList;
    private  Label mqttStatus = new Label("Status unknown");
    private Button connectButton = new Button("Connect");
    private MqttClient mainClient;
    private HashMap<String,String> mqttMap = new HashMap<>();
    public MqttManager(Sensor[] sensorList,Actuator[] actuatorList)
    {
        this.actuatorList = actuatorList;
        this.sensorList = sensorList;
        for(Actuator actuator : actuatorList)
        {
            for(String[] val : actuator.getActuatorParams().values()) mqttMap.put(val[1],"NaN");
            for(String topic : actuator.getControls()) mqttMap.put(topic,"NaN");
        }
        for(Sensor sensor: sensorList)
        {
            for(String[] keyList : sensor.getSensorKeyParams().keySet()) mqttMap.put(keyList[2],"NaN");
        }
    }
    public boolean getConnectionStatus()
    {
        return mainClient.isConnected();
    }
    public void initMqtt(String brokerURI)
    {
        try
        {
        mainClient = new MqttClient(brokerURI,"zaku-desktop");
        mainClient.setCallback(this);

        }
        catch(MqttException e)
        {
            System.out.println(e.getCause());
        }
    }

    public void connect()
    {
        try
        {
            for(int i = 0; i < maxAttempts;i++)
            {
                if(mainClient.isConnected()) break;
                else mainClient.connect();

            }
        }
        catch(MqttException e)
        {
            System.out.println(e.getCause());
        }
    }
    public void disconnect()
    {
        try
        {
            mainClient.disconnect();
        }
        catch(MqttException e)
        {
            System.out.println(e.getCause());
        }
    }
    public void addTopic(String topic,String defaultValue)
    {
        mqttMap.put(topic,defaultValue);
    }
    public void subscribeToTopics()
    {
        try {
            if(mainClient.isConnected())
            {
                for (String topic : mqttMap.keySet())
                {
                        mainClient.subscribe(topic);
                }
            }
        }
        catch(MqttException e)
        {
            System.out.println(e.getCause());
        }
    }
    public void publish(String topic,String payload)
    {
        mqttMap.put(topic,payload);

        try {
            if(mainClient.isConnected()) mainClient.publish(topic, new MqttMessage(payload.getBytes()));
        }
        catch(MqttException e)
        {
            System.out.println(e.getCause());
        }
    }
    public void updateMqttStatus()
    {
        if(mainClient.isConnected())
        {
            mqttStatus.setText("Connected to " + mainClient.getServerURI());
            connectButton.setOnMousePressed(e -> disconnect());
            connectButton.setText("Disconnect");
        }
        else
        {
            mqttStatus.setText("Disconnected");
            connectButton.setOnMousePressed(e-> connect());
            connectButton.setText("Connect");
        }
    }
    public String getTopicData(String topic)
    {
        return mqttMap.get(topic);
    }
    public Node generateMqttBar()
    {
        FlowPane mqttFPane = new FlowPane();
        mqttFPane.setHgap(20);
        mqttFPane.setAlignment(Pos.BASELINE_RIGHT);
        mqttFPane.getChildren().add(mqttStatus);
        mqttFPane.getChildren().add(connectButton);

        return mqttFPane;
    }
    @Override
    public void connectionLost(Throwable throwable) {

    }

    @Override
    public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception
    {
        mqttMap.put(topic,new  String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
