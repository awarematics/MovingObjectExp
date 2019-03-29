
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.FileReader;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;

import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MGeometry;

@SuppressWarnings("serial")
public class GetAndPostLayer extends HttpServlet {
	private MGeometryFactory mgeometryFactory;
	private GeometryFactory geometryFactory;
	private BufferedReader in;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response, String method)
			throws ServletException, IOException {

		response.setContentType("text/xml");
		mgeometryFactory = new MGeometryFactory();
		geometryFactory = new GeometryFactory();

		WKTReader reader = new WKTReader(geometryFactory);
		String temp = GetAndPostData.responseLayer;
		String result = "";
		if (temp != null) {
			String[] sss = temp.split(" ");
			for (int i = 3; i < sss.length - 1; i++) {
				temp = "D://mfs/" + sss[i].replace("\"", "");
				String result2 = "";
				try {
					in = new BufferedReader(new FileReader(temp));
					String str;
					while ((str = in.readLine()) != null) {
						// result += str;
						result2 += str;
					}
				} catch (IOException e) {
				}
/*
				String type = "";
				if (result2.contains("mpolygon")) {
					type = "mpolygon";
				}

				String data1 = result2.split(type + "\":\\[")[1];
				data1 = data1.split("\\]")[0];
				// System.out.println(data1 + "\t data1");
				String[] data2 = data1.split("\\},");
				Polygon[] listPolygon = new Polygon[data2.length];
				long[] creationTime = new long[data2.length];
				Coordinate[] coords = new Coordinate[data2.length];*/
			/*	if (type.equals("mpolygon")) {
					result2 = "";
					for (int j = 0; j < data2.length; j++) {
						String[] data3 = data2[j].split("\\:");

						String tempread = data3[1].split("\",")[0];
						tempread = tempread.replaceAll("\"", "");
						try {
							listPolygon[j] = (Polygon) reader.read(tempread);
							coords[j] = new Coordinate();
							coords[j].y = (listPolygon[j].getCoordinates()[0].y);
							coords[j].x = (listPolygon[j].getCoordinates()[0].x);
						} catch (org.locationtech.jts.io.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						creationTime[j] = Long.parseLong(data3[2].split("\"")[1]);
					}
					MGeometry mg = mgeometryFactory.createMPoint(coords, creationTime);

					result2 = "\\{\\\"mpolygon\\\":\\[";
					for (int k = 0; k < mg.numOf(); k++) {
						if (k != mg.numOf() - 1)
							result2 += "\\{\\\"x\\\"\\:" + mg.getCoords()[k].x + "," + "\\\"y\\\"\\:"
									+ mg.getCoords()[k].y + "," + "\\\"time\\\"\\:" + mg.getTimes()[k] + "\\\"\\},";
						else
							result2 += "\\{\\\"x\\\"\\:" + mg.getCoords()[k].x + "," + "\\\"y\\\"\\:"
									+ mg.getCoords()[k].y + "," + "\\\"time\\\"\\:" + mg.getTimes()[k]
									+ "\\\"\\}\\]\\}";
					}
				}*/
			//	result2 = result2.replaceAll("\\\\", "");

				System.out.println(result);
				result += result2 + "@";
			}
		}
		// System.out.println(result+"\nresult");
		PrintWriter out = response.getWriter();
		out.println(result);
		out.close();
		// }

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get主函数调用processRequest,完成Get方法的参数接受，返回的过程

		processRequest(request, response, "GET");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// POST主函数调用processRequest,完成POST方法的参数接受，返回的过程
		processRequest(request, response, "POST");
	}
}