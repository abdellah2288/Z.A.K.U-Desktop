package com.zaku_desktop.utilities;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MiscUtils
{
    static public Image getImage(String imageName) throws FileNotFoundException
    {
        return new Image(new FileInputStream("/home/abdellah/IdeaProjects/Zaku desktop/static/"+ imageName));
    }
    static public String trimStringToDigits(String originalString)
    {
        String holder = "";
        for(char c : originalString.toCharArray())
        {
            if(Character.isDigit(c))
            {
                holder = holder + c;
            }
        }
        return holder;
    }
}
