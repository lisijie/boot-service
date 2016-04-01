<?php
/**
 * 以下代码为程序生成，请勿修改！！！
 *
 * @author jesse.li <lsj86@qq.com>
 * @date 2016-04-01 09:40:40
 */
namespace Service;

class UserService extends Service
{
	protected $serviceName = 'UserService';

	// 用户注册
	public function registerUser($params)
	{
		return $this->call('registerUser', $params);
	}

	// 用户登录
	public function login($params)
	{
		return $this->call('login', $params);
	}

}
