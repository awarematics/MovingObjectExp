package com.awarematics.postmedia.transform;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
//import org.postgresql.pljava.annotation.Function;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;

import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MDouble;
import com.awarematics.postmedia.types.mediamodel.MPoint;
import com.awarematics.postmedia.types.mediamodel.MVideo;

public class BddDataToPostgreSQL {
	static double[] coordinate1_x;
	static double[] coordinate1_y;
	static long[] timeArray;
	static double[] doubleaccx;
	static double[] doubleaccy;
	static double[] doubleaccz;
	static double[] doublegyrox;
	static double[] doublegyroy;
	static double[] doublegyroz;
	static MPoint mp = null;
	static MVideo mv = null;
	static MDouble mb = null;
	//int num = 1;

	public static void main(String args[]) throws IOException {
		String[] result = BddDataToPostgre("D://train/", "mpoint");
		String writerContent = "";
		
		for (int i = 0; i < result.length; i++) {
			writerContent = writerContent + " UPDATE usertrajs \r\n SET mt  = append(mt , '" + result[i]
					+ "')\r\n" + " WHERE id = " + (i + 1) + ";\r\n\n\n";
		}
		File file = new File("D:\\mpoint.txt");
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter writer = new FileWriter(file);
		writer.write(writerContent);
		writer.flush();
		writer.close();
	}

	// @Function
	public static String[] BddDataToPostgre(String uri, String type) throws IOException {
		String[] bdd = null;
		ArrayList<String> bddString = new ArrayList<String>();
		for (int numof = 1; numof <= 100; numof++) {
			long[] timeArray;
			try {
				File file = new File(uri + "/1 (" + numof + ").json");
				String content = FileUtils.readFileToString(file, "UTF-8");
				JSONObject jsonObject = new JSONObject(content);
				JSONArray getJsonArray = jsonObject.getJSONArray("gps");
				int num = getJsonArray.length();
				if(num>0)
				{
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
				}
				MGeometryFactory geometryFactory = new MGeometryFactory();
				mp = geometryFactory.createMPoint(coordinate1, timeArray);
				if (mp != null) {
					bddString.add(mp.toGeoString());
				}
				}
				else continue;
			} catch (Exception e) {
				//System.out.println(numof);
				continue;
				// e.printStackTrace();
			}
		}
		bdd = new String[bddString.size()];
		for (int i = 0; i < bdd.length; i++)
			bdd[i] = bddString.get(i);
		return bdd;

	}
}