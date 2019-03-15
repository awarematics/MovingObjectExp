package com.awarematics.postmedia.algorithms.distance;

import java.awt.List;

import org.locationtech.jts.algorithm.PointLocator;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.operation.distance.GeometryLocation;

import com.awarematics.postmedia.types.mediamodel.MGeometry;

public class MDistanceOp {
	
	 public static double distance(MGeometry g0, MGeometry g1)
	  {
		System.out.println("1");
	    MDistanceOp distOp = new MDistanceOp(g0, g1);
	    return distOp.distance();
	  }
	 public static double distance(MGeometry g0, Geometry g1)
	  {
		System.out.println("1");
	    MDistanceOp distOp = new MDistanceOp(g0, g1);
	    return distOp.distance();
	  }

	  public static boolean isWithinDistance(MGeometry g0, MGeometry g1, double distance)
	  {
	    MDistanceOp distOp = new MDistanceOp(g0, g1, distance);
	    return distOp.distance() <= distance;
	  }

	  public static Coordinate[] nearestPoints(MGeometry g0, MGeometry g1)
	  {
	    MDistanceOp distOp = new MDistanceOp(g0, g1);
	    return distOp.nearestPoints();
	  }

	  public static Coordinate[] closestPoints(MGeometry g0, MGeometry g1)
	  {
	    MDistanceOp distOp = new MDistanceOp(g0, g1);
	    return distOp.nearestPoints();
	  }

	  // input
	  private MGeometry[] geom;
	  private Geometry[] geomm;
	  private double terminateDistance = 0.0;
	  // working
	  @SuppressWarnings("unused")
	private PointLocator ptLocator = new PointLocator();
	  private GeometryLocation[] minDistanceLocation;
	  private double minDistance = Double.MAX_VALUE;

	  public MDistanceOp(MGeometry g0, MGeometry g1)
	  {
	    this(g0, g1, 0.0);
	  }
	  
	  public MDistanceOp(MGeometry g0, Geometry g1)
	  {
	    this(g0, g1, 0.0);
	  }

	  public MDistanceOp(MGeometry g0, MGeometry g1, double terminateDistance)
	  {
	    this.geom = new MGeometry[2];
	    geom[0] = g0;
	    geom[1] = g1;
	    this.terminateDistance = terminateDistance;
	  }
	  public MDistanceOp(MGeometry g0, Geometry g1, double terminateDistance)
	  {
	    this.geom = new MGeometry[2];
	    geom[0] = g0;
	    geomm[0] = g1;
	    this.terminateDistance = terminateDistance;
	  }

	  public double distance()
	  {System.out.println("2");
	  	if (geom[0] == null || geom[1] == null)
	  		throw new IllegalArgumentException("null geometries are not supported");
	  //	if (geom[0].isEmpty() || geom[1].isEmpty()) 
	  //		return 0.0;
	  	
	    computeMinDistance();
	    return minDistance;
	  }

	  public Coordinate[] nearestPoints()
	  {
	    computeMinDistance();
	    Coordinate[] nearestPts
	        = new Coordinate[] {
	          minDistanceLocation[0].getCoordinate(),
	          minDistanceLocation[1].getCoordinate() };
	    return nearestPts;
	  }
	  
	  public Coordinate[] closestPoints()
	  {
	    return nearestPoints();
	  }

	  public GeometryLocation[] nearestLocations()
	  {
	    computeMinDistance();
	    return minDistanceLocation;
	  }
	  
	  public GeometryLocation[] closestLocations()
	  {
	    return nearestLocations();
	  }

	  @SuppressWarnings("unused")
	private void updateMinDistance(GeometryLocation[] locGeom, boolean flip)
	  {
	    // if not set then don't update
	    if (locGeom[0] == null) return;

	    if (flip) {
	      minDistanceLocation[0] = locGeom[1];
	      minDistanceLocation[1] = locGeom[0];
	    }
	    else {
	      minDistanceLocation[0] = locGeom[0];
	      minDistanceLocation[1] = locGeom[1];
	    }
	  }

