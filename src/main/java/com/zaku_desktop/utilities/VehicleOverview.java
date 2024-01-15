package com.zaku_desktop.utilities;

import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class VehicleOverview
{
    static private  double canvasWidth = 400;
    static private  double canvasHeight = 400;
    static private double xOffset = 20;
    static private double yOffset = 20;
    static private double IRwidth = canvasWidth / 20;
    static private double IRheight = canvasHeight / 10;
    static private double sensorCount = 4;
    static private double vehicleWidth = canvasWidth / 2 + 50;
    static private double vehicleHeight = sensorCount * IRheight;
    static private double usWidth = vehicleWidth / 10;
    static private double usHeight = vehicleHeight / 3;
    static private double usEdgeOffset = 10;
    static private double wheelChassisDistance = 1;
    static private double wheelEdgeOffset = 20;
    static private double wheelHeight = 15;
    static private double wheelWidth = vehicleWidth - 2 * wheelEdgeOffset;
    static private final Canvas mainCanvas = new Canvas(400,400);
    static public Node generateOverview()
    {
        drawComponent("body",0,false);
        drawComponent("wheel",0,false);
        drawComponent("wheel",1,false);
        for(int i = 0; i < sensorCount; i++)
        {
            drawComponent("ir",i,false);
            drawComponent("us",i,false);
        }
        return mainCanvas;
    }

    static private void drawComponent(String compName,int compID,boolean fill)
    {
        GraphicsContext mainContext = mainCanvas.getGraphicsContext2D();
        mainContext.setFill(Color.RED);
        mainContext.setStroke(Color.BLACK);
        mainContext.setLineWidth(2);
        double compX;
        double compY;
        switch(compName.toLowerCase())
        {
            case "body":
                if(fill) mainContext.fillRoundRect(xOffset + IRwidth ,yOffset,vehicleWidth,vehicleHeight,10,10);
                else mainContext.strokeRoundRect(xOffset + IRwidth ,yOffset,vehicleWidth,vehicleHeight,10,10);
                break;
            case "us":
                    if(compID % 2 == 0)
                    {
                        compX =xOffset + ((2-compID)/2) * IRwidth + (compID/2) * vehicleWidth + usEdgeOffset - (compID/2) * usWidth;
                        compY =yOffset + (vehicleHeight/2.0) - (usHeight/2.0);
                        if(fill)
                        {
                            mainContext.fillRoundRect(compX,
                                    compY,
                                    usWidth,
                                    usHeight,
                                    10,
                                    10 );
                        }
                        else
                        {
                            mainContext.clearRect(compX,
                                    compY,
                                    usWidth,
                                    usHeight);
                            mainContext.strokeRoundRect(compX,
                                    compY,
                                    usWidth,
                                    usHeight,
                                    10,
                                    10 );
                        }
                    }
                    else
                    {
                        compX =xOffset + (vehicleWidth/2.0) - (usHeight/2.0);
                        compY = yOffset + ((compID - 1)/2.0) * (vehicleHeight - usWidth )+ ( 2 - compID ) * usEdgeOffset;
                        if(fill)
                        {
                            mainContext.fillRoundRect(compX,
                                    compY,
                                    usHeight,
                                    usWidth,
                                    10,
                                    10 );
                        }
                        else
                        {
                            mainContext.clearRect(compX,
                                    compY,
                                    usHeight,
                                    usWidth);
                            mainContext.strokeRoundRect(compX,
                                    compY,
                                    usHeight,
                                    usWidth,
                                    10,
                                    10 );
                        }
                    }

                break;
            case "ir":
                compX = xOffset;
                compY =yOffset + IRheight * compID;
                if(fill) mainContext.fillRoundRect(compX,compY , IRwidth, IRheight, 10, 10);
                else
                {
                    mainContext.clearRect(compX,compY,IRwidth,IRheight);
                    mainContext.strokeRoundRect(compX, compY, IRwidth, IRheight, 10, 10);
                }
                break;
            case "wheel":
                compX = xOffset + IRwidth + wheelEdgeOffset;
                compY =yOffset - (1 - compID) * (wheelHeight - wheelChassisDistance)+ compID * vehicleHeight;
                if(fill)
                {
                    mainContext.fillRoundRect(compX, compY, wheelWidth,wheelHeight,10,10);
                }
                else
                {
                    mainContext.clearRect(compX,compY,wheelWidth,wheelHeight);
                    mainContext.strokeRoundRect(compX, compY, wheelWidth,wheelHeight,10,10);
                }
                break;
            default:
                System.out.println("Unknown component");
                break;
        }
    }
    static public void resizeCanvas(double newWidth,double newHeight)
    {
        canvasWidth = newWidth;
        canvasHeight = newHeight;
        IRwidth = canvasWidth / 20;
        IRheight = canvasHeight / 10;
        sensorCount = 4;
        vehicleWidth = canvasWidth / 1.5;
        vehicleHeight = sensorCount * IRheight;
        usWidth = vehicleWidth / 10;
        usHeight = vehicleHeight / 3;
        wheelWidth = vehicleWidth - 2 * wheelEdgeOffset;
        mainCanvas.getGraphicsContext2D().clearRect(0,0,mainCanvas.getWidth(),mainCanvas.getHeight());
        mainCanvas.setHeight(newHeight);
        mainCanvas.setWidth(newWidth);
        generateOverview();
    }
    static public void updateComponent(String compName,int compID,String value)
    {
        drawComponent(compName,compID,Double.valueOf(value) > 0);
    }
    static public void updateUS(int usID,String value)
    {
        updateComponent("us",usID,value);
    }
    static public void updateWheel(int wheelID,String value)
    {
        updateComponent("wheel",wheelID,value);
    }
}
