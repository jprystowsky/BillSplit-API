-- Create schema script
create table billsplit.public.AddressLineEntity (ID bytea not null,
                                                 index int4 not null,
                                                 value varchar(255) not null,
  primary key (ID));

create table billsplit.public.BillDelegateEntity (ID bytea not null,
                                                  amount numeric(12,
                                                     2) not null,
                                                  comment varchar(255),
                                                  user_id bytea not null,
  primary key (ID));

create table billsplit.public.BillEntity (ID bytea not null,
                                          amount numeric(15,
                                             2) not null,
                                          date date,
                                          notes varchar(255),
                                          biller_ID bytea not null,
                                          category_ID bytea,
                                          settlement_ID bytea,
  primary key (ID));

create table billsplit.public.BillEntity_TagEntity (BillEntity_ID bytea not null,
                                                    tags_ID bytea not null);

create table billsplit.public.BillSetEntity (id bytea not null,
                                             name varchar(255) not null,
  primary key (id));

create table billsplit.public.BillSetEntity_BillDelegateEntity (BillSetEntity_id bytea not null,
                                                                billDelegates_ID bytea not null,
  primary key (BillSetEntity_id,
               billDelegates_ID));

create table billsplit.public.BillSetEntity_BillEntity (BillSetEntity_id bytea not null,
                                                        bills_ID bytea not null,
  primary key (BillSetEntity_id,
               bills_ID));

create table billsplit.public.BillSetEntity_UserEntity (BillSetEntity_id bytea not null,
                                                        users_id bytea not null,
  primary key (BillSetEntity_id,
               users_id));

create table billsplit.public.BillerEntity (ID bytea not null,
                                            name varchar(255) not null,
                                            contactEntity_ID bytea,
  primary key (ID));

create table billsplit.public.BillerEntity_BillEntity (BillerEntity_ID bytea not null,
                                                       bills_ID bytea not null);

create table billsplit.public.CategoryEntity (ID bytea not null,
                                              name varchar(255) not null,
                                              parent_ID bytea,
  primary key (ID));

create table billsplit.public.CategoryEntity_CategoryEntity (CategoryEntity_ID bytea not null,
                                                             children_ID bytea not null);

create table billsplit.public.ContactEntity (ID bytea not null,
                                             city varchar(255),
                                             phoneNumber varchar(255),
                                             state varchar(255),
                                             website varchar(255),
                                             zipCode varchar(255),
                                             contactType_ID bytea,
  primary key (ID));

create table billsplit.public.ContactEntity_AddressLineEntity (ContactEntity_ID bytea not null,
                                                               addressLines_ID bytea not null);

create table billsplit.public.ContactEntity_PersonEntity (ContactEntity_ID bytea not null,
                                                          persons_ID bytea not null);

create table billsplit.public.ContactTypeEntity (ID bytea not null,
                                                 name varchar(255) not null,
  primary key (ID));

create table billsplit.public.PersonEntity (ID bytea not null,
                                            name varchar(255) not null,
                                            notes varchar(255),
                                            role varchar(255),
  primary key (ID));

create table billsplit.public.PersonEntity_ContactEntity (PersonEntity_ID bytea not null,
                                                          contactEntities_ID bytea not null);

create table billsplit.public.SettlementEntity (ID bytea not null,
                                                comments varchar(255),
                                                date date not null,
  primary key (ID));

create table billsplit.public.TagEntity (ID bytea not null,
                                         name varchar(255) not null,
                                         parent_ID bytea,
  primary key (ID));

create table billsplit.public.TagEntity_TagEntity (TagEntity_ID bytea not null,
                                                   children_ID bytea not null);

create table billsplit.public.UserEntity (id bytea not null,
                                          email varchar(255) not null,
                                          firstName varchar(255) not null,
                                          googleId varchar(255) not null,
                                          lastName varchar(255) not null,
  primary key (id));

alter table billsplit.public.BillDelegateEntity add constraint FK_85b9ya89a0tblfv34qqx9o110 foreign key (user_id) references billsplit.public.UserEntity;

alter table billsplit.public.BillEntity add constraint FK_mnu1qx2tcd7o3huwcwxr4ctgj foreign key (biller_ID) references billsplit.public.BillerEntity;

alter table billsplit.public.BillEntity add constraint FK_ppyf0lc5o9sp430s7q5ggfwm6 foreign key (category_ID) references billsplit.public.CategoryEntity;

alter table billsplit.public.BillEntity add constraint FK_gv5yiwktttxy3p7208whn5vyp foreign key (settlement_ID) references billsplit.public.SettlementEntity;

alter table billsplit.public.BillEntity_TagEntity add constraint FK_lo9scyt3ulgf3ovlpuawp3ljd foreign key (tags_ID) references billsplit.public.TagEntity;

alter table billsplit.public.BillEntity_TagEntity add constraint FK_brg6rwiapnpun94cua6ppc40g foreign key (BillEntity_ID) references billsplit.public.BillEntity;

alter table billsplit.public.BillSetEntity_BillDelegateEntity add constraint FK_2f15i8t7u4w9hn5rbpqjl7oxy foreign key (billDelegates_ID) references billsplit.public.BillDelegateEntity;