	  private void computeMinDistance()
	  {
	    // only compute once!
	    if (minDistanceLocation != null) return;

	    minDistanceLocation = new GeometryLocation[2];
	    computeContainmentDistance();
	    if (minDistance <= terminateDistance) return;
	    //computeFacetDistance();
	  }

	  private void computeContainmentDistance()
	  {
	    GeometryLocation[] locPtPoly = new GeometryLocation[2];
	    // test if either geometry has a vertex inside the other
	    computeContainmentDistance(0, locPtPoly);
	    if (minDistance <= terminateDistance) return;
	    computeContainmentDistance(1, locPtPoly);
	  }
	  
	  private void computeContainmentDistance(int polyGeomIndex, GeometryLocation[] locPtPoly)
	  {
	   /* Geometry polyGeom = geom[polyGeomIndex];
	 
	  	int locationsIndex = 1 - polyGeomIndex;
	    List polys = PolygonExtracter.getPolygons(polyGeom);
	    if (polys.size() > 0) {
	      List insideLocs = ConnectedElementLocationFilter.getLocations(geom[locationsIndex]);
	      computeContainmentDistance(insideLocs, polys, locPtPoly);
	      if (minDistance <= terminateDistance) {
	      	// this assigment is determined by the order of the args in the computeInside call above
	        minDistanceLocation[locationsIndex] = locPtPoly[0];
	        minDistanceLocation[polyGeomIndex] 	= locPtPoly[1];
	        return;
	      }
	    }*/	
	  }
	  
