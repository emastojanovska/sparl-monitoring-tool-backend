
--
-- PostgreSQL database dump
--

-- Dumped from database version 13.0
-- Dumped by pg_dump version 13.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

ALTER TABLE IF EXISTS ONLY public.file DROP CONSTRAINT IF EXISTS fk8sjdrh1p8lt551a84qeafe0pi;
ALTER TABLE IF EXISTS ONLY public.query DROP CONSTRAINT IF EXISTS fk7jb1hqeffxk3r4satc96d9l71;
ALTER TABLE IF EXISTS ONLY public.void_dataset_statistics DROP CONSTRAINT IF EXISTS fk4k92470xkxroq2oshgf5wt6me;
ALTER TABLE IF EXISTS ONLY public.result_value DROP CONSTRAINT IF EXISTS fk136qlcgtrmuorbftytmep4hxl;
ALTER TABLE IF EXISTS ONLY public.void_dataset_statistics DROP CONSTRAINT IF EXISTS void_dataset_statistics_pkey;
ALTER TABLE IF EXISTS ONLY public.result_value DROP CONSTRAINT IF EXISTS result_value_pkey;
ALTER TABLE IF EXISTS ONLY public.query DROP CONSTRAINT IF EXISTS query_pkey;
ALTER TABLE IF EXISTS ONLY public.file DROP CONSTRAINT IF EXISTS file_pkey;
ALTER TABLE IF EXISTS ONLY public.endpoint DROP CONSTRAINT IF EXISTS endpoint_pkey;
ALTER TABLE IF EXISTS public.void_dataset_statistics ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.result_value ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.query ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.file ALTER COLUMN id DROP DEFAULT;
ALTER TABLE IF EXISTS public.endpoint ALTER COLUMN id DROP DEFAULT;
DROP SEQUENCE IF EXISTS public.void_dataset_statistics_id_seq;
DROP TABLE IF EXISTS public.void_dataset_statistics;
DROP SEQUENCE IF EXISTS public.result_value_id_seq;
DROP TABLE IF EXISTS public.result_value;
DROP SEQUENCE IF EXISTS public.query_id_seq;
DROP TABLE IF EXISTS public.query;
DROP SEQUENCE IF EXISTS public.file_id_seq;
DROP TABLE IF EXISTS public.file;
DROP SEQUENCE IF EXISTS public.endpoint_id_seq;
DROP TABLE IF EXISTS public.endpoint;
SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: endpoint; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.endpoint (
    id bigint NOT NULL,
    url character varying(255) NOT NULL,
    void boolean,
    available boolean,
    coherence double precision,
    down boolean,
    name character varying(255),
    new_version boolean,
    num_available integer,
    num_unavailable integer,
    relation_speciality double precision,
    server_name character varying(255),
    void_file boolean
);


ALTER TABLE public.endpoint OWNER TO postgres;

--
-- Name: endpoint_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.endpoint_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.endpoint_id_seq OWNER TO postgres;

--
-- Name: endpoint_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.endpoint_id_seq OWNED BY public.endpoint.id;


--
-- Name: file; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.file (
    id bigint NOT NULL,
    name character varying(255),
    mime character varying(255),
    path character varying(255) NOT NULL,
    endpoint_id bigint
);


ALTER TABLE public.file OWNER TO postgres;

--
-- Name: file_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.file_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.file_id_seq OWNER TO postgres;

--
-- Name: file_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.file_id_seq OWNED BY public.file.id;


--
-- Name: query; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.query (
    id bigint NOT NULL,
    name character varying(255),
    num_executed integer,
    response_time double precision,
    type character varying(255),
    endpoint_id bigint
);


ALTER TABLE public.query OWNER TO postgres;

--
-- Name: query_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.query_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.query_id_seq OWNER TO postgres;

--
-- Name: query_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.query_id_seq OWNED BY public.query.id;


--
-- Name: result_value; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.result_value (
    id bigint NOT NULL,
    datatype character varying(255),
    json_value character varying(255),
    key_name character varying(255),
    link character varying(255),
    name character varying(255),
    type character varying(255),
    value integer,
    void_dataset_statistics_id bigint
);


