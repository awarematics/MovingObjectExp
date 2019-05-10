-- FUNCTION: public.addmgeometrycolumn(character varying, character varying, character varying, integer, character varying, integer, integer)
-- FUNCTION: public.append(mpoint, character varying)
-- FUNCTION: public.append(mdouble, character varying)
-- FUNCTION: public.append(mpoint, point, timestamp without time zone)
-- FUNCTION: public.append(mdouble, double precision, timestamp without time zone)
-- FUNCTION: public.delete_mpoint()
-- FUNCTION: public.delete_mdouble()
-- FUNCTION: public.insert_mpoint()
-- FUNCTION: public.insert_mdouble()


CREATE OR REPLACE FUNCTION public.addmgeometrycolumn(
    character varying,
    character varying,
    character varying,
    integer,
    character varying,
    integer,
    integer)
  RETURNS text AS
$BODY$
DECLARE
    f_schema_name     alias for $1;
    f_table_name     alias for $2;
    f_column_name     alias for $3;
    srid        alias for $4;
    new_type     alias for $5;
    dimension     alias for $6;
	
    tpseg_size    alias for $7;
    real_schema name;
    sql text;
    table_oid text;
    temp_segtable_name text;
    f_mgeometry_segtable_name text;
    f_sequence_name    text;
    f_segtable_oid    oid;

BEGIN
    --verify SRID
    IF ( f_schema_name IS NOT NULL AND f_schema_name != '' ) THEN
        sql := 'SELECT nspname FROM pg_namespace ' ||
            'WHERE text(nspname) = ' || quote_literal(f_schema_name) ||
            'LIMIT 1';
        RAISE DEBUG '%', sql;
        EXECUTE sql INTO real_schema;

        IF ( real_schema IS NULL ) THEN
            RAISE EXCEPTION 'Schema % is not a valid schemaname', quote_literal(f_schema_name);
            RETURN 'fail';
        END IF;
    END IF;

    IF ( real_schema IS NULL ) THEN
        RAISE DEBUG 'Detecting schema';
        sql := 'SELECT n.nspname AS schemaname ' ||
            'FROM pg_catalog.pg_class c ' ||
              'JOIN pg_catalog.pg_namespace n ON n.oid = c.relnamespace ' ||
            'WHERE c.relkind = ' || quote_literal('r') ||
            ' AND n.nspname NOT IN (' || quote_literal('pg_catalog') || ', ' || quote_literal('pg_toast') || ')' ||
            ' AND pg_catalog.pg_table_is_visible(c.oid)' ||
            ' AND c.relname = ' || quote_literal(f_table_name);
        RAISE DEBUG '%', sql;
        EXECUTE sql INTO real_schema;

        IF ( real_schema IS NULL ) THEN
            RAISE EXCEPTION 'Table % does not occur in the search_path', quote_literal(f_table_name);
            RETURN 'fail';
        END IF;
    END IF;

    sql := 'select '|| quote_literal(f_table_name) ||'::regclass::oid';
    RAISE DEBUG '%', sql;
    EXECUTE sql INTO table_oid;
