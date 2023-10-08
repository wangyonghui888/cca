alter table r_merchant_market_bet_info rename to r_merchant_market_bet_info_0510_bak;

create table r_merchant_market_bet_info like r_merchant_market_bet_info_0510_bak;

ALTER TABLE `r_merchant_market_bet_info` ADD INDEX idx_index_match_id(`match_id`);

ALTER TABLE `r_merchant_market_bet_info` ADD INDEX idx_index_merchant_code(`merchant_code`);

#一条一条执行
insert into r_merchant_market_bet_info select * from r_merchant_market_bet_info_0510_bak WHERE updated_time >= 1652025600 and updated_time < 1649595678;

insert into r_merchant_market_bet_info select * from r_merchant_market_bet_info_0510_bak WHERE updated_time >= 1651939200 and updated_time < 1652025600;

insert into r_merchant_market_bet_info select * from r_merchant_market_bet_info_0510_bak WHERE updated_time >= 1651852800 and updated_time < 1651939200;

insert into r_merchant_market_bet_info select * from r_merchant_market_bet_info_0510_bak WHERE updated_time >= 1651766400 and updated_time < 1651852800;

insert into r_merchant_market_bet_info select * from r_merchant_market_bet_info_0510_bak WHERE updated_time >= 1651680000 and updated_time < 1651766400;

insert into r_merchant_market_bet_info select * from r_merchant_market_bet_info_0510_bak WHERE updated_time >= 1651593600 and updated_time < 1651680000;

insert into r_merchant_market_bet_info select * from r_merchant_market_bet_info_0510_bak WHERE updated_time >= 1651507200 and updated_time < 1651593600;

