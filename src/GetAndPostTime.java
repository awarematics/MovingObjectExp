
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

import com.awarematics.postmedia.algorithms.distance.MovingDistance;
import com.awarematics.postmedia.algorithms.similarity.MHausdorff;
import com.awarematics.postmedia.algorithms.similarity.MLCSS;
import com.awarematics.postmedia.algorithms.similarity.MLCVS;
import com.awarematics.postmedia.algorithms.similarity.MLCVSwithMBR;
import com.awarematics.postmedia.algorithms.similarity.MLCVSwithMBT;
import com.awarematics.postmedia.algorithms.similarity.MTrajHaus;
import com.awarematics.postmedia.compareSECONDO.Distance;
import com.awarematics.postmedia.compareSECONDO.Intersects;
import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.FoV;
import com.awarematics.postmedia.types.mediamodel.MGeometry;
import com.awarematics.postmedia.types.mediamodel.MPolygon;

@SuppressWarnings("serial")
public class GetAndPostTime extends HttpServlet {
	private MGeometryFactory mgeometryFactory;
	private GeometryFactory geometryFactory;
	MLCSS lcss;
	MLCVS lcvs;
	MLCVSwithMBT lcvsmbt;
	MLCVSwithMBR lcvsmbr;
	MHausdorff haus;
	MTrajHaus trajhaus;
	private BufferedReader in;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
			throws ServletException, IOException, ParseException {

		response.setContentType("text/xml");
		String temp = request.getParameter("order3");
		// System.out.println("order3\t\t" + temp);
		mgeometryFactory = new MGeometryFactory();
		geometryFactory = new GeometryFactory();
		WKTReader reader = new WKTReader(geometryFactory);
		MWKTReader mreader = new MWKTReader(mgeometryFactory);
		MGeometry mg = null;
		ArrayList<MGeometry> mglist = new ArrayList<MGeometry>();
		String outputString = "";
		String type = "";

		if (temp != null) {
			String[] sss = temp.split(" ");
			// System.out.println("length\t\t"+sss.length);//0-6 3 4 6
			String function = sss[sss.length - 2];
			String parameter = sss[sss.length - 1];
			// System.out.println("function name\t\t" + function);
			MGeometry mg1 = null;
			Object mg2 = null;
			if (temp.contains("Algorithm")) {
				String ttemp ="";
				for (int i = 3; i < sss.length - 3; i++) {
					temp = sss[i].replace("\"", "");
					//System.out.println("root\t\t" + temp);
					ttemp += temp +" ";

				}
					type = "";

					if (temp.contains("MPOINT"))
						type = "mpoint";
					if (temp.contains("MPOLYGON"))
						type = "mpolygon";
					if (temp.contains("MDOUBLE"))
						type = "mdouble";
					if (temp.contains("MVIDEO"))
						type = "mvideo";
					if (temp.contains("MPHOTO"))
						type = "mphoto";
					if (temp.contains("POINT"))
						type = "point";
					if (temp.contains("POLYGON"))
						type = "polygon";
					if (temp.contains("LINESTRING"))
						type = "linestring";
					ttemp= ttemp.substring(0,ttemp.length()-1);
					System.out.println(ttemp);
						try {
							if (temp.contains("POINT") || temp.contains("POLYGON") || temp.contains("LINESTRING"))
								mg2 = reader.read(ttemp);
							else
								mg2 = mreader.read(ttemp);
						} catch (org.locationtech.jts.io.ParseException e) {
							e.printStackTrace();
						}
System.out.println(mg2.toString());


			} else {
				for (int i = 3; i < sss.length - 3; i++) {
					temp = "D://mfs/" + sss[i].replace("\"", "");
					System.out.println("root\t\t" + temp);
					String result = "";
					try {
						in = new BufferedReader(new FileReader(temp));
						String str;
						while ((str = in.readLine()) != null) {
							result += str;
						}
					} catch (IOException e) {
					}
					// int num = 0;
					// System.out.println(result);
					type = "";
					if (result.contains("mpoint"))
						type = "mpoint";
					if (result.contains("mpolygon"))
						type = "mpolygon";
					if (result.contains("mdouble"))
						type = "mdouble";
					if (result.contains("mvideo"))
						type = "mvideo";
					if (result.contains("mphoto"))
						type = "mphoto";
					// System.out.println("type\t\t" + type);

					String data1 = result.split(type + "\":\\[")[1];
					data1 = data1.split("\\]")[0];
					// System.out.println(data1+"\t data1");
					String[] data2 = data1.split("\\},");

					double[] viewAngle = new double[data2.length];
					double[] distance = new double[data2.length];
					double[] direction = new double[data2.length];
					Coordinate[] coords = new Coordinate[data2.length];
					long[] creationTime = new long[data2.length];
					Polygon[] listPolygon = new Polygon[data2.length];
					FoV[] fov = new FoV[data2.length];

					String[] uri = new String[data2.length];
					double[] width = new double[data2.length];
					double[] height = new double[data2.length];
					double[] verticalAngle = new double[data2.length];
					double[] direction3d = new double[data2.length];
					double[] altitude = new double[data2.length];
					String[] annotationJson = new String[data2.length];
					String[] exifJson = new String[data2.length];
					if (type.equals("mphoto")) {
						for (int j = 0; j < data2.length; j++) {
							String[] data3 = data2[j].split("\\:");
							viewAngle[j] = Double.parseDouble(data3[5].split(",")[0]);
							distance[j] = Double.parseDouble(data3[1].split(",")[0]);
							direction[j] = Double.parseDouble(data3[11].split("\\}")[0]);
							coords[j] = new Coordinate();
							coords[j].y = Double.parseDouble(data3[3].split(",")[0]);
							coords[j].x = Double.parseDouble(data3[4].split(",")[0]);

							String longtime = data3[6].split(",")[0].replace("\"", "");
							creationTime[j] = Long.parseLong(longtime);
							fov[j] = new FoV();
							fov[j].setDirection(direction[j]);
							fov[j].setDistance(distance[j]);
							fov[j].setViewAngle(viewAngle[j]);
							fov[j].setCoord(coords[j]);
							listPolygon[j] = genFoVArea(coords[j].x, coords[j].y, fov[j]);
							uri[j] = (data3[7].split(",")[0] + ":" + data3[8].split(",")[0] + ":"
									+ data3[9].split(",")[0]);
							width[j] = Double.parseDouble(data3[2].split(",")[0]);
							height[j] = Double.parseDouble(data3[10].split(",")[0]);
							verticalAngle[j] = 0;
							direction3d[j] = 0;
							altitude[j] = 0;
							annotationJson[j] = "annotationJson.json";
							exifJson[j] = "exifJson.json";
						}
						mg = mgeometryFactory.createMPhoto(uri, width, height, viewAngle, verticalAngle, distance,
								direction, direction3d, altitude, annotationJson, exifJson, coords, creationTime,
								listPolygon, fov);
						mglist.add(mg);
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
							creationTime[j] = Long.parseLong(data3[5].split(",")[0].replace("\"", ""));
							fov[j] = new FoV();
							fov[j].setDirection(direction[j]);
							fov[j].setDistance(distance[j]);
							fov[j].setViewAngle(viewAngle[j]);
							fov[j].setCoord(coords[j]);
							listPolygon[j] = genFoVArea(coords[j].x, coords[j].y, fov[j]);
							uri[j] = data3[6].split(",")[0] + ":" + data3[7].split(",")[0] + ":"
									+ data3[8].split(",")[0];
							verticalAngle[j] = 0;
							direction3d[j] = 0;
							altitude[j] = 0;
							annotationJson[j] = "annotationJson.json";
							exifJson[j] = "exifJson.json";
						}
						mg = mgeometryFactory.createMVideo(uri,viewAngle, verticalAngle, distance,
								direction, direction3d, altitude, annotationJson, exifJson, coords, creationTime,
								listPolygon, fov);
						mglist.add(mg);
					}
					if (type.equals("mpoint")) {
						for (int j = 0; j < data2.length; j++) {
							String[] data3 = data2[j].split("\\:");
							coords[j] = new Coordinate();
							coords[j].y = Double.parseDouble(data3[1].split(",")[0]);
							coords[j].x = Double.parseDouble(data3[2].split(",")[0]);
							creationTime[j] = Long.parseLong(data3[3].split("\"")[1]);
							// Point point =
							// geometryFactory.createPoint(coords[j]);
						}
						mg = mgeometryFactory.createMPoint(coords, creationTime);
						mglist.add(mg);
					}
					if (type.equals("mpolygon")) {
						for (int j = 0; j < data2.length; j++) {
							String[] data3 = data2[j].split("\\:");

							String tempread = data3[1].split("\",")[0];
							tempread = tempread.replaceAll("\"", "");
							// System.out.println(tempread);
							try {
								listPolygon[j] = (Polygon) reader.read(tempread);
							} catch (org.locationtech.jts.io.ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							creationTime[j] = Long.parseLong(data3[2].split("\"")[1]);
						}
						mg = mgeometryFactory.createMPolygon(listPolygon, creationTime);
						mglist.add(mg);
					}
					if (function.equals("M_Snapshot")) {
						long ts = Long.parseLong(parameter.split("@")[0]);
						if (mg.snapshot(ts) == null) {
							outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
									+ "No Point In This Time_Series!" + "#";
						} else
							outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
									+ mg.snapshot(ts).toText() + "#";
					}
					if (function.equals("M_Slice")) {
						long fromTime = Long.parseLong(parameter.split("@")[1]);
						long toTime = Long.parseLong(parameter.split("@")[2]);
						if (mg.slice(fromTime, toTime) == null) {
							outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
									+ "No Point In This Time_Series!" + "#";
						} else
							outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
									+ mg.slice(fromTime, toTime).toGeoString() + "#";
					}
					if (function.equals("M_Atomize")) {
						long ts = Long.parseLong(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.atomize(ts).toGeoString() + "#";
					}
					if (function.equals("M_Lattice")) {
						long ts = Long.parseLong(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.lattice(ts).toGeoString() + "#";
					}
					if (function.equals("M_First")) {
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.first().toGeoString() + "#";
					}
					if (function.equals("M_Last")) {
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.last().toGeoString() + "#";
					}
					if (function.equals("M_At")) {
						int ts = Integer.parseInt(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.at(ts).toGeoString() + "#";
					}
					if (function.equals("M_NumOf")) {
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.numOf() + "#";
					}
					if (function.equals("M_VeolocityAtTime")) {
						long ts = Long.parseLong(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.veolocityAtTimeTime(ts) + "#";
					}
					if (function.equals("M_AccelerationAtTime")) {
						long ts = Long.parseLong(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.accelerationAtTimeTime(ts) + "#";
					}
					if (function.equals("M_TimeAtDistance")) {
						double ts = Double.parseDouble(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.timeAtDistance(ts) + "#";
					}
					if (function.equals("M_TimeToDistance")) {
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.timeToDistance().toGeoString() + "#";
					}
					if (function.equals("M_TimeAtCummulativer")) {
						double ts = Double.parseDouble(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.timeAtCummulativeDistance(ts) + "#";
					}
					if (function.equals("M_SnapToGrid")) {
						int ts = Integer.valueOf(parameter.split("@")[0]);
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.snapToGrid(ts) + "#";
					}
					if (function.equals("M_BBox")) {
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.bbox().toText() + "#";
					}
					if (function.equals("M_BTime")) {
						outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type + "@"
								+ mg.btime().toGeoString() + "#";
					}
				}
				if (function.equals("M_LCSS")) {
					lcss = new MLCSS();
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							double epsilon = Double.parseDouble(parameter.split("@")[1]);
							double delta = Double.parseDouble(parameter.split("@")[2]);
							double similar = lcss.similarity(mglist.get(i), mglist.get(j), epsilon, delta);
							outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
									+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
						}
					}
				}
				if (function.equals("M_LCVS")) {
					lcvs = new MLCVS();
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							double delta = Double.parseDouble(parameter.split("@")[0]);
							double similar = lcvs.similarity(mglist.get(i), mglist.get(j), delta);
							outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
									+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
						}
					}
				}
				if (function.equals("M_LCVSMBT")) {

					lcvsmbt = new MLCVSwithMBT();
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							double delta = Double.parseDouble(parameter.split("@")[0]);
							double similar = lcvsmbt.similarity(mglist.get(i), mglist.get(j), delta);
							outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
									+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
						}
					}
				}
				if (function.equals("M_MCVSMBR")) {
					lcvsmbr = new MLCVSwithMBR();
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							double delta = Double.parseDouble(parameter.split("@")[0]);
							double similar = lcvsmbr.similarity(mglist.get(i), mglist.get(j), delta);
							outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
									+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
						}
					}
				}
				if (function.equals("M_TrajHausdorff")) {
					trajhaus = new MTrajHaus();
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							double similar = trajhaus.calculate(mglist.get(i), mglist.get(j));
							outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
									+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
						}
					}
				}
				if (function.equals("MGeometry_Distance")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MovingDistance.distance(mglist.get(i), mglist.get(j)) == null) {
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MovingDistance.distance(mglist.get(i), mglist.get(j)).toGeoString();
								System.out.println("similar1" + similar);
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("Secondo_Distance")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (Distance.distance(mglist.get(i), mglist.get(j)) == null) {
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = Distance.distance(mglist.get(i), mglist.get(j)).toGeoString();
								System.out.println("similar2" + similar);
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_Hausdorff")) {
					System.out.println("visit" + "\t" + mglist.size());
					haus = new MHausdorff();
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							double similar = haus.measure(mglist.get(i), mglist.get(j));
							outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
									+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							System.out.println(outputString);
						}
					}
				}
				if (function.equals("M_Equal")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {

							if (MGeometry.equal(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.equal(
								// mglist.get(i),
								// mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.equal(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_Meet")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.meet(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.meet( mglist.get(i),
								// mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.meet(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_Intersects")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.intersects(mglist.get(i), mglist.get(j)) == null) {
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.intersects(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}

				if (function.equals("MGeometry_Intersects")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.intersects(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.intersects(
								// mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.intersects(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				
				if (function.equals("MGeometry_Intersects_Input")) {
					
							if (MGeometry.intersects(mg1, (MGeometry)mg2) == null) {
								// String similar=MGeometry.intersects(
								// mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[3].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.intersects(mg1, (MGeometry)mg2).toGeoString();
								outputString = outputString + function + "@" + sss[ 3].replace("\"", "") + ":"
										+ sss[4].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
					
					
				}
				if (function.equals("Secondo_Intersects_Input")) {
					
							if (MGeometry.intersects(mg1, (MGeometry)mg2) == null) {
								// String similar=MGeometry.intersects(
								// mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[3].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.intersects(mg1, (MGeometry)mg2).toGeoString();
								outputString = outputString + function + "@" + sss[ 3].replace("\"", "") + ":"
										+ sss[4].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
					
					
				}

				if (function.equals("Secondo_Intersects")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (mglist.get(i) instanceof MPolygon && mglist.get(j) instanceof MPolygon) {
								if (Intersects.operation((MPolygon) mglist.get(i), (MPolygon) mglist.get(j),
										"intersects") == null) {
									outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
											+ "@" + "No Common Time In This Time_Series!" + "#";
								} else {
									MPolygon a = Intersects.operation((MPolygon) mglist.get(i),
											(MPolygon) mglist.get(j), "intersects");
									MPolygon b = Intersects.operation((MPolygon) mglist.get(j),
											(MPolygon) mglist.get(i), "intersects");
									String similar = Intersects.result(a, b);
									outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
											+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
								}
							} else {
								if (MGeometry.intersects(mglist.get(i), mglist.get(j)) == null) {
									// String similar=MGeometry.intersects(
									// mglist.get(i),
									// mglist.get(j)).toGeoString();
									outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
											+ "@" + "No Common Time In This Time_Series!" + "#";
								} else {
									String similar = MGeometry.intersects(mglist.get(i), mglist.get(j)).toGeoString();
									outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
											+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
								}
							}
						}
					}
				}

				if (function.equals("M_Inside")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.inside(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.inside(
								// mglist.get(i),
								// mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.inside(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_Contains")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.contains(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.contains(
								// mglist.get(i),
								// mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.contains(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_Disjoint")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.disjoint(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.disjoint(
								// mglist.get(i),
								// mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.disjoint(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_EvenTime")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.eventTime(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.eventTime(
								// mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.eventTime(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_Overlaps")) {
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							if (MGeometry.overlaps(mglist.get(i), mglist.get(j)) == null) {
								// String similar=MGeometry.overlaps(
								// mglist.get(i),
								// mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i].replace("\"", "") + "@" + type
										+ "@" + "No Common Time In This Time_Series!" + "#";
							} else {
								String similar = MGeometry.overlaps(mglist.get(i), mglist.get(j)).toGeoString();
								outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
										+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
							}
						}
					}
				}
				if (function.equals("M_Relationship")) {
					System.out.println("visit\t" + mglist.size());
					for (int i = 0; i < mglist.size() - 1; i++) {
						for (int j = i + 1; j < mglist.size(); j++) {
							String similar = MGeometry.relationship(mglist.get(i), mglist.get(j)).toGeoString();
							outputString = outputString + function + "@" + sss[i + 3].replace("\"", "") + ":"
									+ sss[j + 3].replace("\"", "") + "@" + type + "@" + similar + "#";
						}
					}
				}
				System.out.println("outputString\t\t" + outputString);
				PrintWriter out = response.getWriter();
				out.println(outputString);
				out.close();
			}
		}

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