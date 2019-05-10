package com.awarematics.postmedia.transform;
//import org.postgresql.pljava.annotation.Function;
import java.text.SimpleDateFormat;

public class JavaToPostgreType {
	// @Function
	public static String MakeMPointCoordinate(String toWhom) {
		
		toWhom = toWhom.replace("MPOINT (", "");
		String result = "";
		String[] split1 = toWhom.split(",");
		for (int i = 0; i < split1.length; i++) {
			String temp = split1[i].split("\\)")[0].replaceAll(" \\(", "");
			temp = temp.replace(" ", ", ");
			if (i == 0)
				result = result + "(" + temp + ")";
			else
				result = result + ";" + "(" + temp + ")";
		}
		result = result.replace("((", "(");
		return result;
	}

	// @Function
	public static String MakeMPointTime(String toWhom) {
		toWhom = toWhom.replace("MPOINT (", "");
		String result = "";
		String[] split1 = toWhom.split(",");
		for (int i = 0; i < split1.length; i++) {
			if (i == 0)
				result = result + "'" + LongToString(Long.parseLong(split1[i].split("\\) ")[1].replaceAll("\\)", "")))
						+ "'";
			else
				result = result + ", '" + LongToString(Long.parseLong(split1[i].split("\\) ")[1].replaceAll("\\)", "")))
						+ "'";
		}
		return result;
	}

	public static String LongToString(long duration) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String timeText = format.format(duration); // long to string
		return timeText;
	}

	// @Function
	public static String MakeMoubleDouble(String toWhom) {

		toWhom = toWhom.replace("MDOUBLE (", "");
		String result = "";
		String[] split1 = toWhom.split(", ");
		for (int i = 0; i < split1.length; i++) {
			String temp = split1[i].split(" ")[0];
			if (i == 0)
				result = result + temp;
			else
				result = result + "; " + temp;
		}
		return result;
	}

	// @Function
	public static String MakeMoubleTime(String toWhom) {
		toWhom = toWhom.replace("MDOUBLE (", "");
		String result = "";
		String[] split1 = toWhom.split(", ");
		for (int i = 0; i < split1.length; i++) {
			if (i == 0)
				result = result + "'{" + LongToString(Long.parseLong(split1[i].split(" ")[1].replaceAll("\\)", "")))
						+ "}'";
			else
				result = result + ", '{" + LongToString(Long.parseLong(split1[i].split(" ")[1].replaceAll("\\)", "")))
						+ "}'";
		}
		return result;
	}

}