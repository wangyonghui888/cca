alter table t_system_config drop column switch_type;
alter table t_system_config drop column pull_msg_rate;

INSERT INTO t_system_config ( id, config_key, config_value, update_time, update_by, create_time, create_by )
VALUES
	(
		5,
		'pullMsgRate',
		'10',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin'
	);