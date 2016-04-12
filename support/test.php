<?php
namespace Service;

require __DIR__ . '/php-sdk/service/autoload.php';

try {
	$service = UserService::getInstance();

	$result = $service->login(['username'=>'admin', 'password'=>'123456']);

	echo "登录成功, token:" . $result['token'];

} catch (\Exception $e) {
    echo $e;
}
