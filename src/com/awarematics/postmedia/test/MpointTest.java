package com.awarematics.postmedia.test;

import java.io.IOException;

import com.awarematics.postmedia.algorithms.distance.MovingDistance;
import com.awarematics.postmedia.algorithms.similarity.MSimilarityMeasure;
import com.awarematics.postmedia.algorithms.similarity.M_Hausdorff;
import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MDouble;
import com.awarematics.postmedia.types.mediamodel.MPhoto;
import com.awarematics.postmedia.types.mediamodel.MPoint;
import com.awarematics.postmedia.types.mediamodel.MTime;
import com.awarematics.postmedia.types.mediamodel.MVideo;
import com.awarematics.postmedia.types.mediamodel.M_pointprojection;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.io.ParseException;

public class MpointTest {


	static M_Hausdorff mh;
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException
	{		
		
		//PrecisionModel precisionModel = new PrecisionModel(1000);
		MGeometryFactory geometryFactory = new MGeometryFactory();
		MWKTReader reader = new MWKTReader(geometryFactory);	  		  
		
		MPoint mp = (MPoint)reader.read("MPOINT ((0 0) 1481480632123, (2 5) 1481480637123, (34 333) 1481480638000, (0.23546789 0) 1481480639123, (3 3) 1481480641123, (333 333) 1481480642556, (0 0) 1481480643123, (2 7) 1481480644123, (333 33) 1481480645556)");
		MPoint mp2 = (MPoint)reader.read("MPOINT ((0 1) 1481480632123, (2 6) 1481480637123, (34 331) 1481480638000, (0 1) 1481480639123, (3 4) 1481480641123, (333 33) 1481480642556, (0 5) 1481480643123, (2 9) 1481480644123, (3 333) 1481480645556)");
		//---------------------------------------------------------------------	
		
		System.out.println(mp.toGeoString());
		System.out.println(mp.numOf());
		//System.out.println(mp.snapshot(1481480635000l));
		System.out.println("slice\t\t"+mp.slice(0l, 14814806321230l).toGeoString());
		//System.out.println(mp.atomize(1000).toGeoString());
		System.out.println(mp.lattice(1000).toGeoString());
		System.out.println(mp.first().toGeoString());
		System.out.println(mp.last().toGeoString());
		System.out.println(mp.at(5).toGeoString());
		
		//---------------------------------------------------------------------
		Geometry cood = mp.spatial();
		System.out.println(cood.getCoordinates());
		
		
		System.out.println(MovingDistance.distance(mp,mp2).toGeoString());
		mh = new M_Hausdorff();
		M_Hausdorff mh;
		mh = new M_Hausdorff();
		System.out.println(mh.measure(mp, mp2));
		}
}