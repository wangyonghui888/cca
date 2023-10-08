SELECT id, sport_id, score, begin_time, match_over, update_time
FROM s_match_info
WHERE (update_time >= :sql_last_value AND update_time <= UNIX_TIMESTAMP(now()) * 1000)
ORDER BY update_time ASC LIMIT 50000;
