--  #备注： C组库 merchant 库
ALTER TABLE merchant.m_domain_area ADD COLUMN code varchar(255) NULL DEFAULT NULL COMMENT '省份拼音';

-- 需求1889 c组merchant库
CREATE TABLE `m_match_info` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `name` varchar(100) CHARACTER SET utf8mb4   DEFAULT NULL COMMENT '赛事名称',
    `tab` varchar(100) CHARACTER SET utf8mb4   DEFAULT NULL COMMENT 'tab',
    `status` tinyint(1) NULL DEFAULT NULL COMMENT '赛事状态:1on2off',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4  COMMENT='赛事状态控制';

-- bug  CYSB 汇总报表库 都要执行
ALTER TABLE t_merchant MODIFY COLUMN currency int(3) NULL DEFAULT NULL COMMENT '币种';