alter table billsplit.public.BillSetEntity_BillDelegateEntity add constraint FK_n26quins0jmyfwhu02yyw7k4r foreign key (BillSetEntity_id) references billsplit.public.BillSetEntity;

alter table billsplit.public.BillSetEntity_BillEntity add constraint UK_fvy1ctn6xvxfayo5ck442ayyi unique (bills_ID);

alter table billsplit.public.BillSetEntity_BillEntity add constraint FK_fvy1ctn6xvxfayo5ck442ayyi foreign key (bills_ID) references billsplit.public.BillEntity;

alter table billsplit.public.BillSetEntity_BillEntity add constraint FK_26o034eunfkmt2uhy3mdx8cy3 foreign key (BillSetEntity_id) references billsplit.public.BillSetEntity;

alter table billsplit.public.BillSetEntity_UserEntity add constraint FK_3laj4gjvu54gqyt6xilbnkquh foreign key (users_id) references billsplit.public.UserEntity;

alter table billsplit.public.BillSetEntity_UserEntity add constraint FK_qeo4i4jkfpiw765u6ndtki9rw foreign key (BillSetEntity_id) references billsplit.public.BillSetEntity;

alter table billsplit.public.BillerEntity add constraint FK_lyuq2udloswdspl8c5aojy4v foreign key (contactEntity_ID) references billsplit.public.ContactEntity;

alter table billsplit.public.BillerEntity_BillEntity add constraint UK_pv19ygwnnahvel6vpyr2phrpp unique (bills_ID);

alter table billsplit.public.BillerEntity_BillEntity add constraint FK_pv19ygwnnahvel6vpyr2phrpp foreign key (bills_ID) references billsplit.public.BillEntity;

alter table billsplit.public.BillerEntity_BillEntity add constraint FK_c9v105cf36otwlr211d7hfdoq foreign key (BillerEntity_ID) references billsplit.public.BillerEntity;

alter table billsplit.public.CategoryEntity add constraint FK_4wqh50kxaj1h15gyl3x5wb5dh foreign key (parent_ID) references billsplit.public.CategoryEntity;

alter table billsplit.public.CategoryEntity_CategoryEntity add constraint UK_4xaqraclig0p0qklildsthdjl unique (children_ID);

alter table billsplit.public.CategoryEntity_CategoryEntity add constraint FK_4xaqraclig0p0qklildsthdjl foreign key (children_ID) references billsplit.public.CategoryEntity;

alter table billsplit.public.CategoryEntity_CategoryEntity add constraint FK_kvjqcdnvax6wmuo3f6h9hmwgj foreign key (CategoryEntity_ID) references billsplit.public.CategoryEntity;

alter table billsplit.public.ContactEntity add constraint FK_1u04b90pfslsvntvefyvg8aky foreign key (contactType_ID) references billsplit.public.ContactTypeEntity;

alter table billsplit.public.ContactEntity_AddressLineEntity add constraint UK_mcawpqje4fut017m9jeq3cat8 unique (addressLines_ID);

alter table billsplit.public.ContactEntity_AddressLineEntity add constraint FK_mcawpqje4fut017m9jeq3cat8 foreign key (addressLines_ID) references billsplit.public.AddressLineEntity;

alter table billsplit.public.ContactEntity_AddressLineEntity add constraint FK_b8sbx9l0cx7jh1w5ejef6ud6a foreign key (ContactEntity_ID) references billsplit.public.ContactEntity;

alter table billsplit.public.ContactEntity_PersonEntity add constraint UK_5hc3tgwtkj5hwgoaxemus70a8 unique (persons_ID);

alter table billsplit.public.ContactEntity_PersonEntity add constraint FK_5hc3tgwtkj5hwgoaxemus70a8 foreign key (persons_ID) references billsplit.public.PersonEntity;

alter table billsplit.public.ContactEntity_PersonEntity add constraint FK_3slwfaxh2jmi2rx9j2y0ahjio foreign key (ContactEntity_ID) references billsplit.public.ContactEntity;

alter table billsplit.public.PersonEntity_ContactEntity add constraint UK_5wiab2pt0lb8nuo300gxs35o8 unique (contactEntities_ID);

alter table billsplit.public.PersonEntity_ContactEntity add constraint FK_5wiab2pt0lb8nuo300gxs35o8 foreign key (contactEntities_ID) references billsplit.public.ContactEntity;

alter table billsplit.public.PersonEntity_ContactEntity add constraint FK_bjy2lvyj9bsk7lsvh6lmy2qwo foreign key (PersonEntity_ID) references billsplit.public.PersonEntity;

alter table billsplit.public.TagEntity add constraint FK_bhuqdk05uyg0k8iumtdrcykhc foreign key (parent_ID) references billsplit.public.TagEntity;

alter table billsplit.public.TagEntity_TagEntity add constraint UK_ivfiyjvjyqcd1273rfdvu1kol unique (children_ID);

alter table billsplit.public.TagEntity_TagEntity add constraint FK_ivfiyjvjyqcd1273rfdvu1kol foreign key (children_ID) references billsplit.public.TagEntity;

alter table billsplit.public.TagEntity_TagEntity add constraint FK_f43khl2psm6x9elp74mhn7yo0 foreign key (TagEntity_ID) references billsplit.public.TagEntity
