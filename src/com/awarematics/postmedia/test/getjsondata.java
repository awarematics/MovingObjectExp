package com.awarematics.postmedia.test;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class getjsondata {
	
	

	public static void main(String args[]) throws IOException {
		 int num_vale = 1;
		for(int test_time = 1; test_time < 2; test_time++)
		{
			 double[] coordinate1_x;
			 double[] coordinate1_y;

			 String[] timeArray;
			 
		File file = new File("d://mvideo/000f8d37-d4c09a0f"+".json");//000f157f-dab3a407   //000f8d37-d4c09a0f
		String content = FileUtils.readFileToString(file, "UTF-8");
		JSONObject jsonObject = new JSONObject(content);		
		try {
			JSONArray getJsonArray = jsonObject.getJSONArray("gps");
			int num = getJsonArray.length();
			coordinate1_x = new double[num];
			coordinate1_y = new double[num];
			//coordinate1_z = new double[num];
			timeArray = new String[num];

			for (int j = 0; j < num; j++) {
				//System.out.println( getJsonArray.get(j).toString());
				String[] array = getJsonArray.get(j).toString().split(":");
				String result_x = array[3].split(",")[0];
				String result_y = array[6].split(",")[0];
				String time =  array[5].split(",")[0];
				coordinate1_x[j] = Double.valueOf(result_x);
				coordinate1_y[j] = Double.valueOf(result_y);
				timeArray[j]= time;
				
				/*
				System.out.println( getJsonArray.get(j).toString());
				String[] array = getJsonArray.get(j).toString().split(":");
				String result_x = array[1].split(",")[0];
				String result_y = array[2].split(",")[0];
				String time =  array[4].split(",")[0].replaceAll("\\}", "");
				String result_z =  array[3].split(",")[0];
				coordinate1_x[j] = Double.valueOf(result_x);
				coordinate1_y[j] = Double.valueOf(result_y);
				coordinate1_z[j] = Double.valueOf(result_z);
				timeArray[j]= time;
				System.out.println( timeArray[j]);*/
			}
			
			String temp = OutputJson.getresult(coordinate1_x,coordinate1_y, timeArray, num_vale);
			//System.out.println(file.toString());
			OutputJson.saveDataToFile( String.valueOf(num_vale) , temp);
			
			
			num_vale++;
			
		}
		 catch(Exception e){
			continue;
		}
		}
	}


	
}