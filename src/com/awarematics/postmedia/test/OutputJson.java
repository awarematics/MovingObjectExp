package com.awarematics.postmedia.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import org.json.JSONArray;
import org.json.JSONObject;

public class OutputJson {

	private static JSONArray jsonArray;
	//private static JSONArray jsonArray2;

	public static String getresult(double[] x, double[] y,String[] time, int file) {
		jsonArray = new JSONArray();
		//jsonArray2 = new JSONArray();
		JSONObject outData = new JSONObject();
		outData.put("mpoint", jsonArray);
		//outData.put("mvideo_uri", jsonArray2);
		
		double[] direction = new double[x.length]; 
		// straight direction in front of dashcam
		//direction = printDirection(x,y);
		
		// random direction 30% 30% 20% 20%
		direction = printRandomDirection(x,y);
		for (int i = 0; i < x.length; i++) {
			JSONObject data = new JSONObject();
			//data.put("vangle", 60);
			//data.put("width", 1280);
		//data.put("height", 720);
			//data.put("distance", 0.001);
			//data.put("direction", direction[i]);
			data.put("time", time[i]);
			data.put("x", x[i]);
			data.put("y", y[i]);
			// data.put("polygon",p[i].toText());
			//JSONObject data2 = new JSONObject();
			//String datastring = "file://D://mvideo/00a395fe-d60c0b47.mov?t=" + (i+1);//file://D://mvideo/000f8d37-d4c09a0f.mov?t=40
			//data.put("uri", datastring);
			jsonArray.put(data);
			//jsonArray2.put(data2);
		}
		//outData.put("id", file + ".json");
		return outData.toString();

	}

	public static void saveDataToFile(String fileName, String data) {
		BufferedWriter writer = null;
		File file = new File("d:\\mfs\\mpoint" + "1" + ".json");
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF-8"));
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	// print direction-------------
	public static double[] printDirection(double[] x, double[] y) {
		double[] k = new double[x.length];
		double start_x = x[0];
		double start_y = y[0];
		
		double[] result = new double[x.length];
		/*
		 * k[0]  start point maybe is a stop point for several seconds
		 */
		int num =0;
		for(int i=1;i<x.length;i++){

			if((x[i]!=start_x||y[i]!=start_y)&& num==0)
			{
				k[0] =  Math.asin(y[i] -start_y)/Math.sqrt(((x[i] -start_x) * (x[i] -start_x)) + (y[i] - start_y) * (y[i] - start_y)); num=1;;
			}
			result[0] = Double.valueOf((k[0]* 180 / Math.PI));
		}
		for (int i = 1; i < x.length; i++) {
			if( Math.sqrt(((x[i] -x[i-1]) * (x[i] - x[i-1])) + (y[i] - y[i-1]) * (y[i] - y[i-1]))!=0){
				k[i] = Math.asin(y[i] - y[i-1])/ Math.sqrt(((x[i] -x[i-1]) * (x[i] - x[i-1])) + (y[i] - y[i-1]) * (y[i] - y[i-1]));
			}
			else
			{
				k[i]=k[i-1];
			}
			//DecimalFormat df = new DecimalFormat("0.000000000");df.format
			result[i] = Double.valueOf((k[i]* 180 / Math.PI));
		}
		return result;
	}
	
	public static double[] printRandomDirection(double[] x, double[] y) {
		double[] k = new double[x.length];
		double start_x = x[0];
		double start_y = y[0];
		
		double[] result = new double[x.length];
		/*
		 * k[0]  start point maybe is a stop point for several seconds
		 */
		int num =0;
		for(int i=1;i<x.length;i++){

			if((x[i]!=start_x||y[i]!=start_y)&& num==0)
			{
				k[0] =  Math.asin(y[i] -start_y)/Math.sqrt(((x[i] -start_x) * (x[i] -start_x)) + (y[i] - start_y) * (y[i] - start_y)); num=1;;
			}
			result[0] = Double.valueOf((k[0]* 180 / Math.PI));
		}
		for (int i = 1; i < x.length; i++) {
			if( Math.sqrt(((x[i] -x[i-1]) * (x[i] - x[i-1])) + (y[i] - y[i-1]) * (y[i] - y[i-1]))!=0){
				k[i] = Math.asin(y[i] - y[i-1])/ Math.sqrt(((x[i] -x[i-1]) * (x[i] - x[i-1])) + (y[i] - y[i-1]) * (y[i] - y[i-1]));
			}
			else
			{
				k[i]=k[i-1];
			}
			/*
			 * random direction 30% 30% 20% 20%
			 */
			if( i >= x.length*0.2 && i < x.length*0.4 )
				result[i] = Double.valueOf((k[i]* 180 / Math.PI))+90;
			else if( i >= x.length*0.4 && i < x.length*0.7 )
				result[i] = Double.valueOf((k[i]* 180 / Math.PI))+180;
			else if( i >= x.length*0.7 )
				result[i] = Double.valueOf((k[i]* 180 / Math.PI))+270;
			//System.out.println(result[i]);
		}
		return result;
	}

}
