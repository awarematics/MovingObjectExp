package com.awarematics.postmedia.test;

import java.io.File;
import java.io.IOException;

import com.awarematics.postmedia.algorithms.distance.MovingDistance;
import com.awarematics.postmedia.algorithms.similarity.MHausdorff;
import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.operation.IO;
import com.awarematics.postmedia.operation.TemporalOP;
import com.awarematics.postmedia.transform.ColumnsProjection;
import com.awarematics.postmedia.types.mediamodel.MVideo;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;

public class MvideoTest {


	static MHausdorff mh;
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
		//uri,viewAngle,verticalAngle, distance, direction,direction3d, altitude,annotationJson,exifJson,coords,creationTime
		//MVideo mv = (MVideo)reader.read("MVIDEO (('http://u-gis.net/1.jpg' 60 0 0.1 30 0 0 'http://mgeometry.u-gis.net/data/json/exif_test1.json' 100 100) 1481480632123, ('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 101 101) 1481480634123), ('localhost:///tmp/drone/test1.jpg' 200 200 60 0 0.1 30 0 0 'annotation' 'exif' 102 102) 1481480637123)");
		MVideo mv = (MVideo)reader.read("MVIDEO ((0000f77c-62c2a288.mov?t=1 60 -1 0.001 -15.5971811517671 -1 -1 'null' 'null' 40.6775779277465 -73.8310543914082) 1503828255000, (0000f77c-62c2a288.mov?t=2 60 -1 0.001 -15.5971811517671 -1 -1 'null' 'null' 40.6774585275358 -73.831088170478) 1503828256000, (0000f77c-62c2a288.mov?t=3 60 -1 0.001 -15.0252048788381 -1 -1 'null' 'null' 40.6773299072317 -73.8311231230142) 1503828257000, (0000f77c-62c2a288.mov?t=4 60 -1 0.001 -14.2325586776312 -1 -1 'null' 'null' 40.6771981856233 -73.831156902084) 1503828258000, (0000f77c-62c2a288.mov?t=5 60 -1 0.001 -8.93153565382268 -1 -1 'null' 'null' 40.6770616863302 -73.8311784435751) 1503828259000, (0000f77c-62c2a288.mov?t=6 60 -1 0.001 -4.27087267612698 -1 -1 'null' 'null' 40.6769282464317 -73.8311884180399) 1503828260000, (0000f77c-62c2a288.mov?t=7 60 -1 0.001 -2.55530222803052 -1 -1 'null' 'null' 40.6768062059215 -73.8311938662769) 1503828261000, (0000f77c-62c2a288.mov?t=8 60 -1 0.001 -4.99489993530309 -1 -1 'null' 'null' 40.6767075509212 -73.8312024996372) 1503828262000, (0000f77c-62c2a288.mov?t=9 60 -1 0.001 -8.34463727316125 -1 -1 'null' 'null' 40.6766346702731 -73.8312132284733) 1503828263000, (0000f77c-62c2a288.mov?t=10 60 -1 0.001 -11.2366268380858 -1 -1 'null' 'null' 40.6765768351412 -73.8312247954997) 1503828264000, (0000f77c-62c2a288.mov?t=11 60 -1 0.001 -12.8961427109887 -1 -1 'null' 'null' 40.6765227718658 -73.8312372845354) 1503828265000, (0000f77c-62c2a288.mov?t=12 60 -1 0.001 -14.5312825215844 -1 -1 'null' 'null' 40.6764565967402 -73.8312546350749) 1503828266000, (0000f77c-62c2a288.mov?t=13 60 -1 0.001 -17.6718213833648 -1 -1 'null' 'null' 40.676376717203 -73.8312805351557) 1503828267000, (0000f77c-62c2a288.mov?t=14 60 -1 0.001 -22.4188574000815 -1 -1 'null' 'null' 40.6762848515442 -73.8313195948245) 1503828268000, (0000f77c-62c2a288.mov?t=15 60 -1 0.001 -27.1214137173315 -1 -1 'null' 'null' 40.67618393343 -73.831373825738) 1503828269000, (0000f77c-62c2a288.mov?t=16 60 -1 0.001 -29.9228346952912 -1 -1 'null' 'null' 40.6760758068791 -73.8314400427731) 1503828270000, (0000f77c-62c2a288.mov?t=17 60 -1 0.001 -33.6361977474 -1 -1 'null' 'null' 40.6759702787182 -73.8315165695491) 1503828271000, (0000f77c-62c2a288.mov?t=18 60 -1 0.001 -36.4648080902999 -1 -1 'null' 'null' 40.6758701149753 -73.8315992151143) 1503828272000, (0000f77c-62c2a288.mov?t=19 60 -1 0.001 -41.0108335048101 -1 -1 'null' 'null' 40.675779506602 -73.8316920866015) 1503828273000, (0000f77c-62c2a288.mov?t=20 60 -1 0.001 -44.5667449160952 -1 -1 'null' 'null' 40.6757038599259 -73.8317857124599) 1503828274000, (0000f77c-62c2a288.mov?t=21 60 -1 0.001 -47.9767108203941 -1 -1 'null' 'null' 40.6756510539359 -73.8318665978255) 1503828275000, (0000f77c-62c2a288.mov?t=22 60 -1 0.001 -50.1767426393922 -1 -1 'null' 'null' 40.6756235612935 -73.8319164701494) 1503828276000, (0000f77c-62c2a288.mov?t=23 60 -1 0.001 -50.0646468201356 -1 -1 'null' 'null' 40.675610359796 -73.8319401909353) 1503828277000, (0000f77c-62c2a288.mov?t=24 60 -1 0.001 -50.9788384598721 -1 -1 'null' 'null' 40.6755987927696 -73.8319627382549) 1503828278000, (0000f77c-62c2a288.mov?t=25 60 -1 0.001 -51.8618651597633 -1 -1 'null' 'null' 40.6755787181115 -73.832005485961) 1503828279000, (0000f77c-62c2a288.mov?t=26 60 -1 0.001 -52.7848207720977 -1 -1 'null' 'null' 40.6755518960214 -73.8320690207871) 1503828280000, (0000f77c-62c2a288.mov?t=27 60 -1 0.001 -52.8098779896296 -1 -1 'null' 'null' 40.6755173625803 -73.8321510796191) 1503828281000, (0000f77c-62c2a288.mov?t=28 60 -1 0.001 -53.1082970113209 -1 -1 'null' 'null' 40.675476877988 -73.832251075724) 1503828282000, (0000f77c-62c2a288.mov?t=29 60 -1 0.001 -53.068188778856 -1 -1 'null' 'null' 40.6754317414394 -73.8323619683029) 1503828283000, (0000f77c-62c2a288.mov?t=30 60 -1 0.001 -53.0305369542053 -1 -1 'null' 'null' 40.6753836712247 -73.8324794825854) 1503828284000, (0000f77c-62c2a288.mov?t=31 60 -1 0.001 -53.3029420481242 -1 -1 'null' 'null' 40.6753343018151 -73.8326047082188) 1503828285000, (0000f77c-62c2a288.mov?t=32 60 -1 0.001 -53.0197532025893 -1 -1 'null' 'null' 40.6752801547206 -73.8327368908318) 1503828286000, (0000f77c-62c2a288.mov?t=33 60 -1 0.001 -52.5874628391729 -1 -1 'null' 'null' 40.6752199726558 -73.8328760304245) 1503828287000, (0000f77c-62c2a288.mov?t=34 60 -1 0.001 -51.9302203881571 -1 -1 'null' 'null' 40.6751535460732 -73.8330185227784) 1503828288000, (0000f77c-62c2a288.mov?t=35 60 -1 0.001 -50.922166841815 -1 -1 'null' 'null' 40.6750807911536 -73.8331595902088) 1503828289000, (0000f77c-62c2a288.mov?t=36 60 -1 0.001 -47.9597861934417 -1 -1 'null' 'null' 40.6749968044839 -73.8332880847844) 1503828290000, (0000f77c-62c2a288.mov?t=37 60 -1 0.001 -43.3243365064324 -1 -1 'null' 'null' 40.6749011250592 -73.8333986420872) 1503828291000, (0000f77c-62c2a288.mov?t=38 60 -1 0.001 -38.9440321737091 -1 -1 'null' 'null' 40.6747988239309 -73.8334934414121) 1503828292000, (0000f77c-62c2a288.mov?t=39 60 -1 0.001 -34.9974711565284 -1 -1 'null' 'null' 40.6746977800882 -73.8335713931116) 1503828293000, (0000f77c-62c2a288.mov?t=40 60 -1 0.001 -30.9875643183179 -1 -1 'null' 'null' 40.6746057467914 -73.833630569348) 1503828294000, (0000f77c-62c2a288.mov?t=41 60 -1 0.001 -27.4478605126405 -1 -1 'null' 'null' 40.6745297229296 -73.8336720597687) 1503828295000)");
		MVideo mv2 = (MVideo)reader.read("MVIDEO (('localhost:///tmp/drone/test1.jpg' 60 -1 0.001 30 -1 -1 'null' 'null' 100 100) 1481480632123, ('localhost:///tmp/drone/test1.jpg' 60 -1 0.001 30 -1 -1 'annotation' 'exif' 101 101) 1481480634123)");
		String ms ="mvideo {\\\"\\\\\\\"localhost:///tmp/drone/test1.jpg\\\\\\\"\\\",\\\"\\\\\\\"localhost:///tmp/drone/test1.jpg\\\\\\\"\\\"};{\\\"(100,100)\\\",\\\"(101,101)\\\"};{60,60};{0.001,0.001};{30,30};{\\\"2016-12-11 13:23:52.123\\\",\\\"2016-12-11 13:23:54.123\\\"}";
		System.out.println(mv.time());
		//System.out.println(TemporalOP.M_Time(mv.toGeoString()));
		//---------------------------------------------------------------------	
		/*
		System.out.println(mv.toGeoString());
		System.out.println(mv.numOf());
		System.out.println(mv.snapshot(1481480632123l));
		System.out.println(mv.slice(1481480633000l, 1481480638000l).toGeoString());
		System.out.println(mv.atomize(1000).toGeoString());
		System.out.println("-----------------------");
		System.out.println(mv.lattice(1000).toGeoString());
		System.out.println(mv.first().toGeoString());
		System.out.println(mv.last().toGeoString());
		System.out.println(mv.at(2).toGeoString());
		*/
		//---------------------------------------------------------------------
		
				//Geometry cood = mv.spatial();
		//System.out.println(cood.getCoordinates()[0].x);	
		/*
		System.out.println(MovingDistance.distance(mv,mv2).toGeoString());
		mh = new MHausdorff();
		MHausdorff mh;
		mh = new MHausdorff();
		System.out.println(mh.measure(mv, mv2));
		*/
		
		for (int numof = 1; numof <= 10; numof++) {
			try {
				File file = new File( "D://train/1 (" + numof + ").json");
				String content = FileUtils.readFileToString(file, "UTF-8");
				String[] rideid = content.split("rideID");
				String name1= rideid[1].split("\",")[0].replaceAll("\"\\: \"", "").substring(0,8);
				String[] filename = content.split("filename");
				String name2= filename[1].split("\",")[0].replaceAll("\"\\: \"", "").substring(0,8);
				String videoName = name1+"-"+name2+".mov";
				
			} catch (Exception e) {
				continue;
			}
		}
		}
}