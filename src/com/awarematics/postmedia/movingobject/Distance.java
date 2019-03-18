package com.awarematics.postmedia.movingobject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.operation.distance.DistanceOp;

import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MBool;
import com.awarematics.postmedia.types.mediamodel.MDouble;
import com.awarematics.postmedia.types.mediamodel.MGeometry;
import com.awarematics.postmedia.types.mediamodel.MPoint;
import com.awarematics.postmedia.types.mediamodel.MPolygon;

public class Distance {
	static MGeometryFactory geometryFactory = new MGeometryFactory();
	static MWKTReader reader = new MWKTReader(geometryFactory);

	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

		MPolygon mpolygon1 = (MPolygon) reader.read(
				"MPOLYGON ((10 10, 20 20, 30 40, 40 50, 10 10) 1481480632223, (10 10, 20 20, 21 21, 30 40, 10 10) 1481480646556)");
		MPoint mp = (MPoint) reader.read(
				"MPOINT ((0 0) 1481480632123, (2 5) 1481480637123, (34 333) 1481480638000, (0.23546789 0) 1481480639123, (3 3) 1481480641123, (333 333) 1481480642556, (0 0) 1481480643123, (2 7) 1481480644123, (333 33) 1481480645556)");

		MDouble mb = distance(mp, mpolygon1);
		System.out.println(mb.toGeoString());

	}

	private static MDouble distance(MPoint mp, MPolygon mpolygon1) {

		MBool mb = NotInside(mp, mpolygon1);
		//System.out.println(mb.numOf());
		double[] mdistance = new double[mb.numOf()];
		long overlappedStartTime = Math.max(mp.getTimes()[0], mpolygon1.getTimes()[0]);
		long overlappedEndTime = Math.min(mp.getTimes()[mp.numOf() - 1], mpolygon1.getTimes()[mpolygon1.numOf() - 1]);
		for (int i = 0; i < mb.numOf(); i++){
			if (mb.getBools()[i] == true) {
				// the distance is 0
				mdistance[i] = 0;
			} else {
				// step 1 get time interval
				// step 2 get time projection
				// step 3 get distance result
				Coordinate[] c = DistanceOp.nearestPoints(mpolygon1.getListPolygon()[i], mp.snapshot(mb.getTimes()[i]));
				mdistance[i] = d(c[0], mp.snapshot(mb.getTimes()[i]).getCoordinates()[0]);
			}
			return geometryFactory.createMDouble(mdistance, mb.getTimes());
		}
		return null;
	}

	private static MBool NotInside(MPoint mp, MPolygon mpolygon1) {
		return at(mp, mpolygon1);
	}

	private static MBool at(MGeometry mg1, MGeometry mg2) {
		long overlappedStartTime = Math.max(mg1.getTimes()[0], mg2.getTimes()[0]);
		long overlappedEndTime = Math.min(mg1.getTimes()[mg1.numOf() - 1], mg2.getTimes()[mg2.numOf() - 1]);

		ArrayList<Boolean> bool = new ArrayList<Boolean>();
		ArrayList<Long> time = new ArrayList<Long>();
		if (overlappedStartTime <= overlappedEndTime) {
			
			for (int i = 0; i < mg2.numOf(); i++) {
				if (mg2.getTimes()[i] >= overlappedStartTime && mg2.getTimes()[i] <= overlappedEndTime) {
					time.add(mg2.getTimes()[i]);
				}
			}
			HashSet<Long> hashset_temp = new HashSet<Long>(time);
			time = new ArrayList<Long>(hashset_temp);
			long[] tempList = time.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
			Arrays.sort(tempList);
			for (int j = 0; j < mg2.numOf(); j++) {
				if (mg2.getTimes()[j] >= overlappedStartTime && mg2.getTimes()[j] <= overlappedEndTime)
					bool.add(mg1.snapshot(mg2.getTimes()[j]).within(mg2.getListPolygon()[j]));
			}
			boolean[] tempBool = new boolean[tempList.length];
			for (int i = 0; i < bool.size(); i++) {
				tempBool[i] = bool.get(i);
			}
			MBool mb = geometryFactory.createMBool(tempBool, tempList);
			return mb;
		}
		return null;
	}

	public static double d(Coordinate p1, Coordinate p2) {
		double x1 = p1.x;
		double y1 = p1.y;
		double x2 = p2.x;
		double y2 = p2.y;
		return Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
}
