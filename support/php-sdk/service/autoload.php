<?php

require __DIR__ . '/Thrift/ClassLoader/ThriftClassLoader.php';

$loader = new \Thrift\ClassLoader\ThriftClassLoader();
$loader->registerNamespace('Thrift', __DIR__.'/' );
$loader->register();

spl_autoload_register(function ($class) {
	if (strpos(strtolower($class), 'service\\') === 0) {
		$classFile = __DIR__ . '/' . str_replace('\\','/',substr($class, 8)) . '.php';
		if (is_file($classFile)) {
			require $classFile;
		}
	}
});

\Service\Config::load();