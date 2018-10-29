CREATE TABLE `blocked_client_pk` (
  `next_val` bigint(20) DEFAULT NULL
);

INSERT INTO blocked_client_pk(next_val) VALUES (1);

CREATE TABLE `blocked_client` (
  `id` bigint(20) NOT NULL,
  `description` varchar(150) DEFAULT NULL,
  `ip` varchar(15) DEFAULT NULL,
  `request_count` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
);
