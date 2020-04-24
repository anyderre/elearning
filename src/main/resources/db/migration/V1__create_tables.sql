--
-- PostgreSQL database dump
--

-- Dumped from database version 10.6
-- Dumped by pg_dump version 10.6

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner:
--

--CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner:
--

--COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: category; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.category (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    description TEXT NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.category OWNER TO cabuser;

--
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.category_id_seq OWNER TO cabuser;

--
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- Name: course; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.course (
    id bigint NOT NULL,
    author character varying(255) NOT NULL,
    creation_date timestamp without time zone,
    deleted boolean NOT NULL,
    description TEXT,
    end_date timestamp without time zone,
    enrolled integer NOT NULL,
    image_url TEXT,
    is_premium boolean NOT NULL,
    last_update timestamp without time zone,
    price double precision NOT NULL,
    private_only boolean NOT NULL,
    ratings real NOT NULL,
    start_date timestamp without time zone,
    title character varying(255) NOT NULL,
    validated boolean NOT NULL,
    category_id bigint NOT NULL,
    section_id bigint NOT NULL,
    sub_category_id bigint NOT NULL,
    sub_section_id bigint NOT NULL,
    user_id bigint NOT NULL,
    CONSTRAINT course_price_check CHECK ((price >= (0)::double precision)),
    CONSTRAINT course_ratings_check CHECK (((ratings >= (0)::double precision) AND (ratings <= (5)::double precision)))
);


ALTER TABLE public.course OWNER TO cabuser;

--
-- Name: course_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.course_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.course_id_seq OWNER TO cabuser;

--
-- Name: course_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.course_id_seq OWNED BY public.course.id;


--
-- Name: course_objectives; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.course_objectives (
    course_id bigint NOT NULL,
    objective_id bigint NOT NULL
);


ALTER TABLE public.course_objectives OWNER TO cabuser;

--
-- Name: course_schools; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.course_schools (
    course_id bigint NOT NULL,
    user_id bigint NOT NULL
);


ALTER TABLE public.course_schools OWNER TO cabuser;

--
-- Name: course_syllabus; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.course_syllabus (
    course_id bigint NOT NULL,
    syllabus_id bigint NOT NULL
);


ALTER TABLE public.course_syllabus OWNER TO cabuser;

--
-- Name: feature; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.feature (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    icon character varying(255) NOT NULL,
    title character varying(255) NOT NULL
);


ALTER TABLE public.feature OWNER TO cabuser;

--
-- Name: feature_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.feature_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.feature_id_seq OWNER TO cabuser;

--
-- Name: feature_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.feature_id_seq OWNED BY public.feature.id;


--
-- Name: history_of_learning; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.history_of_learning (
    id bigint NOT NULL,
    time_stamp timestamp without time zone,
    video_id bigint NOT NULL
);


ALTER TABLE public.history_of_learning OWNER TO cabuser;

--
-- Name: history_of_learning_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.history_of_learning_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.history_of_learning_id_seq OWNER TO cabuser;

--
-- Name: history_of_learning_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.history_of_learning_id_seq OWNED BY public.history_of_learning.id;


--
-- Name: languages; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.languages (
    id bigint NOT NULL,
    ln_content character varying(255),
    ln_key character varying(255),
    locale character varying(255)
);


ALTER TABLE public.languages OWNER TO cabuser;

--
-- Name: languages_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.languages_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.languages_id_seq OWNER TO cabuser;

--
-- Name: languages_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.languages_id_seq OWNED BY public.languages.id;


--
-- Name: objective; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.objective (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    description TEXT NOT NULL
);


ALTER TABLE public.objective OWNER TO cabuser;

--
-- Name: objective_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.objective_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.objective_id_seq OWNER TO cabuser;

--
-- Name: objective_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.objective_id_seq OWNED BY public.objective.id;


--
-- Name: overview; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.overview (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    course_id bigint NOT NULL
);


ALTER TABLE public.overview OWNER TO cabuser;

--
-- Name: overview_features; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.overview_features (
    overview_id bigint NOT NULL,
    feature_id bigint NOT NULL
);


ALTER TABLE public.overview_features OWNER TO cabuser;

--
-- Name: overview_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.overview_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.overview_id_seq OWNER TO cabuser;

--
-- Name: overview_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.overview_id_seq OWNED BY public.overview.id;


--
-- Name: overview_requirements; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.overview_requirements (
    overview_id bigint NOT NULL,
    requirement_id bigint NOT NULL
);


ALTER TABLE public.overview_requirements OWNER TO cabuser;

--
-- Name: requirement; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.requirement (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    description TEXT NOT NULL
);


ALTER TABLE public.requirement OWNER TO cabuser;

--
-- Name: requirement_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.requirement_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.requirement_id_seq OWNER TO cabuser;

--
-- Name: requirement_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.requirement_id_seq OWNED BY public.requirement.id;


--
-- Name: rol; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.rol (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    description TEXT NOT NULL,
    enabled boolean NOT NULL,
    generated boolean NOT NULL,
    name character varying(255) NOT NULL
);


ALTER TABLE public.rol OWNER TO cabuser;

--
-- Name: rol_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.rol_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.rol_id_seq OWNER TO cabuser;

--
-- Name: rol_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.rol_id_seq OWNED BY public.rol.id;


--
-- Name: section; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.section (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    description TEXT,
    name character varying(255) NOT NULL
);


ALTER TABLE public.section OWNER TO cabuser;

--
-- Name: section_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.section_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.section_id_seq OWNER TO cabuser;

--
-- Name: section_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.section_id_seq OWNED BY public.section.id;


--
-- Name: sub_category; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.sub_category (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    description TEXT,
    name character varying(255) NOT NULL,
    category_id bigint
);


ALTER TABLE public.sub_category OWNER TO cabuser;

--
-- Name: sub_category_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.sub_category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sub_category_id_seq OWNER TO cabuser;

--
-- Name: sub_category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.sub_category_id_seq OWNED BY public.sub_category.id;


--
-- Name: sub_category_sub_categories; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.sub_category_sub_categories (
    sub_category_id bigint NOT NULL,
    sub_categories_id bigint NOT NULL
);


ALTER TABLE public.sub_category_sub_categories OWNER TO cabuser;

--
-- Name: sub_section; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.sub_section (
    id bigint NOT NULL,
    deleted boolean NOT NULL,
    description TEXT,
    name character varying(255) NOT NULL,
    section_id bigint
);


ALTER TABLE public.sub_section OWNER TO cabuser;

--
-- Name: sub_section_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.sub_section_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.sub_section_id_seq OWNER TO cabuser;

--
-- Name: sub_section_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.sub_section_id_seq OWNED BY public.sub_section.id;


--
-- Name: syllabus; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.syllabus (
    id bigint NOT NULL,
    chapter_title character varying(255),
    deleted boolean NOT NULL
);


ALTER TABLE public.syllabus OWNER TO cabuser;

--
-- Name: syllabus_chapter_tuts; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.syllabus_chapter_tuts (
    syllabus_id bigint NOT NULL,
    video_id bigint NOT NULL
);


ALTER TABLE public.syllabus_chapter_tuts OWNER TO cabuser;

--
-- Name: syllabus_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.syllabus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.syllabus_id_seq OWNER TO cabuser;

--
-- Name: syllabus_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.syllabus_id_seq OWNED BY public.syllabus.id;


--
-- Name: user; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public."user" (
    id bigint NOT NULL,
    agree_with_terms boolean NOT NULL,
    bio character varying(255) NOT NULL,
    country character varying(255) NOT NULL,
    deleted boolean NOT NULL,
    email character varying(255) NOT NULL,
    enabled integer DEFAULT 1 NOT NULL,
    first_name character varying(255) NOT NULL,
    last_name character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    password character varying(60) NOT NULL,
    photourl TEXT NOT NULL,
    username character varying(30) NOT NULL,
    workspace_name character varying(255) NOT NULL,
    rol_id bigint NOT NULL
);


ALTER TABLE public."user" OWNER TO cabuser;

--
-- Name: user_categories; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.user_categories (
    user_id bigint NOT NULL,
    category_id bigint NOT NULL
);


ALTER TABLE public.user_categories OWNER TO cabuser;

--
-- Name: user_courses; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.user_courses (
    user_id bigint NOT NULL,
    course_id bigint NOT NULL
);


ALTER TABLE public.user_courses OWNER TO cabuser;

--
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO cabuser;

--
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.user_id_seq OWNED BY public."user".id;


--
-- Name: user_organizations; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.user_organizations (
    user_id bigint NOT NULL,
    organizations_id bigint NOT NULL
);


ALTER TABLE public.user_organizations OWNER TO cabuser;

--
-- Name: user_schools; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.user_schools (
    user_id bigint NOT NULL,
    schools_id bigint NOT NULL
);


ALTER TABLE public.user_schools OWNER TO cabuser;

--
-- Name: user_sub_categories; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.user_sub_categories (
    user_id bigint NOT NULL,
    sub_category_id bigint NOT NULL
);


ALTER TABLE public.user_sub_categories OWNER TO cabuser;

--
-- Name: video; Type: TABLE; Schema: public; Owner: cabuser
--

CREATE TABLE public.video (
    id bigint NOT NULL,
    attachment character varying(255),
    deleted boolean NOT NULL,
    video_title character varying(255),
    videourl TEXT
);


ALTER TABLE public.video OWNER TO cabuser;

--
-- Name: video_id_seq; Type: SEQUENCE; Schema: public; Owner: cabuser
--

CREATE SEQUENCE public.video_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.video_id_seq OWNER TO cabuser;

--
-- Name: video_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: cabuser
--

ALTER SEQUENCE public.video_id_seq OWNED BY public.video.id;


--
-- Name: category id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- Name: course id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course ALTER COLUMN id SET DEFAULT nextval('public.course_id_seq'::regclass);


--
-- Name: feature id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.feature ALTER COLUMN id SET DEFAULT nextval('public.feature_id_seq'::regclass);


--
-- Name: history_of_learning id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.history_of_learning ALTER COLUMN id SET DEFAULT nextval('public.history_of_learning_id_seq'::regclass);


--
-- Name: languages id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.languages ALTER COLUMN id SET DEFAULT nextval('public.languages_id_seq'::regclass);


--
-- Name: objective id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.objective ALTER COLUMN id SET DEFAULT nextval('public.objective_id_seq'::regclass);


--
-- Name: overview id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview ALTER COLUMN id SET DEFAULT nextval('public.overview_id_seq'::regclass);


--
-- Name: requirement id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.requirement ALTER COLUMN id SET DEFAULT nextval('public.requirement_id_seq'::regclass);


--
-- Name: rol id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.rol ALTER COLUMN id SET DEFAULT nextval('public.rol_id_seq'::regclass);


--
-- Name: section id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.section ALTER COLUMN id SET DEFAULT nextval('public.section_id_seq'::regclass);


--
-- Name: sub_category id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_category ALTER COLUMN id SET DEFAULT nextval('public.sub_category_id_seq'::regclass);


--
-- Name: sub_section id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_section ALTER COLUMN id SET DEFAULT nextval('public.sub_section_id_seq'::regclass);


--
-- Name: syllabus id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.syllabus ALTER COLUMN id SET DEFAULT nextval('public.syllabus_id_seq'::regclass);


--
-- Name: user id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public."user" ALTER COLUMN id SET DEFAULT nextval('public.user_id_seq'::regclass);


--
-- Name: video id; Type: DEFAULT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.video ALTER COLUMN id SET DEFAULT nextval('public.video_id_seq'::regclass);


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.category (id, deleted, description, name) FROM stdin;
\.


--
-- Data for Name: course; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.course (id, author, creation_date, deleted, description, end_date, enrolled, image_url, is_premium, last_update, price, private_only, ratings, start_date, title, validated, category_id, section_id, sub_category_id, sub_section_id, user_id) FROM stdin;
\.


--
-- Data for Name: course_objectives; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.course_objectives (course_id, objective_id) FROM stdin;
\.


--
-- Data for Name: course_schools; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.course_schools (course_id, user_id) FROM stdin;
\.


--
-- Data for Name: course_syllabus; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.course_syllabus (course_id, syllabus_id) FROM stdin;
\.


--
-- Data for Name: feature; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.feature (id, deleted, icon, title) FROM stdin;
\.


--
-- Data for Name: history_of_learning; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.history_of_learning (id, time_stamp, video_id) FROM stdin;
\.


--
-- Data for Name: languages; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.languages (id, ln_content, ln_key, locale) FROM stdin;
\.


--
-- Data for Name: objective; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.objective (id, deleted, description) FROM stdin;
\.


--
-- Data for Name: overview; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.overview (id, deleted, course_id) FROM stdin;
\.


--
-- Data for Name: overview_features; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.overview_features (overview_id, feature_id) FROM stdin;
\.


--
-- Data for Name: overview_requirements; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.overview_requirements (overview_id, requirement_id) FROM stdin;
\.


--
-- Data for Name: requirement; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.requirement (id, deleted, description) FROM stdin;
\.


--
-- Data for Name: rol; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.rol (id, deleted, description, enabled, generated, name) FROM stdin;
1	f	ROLE_ADMIN	t	t	Admin
2	f	ROLE_SCHOOL	t	t	School
3	f	ROLE_PROFESSOR	t	t	Professor
4	f	ROLE_STUDENT	t	t	Student
5	f	ROLE_FREELANCER	t	t	Freelancer
6	f	ROLE_FREE_STUDENT	t	t	Free Student
7	f	ROLE_ORGANIZATION	t	t	Organization
8	f	ROLE_EMPLOYEE	t	t	Employee
9	f	ROLE_SUPER_ADMIN	t	t	Super Admin
10	f	ROLE_INSTRUCTOR	t	t	Instructor
\.


--
-- Data for Name: section; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.section (id, deleted, description, name) FROM stdin;
\.


--
-- Data for Name: sub_category; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.sub_category (id, deleted, description, name, category_id) FROM stdin;
\.


--
-- Data for Name: sub_category_sub_categories; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.sub_category_sub_categories (sub_category_id, sub_categories_id) FROM stdin;
\.


--
-- Data for Name: sub_section; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.sub_section (id, deleted, description, name, section_id) FROM stdin;
\.


--
-- Data for Name: syllabus; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.syllabus (id, chapter_title, deleted) FROM stdin;
\.


--
-- Data for Name: syllabus_chapter_tuts; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.syllabus_chapter_tuts (syllabus_id, video_id) FROM stdin;
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public."user" (id, agree_with_terms, bio, country, deleted, email, enabled, first_name, last_name, name, password, photourl, username, workspace_name, rol_id) FROM stdin;
1	t		Haiti	f	marcridore@gmail.com	1	Joseph Marc-Antoine	Ridore	Joseph Marc-Antoine Ridore	$2a$10$f2LFrzndBlPPTbxg7EvkQeDIYgj5QHGnD//uMz2tFQxJVF61Sg1k2		admin		1
\.


--
-- Data for Name: user_categories; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.user_categories (user_id, category_id) FROM stdin;
\.


--
-- Data for Name: user_courses; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.user_courses (user_id, course_id) FROM stdin;
\.


--
-- Data for Name: user_organizations; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.user_organizations (user_id, organizations_id) FROM stdin;
\.


--
-- Data for Name: user_schools; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.user_schools (user_id, schools_id) FROM stdin;
\.


--
-- Data for Name: user_sub_categories; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.user_sub_categories (user_id, sub_category_id) FROM stdin;
\.


--
-- Data for Name: video; Type: TABLE DATA; Schema: public; Owner: cabuser
--

COPY public.video (id, attachment, deleted, video_title, videourl) FROM stdin;
\.


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.category_id_seq', 1, false);


--
-- Name: course_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.course_id_seq', 1, false);


--
-- Name: feature_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.feature_id_seq', 1, false);


--
-- Name: history_of_learning_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.history_of_learning_id_seq', 1, false);


--
-- Name: languages_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.languages_id_seq', 1, false);


--
-- Name: objective_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.objective_id_seq', 1, false);


--
-- Name: overview_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.overview_id_seq', 1, false);


--
-- Name: requirement_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.requirement_id_seq', 1, false);


--
-- Name: rol_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.rol_id_seq', 10, true);


--
-- Name: section_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.section_id_seq', 1, false);


--
-- Name: sub_category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.sub_category_id_seq', 1, false);


--
-- Name: sub_section_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.sub_section_id_seq', 1, false);


--
-- Name: syllabus_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.syllabus_id_seq', 1, false);


--
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.user_id_seq', 1, true);


--
-- Name: video_id_seq; Type: SEQUENCE SET; Schema: public; Owner: cabuser
--

SELECT pg_catalog.setval('public.video_id_seq', 1, false);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: course course_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT course_pkey PRIMARY KEY (id);


--
-- Name: feature feature_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.feature
    ADD CONSTRAINT feature_pkey PRIMARY KEY (id);


--
-- Name: history_of_learning history_of_learning_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.history_of_learning
    ADD CONSTRAINT history_of_learning_pkey PRIMARY KEY (id);


--
-- Name: languages languages_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.languages
    ADD CONSTRAINT languages_pkey PRIMARY KEY (id);


--
-- Name: objective objective_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.objective
    ADD CONSTRAINT objective_pkey PRIMARY KEY (id);


--
-- Name: overview overview_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview
    ADD CONSTRAINT overview_pkey PRIMARY KEY (id);


--
-- Name: requirement requirement_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.requirement
    ADD CONSTRAINT requirement_pkey PRIMARY KEY (id);


--
-- Name: rol rol_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.rol
    ADD CONSTRAINT rol_pkey PRIMARY KEY (id);


--
-- Name: section section_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.section
    ADD CONSTRAINT section_pkey PRIMARY KEY (id);


--
-- Name: sub_category sub_category_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_category
    ADD CONSTRAINT sub_category_pkey PRIMARY KEY (id);


--
-- Name: sub_section sub_section_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_section
    ADD CONSTRAINT sub_section_pkey PRIMARY KEY (id);


--
-- Name: syllabus syllabus_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.syllabus
    ADD CONSTRAINT syllabus_pkey PRIMARY KEY (id);


--
-- Name: overview uk_1w8xsn9cfyb0utev412migig7; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview
    ADD CONSTRAINT uk_1w8xsn9cfyb0utev412migig7 UNIQUE (course_id);


--
-- Name: user uk_ob8kqyqqgmefl0aco34akdtpe; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT uk_ob8kqyqqgmefl0aco34akdtpe UNIQUE (email);


--
-- Name: user uksb8bbouer5wak8vyiiy4pf2bx; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT uksb8bbouer5wak8vyiiy4pf2bx UNIQUE (username);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (id);


--
-- Name: video video_pkey; Type: CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.video
    ADD CONSTRAINT video_pkey PRIMARY KEY (id);


--
-- Name: overview_features fk1kgtq3fy57vnbc3g8i59slh40; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview_features
    ADD CONSTRAINT fk1kgtq3fy57vnbc3g8i59slh40 FOREIGN KEY (overview_id) REFERENCES public.overview(id);


--
-- Name: course_objectives fk2e8bew6ogm1d6mui12433wc1a; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course_objectives
    ADD CONSTRAINT fk2e8bew6ogm1d6mui12433wc1a FOREIGN KEY (objective_id) REFERENCES public.objective(id);


--
-- Name: user_schools fk2gd1yegr29y5ys1oqfop74q5f; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_schools
    ADD CONSTRAINT fk2gd1yegr29y5ys1oqfop74q5f FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: sub_category_sub_categories fk3j3vsf412nkmj0rn9tfa9qsdp; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_category_sub_categories
    ADD CONSTRAINT fk3j3vsf412nkmj0rn9tfa9qsdp FOREIGN KEY (sub_category_id) REFERENCES public.sub_category(id);


--
-- Name: user_courses fk4leaja1jtelxjs2iqqk4yuxna; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_courses
    ADD CONSTRAINT fk4leaja1jtelxjs2iqqk4yuxna FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: overview_requirements fk6npwrl8hab2gvx0trhs4t3ns; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview_requirements
    ADD CONSTRAINT fk6npwrl8hab2gvx0trhs4t3ns FOREIGN KEY (overview_id) REFERENCES public.overview(id);


--
-- Name: sub_section fk6qmlkrayugejciehhvnyoohut; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_section
    ADD CONSTRAINT fk6qmlkrayugejciehhvnyoohut FOREIGN KEY (section_id) REFERENCES public.section(id);


--
-- Name: syllabus_chapter_tuts fk8byodg7fucjxyfo6cert8sb51; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.syllabus_chapter_tuts
    ADD CONSTRAINT fk8byodg7fucjxyfo6cert8sb51 FOREIGN KEY (syllabus_id) REFERENCES public.syllabus(id);


--
-- Name: overview_features fk8j81q8370j8birvuf6ps45rkt; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview_features
    ADD CONSTRAINT fk8j81q8370j8birvuf6ps45rkt FOREIGN KEY (feature_id) REFERENCES public.feature(id);


--
-- Name: user_sub_categories fkfau7as5578u3omu5xoydbt7ab; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_sub_categories
    ADD CONSTRAINT fkfau7as5578u3omu5xoydbt7ab FOREIGN KEY (sub_category_id) REFERENCES public.sub_category(id);


--
-- Name: course_schools fkfutsmewgpmraospac39sihxad; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course_schools
    ADD CONSTRAINT fkfutsmewgpmraospac39sihxad FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: user_organizations fkfycv8cdobu9ncohkox8km54i; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_organizations
    ADD CONSTRAINT fkfycv8cdobu9ncohkox8km54i FOREIGN KEY (organizations_id) REFERENCES public."user"(id);


--
-- Name: syllabus_chapter_tuts fkg6rar2nmimphue3l18ed5iidb; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.syllabus_chapter_tuts
    ADD CONSTRAINT fkg6rar2nmimphue3l18ed5iidb FOREIGN KEY (video_id) REFERENCES public.video(id);


--
-- Name: course_syllabus fkg77me3qjdp6dnssmc4c9w91mj; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course_syllabus
    ADD CONSTRAINT fkg77me3qjdp6dnssmc4c9w91mj FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: sub_category_sub_categories fkh8iiyg2fnf7jap40gcuwjvwll; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_category_sub_categories
    ADD CONSTRAINT fkh8iiyg2fnf7jap40gcuwjvwll FOREIGN KEY (sub_categories_id) REFERENCES public.sub_category(id);


--
-- Name: overview_requirements fkhadqmf2r66o0sndxg495lv8km; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview_requirements
    ADD CONSTRAINT fkhadqmf2r66o0sndxg495lv8km FOREIGN KEY (requirement_id) REFERENCES public.requirement(id);


--
-- Name: course fkhmocwqy091btf5lmvi7n37p2j; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkhmocwqy091btf5lmvi7n37p2j FOREIGN KEY (section_id) REFERENCES public.section(id);


--
-- Name: user_categories fkjkgs8j660t63yccvvyus2opmf; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_categories
    ADD CONSTRAINT fkjkgs8j660t63yccvvyus2opmf FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: course fkkyes7515s3ypoovxrput029bh; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fkkyes7515s3ypoovxrput029bh FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: sub_category fkl65dyy5me2ypoyj8ou1hnt64e; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.sub_category
    ADD CONSTRAINT fkl65dyy5me2ypoyj8ou1hnt64e FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: course fklac455o2f9ppjo01l7w52s4cl; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fklac455o2f9ppjo01l7w52s4cl FOREIGN KEY (sub_category_id) REFERENCES public.sub_category(id);


--
-- Name: user_schools fklirbi0lqqgmmnpikdqqbb2xyu; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_schools
    ADD CONSTRAINT fklirbi0lqqgmmnpikdqqbb2xyu FOREIGN KEY (schools_id) REFERENCES public."user"(id);


--
-- Name: user_courses fklqgnqmcg0mn2ci98xag6vxpdq; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_courses
    ADD CONSTRAINT fklqgnqmcg0mn2ci98xag6vxpdq FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: course_objectives fklw2btj3108um61v6ex5h0rvhe; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course_objectives
    ADD CONSTRAINT fklw2btj3108um61v6ex5h0rvhe FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: course fko3767wbj6ow5axv38qej0gxo9; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fko3767wbj6ow5axv38qej0gxo9 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: user_organizations fkohdmhliemkiu03y43tel8fy17; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_organizations
    ADD CONSTRAINT fkohdmhliemkiu03y43tel8fy17 FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: user fkpikdt34c2nqt413csrypwhe69; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public."user"
    ADD CONSTRAINT fkpikdt34c2nqt413csrypwhe69 FOREIGN KEY (rol_id) REFERENCES public.rol(id);


--
-- Name: overview fkpv3sfgvhrvoun46hkr407ongd; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.overview
    ADD CONSTRAINT fkpv3sfgvhrvoun46hkr407ongd FOREIGN KEY (course_id) REFERENCES public.course(id);


--
-- Name: user_categories fkqhdol0ia96a31f8ir2g928ems; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_categories
    ADD CONSTRAINT fkqhdol0ia96a31f8ir2g928ems FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: history_of_learning fkrc7n1oqb0uog0k1ilanm3r7xb; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.history_of_learning
    ADD CONSTRAINT fkrc7n1oqb0uog0k1ilanm3r7xb FOREIGN KEY (video_id) REFERENCES public.video(id);


--
-- Name: course fks0150nmr4edwkiai3db6pouef; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course
    ADD CONSTRAINT fks0150nmr4edwkiai3db6pouef FOREIGN KEY (sub_section_id) REFERENCES public.sub_section(id);


--
-- Name: course_syllabus fksdv12alfl53q3lcp56m0ythah; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course_syllabus
    ADD CONSTRAINT fksdv12alfl53q3lcp56m0ythah FOREIGN KEY (syllabus_id) REFERENCES public.syllabus(id);


--
-- Name: user_sub_categories fktiaxy1nspah4ho3ee4hrux5na; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.user_sub_categories
    ADD CONSTRAINT fktiaxy1nspah4ho3ee4hrux5na FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- Name: course_schools fktnyqv8a0fgy5xcvaws40f2mgi; Type: FK CONSTRAINT; Schema: public; Owner: cabuser
--

ALTER TABLE ONLY public.course_schools
    ADD CONSTRAINT fktnyqv8a0fgy5xcvaws40f2mgi FOREIGN KEY (user_id) REFERENCES public."user"(id);


--
-- PostgreSQL database dump complete
--

