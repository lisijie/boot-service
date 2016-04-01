<?php
namespace Service;

/**
 * 服务配置加载
 * 
 * @author jesse.li <lsj86@qq.com>
 */
class Config 
{
	private static $config = [];
	private static $loaded = false;

	// 加载配置
	public static function load($filename = '', $reload = false) 
	{
		if (!static::$loaded || $reload) {
			if (empty($filename)) {
				$filename = __DIR__ . '/Config/service.php';
			}
			if (is_file($filename)) {
				static::$config = include $filename;
			} else {
				die('service配置文件不存在：' . $filename);
			}
			static::$loaded = true;
		}
	}

	// 获取AppId
	public static function getAppId() {
		return static::getConfig('app_id', 0);
	}

	// 获取AppKey
	public static function getAppKey() {
		return static::getConfig('app_key', '');
	}

	// 获取某个服务配置
	public static function getService($name) {
		return static::getConfig("service_list.{$name}", []);
	}

	// 获取配置
	public static function getConfig($name, $default = null) {
		if (strpos($name, '.') === false) {
			return array_key_exists($name, static::$config) ? static::$config[$name] : $default;
		}
		$parts = explode('.', $name);
		$config = static::$config;
		while (!empty($parts)) {
			$key = array_shift($parts);
			if (array_key_exists($key, $config)) {
				$config = $config[$key];
			} else {
				return $default;
			}
		}
		return $config;
	}

	// 重新设置配置信息
	public static function setConfig($name, $value) {
		static::$config[$name] = $value;
	}
}