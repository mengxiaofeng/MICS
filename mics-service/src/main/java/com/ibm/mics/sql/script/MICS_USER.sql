CREATE TABLE MICS_USER (
    user_id character varying(30) NOT NULL,
    user_name character varying(64),
    org_id character varying(20),
    depart_id character varying(20),
    role_id character varying(20),
    password character varying(50),
    id character varying(20),
    email character varying(30),
    phone_no character varying(20),
    create_time timestamp without time zone DEFAULT now(),
    lock character varying(5)
);

--CREATE TABLE MICS_ROLE (
--    id character varying(20) NOT NULL,
--    name character varying(20),
--    perm character varying(200)
--);
--
--CREATE TABLE MICS_USER_ROLE (
--    user_id character varying(30) NOT NULL,
--    role_id character varying(20)
--);
--
--CREATE TABLE MICS_ORG (
--    id character varying(20) NOT NULL,
--    name character varying(20),
--    fullname character varying(100)
--);
--
--CREATE TABLE MICS_PUB_INFO (
--    type_id character varying(10) NOT NULL,
--    type_desc character varying(10),
--    id character varying(20),
--    name character varying(20),
--    comment character varying(20)
--);
--
--CREATE TABLE MICS_USER_LOCK (
--    user_id character varying(30) NOT NULL,
--    count integer
--);