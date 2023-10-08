INSERT INTO tybss_new.t_merchant_config (merchant_code) SELECT t.merchant_code FROM tybss_new.t_merchant t where t.agent_level in (0,2);
