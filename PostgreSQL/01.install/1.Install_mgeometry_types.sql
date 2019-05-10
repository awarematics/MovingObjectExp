
----mgeometry fov  stphoto   mvideo   mphoto   mpolygon    mbool    mpoint   point   mdouble   mduration   mint  minstant  mmultipoint   mstring   mlinestring   mperiod   period
/*
MPeriod :  MPERIOD ((period), (period), ….)
MDuration :  MDURATION (duration, duration, duration….)
MInstant : MINSTANT (instant, instant, instant ….)
MInt :  MINT (counts times, counts times, ….)
MBool :  MBOOL (boolean times, boolean times, boolean times...)
MDouble : MDOUBLE (double times, double times ….)
MMultiPoint :  MMUltiPoint ( points[]  times, points[]  times...)
MString :  MSTRING (text times, text times…)
MPoint :  MPOINT (point times, point times…)
MLineString :  MLINESTRING (linestring times, linestring times…)
MPolygon : MPOLYGON (polygon times, polygon times...)
MVideo :  MVIDEO ((uri altitude times annotationjson point fov(viewangle verticalangle direction direction3d distance)),(uri altitude times annotationjson point fov(viewangle verticalangle direction direction3d distance)) ...)
MPhoto :  MPHOTO ((uri width height altitude times annotationjson point fov(viewangle verticalangle direction direction3d distance)),(uri width height altitude times annotationjson point fov(viewangle verticalangle direction direction3d distance))...)
*/

CREATE TYPE mgeometry AS(
	mdouble	mdouble,
	mpoint	mpoint,
	mvideo	mvideo,
	mphoto	mphoto,
	mpolygon	mpolygon,
	mlinestring mlinestring,
	mbool	mbool,
	mduration	mduration,
	mint	mint,
	minstant	minstant,
	mmultipoint	mmultipoint,
	mstring		mstring,
	mperiod		mperiod
);

CREATE TYPE mdouble AS
(
	id oid,
	segid  text,
	doubles double precision[],
	timeline timestamp without time zone[],
	interpolation json
);

CREATE TYPE point AS
(
	pointx real,
	pointy real
);

CREATE TYPE mpoint AS
(
	id oid,
	coordinates point[],
	timeline timestamp without time zone[],
	interpolation json
);

CREATE TYPE mbool AS
(
	id oid,
	bools boolean[],
	timeline timestamp without time zone[],
	interpolation json
);

CREATE TYPE mpolygon AS
(
	id oid,
	polygons polygon[],
	timeline timestamp without time zone[],
	interpolation json
);

CREATE TYPE mphoto AS
(
	id oid,
	uri text[],
	width integer[],
	height integer[],
	altitude double precision[],
	times timestamp without time zone[],
	annotations json,
	points point[],
	fov FoV[]
);

CREATE TYPE mvideo AS
(
	id oid,
	uri text[],
	altitude double precision[],
	times timestamp without time zone[],
	annotations json[],
	points point,
	fov FoV[]
);

CREATE TYPE stphoto AS
(
	id oid,
	uri text,
	width integer,
	height integer,
	altitude double precision,
	times timestamp without time zone,
	annotations json,
	points point,
	fov FoV
);

CREATE TYPE FoV AS
(
	viewAngle double precision,
	verticalAngle double precision,
	direction double precision,
	distance double precision,
	direction3d double precision
);

CREATE TYPE mduration AS
(
	id oid,
	duration bigint[],
	interpolation json
);

CREATE TYPE minstant AS
(
	id oid,
	instant timestamp without time zone[],
	interpolation json
);

CREATE TYPE mint AS
(
	id oid,
	counts integer[],
	interpolation json
);

CREATE TYPE mmultipoint AS
(
	id oid,
	moints point[],
	times timestamp without time zone[],
	interpolation json
);

CREATE TYPE mstring AS
(
	id oid,
	mstrings text[],
	times timestamp without time zone[],
	interpolation json
);

CREATE TYPE mlinestring AS
(
	id oid,
	mlinestrings text[],
	times timestamp without time zone[],
	interpolation json
);

CREATE TYPE mperiod AS
(
	id oid,
	periods period[],
	interpolation json
);

CREATE TYPE period AS
(
	fromtime bigint,
	totime bigint
);

