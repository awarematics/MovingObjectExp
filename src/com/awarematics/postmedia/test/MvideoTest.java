package com.awarematics.postmedia.test;

import java.io.IOException;

import com.awarematics.postmedia.algorithms.distance.MDistanceOp;
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

public class MvideoTest {


	static M_Hausdorff mh;
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException
	{		
		
		//PrecisionModel precisionModel = new PrecisionModel(1000);
		MGeometryFactory geometryFactory = new MGeometryFactory();
		MWKTReader reader = new MWKTReader(geometryFactory);	  		  
/*
CREATE TYPE stphoto as(
	uri				text,	--name
	width			int,  --type
	height			int,
	viewAngle		float(4),			
	verticalAngle  	float(4),
	distance 	 	float(4),
	direction		float(4),
	direction3d		float(4),
	altitude		float(4),
	annotationJson	json,
	exifJson		json,
	loc				GEOMETRY(POINT, 4326),
	creationTime	bigint
);*/
		MVideo mv = (MVideo)reader.read("MVIDEO (('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 100 100) 1481480632123, ('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 101 101) 1481480634123), ('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 102 102) 1481480637123)");
		MVideo mv2 = (MVideo)reader.read("MVIDEO (('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 100 100) 1481480632123, ('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 101 101) 1481480634123), ('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 102 102) 1481480637123)");
		//---------------------------------------------------------------------	
		/*
		System.out.println(mv.toGeoString());
		System.out.println(mv.numOf());
		System.out.println(mv.snapshot(1481480635000l));
		System.out.println(mv.slice(1481480633000l, 1481480638000l).toGeoString());
		System.out.println(mv.atomize(1000).toGeoString());
		System.out.println("-----------------------");
		System.out.println(mv.lattice(1000).toGeoString());
		System.out.println(mv.first().toGeoString());
		System.out.println(mv.last().toGeoString());
		System.out.println(mv.at(2).toGeoString());
		*/
		//---------------------------------------------------------------------
		
		
		Geometry cood = mv.spatial();
		System.out.println(cood.getCoordinates()[0].x);	
		
		System.out.println(MovingDistance.distance(mv,mv2).toGeoString());
		mh = new M_Hausdorff();
		M_Hausdorff mh;
		mh = new M_Hausdorff();
		System.out.println(mh.measure(mv, mv2));
		
		}
}