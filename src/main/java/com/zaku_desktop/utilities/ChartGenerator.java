package com.zaku_desktop.utilities;

import com.zaku_desktop.sensors.Sensor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Pair;

import java.time.Clock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartGenerator
{
    static int sampleCount = 0;
    private static Map<Sensor, XYChart.Series> sensorSeriesMap = new HashMap<>();
    static final NumberAxis xAxis = new NumberAxis();
    static final NumberAxis yAxis = new NumberAxis();
    private static LineChart<Number,Number> mainChart = new LineChart<>(xAxis,yAxis);
    static public void addSensor(Sensor sensor)
    {
        yAxis.setLabel("Sensor Level");
        xAxis.setLabel("Sample count");
        XYChart.Series tempSeries = new XYChart.Series();
        tempSeries.setName(sensor.sensorName);
        sensorSeriesMap.put(sensor, tempSeries);
        mainChart.getData().add(sensorSeriesMap.get(sensor));

    }
    static public void updateChart()
    {
        for(Sensor sensor : sensorSeriesMap.keySet())
        {
            sensorSeriesMap.get(sensor).getData().add(new XYChart.Data<>(sampleCount,Long.valueOf(MiscUtils.trimStringToDigits((String) sensor.getSensorValues().values().toArray()[0]))));
        }
        sampleCount++;
    }
    static public LineChart<Number,Number> getMainChart()
    {
        return mainChart;
    }

}
