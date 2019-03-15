package com.awarematics.postmedia.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MPolygon;

public class MvoingRegions {
	static MGeometryFactory geometryFactory = new MGeometryFactory();
	static MWKTReader reader = new MWKTReader(geometryFactory);
	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

		
		MPolygon mpolygon1 = (MPolygon) reader
				.read("MPOLYGON ((10 10, 20 20, 30 40, 10 10) 123, (10 10, 20 20, 21 21, 30 40, 10 10) 456)");
		 MPolygon mpolygon2 = (MPolygon) reader.read("MPOLYGON ((10 10, 20 20, 30 50, 10 10) 222, (10 10, 20 20, 21 71, 20 50, 10 10) 333)");
		//MPolygon mpolygon2 = (MPolygon) reader.read("MPOLYGON ((0 0, 1 1, 1 0, 0 0) 1000)");
		
		ArrayList<MPolygon> a = Operation(mpolygon1,mpolygon2);		
		ArrayList<MPolygon> b = Operation(mpolygon2,mpolygon1);
	    
		

	}
	public static ArrayList<MPolygon> Operation(MPolygon mpolygon1, MPolygon mpolygon2)
	{
		Polygon[] pol1 = mpolygon1.getListPolygon();
		Polygon[] pol2 = mpolygon2.getListPolygon();
		// Polygon[] pol3 = mpolygon2.getListPolygon();
		long[] t1 = mpolygon1.getTimes();

		ArrayList<Polygon> Lf = new ArrayList<Polygon>();
		ArrayList<Polygon> Lg = new ArrayList<Polygon>();

		for (int i = 0; i < pol1.length; i++)
			for (int j = 0; j < pol2.length; j++) {
				if (pol1[i].intersects(pol2[j])) {
					Lf.add((Polygon) pol1[i].intersection(pol2[j]));
				}
			}
		for (int i = 0; i < pol2.length; i++)
			for (int j = 0; j < pol1.length; j++) {
				if (pol2[i].intersects(pol1[j])) {
					Lg.add((Polygon) pol2[i].intersection(pol1[j]));
				}
			}
		// -----------------------------------algorithm1
		ArrayList<Polygon> Sf = new ArrayList<Polygon>();
		ArrayList<Long> t = new ArrayList<Long>();
		/*
		 * this is Sf -----> union intersection difference
		 */
		Polygon[][] differenceFtoG = new Polygon[pol1.length][pol2.length];
		Polygon[][] differenceGtoF = new Polygon[pol1.length][pol2.length];
		Polygon[][] union = new Polygon[pol1.length][pol2.length];
		Polygon[][] intersection = new Polygon[pol1.length][pol2.length];
		
		for (int i = 0; i < pol1.length; i++) {
			for (int j = i; j < pol2.length;) {
				if (pol1[i].intersects(pol2[j])) {
					if (pol1[i].within(pol2[j])) {
						// inside
						differenceFtoG[i][j] = null;
						union[i][j] = null;
						differenceGtoF[i][j] = pol1[i];
						intersection[i][j] = pol1[i];
						Sf.add(pol1[i]);// Sf --->intersection example
						t.add(t1[i]);
						break;
					} else { // intersection--- 6.1.2 algorithm
						Sf.add((Polygon) pol1[i].intersection(pol2[j]));
						t.add(t1[i]);
						//num++;
						break;
					}

				} else { // ouside
					differenceFtoG[i][j] = pol2[j];
					union[i][j] = pol2[j];
					differenceGtoF[i][j] = null;
					intersection[i][j] = null;
					//Sf.add(null);// Sf --->intersection example  null
					//t.add(t1[i]);
					break;
				}
			}
		}
		/*
		 * lexicograzphically
		 */
		
		ArrayList<Polygon> temp_Sf = new ArrayList<Polygon>();
		ArrayList<Long> temp_t = new ArrayList<Long>();
		for (int i = 0; i < Sf.size(); i++)
			temp_Sf.add(Sf.get(i));
		extracted(Sf);
		
		for (int i = 0; i < Sf.size(); i++) {
			for (int j = 0; j < temp_Sf.size(); j++)
				if (temp_Sf.get(j).equals(Sf.get(i))) {				
						temp_t.add(t.get(j));
						break;
				}
		}
		System.out.println(temp_Sf);
		System.out.println(Sf);
		System.out.println(t);
		System.out.println(temp_t);
		ArrayList<MPolygon> Df = new ArrayList<MPolygon>();

		for (int i = 0; i < Sf.size(); i++) {
			if (i == 0) {
				Polygon[] pp = new Polygon[1];
				long[] tt = new long[1];
				pp[0] = Sf.get(i);
				tt[0] = temp_t.get(i);
				MPolygon mp = geometryFactory.createMPolygon(pp, tt);
				Df.add(mp);
			} else if (temp_t.get(i) > temp_t.get(i - 1)) {
				Polygon[] pp = new Polygon[1];
				long[] tt = new long[1];
				pp[0] = Sf.get(i);
				tt[0] = temp_t.get(i);
				MPolygon mp = geometryFactory.createMPolygon(pp, tt);
				Df.add(mp);

			}
		}
		System.out.println(Df.size());
		System.out.println(Df.get(0).getTimes()[0]);
		return Df;
	}
	//--------------------------------------------algorithm2
	@SuppressWarnings("unchecked")
	private static void extracted(ArrayList<Polygon> Sf) {
		Collections.sort(Sf);
	}
}