ALTER TABLE public.result_value OWNER TO postgres;

--
-- Name: result_value_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.result_value_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.result_value_id_seq OWNER TO postgres;

--
-- Name: result_value_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.result_value_id_seq OWNED BY public.result_value.id;


--
-- Name: void_dataset_statistics; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.void_dataset_statistics (
    id bigint NOT NULL,
    num_classes integer,
    num_entities integer,
    num_objects integer,
    num_predicates integer,
    num_properties integer,
    num_subjects integer,
    num_triples integer,
    ex_resource_1 character varying(255),
    ex_resource_2 character varying(255),
    ex_resource_3 character varying(255),
    endpoint_id bigint
);


ALTER TABLE public.void_dataset_statistics OWNER TO postgres;

--
-- Name: void_dataset_statistics_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.void_dataset_statistics_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.void_dataset_statistics_id_seq OWNER TO postgres;

--
-- Name: void_dataset_statistics_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.void_dataset_statistics_id_seq OWNED BY public.void_dataset_statistics.id;


--
-- Name: endpoint id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.endpoint ALTER COLUMN id SET DEFAULT nextval('public.endpoint_id_seq'::regclass);


--
-- Name: file id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.file ALTER COLUMN id SET DEFAULT nextval('public.file_id_seq'::regclass);


--
-- Name: query id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.query ALTER COLUMN id SET DEFAULT nextval('public.query_id_seq'::regclass);


--
-- Name: result_value id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.result_value ALTER COLUMN id SET DEFAULT nextval('public.result_value_id_seq'::regclass);


--
-- Name: void_dataset_statistics id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.void_dataset_statistics ALTER COLUMN id SET DEFAULT nextval('public.void_dataset_statistics_id_seq'::regclass);


--
-- Data for Name: endpoint; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.endpoint VALUES (2, 'https://dbpedia.org/sparql', false, false, 0.060167889354185045, false, 'DbPedia', false, 10, 0, 7080247.359709684, 'Virtuoso/08.03.3326 (Linux) x86_64-generic-linux-glibc25  VDB', true);
INSERT INTO public.endpoint VALUES (9, 'http://www.scholarlydata.org/sparql/', false, false, 0.4159452701309662, false, 'Scholary Data', false, 0, 0, 339.69779142617205, 'aruba-proxy', true);
INSERT INTO public.endpoint VALUES (3, 'http://data.allie.dbcls.jp/sparql', true, false, 0, false, 'Allie Abbreviation And Long Form Database in Life Science', false, 10, 0, 0, 'nginx/1.16.0', false);
INSERT INTO public.endpoint VALUES (1, 'http://linkeddata.finki.ukim.mk/sparql', false, false, 0.820226337358937, false, 'Drug Data from the Health Insurance Fund of Macedonia', false, 11, 0, 648.31, 'Virtuoso/07.20.3217 (Linux) x86_64-unknown-linux-gnu', true);
INSERT INTO public.endpoint VALUES (4, 'https://data.netwerkdigitaalerfgoed.nl/Rijksmuseum/collection/sparql/collection', false, false, 0, false, 'Rijeks Museum', false, 11, 0, 0, 'nginx', false);


