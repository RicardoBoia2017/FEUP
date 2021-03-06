CREATE TABLE user (
  usr_id        INTEGER PRIMARY KEY AUTOINCREMENT,
  usr_username  VARCHAR NOT NULL UNIQUE,
  usr_password  VARCHAR NOT NULL,
  usr_name      VARCHAR,
  usr_email     VARCHAR UNIQUE,
  /*usr_birthdate VARCHAR,*/
  usr_gender    VARCHAR
);

CREATE TABLE todoList (
  tdl_id    INTEGER PRIMARY KEY,
  tdl_name  VARCHAR NOT NULL,
  tdl_cat   VARCHAR NOT NULL,
  usr_id    INTEGER REFERENCES user(usr_id)
);

CREATE TABLE task (
  tsk_id           INTEGER PRIMARY KEY AUTOINCREMENT,
  tsk_description  TEXT NOT NULL,
  tsk_datedue      INTEGER,
  tsk_status       INTEGER,
  tdl_id           INTEGER REFERENCES todolist(tdl_id)
);



/*INSERT INTO user VALUES (Null, 'afonso', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Afonso', 'afonso@gmail.com', '1997-01-12', 'Male');
INSERT INTO user VALUES (Null, 'tomas', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Tomas', 'tomas@gmail.com', '1997-04-15', 'Male');
INSERT INTO user VALUES (Null, 'claudia', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Claudia', 'claudia@gmail.com', '1997-12-19', 'Female');
INSERT INTO user VALUES (Null, 'guest', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Guest', 'guest@gmail.com', '1997-07-22', 'Male');*/
INSERT INTO user VALUES (Null, 'afonso', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Afonso', 'afonso@gmail.com', 'Male');
INSERT INTO user VALUES (Null, 'tomas', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Tomas', 'tomas@gmail.com', 'Male');
INSERT INTO user VALUES (Null, 'claudia', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Claudia', 'claudia@gmail.com', 'Female');
INSERT INTO user VALUES (Null, 'guest', '$2y$12$kwSuelerXAgWimHRpJBf0eLCEZhVtb/xKgUsES4yuepI.qZvAF5wG', 'Guest', 'guest@gmail.com', 'Male');

INSERT INTO todoList VALUES (NULL, 'CollegeTDL', 'College', 1);
INSERT INTO todoList VALUES (NULL, 'OthersTDL', 'Others', 1);
INSERT INTO todoList VALUES (NULL, 'WorkTDL', 'Others', 2);
INSERT INTO todoList VALUES (NULL, 'ComputersTDL', 'Others', 2);
INSERT INTO todoList VALUES (NULL, 'DanceTDL', 'College', 3);
INSERT INTO todoList VALUES (NULL, 'OthersTDL', 'Others', 3);


INSERT INTO task VALUES (NULL, 'Finish this website ', Null, 0, 1);
INSERT INTO task VALUES (NULL, 'Sleep ', Null, 1, 2);
INSERT INTO task VALUES (NULL, 'Finish CSS ', Null, 1, 3);
INSERT INTO task VALUES (NULL, 'Install Linux ', Null, 1, 4);
INSERT INTO task VALUES (NULL, 'Dance Class ', Null, 1, 5);
INSERT INTO task VALUES (NULL, 'Install Linux ', Null, 1, 6);

