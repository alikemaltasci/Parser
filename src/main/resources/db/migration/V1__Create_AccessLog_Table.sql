CREATE TABLE `access_log_pk` (
  `next_val` bigint(20) DEFAULT NULL
);

INSERT INTO access_log_pk(next_val) VALUES (1);

CREATE TABLE `access_log` (
  `id` bigint(20) NOT NULL,
  `access_date` datetime DEFAULT NULL,
  `ip` varchar(15) DEFAULT NULL,
  `request` varchar(20) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
