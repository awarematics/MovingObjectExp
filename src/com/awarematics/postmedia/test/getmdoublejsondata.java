package com.awarematics.postmedia.test;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MDouble;

public class getmdoublejsondata {

	public static void main(String args[]) throws IOException {
		for (int test_time = 1; test_time < 2; test_time++) {
			double[] doubleacc;
			long[] timeArray;

			File file = new File("d://mvideo/000f157f-dab3a407" + ".json");// 000f157f-dab3a407//000f8d37-d4c09a0f//c: 00a395fe-d60c0b47
			String content = FileUtils.readFileToString(file, "UTF-8");
			JSONObject jsonObject = new JSONObject(content);
			
			try {
				JSONArray getJsonArray = jsonObject.getJSONArray("accelerometer"); //gyro //accelerometer
				int num = getJsonArray.length();
				doubleacc = new double[num];
				timeArray = new long[num];

				for (int j = 0; j < num; j++) {
					//System.out.println(getJsonArray.get(j).toString());
					String[] array = getJsonArray.get(j).toString().split(":");
					String result_x = array[3].split(",")[0];//2 //3
					String time = array[4].split("\\}")[0];
					doubleacc[j] = Double.valueOf(result_x);
					timeArray[j] = Long.parseLong(time);
				}
				MGeometryFactory geometryFactory = new MGeometryFactory();
				MDouble mb = geometryFactory.createMDouble(doubleacc, timeArray);
				System.out.println(mb.toGeoString());
			} catch (Exception e) {
				continue;
			}
		}
	}

}