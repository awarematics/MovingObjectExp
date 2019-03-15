package com.awarematics.postmedia.types.mediamodel;

//import org.postgresql.pljava.annotation.Function;
//import static org.postgresql.pljava.annotation.Function.Effects.IMMUTABLE;
//import static org.postgresql.pljava.annotation.Function.OnNullInput.RETURNS_NULL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

public class M_pointprojection {
	//@Function(onNullInput=RETURNS_NULL, effects=IMMUTABLE)
	public static String m_pointprojection(String ss1) throws ParseException {
	String temp="";
	String[] mpointlist = ss1.split("\",\"");
	for(int i=0; i<mpointlist.length;i++)
	{	
		mpointlist[i] = mpointlist[i].replaceAll("\\(\\)","");
		String[] shortlist = mpointlist[i].split(",");
		String mpointString="";
		if(i==0)
		mpointString =  "("+shortlist[2] + " "+ shortlist[3] +" " +StringToLong(shortlist[1]);
		else
		mpointString =  ", ("+shortlist[2] + " "+ shortlist[3] +" " +StringToLong(shortlist[1]); 
		temp = temp+mpointString.replaceAll("\"","");
	}
	
	
	
		return temp;
	}
	public static long StringToLong(String s) throws ParseException{
		String ss = s.replaceAll("\"","");
		ss = ss.replaceAll("\\\\","");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date dt = format.parse(ss);
		return dt.getTime();
	}
	
	public static String createJSONObject(String temp) {
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
        return jsonArray.toString();  
	}
}
