CREATE TABLE guestbook (
  id integer PRIMARY KEY AUTOINCREMENT,
  name varchar(30),
  message varchar(200),
  timestamp timestamp DEFAULT CURRENT_TIMESTAMP
);
