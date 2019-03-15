package com.awarematics.postmedia.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MPolygon;

public class MvoingRegions {
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

		MGeometryFactory geometryFactory = new MGeometryFactory();
		MWKTReader reader = new MWKTReader(geometryFactory);
		MPolygon mpolygon1 = (MPolygon) reader.read("MPOLYGON ((10 10, 20 20, 30 40, 10 10) 523, (10 10, 20 20, 21 21, 30 40, 10 10) 456)");
	//	MPolygon mpolygon2 = (MPolygon) reader.read("MPOLYGON ((10 10, 20 20, 30 50, 10 10) 123, (10 10, 20 20, 21 71, 20 50, 10 10) 456)");
		MPolygon mpolygon2 = (MPolygon) reader.read("MPOLYGON ((0 0, 1 1, 1 0, 0 0) 1000)");
		
		Polygon[] pol1 = mpolygon1.getListPolygon();
		Polygon[] pol2 = mpolygon2.getListPolygon();
		//Polygon[] pol3 = mpolygon2.getListPolygon();
		long[] t1 = mpolygon1.getTimes();
		long[] t2 = mpolygon2.getTimes();
		
	
		
		ArrayList<Polygon> Lf = new ArrayList<Polygon>();
		ArrayList<Polygon> Lg = new ArrayList<Polygon>();
		
		for(int i =0;i< pol1.length;i++)
			for(int j=0;j< pol2.length;j++)
			{
				if(pol1[i].intersects(pol2[j]))
				{
					Lf.add((Polygon) pol1[i].intersection(pol2[j]));
				}
			}
		for(int i =0;i< pol2.length;i++)
			for(int j=0;j< pol1.length;j++)
			{
				if(pol2[i].intersects(pol1[j]))
				{
					Lg.add((Polygon) pol2[i].intersection(pol1[j]));
				}
			}
		//-----------------------------------algorithm1
		ArrayList<MPolygon> Sf = new ArrayList<MPolygon>();
		ArrayList<Long> t = new ArrayList<Long>();	
		/*
		 * this is Sf -----> union intersection  difference
		 */
		Polygon[][] differenceFtoG = new Polygon[pol1.length][pol2.length];
		Polygon[][] differenceGtoF = new Polygon[pol1.length][pol2.length];
		Polygon[][] union = new Polygon[pol1.length][pol2.length];
		Polygon[][] intersection = new Polygon[pol1.length][pol2.length];
		
		for(int i =0;i< pol1.length;i++)
		{
			for(int j=0;j< pol2.length;j++)
			{
				if(pol1[i].intersects(pol2[j]))
				{
					if(pol1[i].within(pol2[j]))
					{
						// inside
						differenceFtoG[i][j]= null;
						union[i][j] = null;
						differenceGtoF[i][j] = pol1[i];
						intersection[i][j] = pol1[i];
						Polygon[] pp = new Polygon[1];
						long[] tt= new long[1];
						pp[0]=pol1[i];
						tt[0]= t1[i];
						MPolygon mp = geometryFactory.createMPolygon(pp, tt);
						Sf.add(mp);//   Sf --->intersection example
					}
					else{ // intersection--- 6.1.2 algorithm
						Polygon[] pp = new Polygon[1];
						long[] tt= new long[1];
						pp[0]=(Polygon) pol1[i].intersection(pol2[j]);
						tt[0]= t1[i];
						MPolygon mp = geometryFactory.createMPolygon(pp, tt);
						Sf.add(mp);
					}
					  
				}
				else { //ouside
					differenceFtoG[i][j]= pol2[j];
					union[i][j] = pol2[j];
					differenceGtoF[i][j] = null;
					intersection[i][j] = null;
					
					Polygon[] pp = new Polygon[1];
					long[] tt= new long[1];
					pp[0]=pol1[i];
					tt[0]= t1[i];
					MPolygon mp = geometryFactory.createMPolygon(pp, tt);
					Sf.add(mp);//   Sf --->intersection example
				}
			}		
		}
		/*
		 * lexicograzphically
		 */
		
		Collections.sort(Sf);
		
	System.out.println(Sf.get(0).toGeoString()+"\t"+Sf.get(1).toGeoString());
		ArrayList<MPolygon> Df = new ArrayList<MPolygon>();
		
		for(int i=0;i<Sf.size();i++)
		{
			//if(t.get(i))
		}
		
	}
}
