BEGIN TRANSACTION;

create table myschema.PERSON (
    ID int not null,
    NAME varchar(100) not null
);

COMMIT;