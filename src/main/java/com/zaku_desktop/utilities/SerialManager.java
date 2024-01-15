package com.zaku_desktop.utilities;

import com.fazecast.jSerialComm.SerialPort;
import javafx.scene.control.ComboBox;
import javafx.util.Pair;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;

public class SerialManager
{
    static public String selectedPort = null;
    static public ComboBox<Pair<String, String>> genSerialList()
    {
        ComboBox<Pair<String, String>> comBox = new ComboBox();
        comBox.setOnMousePressed(e -> SerialManager.refreshSerialList(comBox));
        List<Pair<String, String>> comPorts = new ArrayList<>();
        SerialPort[] availablePorts = SerialPort.getCommPorts();
        for(SerialPort port : availablePorts)
        {
            comPorts.add(new Pair<>(port.getSystemPortName(),port.getSystemPortPath()));
        }

        comBox.getItems().addAll(comPorts);
        comBox.setConverter( new StringConverter<Pair<String,String>>() {
            @Override
            public String toString(Pair<String, String> pair)
            {
                return pair == null ? null : pair.getKey();
            }

            @Override
            public Pair<String, String> fromString(String string)
            {
                return null;
            }
        });

        return comBox;
    }
    static public void refreshSerialList(ComboBox serialCBox)
    {
        List<Pair<String, String>> comPorts = new ArrayList<>();
        SerialPort[] availablePorts = SerialPort.getCommPorts();
        for(SerialPort port : availablePorts)
        {
            comPorts.add(new Pair<>(port.getSystemPortName(),port.getSystemPortPath()));
        }
        serialCBox.getItems().clear();
        serialCBox.getItems().addAll(comPorts);
    }
}
