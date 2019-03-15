package com.awarematics.postmedia.types.mediamodel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class M_Getmphotojson {
	public static String m_getmphotojson(String temp) {
        String mphoto = temp.replace("MPHOTO ", "");    
        String[] array = mphoto.split(", ");
        System.out.println(array.length);
        JSONArray jsonArray = new JSONArray();
        for( int i=0; i<array.length;i++ )
        {
        	String[] column = array[i].replaceAll("\\(|\\)","").split(" ");
        	JSONObject data = new JSONObject(); 
        	 data.put("uri", column[0]);  
             data.put("vangle", column[1]);  
             data.put("hangle", column[2]);
             data.put("distance", column[3]);  
             data.put("direction", column[4]);   
             data.put("attitude", column[5]);  
             data.put("x", column[6]);  
             data.put("y", column[7]); 
             data.put("stamp", column[8]); 
             jsonArray.add(i, data);  
        }        
      //  String s = 
        return jsonArray.toString();  
	}

}
