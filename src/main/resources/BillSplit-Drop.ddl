-- Drop schema script
alter table billsplit.public.BillSetUserEntity drop constraint FK_be76mi07peawnww597lheb0qy;
alter table billsplit.public.BillSetUserEntity drop constraint FK_6bhq1tk4jk7nwe39kuqdc5m4h;
drop table if exists billsplit.public.BillSetEntity cascade;
drop table if exists billsplit.public.BillSetUserEntity cascade;
drop table if exists billsplit.public.UserEntity cascade