package com.awarematics.postmedia.test;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MPoint;

public class getmpointjsondata {

	public static void main(String args[]) throws IOException {
		for (int test_time = 1; test_time < 2; test_time++) {
			double[] coordinate1_x;
			double[] coordinate1_y;
			long[] timeArray;

			File file = new File("d://mvideo/00a395fe-d60c0b47" + ".json");// b 000f157f-dab3a407//a 000f8d37-d4c09a0f//00a395fe-d60c0b47
			String content = FileUtils.readFileToString(file, "UTF-8");
			JSONObject jsonObject = new JSONObject(content);
			
			try {
				JSONArray getJsonArray = jsonObject.getJSONArray("gps");
				int num = getJsonArray.length();
				coordinate1_x = new double[num];
				coordinate1_y = new double[num];
				Coordinate[] coordinate1 = new Coordinate[num];
				timeArray = new long[num];

				for (int j = 0; j < num; j++) {
					String[] array = getJsonArray.get(j).toString().split(":");
					String result_x = array[3].split(",")[0];
					String result_y = array[6].split(",")[0];
					String time = array[5].split(",")[0];
					coordinate1_x[j] = Double.valueOf(result_x);
					coordinate1_y[j] = Double.valueOf(result_y);
					timeArray[j] = Long.parseLong(time);
					coordinate1[j] = new Coordinate();
					coordinate1[j].x = coordinate1_x[j];
					coordinate1[j].y = coordinate1_y[j];
					/*
					 * System.out.println( getJsonArray.get(j).toString());
					 * String[] array =
					 * getJsonArray.get(j).toString().split(":"); String
					 * result_x = array[1].split(",")[0]; String result_y =
					 * array[2].split(",")[0]; String time =
					 * array[4].split(",")[0].replaceAll("\\}", ""); String
					 * result_z = array[3].split(",")[0]; coordinate1_x[j] =
					 * Double.valueOf(result_x); coordinate1_y[j] =
					 * Double.valueOf(result_y); coordinate1_z[j] =
					 * Double.valueOf(result_z); timeArray[j]= time;
					 * System.out.println( timeArray[j]);
					 */
				}
				MGeometryFactory geometryFactory = new MGeometryFactory();

				MPoint mp = geometryFactory.createMPoint(coordinate1, timeArray);
				System.out.println(mp.toGeoString());
				// String temp =
				// OutputJson.getresult(coordinate1_x,coordinate1_y, timeArray,
				// num_vale);
				// System.out.println(file.toString());
				// OutputJson.saveDataToFile( String.valueOf(num_vale) , temp);
			} catch (Exception e) {
				continue;
			}
		}
	}

}