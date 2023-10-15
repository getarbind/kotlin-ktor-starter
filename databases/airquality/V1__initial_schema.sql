create table airquality (
    id serial primary key,
    city varchar(255),
    date varchar(255),
    pm10_avg int,
    pm10_max int,
    pm10_min int
);

create table airqualityanalysis (
    id serial primary key,
    city varchar(255),
    avg_pm10_avg int,
    avg_pm10_max int,
    avg_pm10_min int,
    p90_pm10_avg int,
    p90_pm10_max int,
    p90_pm10_min int
);