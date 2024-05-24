CREATE SCHEMA LightningDB;
SET SEARCH_PATH TO LightningDB;

CREATE TABLE day (
    id SERIAL UNIQUE NOT NULL,
    date DATE NOT NULL,
    total_lightnings INTEGER
);

CREATE TABLE type (
    id  SERIAL UNIQUE NOT NULL,
    type_name VARCHAR UNIQUE NOT NULL
);

CREATE TABLE lightning (
    id SERIAL UNIQUE NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    day_id INTEGER NOT NULL,
    type_id INTEGER NOT NULL
);

INSERT INTO type (type_name) VALUES ('cloud_to_ground_negative'),('cloud_to_ground_positive'),('cloud_to_cloud');