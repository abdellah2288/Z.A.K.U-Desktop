package com.zaku_desktop.utilities;

import com.zaku_desktop.sensors.Sensor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.time.Clock;
import java.util.Map;

public class CSVgenerator
{
    private static String csvPath;
    private static Sensor[] sensorList;
    public static void initGenerator(Sensor[] _sensorList)
    {
        csvPath = ConfigManager.getConfigEntry("csv_path");
        sensorList = _sensorList;
        System.out.println(csvPath);
    }
    public static void updateGenerator()
    {
        if(ConfigManager.getConfigEntry("autogen_csv").toLowerCase().equals("true"))
        {
            csvPath = ConfigManager.getConfigEntry("csv_path");
            File file = new File(csvPath);

            try
            {
                if(!file.isFile())
                {
                    if(!file.getParentFile().isDirectory()) file.getParentFile().mkdirs();
                    file.createNewFile();
                }

                Writer writer = new PrintWriter(new FileOutputStream(file,true));
                for (Sensor sensor : sensorList)
                {
                    String lineToInsert = Clock.systemUTC().instant() + "," + sensor.getSensorName();
                    for (Map.Entry<String, String> entry : sensor.getSensorValues().entrySet()) {
                        lineToInsert = lineToInsert + "," + entry.getKey() + "," + entry.getValue();
                    }
                    lineToInsert = lineToInsert + "\n";
                    writer.append(lineToInsert);
                }
                writer.close();
            }
            catch(Exception e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
