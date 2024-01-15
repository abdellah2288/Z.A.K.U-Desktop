package com.zaku_desktop.utilities;
import com.fazecast.jSerialComm.SerialPort;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Parser
{
    static public HashMap<String,String> parseFile(String filePath, List<String> keys) throws  IOException
    {
        FileInputStream inputStream = new FileInputStream(filePath);
        return  streamToMap(inputStream.readAllBytes(),";",keys);
    }
    static public List<Pair<String,String>> parseSerial(SerialPort port, List<String> keys) throws IOException
    {
        if(!port.isOpen()) return null;
        return streamToList(port.getInputStream().readAllBytes(), "\\s",keys);
    }

    static private HashMap<String,String> streamToMap(byte[] inputStream, String regex, List<String> keys)
    {
        HashMap<String,String> parsedTokens = new HashMap<>();
        String[] streamContents = new String(inputStream).split(regex,-1);

        for(String configWord : streamContents)
        {
            String[] configLine = configWord.replaceAll("\\s","").split("=",-1);
            if(keys.contains(configLine[0]))
            {
                parsedTokens.put(configLine[0], configLine[1]);
            }
        }
        return parsedTokens;
    }
    static private List<Pair<String,String>> streamToList(byte[] inputStream, String regex, List<String> keys)
    {
        ArrayList<Pair<String,String>> parsedTokens = new ArrayList<>();

        String[] streamContents = new String(inputStream).split(regex,-1);

        Iterator iterator = Arrays.stream(streamContents).iterator();

        while(iterator.hasNext())
        {
            String word = (String) iterator.next();
            if(keys.contains(word) && iterator.hasNext())
            {
                parsedTokens.add(new Pair<String,String>(word,(String) iterator.next()));
            }
        }
        return parsedTokens;
    }
}
