alter table tybss_new.t_user
    add column language_type varchar(10) null COMMENT '用户设置语种';

alter table tybss_new.t_merchant
    add column language_list varchar(255) null COMMENT '商户可选语种设置，多个以逗号分隔';


update tybss_new.t_merchant
set language_list ='zh,en';


drop table if exists tybss_new.t_merchant_language;
create table if not exists tybss_new.t_merchant_language
(
    `id`            bigint(64) NOT NULL,
    `language_name` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs  DEFAULT NULL COMMENT '语种',
    `status`        tinyint(1) NOT NULL COMMENT '0:无效,1:有效',
    `msg`           varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '名称',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_as_cs
  ROW_FORMAT = DYNAMIC;


insert into tybss_new.t_merchant_language
values (1, 'zh', 1, '中文简体'),
       (2, 'en', 1, '英文'),
       (3, 'tw', 0, '中文繁体'),
       (4, 'jp', 0, '日语'),
       (5, 'pt', 0, '葡萄牙语'),
       (6, 'ru', 0, '俄罗斯语'),
       (7, 'it', 0, '意大利语'),
       (8, 'de', 0, '德语'),
       (9, 'fr', 0, '法语'),
       (10, 'ko', 0, '韩语'),
       (11, 'th', 0, '泰语'),
       (12, 'vi', 0, '越南语'),
       (13, 'es', 0, '西班牙语');