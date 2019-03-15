
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.FoV;
import com.awarematics.postmedia.types.mediamodel.MPhoto;

@SuppressWarnings("serial")
public class GetAndPostRange extends HttpServlet {
	private MGeometryFactory mgeometryFactory;
	private GeometryFactory geometryFactory;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
			throws ServletException, IOException, ParseException {

		response.setContentType("text/xml");
		String range = request.getParameter("order_range");
		String temp = GetAndPostData.responseLayer;
		mgeometryFactory = new MGeometryFactory();
		geometryFactory = new GeometryFactory();
		Polygon polygon = null;
		String outputString="";
		String rangeString="";
		range= range.replaceAll(",", "#");
		range= range.replaceAll(" ", ", ");
		range= range.replaceAll("#", " ");
		range="POLYGON (("+range+"))";
		
		WKTReader reader = new WKTReader(geometryFactory);

			try {
				polygon = (Polygon) reader.read(range);
			} catch (org.locationtech.jts.io.ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			String fromtime="";
			String totime="";
		if (temp != null) {
			String[] sss = temp.split(" ");
			// System.out.println("length\t\t"+sss.length);
			for (int i = 3; i < sss.length - 1; i++) {
				temp = "D://mfs/" + sss[i].replace("\"", "");
				System.out.println("root\t\t" + temp);
				String result = "";
				try {
					BufferedReader in = new BufferedReader(new FileReader(temp));
					String str;
					while ((str = in.readLine()) != null) {
						result += str;
					}
				} catch (IOException e) {
				}
				int num=0;
				String type= result.split("\"type\"\\:")[1];
				type = type.split("\"")[1];
				System.out.println("type\t\t"+type);
				
				String data1 = result.split("\\[")[1];
				data1 = data1.split("\\]")[0];
				
				String[] data2 = data1.split("\\},");
				fromtime="";
				totime="";
				
				double[] viewAngle = new double[data2.length];
				double[] distance = new double[data2.length];
				double[] direction = new double[data2.length];
				Coordinate[] coords = new Coordinate[data2.length];
				String[] creationTime = new String[data2.length];
				Polygon[] listPolygon = new Polygon[data2.length];
				FoV[] fov = new FoV[data2.length];
			
				if (type.equals("mphoto")) {
					
					for (int j = 0; j < data2.length; j++) {
						
					
						String[] data3 = data2[j].split("\\:");				
						viewAngle[j] = Double.parseDouble(data3[5].split(",")[0]);						
						distance[j] = Double.parseDouble(data3[1].split(",")[0]);
						direction[j] = Double.parseDouble(data3[11].split("\\}")[0]);					
						coords[j] = new Coordinate();
						coords[j].y = Double.parseDouble(data3[3].split(",")[0]);
						coords[j].x = Double.parseDouble(data3[4].split(",")[0]);
						creationTime[j] = (data3[6].split(",")[0]);
						fov[j] = new FoV();
						fov[j].setDirection(direction[j]);
						fov[j].setDistance(distance[j]);
						fov[j].setViewAngle(viewAngle[j]);
						listPolygon[j] = genFoVArea(coords[j].x, coords[j].y, fov[j]);
						
						if(listPolygon[j].intersects(polygon)||listPolygon[j].contains(polygon)||listPolygon[j].within(polygon)||listPolygon[j].overlaps(polygon)||listPolygon[j].covers(polygon)){
							num=num+1;
							if(num==1) {fromtime = creationTime[j]; totime= creationTime[j];}
							if(num>1) {totime = creationTime[j];}
						}
					}
				}
				if (type.equals("mvideo")) {
					
					for (int j = 0; j < data2.length; j++) {
						
					
						String[] data3 = data2[j].split("\\:");				
						viewAngle[j] = Double.parseDouble(data3[4].split(",")[0]);						
						distance[j] = Double.parseDouble(data3[1].split(",")[0]);
						direction[j] = Double.parseDouble(data3[9].split("\\}")[0]);					
						coords[j] = new Coordinate();
						coords[j].y = Double.parseDouble(data3[2].split(",")[0]);
						coords[j].x = Double.parseDouble(data3[3].split(",")[0]);
						creationTime[j] = (data3[5].split(",")[0]);
						fov[j] = new FoV();
						fov[j].setDirection(direction[j]);
						fov[j].setDistance(distance[j]);
						fov[j].setViewAngle(viewAngle[j]);
						listPolygon[j] = genFoVArea(coords[j].x, coords[j].y, fov[j]);
						
						if(listPolygon[j].intersects(polygon)||listPolygon[j].contains(polygon)||listPolygon[j].within(polygon)||listPolygon[j].overlaps(polygon)||listPolygon[j].covers(polygon)){
							num=num+1;
							if(num==1) {fromtime = creationTime[j]; totime= creationTime[j];}
							if(num>1) {totime = creationTime[j];}
						}
					}
				}
				if (type.equals("mpoint")) {
					
					for (int j = 0; j < data2.length; j++) {
						
					
						String[] data3 = data2[j].split("\\:");				
										
						coords[j] = new Coordinate();
						coords[j].y = Double.parseDouble(data3[1].split(",")[0]);
						coords[j].x = Double.parseDouble(data3[2].split(",")[0]);
						creationTime[j] = (data3[3].split("\\}")[0]);
						 Point point = geometryFactory.createPoint( coords[j] );
						if(polygon.intersects(point)||polygon.contains(point)||polygon.touches(point)||polygon.overlaps(point)||polygon.covers(point)){
							num=num+1;
							if(num==1) {fromtime = creationTime[j]; totime= creationTime[j];}
							if(num>1) {totime = creationTime[j];}
						}
					}
				}
				System.out.println("num\t\t"+num);
				if(num>0)
				{
					rangeString=rangeString+"#"+ sss[i].replace("\"", "")+"@"+type+"@"+fromtime+"@"+totime+"@"+polygon;
				}

			}
		}

		
		outputString = outputString+" "+rangeString;
		System.out.println("outputString\t\t" + outputString);
		PrintWriter out = response.getWriter();
		out.println(outputString);
		out.close();
		// }
			

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get主函数调用processRequest,完成Get方法的参数接受，返回的过程

		try {
			processRequest(request, response, "GET");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// POST主函数调用processRequest,完成POST方法的参数接受，返回的过程
		try {
			processRequest(request, response, "POST");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private Polygon genFoVArea(double x, double y, FoV fov) {
		int times = 1;
		double x4 = (double) x;
		double y4 = (double) y;

		double x2 = (double) x + fov.getDistance() * 2 / Math.sqrt(3)
				* Math.sin(Math.toRadians(fov.getDirection() + (fov.getViewAngle()) / 2)) * times;
		double y2 = (double) y + fov.getDistance() * 2 / Math.sqrt(3)
				* Math.cos(Math.toRadians(fov.getDirection() + (fov.getViewAngle()) / 2)) * times;// left
		double x3 = (double) x + fov.getDistance() * 2 / Math.sqrt(3)
				* Math.sin(Math.toRadians(fov.getDirection() - (fov.getViewAngle()) / 2)) * times;
		double y3 = (double) y + fov.getDistance() * 2 / Math.sqrt(3)
				* Math.cos(Math.toRadians(fov.getDirection() - (fov.getViewAngle()) / 2)) * times;// right
		GeometryFactory geometryFactory = new GeometryFactory();
		Coordinate[] coor1 = new Coordinate[4];
		coor1[0] = new Coordinate(x4, y4);
		coor1[1] = new Coordinate(x2, y2);
		coor1[2] = new Coordinate(x3, y3);
		coor1[3] = new Coordinate(x4, y4);
		LinearRing line = geometryFactory.createLinearRing(coor1);
		Polygon pl1 = geometryFactory.createPolygon(line, null);
		return pl1;
	}

}