-------------------------------------------mpoint	
 	IF (new_type = 'mpoint')
    THEN       
    	f_sequence_name = quote_ident(f_table_name) || '_' || quote_ident(f_column_name) || '_mpointid_seq';

    	sql := 'CREATE SEQUENCE ' || quote_ident(f_sequence_name) || ' START 1';
   	 	RAISE DEBUG '%', sql;
   	 	EXECUTE sql;

    	-- Add trajectory column to table
    	sql := 'ALTER TABLE ' || quote_ident(f_table_name) || 
        	' ADD ' || quote_ident(f_column_name) || ' mpoint';
   		RAISE DEBUG '%', sql;
    	RAISE INFO '%', sql;
   		EXECUTE sql;    

    	-- Delete stale record in geometry_columns (if any)
   		sql := 'DELETE FROM mgeometry_columns WHERE
			f_table_name = ' || quote_literal(f_table_name) ||
        	' AND f_mgeometry_column = ' || quote_literal(f_column_name);
    	RAISE DEBUG '%', sql;
    	EXECUTE sql;

    	sql := 'DELETE FROM mgeometry_columns WHERE
       	 f_table_catalog = ' || quote_literal('') ||
       	 ' AND f_table_schema = ' ||quote_literal(real_schema) ||
       	 ' AND f_table_name = ' || quote_literal(f_table_name) ||
       	 ' AND f_mgeometry_column = ' || quote_literal(f_column_name);
   		RAISE DEBUG '%', sql;
   		EXECUTE sql;
   	 	temp_segtable_name := 'mpseq_' || table_oid || '_' || f_column_name;
	
    	EXECUTE 'CREATE TABLE ' || temp_segtable_name || ' 
        (
            mpid        integer,
            segid        integer,
            next_segid    integer,
            before_segid    integer,
            mpcount        integer,
			jsonfile	json,
            time    	timestamp[],
            tpseg        point[]
        )';
    	sql := 'select '|| quote_literal(temp_segtable_name) ||'::regclass::oid';
   		RAISE DEBUG '%', sql;
    	EXECUTE sql INTO f_segtable_oid;
   		-- segment table name
    	f_mgeometry_segtable_name := 'mpseq_' || f_segtable_oid ;   
   	 	EXECUTE 'ALTER TABLE ' || quote_ident(temp_segtable_name) || ' RENAME TO ' || quote_ident(f_mgeometry_segtable_name);
	
    	-- Add record in geometry_columns 
    	sql := 'INSERT INTO mgeometry_columns (f_table_catalog, f_table_schema, f_table_name, ' ||
            'f_mgeometry_column, f_mgeometry_segtable_name, coord_dimension, srid, type, '|| 
            'f_segtableoid, f_sequence_name, tpseg_size)' ||
        	' VALUES (' ||
       	 	quote_literal('') || ',' ||
        	quote_literal(real_schema) || ',' ||
        	quote_literal(f_table_name) || ',' ||
        	quote_literal(f_column_name) || ',' ||
        	quote_literal(f_mgeometry_segtable_name) || ',' || 
        	dimension::text || ',' ||
        	srid::text || ',' ||
        	quote_literal(new_type) || ', ' ||
        	quote_literal(f_segtable_oid) || ', ' ||
        	quote_literal(f_sequence_name) || ', ' ||
        	tpseg_size || ')';
    	RAISE DEBUG '%', sql;
    	EXECUTE sql;

    	sql := 'UPDATE ' || quote_ident(f_table_name)|| ' SET ' || quote_ident(f_column_name) || '.id = NEXTVAL(' || quote_literal(f_sequence_name) ||')';
    	RAISE DEBUG '%', sql;
    	EXECUTE sql;
    END IF;	
	-------------------------------------------mdouble	
 	IF (new_type = 'mdouble')
	THEN
		sql := 'select '|| quote_literal(f_table_name) ||'::regclass::oid';
    	RAISE DEBUG '%', sql;
    	EXECUTE sql INTO table_oid;

    	f_sequence_name = quote_ident(f_table_name) || '_' || quote_ident(f_column_name) || '_mdoubleid_seq';

    	sql := 'CREATE SEQUENCE ' || quote_ident(f_sequence_name) || ' START 1';
    	RAISE DEBUG '%', sql;
    	EXECUTE sql;

    -- Add trajectory column to table
    	sql := 'ALTER TABLE ' || quote_ident(f_table_name) || 
        	' ADD ' || quote_ident(f_column_name) || ' mdouble';
     	RAISE DEBUG '%', sql;
   		RAISE INFO '%', sql;
   	 	EXECUTE sql;    
	
 	-- Delete stale record in geometry_columns (if any)
   	 	sql := 'DELETE FROM mgeometry_columns WHERE
        	f_table_name = ' || quote_literal(f_table_name) ||
        	' AND f_mgeometry_column = ' || quote_literal(f_column_name);
    	RAISE DEBUG '%', sql;
   	 	EXECUTE sql;
	
    	sql := 'DELETE FROM mgeometry_columns WHERE
        	f_table_catalog = ' || quote_literal('') ||
        	' AND f_table_schema = ' ||quote_literal(real_schema) ||
        	' AND f_table_name = ' || quote_literal(f_table_name) ||
        	' AND f_mgeometry_column = ' || quote_literal(f_column_name);
    	RAISE DEBUG '%', sql;
    	EXECUTE sql;
    	temp_segtable_name := 'mdseq_' || table_oid || '_' || f_column_name;
	
    	EXECUTE 'CREATE TABLE ' || temp_segtable_name || ' 
        (
            mpid        integer,
            segid        integer,
            next_segid    integer,
            before_segid    integer,
            mdcount        integer,
            time    	timestamp[],
			jsonfile	json,
            tpseg        double precision[]
        )';
    	sql := 'select '|| quote_literal(temp_segtable_name) ||'::regclass::oid';
    	RAISE DEBUG '%', sql;
    	EXECUTE sql INTO f_segtable_oid;
    
    -- segment table name
    	f_mgeometry_segtable_name := 'mdseq_' || f_segtable_oid ;   
    	EXECUTE 'ALTER TABLE ' || quote_ident(temp_segtable_name) || ' RENAME TO ' || quote_ident(f_mgeometry_segtable_name);
	
    -- Add record in geometry_columns 
   	 	sql := 'INSERT INTO mgeometry_columns (f_table_catalog, f_table_schema, f_table_name, ' ||
           	 'f_mgeometry_column, f_mgeometry_segtable_name, coord_dimension, srid, type, '|| 
           	 'f_segtableoid, f_sequence_name, tpseg_size)' ||
        	' VALUES (' ||
        	quote_literal('') || ',' ||
        	quote_literal(real_schema) || ',' ||
        	quote_literal(f_table_name) || ',' ||
        	quote_literal(f_column_name) || ',' ||
        	quote_literal(f_mgeometry_segtable_name) || ',' || 
        	dimension::text || ',' ||
        	srid::text || ',' ||
        	quote_literal(new_type) || ', ' ||
        	quote_literal(f_segtable_oid) || ', ' ||
        	quote_literal(f_sequence_name) || ', ' ||
        	tpseg_size || ')';
    	RAISE DEBUG '%', sql;
    	EXECUTE sql;
		sql := 'UPDATE ' || quote_ident(f_table_name)|| ' SET ' || quote_ident(f_column_name) || '.id '
     	|| '= NEXTVAL(' || quote_literal(f_sequence_name) ||'), ' || quote_ident(f_column_name) || '.segid = ' || f_segtable_oid;
   		-- sql := 'UPDATE ' || quote_ident(f_table_name)|| ' SET ' || quote_ident(f_column_name) || '.id = NEXTVAL(' || quote_literal(f_sequence_name) ||')';
   		 RAISE DEBUG '%', sql;
   		 EXECUTE sql;
	END IF;	
	EXECUTE 'DROP TRIGGER IF EXISTS insert_mpoint ON ' || quote_ident(f_table_name);
	EXECUTE 'DROP TRIGGER IF EXISTS delete_mpoint ON ' || quote_ident(f_table_name);
    EXECUTE 'CREATE TRIGGER insert_mpoint 
        BEFORE INSERT ON ' || quote_ident(f_table_name) || ' FOR EACH ROW EXECUTE PROCEDURE insert_mpoint()';  
    EXECUTE 'CREATE TRIGGER delete_mpoint 
        AFTER DELETE ON ' || quote_ident(f_table_name) || ' FOR EACH ROW EXECUTE PROCEDURE delete_mpoint()';

    RETURN
        real_schema || '.' ||
        f_table_name || '.' || f_column_name ||
        ' SRID:' || srid::text ||
        ' TYPE:' || new_type ||
        ' DIMS:' || dimension::text || ' ';
