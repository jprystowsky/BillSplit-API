-- Create schema script
create table billsplit.public.BillSetEntity (id bytea not null,
                                             name varchar(255) not null,
  primary key (id));

create table billsplit.public.BillSetUserEntity (id bytea not null,
                                                 billSet_id bytea not null,
                                                 user_id bytea not null,
  primary key (id));

create table billsplit.public.UserEntity (id bytea not null,
                                          email varchar(255) not null,
                                          firstName varchar(255) not null,
                                          googleId varchar(255) not null,
                                          lastName varchar(255) not null,
  primary key (id));

alter table billsplit.public.BillSetUserEntity add constraint FK_be76mi07peawnww597lheb0qy foreign key (billSet_id) references billsplit.public.BillSetEntity;

alter table billsplit.public.BillSetUserEntity add constraint FK_6bhq1tk4jk7nwe39kuqdc5m4h foreign key (user_id) references billsplit.public.UserEntity