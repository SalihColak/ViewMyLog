package com.thk.viewmylog.helper;

import com.thk.viewmylog.entities.Log;

import java.util.ArrayList;
import java.util.List;

public class LogParser {

    public static Log getLogFromMessage(String msg){
        String[] splittedMsg = msg.split(":");

        if(splittedMsg.length>=3){
            Log log = new Log();
            String message = "";
            for (int i = 3; i< splittedMsg.length; i++){
                if(i == splittedMsg.length-1){
                    message += splittedMsg[i];
                }else{
                    message += splittedMsg[i] +":";
                }

            }
            if(message.isEmpty() || message.equals(" ")) return null;
            log.setMessage(message);
            String metaDataRaw = splittedMsg[0]+":"+splittedMsg[1]+ ":"+splittedMsg[2];
            String[] metaData = metaDataRaw.split(" ");
            List<String> metaDataList = new ArrayList<>();
            for (int k=0; k<metaData.length; k++){
                if(!metaData[k].equals("")){
                    metaDataList.add(metaData[k]);
                }
            }
            metaData = metaDataList.toArray(new String[0]);
            if(metaData.length == 6){
                log.setTime(metaData[0]+" "+metaData[1]);
                log.setPid(Integer.parseInt(metaData[2]));
                log.setTid(Integer.parseInt(metaData[3]));
                log.setLevel(metaData[4]);
                log.setTag(metaData[5]);
                return log;
            }
        }
        return null;
    }
}