END;
$BODY$
  LANGUAGE plpgsql VOLATILE STRICT
  COST 100;
ALTER FUNCTION public.addmgeometrycolumn(character varying, character varying, character varying, integer, character varying, integer, integer)
  OWNER TO postgres;
  
  
  
  
CREATE OR REPLACE FUNCTION append(mpoint, character varying) RETURNS mpoint AS
$$
DECLARE
	c_trajectory	alias for $1;
	array_mpoint	alias for $2;

	f_trajectory_segtable_name	text;
	f_table_name			text;
	array_size	integer;
	array_time	timestamp without time zone[];
	array_point	point[];

	i		integer;
	sql		text;
BEGIN
	array_time = regexp_split_to_array(MakeMPointTime(array_mpoint),',') ::timestamp without time zone[];
	array_point = regexp_split_to_array(MakeMPointCoordinate(array_mpoint),';') ::point[];
	
	execute 'select array_upper( $1, 1 )'
	into array_size using array_point;

	i := 1;
	WHILE( i <= array_size ) LOOP
		execute 'select append( $1, $2[$4], $3[$4] )'
		using c_trajectory, array_point,array_time ,i;
		 i := i+1;
	END LOOP;

	RETURN c_trajectory;
END
$$
LANGUAGE 'plpgsql' VOLATILE STRICT
COST 100;


