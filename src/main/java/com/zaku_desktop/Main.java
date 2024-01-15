package com.zaku_desktop;

import com.zaku_desktop.actuators.Actuator;
import com.zaku_desktop.actuators.DCmotor;
import com.zaku_desktop.actuators.Servo;
import com.zaku_desktop.sensors.USsensor;
import com.zaku_desktop.utilities.ConfigManager;
import com.zaku_desktop.sensors.DHTS;
import com.zaku_desktop.sensors.Sensor;
import com.zaku_desktop.utilities.MqttManager;
import com.zaku_desktop.utilities.VehicleOverview;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Orientation;

import javafx.scene.Node;
import javafx.scene.Scene;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Flow;


public class Main extends Application {
    static public FlowPane dashBoard = new FlowPane(){{
        setOrientation(Orientation.VERTICAL);
        setVgap(10);
        setHgap(10);
    }};
    static public MenuBar topBar;
    static public Sensor[] sensorList;
    static public Actuator[] actuatorList;
    static public FlowPane overviewPane;
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    public  void start(Stage mainStage)
    {
        try {
            sensorList = new Sensor[] {new DHTS() ,new USsensor(), new USsensor(),new USsensor(),new USsensor()};
            actuatorList = new Actuator[] {new DCmotor() ,new DCmotor(),new Servo()};
            topBar = generateMenu();

            Stage camStage = new Stage();
            FlowPane camPane = new FlowPane();
            WebView camView = new WebView();
            camView.getEngine().load("http://192.168.12.147/");
            camPane.getChildren().add(camView);
            camStage.setScene(new Scene(camPane));
            camStage.show();
            MqttManager mainMqttManager = new MqttManager(sensorList,actuatorList);

            mainStage.setScene(new Scene(new BorderPane(), Color.rgb(250, 250, 250)));

            ((BorderPane) mainStage.getScene().getRoot()).setTop(topBar);
            ((BorderPane) mainStage.getScene().getRoot()).setRight(dashBoard);
            ((BorderPane) mainStage.getScene().getRoot()).setLeft(ConfigManager.generateSettings(sensorList));
            ((BorderPane) mainStage.getScene().getRoot()).setCenter(VehicleOverview.generateOverview());
            ((BorderPane) mainStage.getScene().getRoot()).setBottom(mainMqttManager.generateMqttBar());

            mainMqttManager.initMqtt(ConfigManager.getConfigMap(sensorList).get("mqtt_broker_uri"));



            for (Sensor sensor : sensorList)
            {
                if(!(sensor instanceof USsensor))dashBoard.getChildren().add(sensor.generateTile());
            }
            dashBoard.getChildren().add(Sensor.generateCluster(2,Arrays.stream(sensorList).filter(e -> e instanceof USsensor).toArray()));
            dashBoard.getChildren().add(new Separator());
            for(Actuator actuator : actuatorList)
            {
                dashBoard.getChildren().add(actuator.generateTile());
                for(Node node : actuator.generateControls()) dashBoard.getChildren().add(node);
            }


            mainStage.sizeToScene();

            mainStage.setResizable(true);
            mainStage.show();
            /*handle scalable components*/
            mainStage.widthProperty().addListener(
                    (x,oldVal,newVal) -> {
                VehicleOverview.resizeCanvas(newVal.doubleValue() / 4.3 , mainStage.getHeight() / 1.7);
            });
            mainStage.heightProperty().addListener(
                    (x,oldVal,newVal) -> {

                        VehicleOverview.resizeCanvas( mainStage.getWidth() / 4.3 , newVal.doubleValue() / 1.7);
                    });

            mainStage.setMinWidth(mainStage.getWidth());
            mainStage.setMinHeight(mainStage.getHeight());
            new Thread(() ->
            {
                while(true)
                {
                    Platform.runLater(
                            new programLoop()
                            {
                                @Override
                                public void run()
                                {

                                        mainMqttManager.updateMqttStatus();
                                        mainMqttManager.subscribeToTopics();

                                        if(mainMqttManager.getConnectionStatus())
                                        {
                                            for(Actuator actuator : actuatorList)
                                            {
                                                actuator.publishControls(mainMqttManager);
                                                actuator.updateActuatorData(mainMqttManager);
                                                actuator.updateActuatorTile();
                                                if(actuator instanceof DCmotor) VehicleOverview.updateWheel(actuator.getActuatorID(),((DCmotor) actuator).getSpeed());
                                            }
                                            for(Sensor sensor: sensorList)
                                            {
                                                if(sensor instanceof USsensor) VehicleOverview.updateUS(((USsensor) sensor).id,((USsensor) sensor).getDistance());
                                                sensor.updateTile(mainMqttManager);
                                            }
                                        }
                                }
                            }
                    );
                    if (!mainStage.isShowing()) System.exit(0);
                    try
                    {
                        Thread.sleep(166);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
            }
            ).start();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
            //System.out.println(e.getCause());
            for(StackTraceElement element : e.getStackTrace())System.out.println(element);
        }
    }
    static public MenuBar generateMenu()
    {
        MenuBar tempBar = new MenuBar();
        tempBar.setMaxWidth(Double.POSITIVE_INFINITY);
        for(int i = 0; i < 3;i++)tempBar.getMenus().add(new Menu("Test"));
        return tempBar;
    }
}