--
-- Data for Name: file; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.file VALUES (1, 'finki-void.json', 'json', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\finki-void.json', 1);
INSERT INTO public.file VALUES (2, 'finki-void.ttl', 'ttl', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\finki-void.ttl', 1);
INSERT INTO public.file VALUES (3, 'finki-void.rdf', 'rdf', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\finki-void.rdf', 1);
INSERT INTO public.file VALUES (4, 'dbpedia-void.json', 'json', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\dbpedia-void.json', 2);
INSERT INTO public.file VALUES (5, 'dbpedia-void.ttl', 'ttl', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\dbpedia-void.ttl', 2);
INSERT INTO public.file VALUES (6, 'dbpedia-void.rdf', 'rdf', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\dbpedia-void.rdf', 2);
INSERT INTO public.file VALUES (27, 'scholary data-void.rdf', 'rdf', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\scholary data-void.rdf', 9);
INSERT INTO public.file VALUES (23, 'scholary data-void.json', 'json', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\scholary data-void.json', 9);
INSERT INTO public.file VALUES (25, 'scholary data-void.ttl', 'ttl', 'C:\Users\Dell\wbs\sparqlMonitoringTool\src\main\resources\static\files\scholary data-void.ttl', 9);


--
-- Data for Name: query; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.query VALUES (7, 'SELECT ?x ?y WHERE {
  VALUES ?x { 1 2 3 4 }
  {
    SELECT ?y WHERE { VALUES ?y { 5 6 7 8 }  }
  }
}', 173, 41, 'SUBQUERY', 2);
INSERT INTO public.query VALUES (8, 'ASK WHERE{ ?s ?p ?o . }', 173, 15, 'ASK', 2);
INSERT INTO public.query VALUES (9, 'PREFIX foaf: <http://xmlns.com/foaf/0.1/>
SELECT ?name
WHERE { ?x foaf:givenName  ?name .
	OPTIONAL { ?x foaf:knows ?who } .
	FILTER (!BOUND(?who)) 
} ', 173, 8107, 'NEGATION', 2);
INSERT INTO public.query VALUES (10, 'SELECT ?s WHERE{ ?s ?p ?o . } LIMIT 1', 173, 17, 'SELECT', 2);
INSERT INTO public.query VALUES (6, 'SELECT (COUNT(?person) AS ?people)
WHERE {
  ?person ?relation ?something .
} group by ?person', 173, 36, 'AGGREGATE', 2);
INSERT INTO public.query VALUES (12, 'SELECT ?x ?y WHERE {
  VALUES ?x { 1 2 3 4 }
  {
    SELECT ?y WHERE { VALUES ?y { 5 6 7 8 }  }
  }
}', 173, 236, 'SUBQUERY', 3);
INSERT INTO public.query VALUES (13, 'ASK WHERE{ ?s ?p ?o . }', 173, 24, 'ASK', 3);
INSERT INTO public.query VALUES (14, 'PREFIX foaf: <http://xmlns.com/foaf/0.1/>
SELECT ?name
WHERE { ?x foaf:givenName  ?name .
	OPTIONAL { ?x foaf:knows ?who } .
	FILTER (!BOUND(?who)) 
} ', 173, 77, 'NEGATION', 3);
INSERT INTO public.query VALUES (15, 'SELECT ?s WHERE{ ?s ?p ?o . } LIMIT 1', 173, 15, 'SELECT', 3);
INSERT INTO public.query VALUES (11, 'SELECT (COUNT(?person) AS ?people)
WHERE {
  ?person ?relation ?something .
} group by ?person', 173, 42, 'AGGREGATE', 3);
INSERT INTO public.query VALUES (5, 'SELECT ?s WHERE{ ?s ?p ?o . } LIMIT 1', 173, 56, 'SELECT', 1);
INSERT INTO public.query VALUES (1, 'SELECT (COUNT(?person) AS ?people)
WHERE {
  ?person ?relation ?something .
} group by ?person', 173, 82, 'AGGREGATE', 1);
INSERT INTO public.query VALUES (2, 'SELECT ?x ?y WHERE {
  VALUES ?x { 1 2 3 4 }
  {
    SELECT ?y WHERE { VALUES ?y { 5 6 7 8 }  }
  }
}', 173, 40, 'SUBQUERY', 1);
INSERT INTO public.query VALUES (3, 'ASK WHERE{ ?s ?p ?o . }', 173, 10, 'ASK', 1);
INSERT INTO public.query VALUES (4, 'PREFIX foaf: <http://xmlns.com/foaf/0.1/>
SELECT ?name
WHERE { ?x foaf:givenName  ?name .
	OPTIONAL { ?x foaf:knows ?who } .
	FILTER (!BOUND(?who)) 
} ', 173, 329, 'NEGATION', 1);
INSERT INTO public.query VALUES (41, 'SELECT (COUNT(?person) AS ?people)
WHERE {
  ?person ?relation ?something .
} group by ?person', 0, 0, 'AGGREGATE', 9);
INSERT INTO public.query VALUES (42, 'SELECT ?x ?y WHERE {
  VALUES ?x { 1 2 3 4 }
  {
    SELECT ?y WHERE { VALUES ?y { 5 6 7 8 }  }
  }
}', 0, 0, 'SUBQUERY', 9);
INSERT INTO public.query VALUES (43, 'ASK WHERE{ ?s ?p ?o . }', 0, 0, 'ASK', 9);
INSERT INTO public.query VALUES (44, 'PREFIX foaf: <http://xmlns.com/foaf/0.1/>
SELECT ?name
WHERE { ?x foaf:givenName  ?name .
	OPTIONAL { ?x foaf:knows ?who } .
	FILTER (!BOUND(?who)) 
} ', 0, 0, 'NEGATION', 9);
INSERT INTO public.query VALUES (45, 'SELECT ?s WHERE{ ?s ?p ?o . } LIMIT 1', 0, 0, 'SELECT', 9);


--
-- Data for Name: result_value; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.result_value VALUES (1, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "533007021" }', 'triples', 'http://vocab.deri.ie/void#triples', 'void:triples', 'literal', 533007021, 1);
INSERT INTO public.result_value VALUES (2, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "336" }', 'properties', 'http://vocab.deri.ie/void#properties', 'void:properties', 'literal', 336, 1);
INSERT INTO public.result_value VALUES (3, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer", "value": "4981"}', 'classes', 'http://vocab.deri.ie/void#classes', 'void:classes', 'literal', 4981, 1);
INSERT INTO public.result_value VALUES (4, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "12770395" }', 'objects', 'http://vocab.deri.ie/void#distinctObjects', 'void:distinctObjects', 'literal', 12770395, 1);
INSERT INTO public.result_value VALUES (5, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "494" }', 'entities', 'http://vocab.deri.ie/void#entities', 'void:entities', 'literal', 494, 1);
INSERT INTO public.result_value VALUES (6, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "11868297" }', 'subjects', 'http://vocab.deri.ie/void#distinctSubjects', 'void:distinctSubjects', 'literal', 11868297, 1);
INSERT INTO public.result_value VALUES (7, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "1141462841" }', 'triples', 'http://vocab.deri.ie/void#triples', 'void:triples', 'literal', 1141462841, 2);
INSERT INTO public.result_value VALUES (8, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "54685" }', 'properties', 'http://vocab.deri.ie/void#properties', 'void:properties', 'literal', 54685, 2);
INSERT INTO public.result_value VALUES (9, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "483639" }', 'classes', 'http://vocab.deri.ie/void#classes', 'void:classes', 'literal', 483639, 2);
INSERT INTO public.result_value VALUES (10, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "270960599 " }', 'objects', 'http://vocab.deri.ie/void#distinctObjects', 'void:distinctObjects', 'literal', 270960599, 2);
INSERT INTO public.result_value VALUES (11, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "41635675 " }', 'entities', 'http://vocab.deri.ie/void#entities', 'void:entities', 'literal', 41635675, 2);
INSERT INTO public.result_value VALUES (12, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "55220524 " }', 'subjects', 'http://vocab.deri.ie/void#distinctSubjects', 'void:distinctSubjects', 'literal', 55220524, 2);
INSERT INTO public.result_value VALUES (19, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "1282460" }', 'triples', 'http://vocab.deri.ie/void#triples', 'void:triples', 'literal', 1282460, 9);
INSERT INTO public.result_value VALUES (20, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "209" }', 'properties', 'http://vocab.deri.ie/void#properties', 'void:properties', 'literal', 209, 9);
INSERT INTO public.result_value VALUES (21, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer", "value": "94"}', 'classes', 'http://vocab.deri.ie/void#classes', 'void:classes', 'literal', 94, 9);
INSERT INTO public.result_value VALUES (22, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "173854" }', 'objects', 'http://vocab.deri.ie/void#distinctObjects', 'void:distinctObjects', 'literal', 173854, 9);
INSERT INTO public.result_value VALUES (23, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "82040" }', 'entities', 'http://vocab.deri.ie/void#entities', 'void:entities', 'literal', 82040, 9);
INSERT INTO public.result_value VALUES (24, 'http://www.w3.org/2001/XMLSchema#integer', '{ "type": "literal" , "datatype": "http://www.w3.org/2001/XMLSchema#integer" , "value": "89539" }', 'subjects', 'http://vocab.deri.ie/void#distinctSubjects', 'void:distinctSubjects', 'literal', 89539, 9);


--
-- Data for Name: void_dataset_statistics; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.void_dataset_statistics VALUES (1, 4981, 494, 12770395, NULL, 336, 11868297, 533007021, 'http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/drugs', 'http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/genericName', 'http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/brandName', 1);
INSERT INTO public.void_dataset_statistics VALUES (2, 483639, 41635675, 270960599, NULL, 54685, 55220524, 1141462841, 'http://dbpedia.org/resource/OpenLink_Software', 'http://dbpedia.org/resource/1932_Winter_Olympics_medal_table', 'http://dbpedia.org/resource/1932_Wisconsin_Badgers_football_team', 2);
INSERT INTO public.void_dataset_statistics VALUES (3, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 3);
INSERT INTO public.void_dataset_statistics VALUES (4, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, 4);
INSERT INTO public.void_dataset_statistics VALUES (9, 94, 82040, 173854, NULL, 209, 89539, 1282460, 'https://w3id.org/scholarlydata/authorlist/iswc-2019-resource-102', 'https://w3id.org/scholarlydata/authorlist/iswc-2019-resource-123', 'https://w3id.org/scholarlydata/authorlist/iswc-2019-resource-143', 9);


--
-- Name: endpoint_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.endpoint_id_seq', 9, true);


--
-- Name: file_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.file_id_seq', 27, true);


--
-- Name: query_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.query_id_seq', 45, true);


--
-- Name: result_value_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.result_value_id_seq', 24, true);


--
-- Name: void_dataset_statistics_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.void_dataset_statistics_id_seq', 9, true);


--
-- Name: endpoint endpoint_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.endpoint
    ADD CONSTRAINT endpoint_pkey PRIMARY KEY (id);


--
-- Name: file file_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.file
    ADD CONSTRAINT file_pkey PRIMARY KEY (id);


--
-- Name: query query_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.query
    ADD CONSTRAINT query_pkey PRIMARY KEY (id);


--
-- Name: result_value result_value_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.result_value
    ADD CONSTRAINT result_value_pkey PRIMARY KEY (id);


--
-- Name: void_dataset_statistics void_dataset_statistics_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.void_dataset_statistics
    ADD CONSTRAINT void_dataset_statistics_pkey PRIMARY KEY (id);


--
-- Name: result_value fk136qlcgtrmuorbftytmep4hxl; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.result_value
    ADD CONSTRAINT fk136qlcgtrmuorbftytmep4hxl FOREIGN KEY (void_dataset_statistics_id) REFERENCES public.void_dataset_statistics(id);


--
-- Name: void_dataset_statistics fk4k92470xkxroq2oshgf5wt6me; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.void_dataset_statistics
    ADD CONSTRAINT fk4k92470xkxroq2oshgf5wt6me FOREIGN KEY (endpoint_id) REFERENCES public.endpoint(id);


--
-- Name: query fk7jb1hqeffxk3r4satc96d9l71; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.query
    ADD CONSTRAINT fk7jb1hqeffxk3r4satc96d9l71 FOREIGN KEY (endpoint_id) REFERENCES public.endpoint(id);


--
-- Name: file fk8sjdrh1p8lt551a84qeafe0pi; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.file
    ADD CONSTRAINT fk8sjdrh1p8lt551a84qeafe0pi FOREIGN KEY (endpoint_id) REFERENCES public.endpoint(id);


--
-- PostgreSQL database dump complete
--