CREATE OR REPLACE FUNCTION append(mdouble, character varying) RETURNS mdouble AS
$$
DECLARE
	c_trajectory	alias for $1;
	array_mdouble	alias for $2;

	f_trajectory_segtable_name	text;
	f_table_name			text;
	array_size	integer;
	array_time	timestamp without time zone[];
	array_double	double precision[];

	i		integer;
	sql		text;
BEGIN
	array_double = regexp_split_to_array(MakeMoubleDouble(array_mdouble),';') ::double precision[];
	array_time = regexp_split_to_array(MakeMoubleTime(array_mdouble),',') ::timestamp without time zone[];
	
	execute 'select array_upper( $1, 1 )'
	into array_size using array_double;

	i := 1;
	WHILE( i <= array_size ) LOOP
		execute 'select append( $1, $2[$4], $3[$4] )'
		using c_trajectory, array_double,array_time ,i;
		 i := i+1;
	END LOOP;

	RETURN c_trajectory;
END
$$
LANGUAGE 'plpgsql' VOLATILE STRICT
COST 100;
  
  

CREATE OR REPLACE FUNCTION public.append(
	mpoint,
	point,
	timestamp without time zone)
    RETURNS mpoint
    LANGUAGE 'plpgsql'

    COST 100
    VOLATILE STRICT 
AS $BODY$
DECLARE
    f_mgeometry            alias for $1;
    tp                alias for $2;
 	tp2                alias for $3;
    traj_prefix            char(50);
    traj_suffix            char(50);   
    f_mgeometry_segtable_name    char(200);
    table_oid            char(100);
    mpid                integer;
    segid                integer;
    mp_count            integer;
    mp_seq                record;
    max_tpseg_count    integer;
    cnt_mpid            integer;   
    sql                text;
    next_segid            integer;
    tp_seg_size            integer;
    new_segid            integer;
	jsonfiles			json;
	trajid			integer;
