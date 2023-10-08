INSERT INTO t_system_config ( id, config_key, config_value, update_time, update_by, create_time, create_by, switch_type )
VALUES
	(
		2,
		'reservedBettingSwitch',
		'1',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin',
		1
	);
INSERT INTO t_system_config ( id, config_key, config_value, update_time, update_by, create_time, create_by, switch_type )
VALUES
	(
		3,
		'videoControlSwitch',
		'0',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin',
		2
	);
INSERT INTO t_system_config ( id, config_key, config_value, update_time, update_by, create_time, create_by, switch_type )
VALUES
	(
		4,
		'chatRoomSwitch',
		'0',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin',
		REPLACE ( unix_timestamp( CURRENT_TIMESTAMP ( 3 )), '.', '' ),
		'admin',
		3
	);