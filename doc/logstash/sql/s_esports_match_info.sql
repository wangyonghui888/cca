SELECT id, sport_id, score, start_time as begin_time, status as match_over, update_time as modify_time
FROM s_esport_matches
WHERE (update_time >= :sql_last_value AND update_time < UNIX_TIMESTAMP(now()) * 1000)
ORDER BY modify_time ASC
LIMIT 50000;