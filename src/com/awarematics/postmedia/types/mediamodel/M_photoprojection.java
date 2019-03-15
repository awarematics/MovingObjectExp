package com.awarematics.postmedia.types.mediamodel;

//import org.postgresql.pljava.annotation.Function;
//import static org.postgresql.pljava.annotation.Function.Effects.IMMUTABLE;
//import static org.postgresql.pljava.annotation.Function.OnNullInput.RETURNS_NULL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class M_photoprojection {
	//@Function(onNullInput=RETURNS_NULL, effects=IMMUTABLE)
	public static String m_photoprojection(String ss1) throws ParseException {
	String temp="";
	String[] mphotolist = ss1.split("\",\"");
	for(int i=0; i<mphotolist.length;i++)
	{	
		mphotolist[i] = mphotolist[i].replaceAll("\\(\\)","");
		String[] shortlist = mphotolist[i].split(",");
		String mphotoString="";
		if(i==0)
		mphotoString = ""+shortlist[0]+" "+shortlist[1]+" "+shortlist[2]+" "+shortlist[3]+" "+shortlist[4]+" "+shortlist[6]+" "+shortlist[8]+" "+shortlist[9]+""+" "+ StringToLong(shortlist[5]);
		else
		mphotoString = ", "+shortlist[0]+" "+shortlist[1]+" "+shortlist[2]+" "+shortlist[3]+" "+shortlist[4]+" "+shortlist[6]+" "+shortlist[8]+" "+shortlist[9]+""+" "+ StringToLong(shortlist[5]); 
		temp = temp+mphotoString.replaceAll("\"","");
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
}
