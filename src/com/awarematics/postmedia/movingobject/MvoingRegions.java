package com.awarematics.postmedia.movingobject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;

import com.awarematics.postmedia.io.MWKTReader;
import com.awarematics.postmedia.mgeom.MGeometryFactory;
import com.awarematics.postmedia.types.mediamodel.MDouble;
import com.awarematics.postmedia.types.mediamodel.MPolygon;

public class MvoingRegions {
	static MGeometryFactory geometryFactory = new MGeometryFactory();
	static MWKTReader reader = new MWKTReader(geometryFactory);

	public static void main(String[] args) throws IOException, ParseException, java.text.ParseException {

		MPolygon mpolygon1 = (MPolygon) reader
				.read("MPOLYGON ((10 10, 20 20, 30 40, 10 10) 123, (10 10, 20 20, 21 21, 30 40, 10 10) 456)");
		MPolygon mpolygon2 = (MPolygon) reader
				.read("MPOLYGON ((10 10, 20 20, 30 50, 10 10) 222, (10 10, 20 20, 21 71, 20 50, 10 10) 333)");
		// MPolygon mpolygon2 = (MPolygon) reader.read("MPOLYGON ((0 0, 1 1, 1
		// 0, 0 0) 1000)");

		ArrayList<MPolygon> a = operation(mpolygon1, mpolygon2, "union");		
		ArrayList<MPolygon> b = operation(mpolygon2, mpolygon1, "union");//ArrayList<MPolygon> b = operation(mpolygon2, mpolygon1, "intersection");
		MDouble mb = result(a, b);
		System.out.println(mb.toGeoString());

	}

	public static ArrayList<MPolygon> operation(MPolygon mpolygon1, MPolygon mpolygon2,String expression) {
		Polygon[] pol1 = mpolygon1.getListPolygon();
		Polygon[] pol2 = mpolygon2.getListPolygon();
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

		for (int i = 0; i < pol1.length; i++) {
			for (int j = i; j < pol2.length;) {
				if (pol1[i].intersects(pol2[j])) {
					if (pol1[i].within(pol2[j])) {
						// inside
						switch(expression){
					    case "union" :
					    	// Sf --->null    outside
					       break; 
					    case "intersection" :
					    	Sf.add(pol1[i]);// Sf --->intersection example
							t.add(t1[i]);
					       break; 
					    case "AdifferenceB" :// a/b  null					       
						       break; 
					    case "BdifferenceA" :
					    	Sf.add(pol1[i]);// Sf --->b/a example
							t.add(t1[i]); 
						       break; 
					    default : break;
					}
						
						break;
					} else { // intersection--- 6.1.2 algorithm
						switch(expression){
					    case "union" :
					    	Sf.add((Polygon) pol1[i].union(pol2[j]));
							t.add(t1[i]);
					       break; 
					    case "intersection" :
					    	Sf.add((Polygon) pol1[i].intersection(pol2[j]));
							t.add(t1[i]);
					       break; 
					    case "AdifferenceB" :
					    	Sf.add((Polygon) pol1[i].difference(pol2[j]));
							t.add(t1[i]);
						       break; 
					    case "BdifferenceA" :
					    	Sf.add((Polygon) pol2[j].difference(pol1[i]));
							t.add(t1[i]);
						       break; 
					    default : break;
						}
						
						// num++;
						break;
					}

				} else { // ouside
					
					switch(expression){
				    case "union" :
				    	Sf.add(pol1[i]);// 
						t.add(t1[i]); 
				       break; 
				    case "intersection" :
				       break; 
				    case "AdifferenceB" :
				    	Sf.add(pol1[i]);//
						t.add(t1[i]); 
					       break; 
				    case "BdifferenceA" ://null				       
					       break; 
				    default : break;
				    }
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

		ArrayList<MPolygon> Df = new ArrayList<MPolygon>();

		for (int i = 0; i < Sf.size(); i++) {

			Polygon[] pp = new Polygon[1];
			long[] tt = new long[1];
			pp[0] = Sf.get(i);
			tt[0] = temp_t.get(i);
			MPolygon mp = geometryFactory.createMPolygon(pp, tt);
			Df.add(mp);
		}
		// System.out.println(Df.get(0).getTimes()[0]);
		return Df;
	}

	// --------------------------------------------algorithm2
	@SuppressWarnings("unchecked")
	private static void extracted(ArrayList<Polygon> Sf) {
		Collections.sort(Sf);
	}

	public static MDouble result(ArrayList<MPolygon> sf1, ArrayList<MPolygon> sf2) {
		ArrayList<MPolygon> St = new ArrayList<MPolygon>();
		ArrayList<Long> sort_time = new ArrayList<Long>();
		ArrayList<Double> sort_value = new ArrayList<Double>();
		ArrayList<Long> stime1 = new ArrayList<Long>();
		ArrayList<Long> stime2 = new ArrayList<Long>();

		for (int i = 0; i < sf1.size(); i++) {
			St.add(sf1.get(i));
			stime1.add(sf1.get(i).getTimes()[0]);
		}
		for (int i = 0; i < sf2.size(); i++) {
			St.add(sf2.get(i)); // union a and b
			stime2.add(sf2.get(i).getTimes()[0]);
		}
		/*
		 * lexicograzphically
		 */
		long[] tempList1 = stime1.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		long[] tempList2 = stime2.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		Arrays.sort(tempList1);
		Arrays.sort(tempList2);
		for (int i = 0; i < St.size(); i++) {
			long min = 9223372036854775807l;
			double sum = 0;
			for (int j = 0; j < St.size(); j++) {
				if (St.get(i).getTimes()[0] < min) {
					min = St.get(i).getTimes()[0];
					sum = St.get(i).getListPolygon()[0].getArea();
				}
			}
			sort_time.add(min);
			sort_value.add(sum);
		}
		long overlappedStartTime = Math.max(tempList1[0], tempList2[0]);
		long overlappedEndTime = Math.min(tempList1[tempList1.length - 1], tempList2[tempList2.length - 1]);
		
		ArrayList<Long> resulttime = new ArrayList<Long>();
		ArrayList<Double> resultvalue = new ArrayList<Double>();
		for(int i=0;i<sort_time.size();i++)
		{
			if(sort_time.get(i) >= overlappedStartTime && sort_time.get(i)<=overlappedEndTime)
			{
				resulttime.add(sort_time.get(i));
				resultvalue.add(sort_value.get(i));
			}
		}
		double[] value =  resultvalue.stream().filter(i -> i != null).mapToDouble(i -> i).toArray();
		long[] times =  resulttime.stream().filter(i -> i != null).mapToLong(i -> i).toArray();
		if (overlappedStartTime > overlappedEndTime) {
			MDouble mdouble = geometryFactory.createMDouble(value, times);
			return mdouble;
		} else {
			MDouble mdouble = geometryFactory.createMDouble(value, times);
			return mdouble;//  in moving object databases, the type is mreal-------same with mdouble
		}

	}
}
