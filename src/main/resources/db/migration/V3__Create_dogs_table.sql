BEGIN TRANSACTION;

create table myschema.dog (
    ID int not null,
    NAME varchar(100) not null
);

COMMIT;