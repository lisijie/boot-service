<?php

return [
	
	// 应用ID
	'app_id' => 1,

	// 应用KEY
	'app_key' => '',

	// 是否记录慢日志
	'log_slow' => true,

	// 慢日志记录时间
	'log_slow_time' => 0.1,

	// 慢日志存放目录
	'log_path' => './',

    // 是否开启debug
	'debug' => false,

	// 服务列表
	'service_list' => [
		'UserService' => ['url' => 'http://localhost:8001/user'],
	],
];