package com.awarematics.postmedia.types.mediamodel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import com.awarematics.postmedia.mgeom.MGeometryFactory;

@SuppressWarnings("rawtypes")
public abstract class MGeometry implements Serializable, Comparable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int MAX_VALUE = 7732345;

	public abstract Geometry spatial();

	public abstract MGeometry spatials();

	public abstract long getDuration();

	public abstract MGeometry lattice(long duration);

	public abstract MGeometry atomize(long duration);

	public abstract MGeometry lattice(MDuration duration);

	public abstract Geometry snapshot(long ts);

	public abstract MGeometry slice(long fromTime, long toTime);

	public abstract MGeometry snapshot(MInstant ts);

	public abstract MGeometry first();

	public abstract MGeometry last();

	public abstract MGeometry at(int n);

	public abstract int numOf();

	public abstract String toGeoString();

	public abstract long[] getTimes();

	public abstract Polygon[] getListPolygon();

	public abstract Coordinate[] getCoords();
	
	public abstract double veolocityAtTimeTime(long instant);
	
	public abstract double accelerationAtTimeTime(long instant);

	public abstract MDouble timeToDistance();
	
	public abstract ArrayList<Long> timeAtDistance(double distance);
	
	public abstract long timeAtCummulativeDistance(double distance);

	public abstract MGeometry snapToGrid(double cellSize);

	public abstract Geometry bbox();

	public abstract MPeriod btime();

	public abstract Period time();

	public abstract Period time(int n);

	public abstract long startTime();

	public abstract long endTime();
	
	public abstract MDouble area();
	
	public abstract MDouble direction();
	
	public abstract MInt count();
	
	public abstract MDouble velocity();
	public static MBool equal(MGeometry mg1, MGeometry mg2) {
		MGeometryFactory mgeom = new MGeometryFactory();
		MBool mbools = intersects(mg1, mg2);
		MBool mbool2 = meet(mg1, mg2);
		boolean[] bools = new boolean[mbools.numOf()];
		long[] times = mbools.getTimes();

		for (int i = 0; i < times.length; i++) {
			if (times[i] >= mg1.getTimes()[0] && times[i] >= mg2.getTimes()[0]
					&& times[i] <= mg1.getTimes()[mg1.numOf() - 1] && times[i] <= mg2.getTimes()[mg2.numOf() - 1]) {
				if (mg1.snapshot(times[i]).equals(mg2.snapshot(times[i])) && mbool2.bools[i] != true) {
					bools[i] = true;
				}
			} else {
				bools[i] = false;
			}
		}
		return mgeom.createMBool(bools, times);
	}

	public static MBool meet(MGeometry mg1, MGeometry mg2) {
		MGeometryFactory mgeom = new MGeometryFactory();
		MBool mbools = intersects(mg1, mg2);
		boolean[] bools = mbools.getBools();
		boolean[] boolsresult = new boolean[mbools.numOf()];
		long[] times = mbools.getTimes();

		for (int i = 1; i < times.length - 1; i++) {
			if (bools[i] == true && bools[i - 1] == true && bools[i + 1] == true)
				boolsresult[i] = false;
			else
				boolsresult[i] = bools[i];
		}
		boolsresult[0] = bools[0]; boolsresult[times.length-1] = bools[times.length-1];
		return mgeom.createMBool(boolsresult, times);
	}

	public static MBool intersects(MGeometry mg1, MGeometry mg2) {
		ArrayList<Long> timeList = new ArrayList<Long>();
		ArrayList<Long> timeEventList = new ArrayList<Long>();
		MGeometryFactory mgeom = new MGeometryFactory();
		for (int i = 0; i < mg1.numOf(); i++) {
			timeList.add(mg1.getTimes()[i]);
		}
		for (int i = 0; i < mg2.numOf(); i++) {
			timeList.add(mg2.getTimes()[i]);
		}
		
		if(eventTime(mg1, mg2).getInstant()!=null){
		long[] ms = eventTime(mg1, mg2).getInstant();
		for (int i = 0; i < ms.length; i++) {
			timeList.add(ms[i]);
			timeEventList.add(ms[i]);
		}}
		LinkedHashSet<Long> set = new LinkedHashSet<Long>(timeList.size());
		set.addAll(timeList);
		timeList.clear();
		timeList.addAll(set);
		long[] tempList = timeList.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		long[] tempNoEventList = timeEventList.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		Arrays.sort(tempList);
		Arrays.sort(tempNoEventList);
		boolean[] bools = new boolean[timeList.size()];

		for (int i = 0; i < tempList.length; i++) {
			int num = 0;
			for (int j = 0; j < tempNoEventList.length; j++) {
				if (tempList[i] == tempNoEventList[j])
					num = 1;
			}
			if (num == 1)
				bools[i] = true;
			else
				bools[i] = false;
		}
		return mgeom.createMBool(bools, tempList);
	}

	
	public static MBool intersects(MGeometry mg1, Geometry mg2) {
		ArrayList<Long> timeList = new ArrayList<Long>();
		ArrayList<Long> timeEventList = new ArrayList<Long>();
		MGeometryFactory mgeom = new MGeometryFactory();
		for (int i = 0; i < mg1.numOf(); i++) {
			timeList.add(mg1.getTimes()[i]);
		}		
		long[] tempList = timeList.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		
		boolean[] bools = new boolean[timeList.size()];

		for (int i = 0; i < tempList.length; i++) {
			if (mg1 instanceof MPoint){
				Point p = new Point(mg1.getCoords()[i], null, 0);
				if(p.intersects(mg2))
					bools[i] = true;
				else
					bools[i] = false;
			}
			if (mg1 instanceof MPolygon){
				if(mg1.getListPolygon()[i].intersects(mg2))
					bools[i] = true;
				else
					bools[i] = false;
			}
			if (mg1 instanceof MLineString){
				if(((MLineString) mg1).getPoints()[i].intersects(mg2))
					bools[i] = true;
				else
					bools[i] = false;
			}
		}
		return mgeom.createMBool(bools, tempList);
	}
	
	public static MBool disjoint(MGeometry mg1, MGeometry mg2) {

		MGeometryFactory mgeom = new MGeometryFactory();
		MBool mbools = intersects(mg1, mg2);
		boolean[] bools = mbools.getBools();
		boolean[] boolsresult = new boolean[bools.length];
		long[] times = mbools.getTimes();

		for (int i = 0; i < times.length; i++) {
			if (bools[i] == true)
				boolsresult[i] = false;
			if (bools[i] == false)
				boolsresult[i] = true;
		}
		return mgeom.createMBool(boolsresult, times);
	}

	public static MInstant eventTime(MGeometry mg1, MGeometry mg2) {
		MGeometryFactory mgeom = new MGeometryFactory();
		GeometryFactory geom = new GeometryFactory();
		long overlappedStartTime = Math.max(mg1.getTimes()[0], mg2.getTimes()[0]);
		long overlappedEndTime = Math.min(mg1.getTimes()[mg1.numOf() - 1], mg2.getTimes()[mg2.numOf() - 1]);
		if(overlappedStartTime>overlappedEndTime){
			return mgeom.createMInstant(null);
		}
		else{
		long[] timeArea = new long[] { overlappedStartTime, overlappedEndTime };		
		ArrayList<Long> timeList = getTimeList(mg1, mg2, timeArea);		
		LinkedHashSet<Long> set = new LinkedHashSet<Long>(timeList.size());
		set.addAll(timeList);
		timeList.clear();
		timeList.addAll(set);
		long[] tempList = timeList.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		Arrays.sort(tempList);
		//System.out.println(tempList.length);
		ArrayList<Long> result = new ArrayList<Long>();
		Coordinate[] coor1 = new Coordinate[timeList.size()];
		Coordinate[] coor2 = new Coordinate[timeList.size()];
		for (int i = 0; i < tempList.length; i++) {
			Geometry p1 = mg1.snapshot(tempList[i]);
			Geometry p2 = mg2.snapshot(tempList[i]);
			coor1[i] = p1.getCoordinate();
			coor2[i] = p2.getCoordinate();
		}
		LineString g1 = geom.createLineString(coor1);
		LineString g2 = geom.createLineString(coor2);
		getInstantList(g1, g2, mg1, mg2, tempList, result);

		if ((mg1 instanceof MPolygon || mg1 instanceof MVideo || mg1 instanceof MPhoto)
				&& (mg2 instanceof MPolygon || mg2 instanceof MVideo || mg2 instanceof MPhoto)) {
			for (int i = 0; i < mg1.getTimes().length; i++) {
				if (mg1.getTimes()[i] >= timeArea[0] && mg1.getTimes()[i] <= timeArea[1]) {
					if (mg1.getListPolygon()[i].intersects(mg2.snapshot(mg1.getTimes()[i]))) {
						result.add(mg1.getTimes()[i]);
					}
				}
			}
			for (int i = 0; i < mg2.getTimes().length; i++) {
				if (mg2.getTimes()[i] >= timeArea[0] && mg2.getTimes()[i] <= timeArea[1]) {
					if (mg2.getListPolygon()[i].intersects(mg1.snapshot(mg2.getTimes()[i]))) {
						result.add(mg2.getTimes()[i]);
					}
				}
			}
		}
		if ((mg1 instanceof MPolygon || mg1 instanceof MVideo || mg1 instanceof MPhoto)
				&& (mg2 instanceof MPoint || mg2 instanceof MLineString)) {

			for (int i = 0; i < mg1.getTimes().length; i++) {
				if (mg1.getTimes()[i] >= timeArea[0] && mg1.getTimes()[i] <= timeArea[1]) {
					if (mg1.getListPolygon()[i].intersects(mg2.snapshot(mg1.getTimes()[i]))) {
						result.add(mg1.getTimes()[i]);
					}
				}
			}
		}
		if ((mg1 instanceof MPoint || mg1 instanceof MLineString)
				&& (mg2 instanceof MPolygon || mg2 instanceof MVideo || mg2 instanceof MPhoto)) {

			for (int i = 0; i < mg2.getTimes().length; i++) {
				if (mg2.getTimes()[i] >= timeArea[0] && mg2.getTimes()[i] <= timeArea[1]) {
					if (mg2.getListPolygon()[i].intersects(mg1.snapshot(mg2.getTimes()[i]))) {
						result.add(mg2.getTimes()[i]);
					}
				}
			}
		}

		LinkedHashSet<Long> sest = new LinkedHashSet<Long>(result.size());
		sest.addAll(result);
		result.clear();
		result.addAll(sest);
		long[] resultList = result.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		Arrays.sort(resultList);
		return mgeom.createMInstant(resultList);}
	}

	public static void getInstantList(LineString g1, LineString g2, MGeometry mg1, MGeometry mg2, long[] tempList,
			ArrayList<Long> result) {
		try {
			if (g1.intersects(g2)) {
				Geometry interPoint = g1.intersection(g2);
				Coordinate[] pp = interPoint.getCoordinates();
				for (int k = 0; k < pp.length; k++) {
					for (int ii = 1; ii < tempList.length; ii++) {
						double rightpart = calDistance(g1.getCoordinates()[ii], pp[k]);
						double leftpart = calDistance(g1.getCoordinates()[ii - 1], pp[k]);
						double total = calDistance(g1.getCoordinates()[ii], g1.getCoordinates()[ii - 1]);
						if ((leftpart + rightpart) - total < 0.00000001) {
							long newTime = (long) ((tempList[ii] - tempList[ii - 1]) * leftpart / total
									+ tempList[ii - 1]);

							if (calDistance(mg2.snapshot(newTime).getCoordinate(),
									mg1.snapshot(newTime).getCoordinate()) < 0.001 && newTime!=0)
								result.add(newTime);
							
						}
					}
				}

			}

		} catch (Exception e) {
		}
	}

	public static MString relationship(MGeometry mg1, MGeometry mg2) {
		MGeometryFactory mgeom = new MGeometryFactory();
		MBool mbools = intersects(mg1, mg2);
		MBool mbool2 = meet(mg1, mg2);
		MBool mbool3 = equal(mg1, mg2);
		MBool mbool4 = overlaps(mg1, mg2);
		MBool mbool5 = inside(mg1, mg2);
		MBool mbool6 = contains(mg1, mg2);
		MBool mbool7 = overlaps(mg1, mg2);
		
		String[] string = new String[mbools.numOf()];
		for (int i = 0; i < mbools.numOf(); i++) {
			if (mbool2.getBools()[i] == true)
				string[i] = "meet";
			else if (mbool3.getBools()[i] == true)
				string[i] = "equal";
			else if (mbool4.getBools()[i] == true)
				string[i] = "overlaps";
			else if (mbool5.getBools()[i] == true)
				string[i] = "inside";
			else if (mbool6.getBools()[i] == true)
				string[i] = "contains";
			else if(mbool7.getBools()[i] == true)
				string[i] = "overlaps";
			else if(mbools.getBools()[i] == true)
				string[i] = "intersect";
			else
				string[i] = "disjoint";
		}
		long[] times = mbools.getTimes();
		MString ms = mgeom.createMString(string, times);
		return ms;
	}

	public static MBool overlaps(MGeometry mg1, MGeometry mg2) {
		MGeometryFactory mgeom = new MGeometryFactory();
		long[] times = null;
		boolean[] bools = null;

			MBool mbools = intersects(mg1, mg2);
			MBool mbool2 = meet(mg1, mg2);
			bools = new boolean[mbools.numOf()];
			times = mbools.getTimes();

			for (int i = 0; i < times.length; i++) {
				if (times[i] >= mg1.getTimes()[0] && times[i] >= mg2.getTimes()[0]
						&& times[i] <= mg1.getTimes()[mg1.numOf() - 1] && times[i] <= mg2.getTimes()[mg2.numOf() - 1]) {
					if (mg1.snapshot(times[i]).overlaps(mg2.snapshot(times[i])) && mbool2.bools[i] != true) {
						bools[i] = true;

					}
				} else {
					bools[i] = false;
				}
			}

		return mgeom.createMBool(bools, times);
	}

	public static MBool inside(MGeometry mg1, MGeometry mg2) {
		MGeometryFactory mgeom = new MGeometryFactory();
		long[] times = null;
		boolean[] bools = null;

		MBool mbools = intersects(mg1, mg2);
		MBool mbool2 = meet(mg1, mg2);
		bools = new boolean[mbools.numOf()];
		times = mbools.getTimes();

		for (int i = 0; i < times.length; i++) {
			if (times[i] >= mg1.getTimes()[0] && times[i] >= mg2.getTimes()[0]
					&& times[i] <= mg1.getTimes()[mg1.numOf() - 1] && times[i] <= mg2.getTimes()[mg2.numOf() - 1]) {
				if (mg1.snapshot(times[i]).within(mg2.snapshot(times[i])) && mbool2.bools[i] != true) {
					bools[i] = true;
				}
			} else {
				bools[i] = false;
			}
		}
		return mgeom.createMBool(bools, times);

	}

	public static MBool contains(MGeometry mg1, MGeometry mg2) {
		MGeometryFactory mgeom = new MGeometryFactory();
		long[] times = null;
		boolean[] bools = null;

		MBool mbools = intersects(mg1, mg2);
		MBool mbool2 = meet(mg1, mg2);
		bools = new boolean[mbools.numOf()];
		times = mbools.getTimes();

		for (int i = 0; i < times.length; i++) {
			if (times[i] >= mg1.getTimes()[0] && times[i] >= mg2.getTimes()[0]
					&& times[i] <= mg1.getTimes()[mg1.numOf() - 1] && times[i] <= mg2.getTimes()[mg2.numOf() - 1]) {
				if (mg1.snapshot(times[i]).contains(mg2.snapshot(times[i])) && mbool2.bools[i] != true) {
					bools[i] = true;
				}
			} else {
				bools[i] = false;
			}
		}
		return mgeom.createMBool(bools, times);

	}

	public static ArrayList<Long> getTimeList(MGeometry mg1, MGeometry mg2, long[] timeArea) {
		ArrayList<Long> nowTime = new ArrayList<Long>();
		if (timeArea[0] > timeArea[1])
			return null;
		for (int i = 0; i < mg1.numOf(); i++) {
			if (mg1.getTimes()[i] >= timeArea[0] && mg1.getTimes()[i] <= timeArea[1]) {
				nowTime.add(mg1.getTimes()[i]);
			}
		}
		for (int i = 0; i < mg2.numOf(); i++) {
			if (mg2.getTimes()[i] >= timeArea[0] && mg2.getTimes()[i] <= timeArea[1]) {
				nowTime.add(mg2.getTimes()[i]);
			}
		}
		return nowTime;
	}
	private static double EARTH_RADIUS = 6378.137;  
    
    private static double rad(double d) {  
        return d * Math.PI / 180.0;  
    }  
	public static double calDistance(Coordinate p1, Coordinate p2) {
		/*double x1 = p1.x;
		double y1 = p1.y;
		double x2 = p2.x;
		double y2 = p2.y;
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));*/
		 

			double x1 = p1.x;
			double y1 = p1.y;
			double x2 = p2.x;
			double y2 = p2.y;	
			 double radLat1 = rad(x1);  
	         double radLat2 = rad(x2);  
	         double a = radLat1 - radLat2;  
	         double b = rad(y1) - rad(y2);  
	         double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)  
	                 + Math.cos(radLat1) * Math.cos(radLat2)  
	                 * Math.pow(Math.sin(b / 2), 2)));  
	         s = s * EARTH_RADIUS;  
	         s = Math.round(s * 10000d) / 10000d;  
	         s = s*1000;  
	         return s;  

			//return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
		
	}
}
