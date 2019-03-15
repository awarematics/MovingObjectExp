package com.awarematics.postmedia.algorithms.similarity;

import org.locationtech.jts.geom.Coordinate;

import com.awarematics.postmedia.types.mediamodel.FoV;
import com.awarematics.postmedia.types.mediamodel.MGeometry;
import com.awarematics.postmedia.types.mediamodel.MPhoto;
import com.awarematics.postmedia.types.mediamodel.MVideo;

public class M_LCVS_MBR {
	MVideo mv1;
	MVideo mv2;

	public static final int EPSILON_R = 2;
	public M_LCVS_MBR() {

	}

	public double measure(MVideo mv1, MVideo mv2, double delta) {

		double[][] c = null;

		c = new double[mv1.numOf() + 1][mv2.numOf() + 1];
		for (int i = 0; i < mv1.numOf(); i++) {
			c[i][0] = 0;
		}
		for (int j = 0; j <= mv2.numOf(); j++) {
			c[0][j] = 0;
		}

		FoV[] fov1 = mv1.getFov();
		FoV[] fov2 = mv2.getFov();
		for (int i = 1; i <= mv1.numOf(); i++) {
			for (int j = 1; j <= mv2.numOf(); j++) {
				if (Math.abs(j - i) <= delta) {
					c[i][j] = c[i - 1][j - 1] + calcCVW(fov1[i - 1], fov2[j - 1]);
				} else if (c[i - 1][j] <= c[i][j - 1]) {
					c[i][j] = c[i][j - 1];
				} else {
					c[i][j] = c[i - 1][j];
				}
			}
		}
		return c[mv1.numOf()][mv2.numOf()];
	}
	public double measure(MGeometry mv1, MGeometry mv2, double delta) {

		double[][] c = null;

		c = new double[mv1.numOf() + 1][mv2.numOf() + 1];
		for (int i = 0; i < mv1.numOf(); i++) {
			c[i][0] = 0;
		}
		for (int j = 0; j <= mv2.numOf(); j++) {
			c[0][j] = 0;
		}

		FoV[] fov1 = null;
		FoV[] fov2 = null;
		if(mv1 instanceof MPhoto){
		fov1 = ((MPhoto)mv1).getFov();
		}
		if(mv1 instanceof MVideo){
			fov1 = ((MVideo)mv1).getFov();
			}
		if(mv2 instanceof MPhoto){
			fov2 = ((MPhoto)mv2).getFov();
			}
			if(mv2 instanceof MVideo){
				fov2 = ((MVideo)mv2).getFov();
				}
		for (int i = 1; i <= mv1.numOf(); i++) {
			for (int j = 1; j <= mv2.numOf(); j++) {
				if (Math.abs(j - i) <= delta) {
					c[i][j] = c[i - 1][j - 1] + calcCVW(fov1[i - 1], fov2[j - 1]);
				} else if (c[i - 1][j] <= c[i][j - 1]) {
					c[i][j] = c[i][j - 1];
				} else {
					c[i][j] = c[i - 1][j];
				}
			}
		}
		return c[mv1.numOf()][mv2.numOf()];
	}

	public double calcCVW(FoV f1, FoV f2) {
		Coordinate[] coor1 = getRectangle(f1);
		Coordinate[] coor2 = getRectangle(f2);	
		double cvw = 0.0;
		cvw = overlappingArea(coor1[0],coor1[1],coor2[0],coor2[1]);
		//ArrayList<Polygon> arr = new ArrayList<Polygon>();
		//arr.add(polygon);
		//arr.add(polygon2);
		//MainFrame frame = new MainFrame(arr);
		//frame.showWindow();
		return cvw;
	}
	
	private Coordinate[] getRectangle(FoV f){
		int times = 1;
		double xf = f.getCoord().x;
		double yf = f.getCoord().y;
		
		double x2 = (double) xf + f.getDistance() * Math.sin(Math.toRadians(f.getDirection() + (f.getViewAngle() ) / 2)) *times;
		double y2 = (double) yf
				+ f.getDistance() *Math.cos(Math.toRadians(f.getDirection() +(f.getViewAngle() )/ 2)) *times;// left
		double x3 = (double) xf
				+ f.getDistance()  *Math.sin(Math.toRadians(f.getDirection() -(f.getViewAngle() ) / 2))*times ;
		double y3 = (double) yf
				+ f.getDistance()  * Math.cos(Math.toRadians(f.getDirection() - (f.getViewAngle() ) / 2)) *times;// right
		
		double xmin = Math.min(Math.min(x2, x3),xf);	
		double ymin =  Math.min(Math.min(y2, y3),yf);
		double xmax =  Math.max(Math.max(x2, x3),xf);
		double ymax =  Math.max(Math.max(y2, y3),yf);
		
		Coordinate[] coor1 = new Coordinate[2];
		coor1[0] = new Coordinate(xmin, ymin);
		coor1[1] = new Coordinate(xmax, ymax);
		return coor1;
	}

	

	double overlappingArea(Coordinate l1, Coordinate r1, Coordinate l2, Coordinate r2) {
		double area1 = Math.abs(l1.x - r1.x) * Math.abs(l1.y - r1.y);
		double result =0;
		double area2 = Math.abs(l2.x - r2.x) * Math.abs(l2.y - r2.y);
		double areaI = (Math.min(r1.x, r2.x) - Math.max(l1.x, l2.x)) * (Math.min(r1.y, r2.y) - Math.max(l1.y, l2.y));
		 result =Math.abs(areaI/(area1 + area2 - areaI));
		
		if(Math.min(r1.x, r2.x) < Math.max(l1.x, l2.x)) result =0;
		if(Math.min(r1.y, r2.y) < Math.max(l1.y, l2.y)) result =0;
		 
		//System.out.println(result);
		return result;
	}

	public double similarity(MVideo mg1, MVideo mg2, double omega) {
		double lcss_distance = 0.0;
		double similarity = 0.0;
		lcss_distance = measure(mg1, mg2, omega);
		similarity = lcss_distance / (Math.min(mg1.numOf(), mg2.numOf()));
		return similarity;
	}
	public double similarity(MGeometry mg1, MGeometry mg2, double omega) {
		double lcss_distance = 0.0;
		double similarity = 0.0;
		lcss_distance = measure(mg1, mg2, omega);
		similarity = lcss_distance / (Math.min(mg1.numOf(), mg2.numOf()));
		return similarity;
	}

}
