package com.thk.viewmylog.helper;

import com.thk.viewmylog.entities.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Diese Klasse dient dazu Log-Meldungen zu parsen.
 */
public class LogParser {

    /**
     * Wandelt eine übergebene als String Log-Meldung in eine com.thk.viewmylog.entities.Log-Instanz um.
     * @param msg Log-Meldung im threadtime-Format
     * @return log-Instanz
     */
    public static Log getLogFromMessage(String msg){
        String[] splittedMsg = msg.split(":");

        if(splittedMsg.length>=3){
            Log log = new Log();
            StringBuilder message = new StringBuilder();
            for (int i = 3; i< splittedMsg.length; i++){
                if(i == splittedMsg.length-1){
                    message.append(splittedMsg[i]);
                }else{
                    message.append(splittedMsg[i]).append(":");
                }

            }
            if((message.length() == 0) || message.toString().equals(" ")) return null;
            log.setMessage(message.toString());
            String metaDataRaw = splittedMsg[0]+":"+splittedMsg[1]+ ":"+splittedMsg[2];
            String[] metaData = metaDataRaw.split(" ");
            List<String> metaDataList = new ArrayList<>();
            for (String metaDatum : metaData) {
                if (!metaDatum.equals("")) {
                    metaDataList.add(metaDatum);
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