BEGIN
    -- traj_prefix := current_setting('traj.prefix');
    -- traj_suffix := current_setting('traj.suffix');
    
    traj_prefix := 'mpseq_' ;  
	traj_suffix := 'mpoint';
	sql := 'select f_segtableoid from mgeometry_columns where type = ' || quote_literal(traj_suffix);
    RAISE DEBUG '%', sql;
	RAISE INFO '%', sql;
    EXECUTE sql INTO trajid;
	
    f_mgeometry_segtable_name := traj_prefix || trajid ;
    mpid := f_mgeometry.id;

    sql := 'SELECT COUNT(*) FROM ' || quote_ident(f_mgeometry_segtable_name) || 
        ' WHERE mpid = ' || f_mgeometry.id;
    RAISE DEBUG '%', sql;
    EXECUTE sql INTO cnt_mpid;
    
    -- tpseg_size 
    sql := 'select tpseg_size from mgeometry_columns where f_mgeometry_segtable_name = ' || quote_literal(f_mgeometry_segtable_name);
    RAISE DEBUG '%', sql;
    EXECUTE sql INTO tp_seg_size;
		
    IF (cnt_mpid < 1) THEN
        EXECUTE 'INSERT INTO ' || quote_ident(f_mgeometry_segtable_name) || '(mpid, segid, mpcount, time, tpseg) 
            VALUES($1, 1, 1, ARRAY[($3)]::timestamp without time zone[], ARRAY[($2)]::point[])'
        USING mpid, tp, tp2;
		--get json ;
		 sql := 'select row_to_json((SELECT d from (select time,tpseg ) d)) from  ' || quote_ident(f_mgeometry_segtable_name) ;
   		 RAISE DEBUG '%', sql;
    	EXECUTE sql INTO jsonfiles;
		
		EXECUTE 'update ' || quote_ident(f_mgeometry_segtable_name) || ' set jsonfile = ($4) WHERE mpid = $1'
       USING  mpid, tp, tp2,jsonfiles;
    END IF;
    
    IF(cnt_mpid > 0) THEN
        sql := 'select segid from ' || quote_ident(f_mgeometry_segtable_name) ||
                ' where mpid = ' || f_mgeometry.id || ' and next_segid IS NULL';
        RAISE DEBUG '%', sql;
        EXECUTE sql INTO segid;
        sql := 'select array_upper((select tpseg from ' || quote_ident(f_mgeometry_segtable_name) || 
            ' where mpid = ' || f_mgeometry.id || ' and segid = ' || segid || '), 1)';
        RAISE DEBUG '%', sql;
        EXECUTE sql INTO max_tpseg_count;
       
        IF( segid IS NOT NULL AND max_tpseg_count < tp_seg_size) THEN
            EXECUTE 'UPDATE ' || quote_ident(f_mgeometry_segtable_name) || 
                ' set mpcount = mpcount+1, time = array_append(time, $2) , tpseg = array_append(tpseg, $3) 
                where mpid = $4 and segid = $5 '
            USING tp, tp2, tp, mpid, segid; 
			 sql := 'select row_to_json((SELECT d from (select time,tpseg ) d)) from  ' || quote_ident(f_mgeometry_segtable_name) ;
   		    RAISE DEBUG '%', sql;
			EXECUTE sql INTO jsonfiles;
			
    	    EXECUTE 'update ' || quote_ident(f_mgeometry_segtable_name) || ' set jsonfile = ($6) WHERE mpid = $4 and segid = $5'
            USING  tp, tp2, tp, mpid, segid,jsonfiles;
        ELSE 
            EXECUTE 'select MAX(segid) from ' || quote_ident(f_mgeometry_segtable_name) || ' where mpid = $1'
            INTO new_segid USING mpid;

            EXECUTE 'INSERT INTO ' || quote_ident(f_mgeometry_segtable_name) ||'(mpid, segid, before_segid, mpcount, time, tpseg) 
                VALUES( $1,  ' || new_segid+1 || ', $2, 1, ARRAY[( $3)]::timestamp without time zone[], ARRAY[( $4)]::point[])'
            USING f_mgeometry.id, segid, tp2, tp;
			
            EXECUTE 'UPDATE ' || quote_ident(f_mgeometry_segtable_name) || 
                ' set next_segid = ' || new_segid+1 || ' where mpid = $1 and segid = $2'
            USING mpid, segid;
			sql := 'select row_to_json((SELECT d from (select time,tpseg ) d)) from  ' || quote_ident(f_mgeometry_segtable_name) ;
   			RAISE DEBUG '%', sql;
    		EXECUTE sql INTO jsonfiles;
			
			EXECUTE 'update ' || quote_ident(f_mgeometry_segtable_name) || ' set jsonfile = ($3) WHERE mpid = $1  and segid = '|| new_segid+1 
     		USING   mpid, segid,jsonfiles;
        END IF;
    END IF;

    return f_mgeometry;
END;
$BODY$;

ALTER FUNCTION public.append(mpoint, point, timestamp without time zone)
    OWNER TO postgres;  
  

CREATE OR REPLACE FUNCTION public.append(
    mdouble,
    double precision, timestamp)
  RETURNS mdouble AS
$BODY$
DECLARE
    f_mgeometry            alias for $1;
    tp                alias for $2;
 	tp2                alias for $3;
    f_mgeometry_segtable_name    char(200);
    table_oid            char(100);
    mpid                integer;
    segid                integer;
    mp_count            integer;
    mp_seq                record;
    max_tpseg_count    integer;
    cnt_mpid            integer;   
    sql                text;
    next_segid            integer;
    tp_seg_size            integer;
    new_segid            integer;
	trajid			integer;
	jsonfiles			json;
