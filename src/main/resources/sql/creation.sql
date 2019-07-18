create table patient (
    patient_id bigint primary key identity,
    first_name varchar(64) not null,
    last_name varchar(64) not null,
    middle_name varchar(64) not null,
    phone_number varchar(18) not null
);

create table doctor (
    doctor_id bigint primary key identity,
    first_name varchar(64) not null,
    last_name varchar(64) not null,
    middle_name varchar(64) not null,
    specialization varchar(256) not null
);

create table recipe (
    recipe_id bigint primary key identity,
    description varchar(256) not null,
    patient_id bigint not null references patient(patient_id),
    doctor_id bigint not null references doctor(doctor_id),
    creation_date date not null,
    validity date not null,
    priority varchar(8) not null
);
INSERT INTO "PUBLIC"."DOCTOR" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "SPECIALIZATION") VALUES ('Алексей', 'Анисимов', 'Артемович', 'Аллерголог');
INSERT INTO "PUBLIC"."DOCTOR" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "SPECIALIZATION") VALUES ('Иван', 'Иванов', 'Иванович', 'Иммунолог');
INSERT INTO "PUBLIC"."DOCTOR" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "SPECIALIZATION") VALUES ('Петр', 'Петров', 'Петрович', 'Психотерапевт');
INSERT INTO "PUBLIC"."DOCTOR" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "SPECIALIZATION") VALUES ('Георгий', 'Гуляев', 'Гавриилович', 'Генетик');

INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Бахрам', 'Боталов', 'Берекович', '+7(777)997-88-89');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Вадим', 'Волков', 'Валентинович', '+7(777)735-16-21');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Галина', 'Гонсалес', 'Геральдовна', '+7(777)287-58-33');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Дарена', 'Джугашвили', 'Дмитровна', '+7(777)818-60-59');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Екатерина', 'Егорова', 'Егоровна', '+7(777)384-59-02');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Жюли', 'Жорж', 'Женевьевна', '+7(777)408-83-98');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Ида', 'Исакова', 'Идовна', '+7(777)838-42-32');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Константин', 'Кабо', 'Кириллович', '+7(777)628-74-94');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Леопольд', 'Львов', 'Леонидович', '+7(777)873-75-07');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Максим', 'Мануев', 'Максудович', '+7(777)618-94-81');
INSERT INTO "PUBLIC"."PATIENT" ("FIRST_NAME", "LAST_NAME", "MIDDLE_NAME", "PHONE_NUMBER") VALUES ('Иван', 'Иванов', 'Иванович', '+7(777)777-77-77');


INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('аАКДС-Геп В+Hib', 0, 1, '2019-05-01', '2020-05-01', 'NORMAL');
INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('C07AG02 Карведилол', 1, 0, '2019-05-01', '2020-05-01', 'NORMAL');
INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('G01AD03 Аскорбиновая кислота', 1, 1, '2019-05-02', '2020-05-01', 'CITO');
INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('L01XC15 Обинутузумаб', 2, 0, '2019-05-03', '2020-05-01', 'STATIM');
INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('L01XC15 Обинутузумаб', 2, 1, '2019-05-04', '2120-05-01', 'NORMAL');
INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('J04AK02 Этамбутол', 2, 2, '2019-05-01', '2020-05-01', 'NORMAL');
INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('C07AG02 Карведилол', 3, 0, '2019-05-01', '2020-05-01', 'NORMAL');
INSERT INTO "PUBLIC"."RECIPE" ("DESCRIPTION", "PATIENT_ID", "DOCTOR_ID", "CREATION_DATE", "VALIDITY", "PRIORITY") VALUES ('C07AG02 Карведилол', 3, 1, '2019-05-01', '2020-05-01', 'NORMAL');

commit;