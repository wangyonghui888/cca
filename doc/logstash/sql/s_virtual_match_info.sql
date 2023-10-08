SELECT id, sport_id, score_result score, begin_time, match_over, modify_time
FROM s_virtual_match_info
WHERE bet_status = 1
  and (modify_time >= :sql_last_value AND modify_time < UNIX_TIMESTAMP(now()) * 1000)
ORDER BY modify_time ASC LIMIT 50000;