BEGIN
	
    sql := 'select f_segtableoid  from mgeometry_columns where f_segtableoid = ' ||quote_literal(f_mgeometry.segid   );
	 RAISE INFO '%', sql;
	 EXECUTE sql INTO trajid;
	  sql := 'select f_mgeometry_segtable_name  from mgeometry_columns where f_segtableoid = ' ||quote_literal(trajid );
	 RAISE INFO '%', sql;
	  EXECUTE sql INTO f_mgeometry_segtable_name;
    mpid := f_mgeometry.id;

    sql := 'SELECT COUNT(*) FROM ' || quote_ident(f_mgeometry_segtable_name) || 
        ' WHERE mpid = ' || f_mgeometry.id;
    RAISE DEBUG '%', sql;
    EXECUTE sql INTO cnt_mpid;
    
    -- tpseg_size 
    sql := 'select tpseg_size from mgeometry_columns where f_mgeometry_segtable_name = ' || quote_literal(f_mgeometry_segtable_name);
    RAISE DEBUG '%', sql;
    EXECUTE sql INTO tp_seg_size;
		
    IF (cnt_mpid < 1) THEN
        EXECUTE 'INSERT INTO ' || quote_ident(f_mgeometry_segtable_name) || '(mpid, segid, mdcount, time, tpseg) 
            VALUES($1, 1, 1, ARRAY[($3)]::timestamp without time zone[], ARRAY[($2)]::double precision[])'
        USING mpid, tp, tp2;
		sql := 'select row_to_json((SELECT d from (select time,tpseg ) d)) from  ' || quote_ident(f_mgeometry_segtable_name) ;
   		RAISE DEBUG '%', sql;
    	EXECUTE sql INTO jsonfiles;
		
		EXECUTE 'update ' || quote_ident(f_mgeometry_segtable_name) || ' set jsonfile = ($4) WHERE mpid = $1'
       	USING  mpid, tp, tp2,jsonfiles;
    END IF;
    
    IF(cnt_mpid > 0) THEN
        sql := 'select segid from ' || quote_ident(f_mgeometry_segtable_name) ||
                ' where mpid = ' || f_mgeometry.id || ' and next_segid IS NULL';
        RAISE DEBUG '%', sql;
        EXECUTE sql INTO segid;
        sql := 'select array_upper((select tpseg from ' || quote_ident(f_mgeometry_segtable_name) || 
            ' where mpid = ' || f_mgeometry.id || ' and segid = ' || segid || '), 1)';
        RAISE DEBUG '%', sql;
        EXECUTE sql INTO max_tpseg_count;
       
        IF( segid IS NOT NULL AND max_tpseg_count < tp_seg_size) THEN
            EXECUTE 'UPDATE ' || quote_ident(f_mgeometry_segtable_name) || 
                ' set mdcount = mdcount+1, time = array_append(time, $2) , tpseg = array_append(tpseg, $3) 
                where mpid = $4 and segid = $5 '
            USING tp, tp2, tp, mpid, segid; 
			 sql := 'select row_to_json((SELECT d from (select time,tpseg ) d)) from  ' || quote_ident(f_mgeometry_segtable_name) ;
   		    RAISE DEBUG '%', sql;
			EXECUTE sql INTO jsonfiles;
			
    	    EXECUTE 'update ' || quote_ident(f_mgeometry_segtable_name) || ' set jsonfile = ($6) WHERE mpid = $4 and segid = $5'
            USING  tp, tp2, tp, mpid, segid,jsonfiles;
        ELSE 
            EXECUTE 'select MAX(segid) from ' || quote_ident(f_mgeometry_segtable_name) || ' where mpid = $1'
            INTO new_segid USING mpid;

            EXECUTE 'INSERT INTO ' || quote_ident(f_mgeometry_segtable_name) ||'(mpid, segid, before_segid, mdcount, time, tpseg) 
                VALUES( $1,  ' || new_segid+1 || ', $2, 1, ARRAY[( $3)]::timestamp without time zone[], ARRAY[( $4)]::double precision[])'
            USING f_mgeometry.id, segid, tp2, tp;
			
            EXECUTE 'UPDATE ' || quote_ident(f_mgeometry_segtable_name) || 
                ' set next_segid = ' || new_segid+1 || ' where mpid = $1 and segid = $2'
            USING mpid, segid;
			sql := 'select row_to_json((SELECT d from (select time,tpseg ) d)) from  ' || quote_ident(f_mgeometry_segtable_name) ;
   			RAISE DEBUG '%', sql;
    		EXECUTE sql INTO jsonfiles;
			
			EXECUTE 'update ' || quote_ident(f_mgeometry_segtable_name) || ' set jsonfile = ($3) WHERE mpid = $1  and segid = '|| new_segid+1 
     		USING   mpid, segid,jsonfiles;
        END IF;
    END IF;

    return f_mgeometry;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE STRICT
  COST 100;
ALTER FUNCTION public.append(mdouble, double precision, timestamp)
  OWNER TO postgres;
  
  
