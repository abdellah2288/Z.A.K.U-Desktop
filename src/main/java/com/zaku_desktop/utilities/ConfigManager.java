package com.zaku_desktop.utilities;

import com.zaku_desktop.actuators.Actuator;
import com.zaku_desktop.actuators.DCmotor;
import com.zaku_desktop.actuators.Servo;
import com.zaku_desktop.sensors.Sensor;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.util.Pair;


import java.io.*;
import java.util.*;

public class ConfigManager
{
    static private Stage mainConfStage = new Stage();
    static private Accordion mainAccordion = new Accordion();
    static private HashMap<String,Node> configMap = new HashMap<>();
    static private  HashMap<String,String> parsedConfig;
    static private Sensor[] _sensorList;
    static private HashMap<String[], Node> generalSettings = new HashMap<>()
    {{
        put(new String[] {"ESP Controller Com Port: ","esp_controller_com",""}, SerialManager.genSerialList());
       put(new String[] {"CSV file path: ","csv_path",System.getenv("HOME") + "/output.csv"},new TextField(System.getenv("HOME") + "/output.csv"));
       put(new String[] {"","autogen_csv","false"},new CheckBox("Autogen CSV file: "));
    }};
    static private HashMap<String[],Node> mqttSettings = new HashMap<>()
    {{
        put(new String[] {"MQTT broker address: ","mqtt_broker_uri",""}, new TextField());
        put(new String[] {"MQTT username: ","mqtt_username",""}, new TextField());
        put(new String[] {"MQTT password: ","mqtt_password",""}, new TextField());
    }};
    static public Node generateSettings(Sensor[] sensorList)
    {
        _sensorList = sensorList;
        populateSettings();
        mainConfStage.sizeToScene();
        mainConfStage.setMinHeight(600);
        mainConfStage.setMinWidth(400);
        ConfigManager.matchConfig();
        mainAccordion.setExpandedPane(mainAccordion.getPanes().get(1));
        return mainAccordion;
    }
    static private void populateSettings()
    {

        /*Generate main settings*/
        generateConfigPane(generalSettings,"General");
        /*Generate mqtt settings*/
        generateConfigPane(mqttSettings,"MQTT");
        /*Generate sensor settings*/
        for(Sensor sensor : _sensorList)
        {
            generateConfigPane(sensor.getSensorKeyParams(),sensor.getSensorName());
        }
        mainConfStage.setScene(new Scene(mainAccordion));
    }
    static public void generateConfigPane(Map<String[],Node> contentMap,String pageName)
    {
        FlowPane contentFPane = new FlowPane();
        contentFPane.setOrientation(Orientation.VERTICAL);
        contentFPane.setVgap(10);
        for(Map.Entry<String[],Node> entry : contentMap.entrySet().stream().sorted((e1,e2) -> e1.getKey()[0].compareTo(e2.getKey()[0])).toList())
        {
            if(entry.getValue() instanceof TextField) ((TextField) entry.getValue()).setMinWidth(400);
            if(entry.getKey()[0].length() > 0) contentFPane.getChildren().add(new Label(entry.getKey()[0]));
            contentFPane.getChildren().add(entry.getValue());
        }
        contentFPane.getChildren().add(new Button("Apply"){{setOnMousePressed(e -> ConfigManager.applyConfig());}});

        addConfigPane(new TitledPane(pageName,contentFPane));
    }
    static public void addConfigPane(TitledPane pane)
    {
        mainAccordion.getPanes().add(pane);
    }
    /**
     * @brief synchronises config across all sensors to match user defined values
     */
    static public void matchConfig()
    {
        if(_sensorList!=null)
        {
            for(Sensor sensor : _sensorList) generateConfigMap(sensor.getSensorKeyParams());
        }
        generateConfigMap(generalSettings);
        generateConfigMap(mqttSettings);
        try
        {
            parsedConfig = Parser.parseFile(System.getenv("HOME" )+ "/.config/zaku/config.conf" , configMap.keySet().stream().toList());
            for(String key : parsedConfig.keySet())
            {
                for(Sensor sensor : _sensorList) sensor.updateSensorConfig(key, parsedConfig.get(key));

                if(configMap.get(key) instanceof TextField)
                {

                    ((TextField) configMap.get(key)).setText(parsedConfig.get(key));
                }

                else if(configMap.get(key) instanceof CheckBox) ((CheckBox) configMap.get(key)).setSelected(parsedConfig.get(key).toLowerCase().equals("true"));
            }
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    static public void generateConfigMap(Map<String[],Node> confMap)
    {
        for(Map.Entry<String[],Node> configEntry : confMap.entrySet())
        {
            configMap.put(configEntry.getKey()[1],configEntry.getValue());
        }
    }
    static public void applyConfig()
    {
        try
        {
            Writer writer = new PrintWriter(new FileOutputStream(createConf()));

            for(Map.Entry<String,Node> entry : configMap.entrySet())
            {
                if(entry.getValue() instanceof TextField)
                {
                    writer.write(entry.getKey() + " = " + ((TextField) entry.getValue()).getText() + ";\n\n");
                }
                else if(entry.getValue() instanceof CheckBox)
                {
                    writer.write(entry.getKey() + " = " + (((CheckBox) entry.getValue()).isSelected() ? "true" : "false") + ";\n\n");
                }
                else if(entry.getValue() instanceof ComboBox<?>)
                {
                    ComboBox<Pair<String,String>> Cbox =(ComboBox) entry.getValue();
                    if(Cbox.getSelectionModel() != null && Cbox.getSelectionModel().getSelectedItem() != null)
                    {
                        SerialManager.selectedPort = Cbox.getSelectionModel().getSelectedItem().getValue();
                    }
                    else SerialManager.selectedPort = null;
                }
            }
            writer.close();
            matchConfig();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
    static private File createConf() throws IOException
    {
        File file = new File(System.getenv("HOME") + "/.config/zaku/config.conf");
        file.getParentFile().mkdirs();
        file.createNewFile();
        return file;
    }
    static public Map<String,String> getConfigMap(Sensor[] sensorList)
    {
        matchConfig();
        HashMap<String,String> confMap = new HashMap<>();
        for(Map.Entry<String,Node> configEntry : configMap.entrySet() )
        {
            if(configEntry.getValue() instanceof TextField) confMap.put(configEntry.getKey(), ((TextField) configEntry.getValue()).getText());
            else if(configEntry.getValue() instanceof CheckBox) confMap.put(configEntry.getKey(),
                    ((CheckBox) configEntry.getValue()).isSelected() ? "true":"false");
        }
        return confMap;
    }
    static public String getConfigEntry(String entry)
    {
        return parsedConfig.get(entry);
    }
}
