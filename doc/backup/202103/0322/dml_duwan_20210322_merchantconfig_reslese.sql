UPDATE tybss_new.t_merchant_config set title = concat('{"zh":"',title,'"}') where title is not null;
