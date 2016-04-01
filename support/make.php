#!/usr/bin/env php
<?php
/**
 * Thrift SDK库生成工具
 * @author jesse.li 2016.03.31
 */

define('TO_PATH', __DIR__ . '/php-sdk/service/');
define('JAVA_OUT_PATH', dirname(__DIR__) . '/src/gen-java');
define('PHP_OUT_PATH', TO_PATH . 'gen-php');

if (!is_dir(JAVA_OUT_PATH)) {
    mkdir(JAVA_OUT_PATH, 0755, true);
}
if (!is_dir(PHP_OUT_PATH)) {
    mkdir(PHP_OUT_PATH, 0755, true);
}

$files = glob(__DIR__ . '/*.thrift');

echo "重新生成Java和PHP的SDK...\n";

foreach ($files as $filename) {
    system('thrift -gen java -out ' . JAVA_OUT_PATH . ' ' . basename($filename));
    system('thrift -gen php -out ' . PHP_OUT_PATH . ' ' . basename($filename));

	$result = parseThrift($filename);
	genClass($result);
	$basename = basename($filename);

	echo "生成 {$basename} ...\n";
}

echo "更新完成, 请将 php-sdk/service 目录拷贝至你的项目!\n";

function parseThrift($filename) {
	$result = [];
	$lines = file($filename);
	foreach ($lines as $key => $line) {
		$line = trim($line);
		if (strpos($line, "service ") === 0) {
			$result['serviceName'] = substr($line, strlen("service "));
			continue;
		}
		if (strpos($line, "Response ") === 0) {
			if (preg_match('/Response\s+([a-z\_]+)\(/i', $line, $matches)) {
				$comments = trim($lines[$key-1]);
				if (strpos($comments, '//') === 0) {
					$comments = trim(substr($comments, 2));
				} else {
					$comments = '';
				}
				$result['methods'][] = [
					'name' => trim($matches[1]),
					'comments' => $comments,
				];
			}
		}
	}
	return $result;
}

function genClass($result) {
	$now = date('Y-m-d H:i:s');
	$content = <<<EOT
<?php
/**
 * 以下代码为程序生成，请勿修改！！！
 *
 * @author jesse.li <lsj86@qq.com>
 * @date {$now}
 */
namespace Service;

class {$result['serviceName']} extends Service
{
	protected \$serviceName = '{$result['serviceName']}';


EOT;

	foreach ($result['methods'] as $method) {
		if ($method['comments']) {
			$content .= "\t// {$method['comments']}\n";
		}
		$content .= <<<EOT
	public function {$method['name']}(\$params)
	{
		return \$this->call('{$method['name']}', \$params);
	}


EOT;
	}

	$content .= "}\r\n";

	file_put_contents(TO_PATH . '/'. $result['serviceName'] . '.php', $content);
}