	  @SuppressWarnings("unused")
	private void computeContainmentDistance(List locs, List polys, GeometryLocation[] locPtPoly)
	  {
	    /*for (int i = 0; i < locs.size(); i++) {
	      GeometryLocation loc = (GeometryLocation) locs.get(i);
	      for (int j = 0; j < polys.size(); j++) {
	      	computeContainmentDistance(loc, (Polygon) polys.get(j), locPtPoly);
	        if (minDistance <= terminateDistance) return;
	      }
	    }*/
	  }
/*
	  private void computeContainmentDistance(GeometryLocation ptLoc,
	      Polygon poly,
	      GeometryLocation[] locPtPoly)
	  {
	    Coordinate pt = ptLoc.getCoordinate();
	    // if pt is not in exterior, distance to geom is 0
	    if (Location.EXTERIOR != ptLocator.locate(pt, poly)) {
	      minDistance = 0.0;
	      locPtPoly[0] = ptLoc;
	      locPtPoly[1] = new GeometryLocation(poly, pt);;
	      return;
	    }
	  }
*//*
	  private void computeFacetDistance()
	  {
	    GeometryLocation[] locGeom = new GeometryLocation[2];

	 
	    List lines0 = LinearComponentExtracter.getLines(geom[0]);
	    List lines1 = LinearComponentExtracter.getLines(geom[1]);

	    List pts0 = PointExtracter.getPoints(geom[0]);
	    List pts1 = PointExtracter.getPoints(geom[1]);

	    // exit whenever minDistance goes LE than terminateDistance
	    computeMinDistanceLines(lines0, lines1, locGeom);
	    updateMinDistance(locGeom, false);
	    if (minDistance <= terminateDistance) return;

	    locGeom[0] = null;
	    locGeom[1] = null;
	    computeMinDistanceLinesPoints(lines0, pts1, locGeom);
	    updateMinDistance(locGeom, false);
	    if (minDistance <= terminateDistance) return;

	    locGeom[0] = null;
	    locGeom[1] = null;
	    computeMinDistanceLinesPoints(lines1, pts0, locGeom);
	    updateMinDistance(locGeom, true);
	    if (minDistance <= terminateDistance) return;

	    locGeom[0] = null;
	    locGeom[1] = null;
	    computeMinDistancePoints(pts0, pts1, locGeom);
	    updateMinDistance(locGeom, false);
	  }

	  private void computeMinDistanceLines(List lines0, List lines1, GeometryLocation[] locGeom)
	  {
	    for (int i = 0; i < lines0.size(); i++) {
	      LineString line0 = (LineString) lines0.get(i);
	      for (int j = 0; j < lines1.size(); j++) {
	        LineString line1 = (LineString) lines1.get(j);
	        computeMinDistance(line0, line1, locGeom);
	        if (minDistance <= terminateDistance) return;
	      }
	    }
	  }

	  private void computeMinDistancePoints(List points0, List points1, GeometryLocation[] locGeom)
	  {
	    for (int i = 0; i < points0.size(); i++) {
	      Point pt0 = (Point) points0.get(i);
	      for (int j = 0; j < points1.size(); j++) {
	        Point pt1 = (Point) points1.get(j);
	        double dist = pt0.getCoordinate().distance(pt1.getCoordinate());
	        if (dist < minDistance) {
	          minDistance = dist;
	          locGeom[0] = new GeometryLocation(pt0, 0, pt0.getCoordinate());
	          locGeom[1] = new GeometryLocation(pt1, 0, pt1.getCoordinate());
	        }
	        if (minDistance <= terminateDistance) return;
	      }
	    }
	  }

	  private void computeMinDistanceLinesPoints(List lines, List points,
	      GeometryLocation[] locGeom)
	  {
	    for (int i = 0; i < lines.size(); i++) {
	      LineString line = (LineString) lines.get(i);
	      for (int j = 0; j < points.size(); j++) {
	        Point pt = (Point) points.get(j);
	        computeMinDistance(line, pt, locGeom);
	        if (minDistance <= terminateDistance) return;
	      }
	    }
	  }

	  private void computeMinDistance(LineString line0, LineString line1,
	                                  GeometryLocation[] locGeom)
	  {
	    if (line0.getEnvelopeInternal().distance(line1.getEnvelopeInternal())
	        > minDistance)
	          return;
	    Coordinate[] coord0 = line0.getCoordinates();
	    Coordinate[] coord1 = line1.getCoordinates();
	      // brute force approach!
	    for (int i = 0; i < coord0.length - 1; i++) {
	      for (int j = 0; j < coord1.length - 1; j++) {
	        double dist = Distance.segmentToSegment(
	                                        coord0[i], coord0[i + 1],
	                                        coord1[j], coord1[j + 1] );
	        if (dist < minDistance) {
	          minDistance = dist;
	          LineSegment seg0 = new LineSegment(coord0[i], coord0[i + 1]);
	          LineSegment seg1 = new LineSegment(coord1[j], coord1[j + 1]);
	          Coordinate[] closestPt = seg0.closestPoints(seg1);
	          locGeom[0] = new GeometryLocation(line0, i, closestPt[0]);
	          locGeom[1] = new GeometryLocation(line1, j, closestPt[1]);
	        }
	        if (minDistance <= terminateDistance) return;
	      }
	    }
	  }

	  private void computeMinDistance(LineString line, Point pt,
	                                  GeometryLocation[] locGeom)
	  {
	    if (line.getEnvelopeInternal().distance(pt.getEnvelopeInternal())
	        > minDistance)
	          return;
	    Coordinate[] coord0 = line.getCoordinates();
	    Coordinate coord = pt.getCoordinate();
	      // brute force approach!
	    for (int i = 0; i < coord0.length - 1; i++) {
	        double dist = Distance.pointToSegment(
	            coord, coord0[i], coord0[i + 1] );
	        if (dist < minDistance) {
	          minDistance = dist;
	          LineSegment seg = new LineSegment(coord0[i], coord0[i + 1]);
	          Coordinate segClosestPoint = seg.closestPoint(coord);
	          locGeom[0] = new GeometryLocation(line, i, segClosestPoint);
	          locGeom[1] = new GeometryLocation(pt, 0, coord);
	        }
	        if (minDistance <= terminateDistance) return;

	    }
	  }*/


}
