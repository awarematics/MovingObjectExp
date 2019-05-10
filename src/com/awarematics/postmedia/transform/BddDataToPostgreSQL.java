package com.awarematics.postmedia.transform;
//import org.postgresql.pljava.annotation.Function;
import java.text.SimpleDateFormat;

public class BddDataToPostgreSQL {
	

	public static String LongToString(long duration) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String timeText = format.format(duration); // long to string
		return timeText;
	}

}