CREATE FUNCTION public.delete_mpoint()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$
DECLARE        
    delete_mpoint        mpoint;
    delete_id        integer;
    
    records            record;
    delete_record        record;
    
    BEGIN
    execute 'select f_mgeometry_segtable_name, f_mgeometry_column from mgeometry_columns where f_table_name = ' || quote_literal(TG_RELNAME)
    into records;

    delete_record := OLD;

    /*
    execute 'select ' || column_name || ' from ' || delete_record
    into delete_trajectory;
    */
    
    delete_mpoint := OLD.mpoint;
    delete_id := delete_mpoint.id;
    
    execute 'DELETE FROM ' || quote_ident(records.f_mgeometry_segtable_name) || ' WHERE mpid = ' || delete_id;

    return NULL;

    END;
$BODY$;

ALTER FUNCTION public.delete_mpoint()
    OWNER TO postgres;
  
  
  
CREATE FUNCTION public.delete_mdouble()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$
DECLARE        
    delete_mdouble        mdouble;
    delete_id        integer;
    
    records            record;
    delete_record        record;
    
    BEGIN
    execute 'select f_mgeometry_segtable_name, f_mgeometry_column from mgeometry_columns where f_table_name = ' || quote_literal(TG_RELNAME)
    into records;
	RAISE INFO '%', records;
	
    delete_record := OLD;
    delete_mdouble := OLD.mdouble;
    delete_id := delete_mdouble.id;
    
    execute 'DELETE FROM ' || quote_ident(records.f_mgeometry_segtable_name) || ' WHERE mpid = ' || delete_id
	into records;
	RAISE INFO '%', records;
    return NULL;

    END;
$BODY$;

ALTER FUNCTION public.delete_mdouble()
    OWNER TO postgres;
    
    
    
    
CREATE FUNCTION public.insert_mpoint()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$
DECLARE    
    segtable_oid        text;
    segcolumn_name        text;
    sequence_name        text;
    moid            text;
    
    sql_text        text;
    records            record;
    
 BEGIN    
    sql_text := 'select f_segtableoid, f_mgeometry_column, f_sequence_name from mgeometry_columns where f_table_name = ' || quote_literal(TG_RELNAME);
    execute sql_text into records;
    
    segtable_oid := records.f_segtableoid;
    segcolumn_name := records.f_mgeometry_column;
    sequence_name := records.f_sequence_name;

    sql_text := 'select nextval(' || quote_literal(sequence_name) || ')';
        
    execute sql_text into moid;

    
    RAISE NOTICE 'sql_text : %', sql_text;
    RAISE NOTICE 'segtable_oid : %', segtable_oid;
    RAISE NOTICE 'segcolumn_name : %', segcolumn_name;
    RAISE NOTICE 'sequence_name : %', sequence_name;
    RAISE NOTICE 'moid : %', moid;
    
    NEW.mpoint = (moid);    
        
    return NEW;
END
$BODY$;

ALTER FUNCTION public.insert_mpoint()
    OWNER TO postgres;
    
    
    
CREATE FUNCTION public.insert_mdouble()
    RETURNS trigger
    LANGUAGE 'plpgsql'
    COST 100
    VOLATILE NOT LEAKPROOF 
AS $BODY$
DECLARE    
    segtable_oid        text;
    segcolumn_name        text;
    sequence_name        text;
    moid            text;
    
    sql_text        text;
    records            record;
    
 BEGIN    
    sql_text := 'select f_segtableoid, f_mgeometry_column, f_sequence_name from mgeometry_columns where f_table_name = ' || quote_literal(TG_RELNAME);
    execute sql_text into records;
    
    segtable_oid := records.f_segtableoid;
    segcolumn_name := records.f_mgeometry_column;
    sequence_name := records.f_sequence_name;

    sql_text := 'select nextval(' || quote_literal(sequence_name) || ')';
        
    execute sql_text into moid;

    
    RAISE NOTICE 'sql_text : %', sql_text;
    RAISE NOTICE 'segtable_oid : %', segtable_oid;
    RAISE NOTICE 'segcolumn_name : %', segcolumn_name;
    RAISE NOTICE 'sequence_name : %', sequence_name;
    RAISE NOTICE 'moid : %', moid;
    
    NEW.mdouble = (moid);    
        
    return NEW;
END
$BODY$;

ALTER FUNCTION public.insert_mdouble()
    OWNER TO postgres;
    
    
    
    
    
  