<?php
/**
 * Thrift PHP客户端调用库
 * 
 * @author jesse.li <lsj86@qq.com>
 */
namespace Service;

use Thrift\Protocol\TBinaryProtocol;
use Thrift\Transport\THttpClient;
use Thrift\Transport\TBufferedTransport;
use Thrift\Exception\TException;
use Service\Thrift\Request;
use Service\Thrift\Response;

// Thrift生成的代码路径
define("THRIFT_GEN_PATH", __DIR__ . '/gen-php/');

require THRIFT_GEN_PATH . 'Service/Thrift/Types.php';

// 服务异常类型
class ServiceException extends \Exception {

}

// 服务基类
class Service
{
	// thrift客户端
    private $client;

    // 是否开启debug
    private $debug = false;

    // 是否记录慢日志
    private $logSlow = false;

    // 慢日志时间
	private $logSlowTime = 0;

	// 日志存放目录
	private $logPath = './';

    // 服务名称
	protected $serviceName;

	// 应用ID
	protected $appId;

	// 应用KEY
	protected $appKey;

	// 服务地址
	protected $serviceUrl;

	// 请求ID
	protected $requestId;

	// 通过之类继承方式获取单一实例
	public static function getInstance()
	{
		static $instance;
		if (!$instance) {
			$instance = new static();
		}
		return $instance;
	}

	/**
	 * 通过服务名获取实例
	 * @param  string $serviceName 服务名
	 * @return object              服务对象
	 */
	public static function getServiceClient($serviceName) 
	{
		static $instances = [];
		if (!isset($instances[$serviceName])) {
		    if (class_exists($serviceName)) {
                $instances[$serviceName] = new $serviceName();
        	} else {
			    $instances[$serviceName] = new static($serviceName);
			}
		}
		return $instances[$serviceName];
	}

	// 构造方法
	private final function __construct($serviceName = '')
	{
		if (!empty($serviceName)) {
			$this->serviceName = $serviceName;
		}
		$this->appId = Config::getAppId();
		$this->appKey = Config::getAppKey();
		$this->debug  = Config::getConfig('debug', false);
		$this->logSlow = Config::getConfig('log_slow', false);
		$this->logSlowTime = Config::getConfig('log_slow_time', 0);
		$this->logPath = Config::getConfig('log_path');
		$config = Config::getService($this->serviceName);
		if (empty($config)) {
			throw new ServiceException("服务配置不存在: {$this->serviceName}");
		}
		$this->serviceUrl = $config['url'];
		$this->requestId = uniqid(str_replace('/', '_', $this->serviceName . '_'));
		$this->init();
	}

	// 初始化thrift客户端
    private function init() {
        $urlInfo = parse_url($this->serviceUrl);
        if (!isset($urlInfo['port'])) {
        	$urlInfo['port'] = 80;
        }
        
        $className = "Service\\Thrift\\{$this->serviceName}Client";

        require THRIFT_GEN_PATH . 'Service/Thrift/' . $this->serviceName . '.php';

        $socket = new THttpClient($urlInfo['host'], $urlInfo['port'], $urlInfo['path'], $urlInfo['scheme']);
        $socket->setTimeoutSecs(3);

        $transport = new TBufferedTransport($socket);
        $protocol = new TBinaryProtocol($transport);
        $this->client = new $className($protocol);
        $transport->open();
    }

    /**
     * 调用方法
     * @param  string $method 方法名
     * @param  array  $params 参数
     * @return array         返回结果
     */
    public function call($method, $params)
	{
		$st = microtime(true);
		$request = new Request([
			'appId'       => $this->appId,
			'appKey'      => $this->appKey,
			'requestId'   => $this->requestId,
			'requestTime' => intval(microtime(true)*1000),
			'clientIp'    => isset($_SERVER['SERVER_ADDR']) ? ip2long($_SERVER['SERVER_ADDR']) : 0,
			'data'        => json_encode($params),
		]);

		try {
			$response = call_user_func([$this->client, $method], $request);

			if ($response->code != 0) {
				throw new ServiceException($response->msg, $response->code);
			}

			$result = [];
			if (!empty($response->data)) {
				$result = json_decode($response->data, true);
			}

			$execTime = sprintf('%.4f', microtime(true) - $st);
			if ($this->logSlow && $execTime >= $this->logSlowTime) {
				$this->writeLog($method, 'SLOW', $execTime, $request, $response);
			} elseif ($this->debug) {
				$this->writeLog($method, 'DEBUG', $execTime, $request, $response);
			}

			return $result;
		} catch (\Exception $e) {
			$execTime = sprintf('%.4f', microtime(true) - $st);
			$this->writeLog($method, 'ERROR', $execTime, $request, new Response(['code'=>$e->getCode(),'msg'=>$e->getMessage()]));
			
			if ($e instanceof ServiceException) {
				throw $e;
			} else {
				throw new ServiceException($e->getMessage(), $e->getCode());
			}
		}
	}

	/**
	 * 记录日志
	 * @param  string $method   方法名
	 * @param  string $type     日志类型
	 * @param  float  $execTime  执行耗时
	 * @param  object $request  请求对象
	 * @param  object $response 输出对象
	 * @return int           
	 */
	public function writeLog($method, $type, $execTime, $request, $response)
	{
		if (empty($this->logPath)) return false;

		$filename = rtrim($this->logPath, '\\/') . '/' . 'log-'.date('Ymd').'.log';

		$msg  = "[".date('Y-m-d H:i:s')."] [{$type}] [{$this->serviceName}::{$method}] [{$execTime}]";
		$msg .= " ==> request: " . $this->jsonEncode($request) . "\n";
		$msg .= " <== response: " . $this->jsonEncode($response)."\n";

		return file_put_contents($filename, $msg, FILE_APPEND | LOCK_EX);
	}

	/**
	 * JSON编码
	 * 
	 * @param  array $data
	 * @return string
	 * 
	 */
	protected function jsonEncode($data)
	{
		$result = json_encode($data);
		$result = preg_replace_callback('#\\\u([0-9a-f]{4})#i', function ($arr) {
			return iconv('UCS-2BE', 'UTF-8', pack('H4', $arr[1]));
		}, $result);
		return $result;
	}
}
