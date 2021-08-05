drop schema IF EXISTS stage CASCADE;

create schema stage;

SET SCHEMA 'stage';


CREATE TABLE device_type (
    id bigint NOT NULL,
    name character varying(255) NOT NULL UNIQUE,
	CONSTRAINT "device_type_PK" PRIMARY KEY (id)
);

CREATE TABLE device (
    id bigint NOT NULL,
    device_address character varying(255),
    device_name character varying(255) NOT NULL UNIQUE,
    device_type_id bigint,
    CONSTRAINT "device_PK" PRIMARY KEY (id),
    CONSTRAINT "device__device_type_FK" FOREIGN KEY (device_type_id) REFERENCES device_type(id)
);



CREATE TABLE connection (
    connection_id bigint NOT NULL,
    name character varying(255) NOT NULL,
    CONSTRAINT "connection_PK" PRIMARY KEY (connection_id)
);


CREATE TABLE interface (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    connection_id bigint NULL,
    device_id bigint,
    CONSTRAINT "interface_PK" PRIMARY KEY (id),
    CONSTRAINT "interface__device_FK" FOREIGN KEY (device_id) REFERENCES device(id),
    CONSTRAINT "interface__connection_FK" FOREIGN KEY (connection_id) REFERENCES device(id)
);

