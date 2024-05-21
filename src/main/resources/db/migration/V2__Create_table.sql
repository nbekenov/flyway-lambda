BEGIN TRANSACTION;

create table myschema.animal (
    ID int not null,
    NAME varchar(100) not null
);

COMMIT;