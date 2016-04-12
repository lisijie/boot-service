CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_name` varchar(15) NOT NULL COMMENT '用户名',
  `nick_name` varchar(15) NOT NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(50) NOT NULL COMMENT '用户邮箱',
  `password` char(32) NOT NULL COMMENT '用户密码',
  `salt` char(10) NOT NULL COMMENT '混淆码',
  `active` tinyint(4) NOT NULL DEFAULT '0' COMMENT '激活状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `user_name` (`user_name`),
  UNIQUE KEY `nick_name` (`nick_name`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;