-- add price column
alter table book add column price integer default -1;

update book set price = random() * 50 + 1;
