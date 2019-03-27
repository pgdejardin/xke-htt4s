create table author
(
  identifier uuid primary key,
  firstName  varchar not null,
  lastName   varchar not null
);

create table book
(
  isbn        uuid primary key,
  title       varchar not null,
  description varchar not null,
  author_id   uuid references author (identifier)
);
