# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table bet (
  bet_id                    integer,
  winner                    varchar(255),
  wager                     integer,
  challenge_id              integer)
;

create table challenge (
  challenge_id              integer,
  challenger_username       varchar(255),
  challenged_username       varchar(255),
  wager                     integer,
  odds                      integer,
  location                  varchar(255),
  time                      timestamp,
  subject                   varchar(255),
  winner                    varchar(255))
;

create table user (
  username                  varchar(255),
  name                      varchar(255),
  points                    integer,
  wins                      integer,
  losses                    integer,
  level                     integer)
;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists bet;

drop table if exists challenge;

drop table if exists user;

SET REFERENTIAL_INTEGRITY TRUE;

