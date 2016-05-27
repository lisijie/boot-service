# boot-service
整合spring boot+thrift+mybatis的服务框架示例，功能还有待完善。欢迎提供改进建议！

### support 目录说明

* **php-sdk** 服务的PHP SDK，为了方便使用，在thrift生成的代码基础上再封装了一层。对输入参数、返回结果、日志记录进行统一处理。
* **autobuild.sh** 用于开发环境的自动构建脚本, 定时从git拉取代码, 如果有代码变动则重新构建并启动服务。
* **demo.sql** 本示例需要用到的数据表结构。
* **make.php** 根据thrift文件自动生成java和php代码的命令行工具。每次接口有更新时执行一下即可完成代码更新：

		jesselimatoMacBook-Air:support jesse$ ./make.php 
		重新生成Java和PHP的SDK...
		生成 user.thrift ...
		更新完成, 请将 php-sdk/service 目录拷贝至你的项目!
		
* **service.sh** 用于开发和测试环境的服务启动脚本。用法：
		
		$ ./service.sh 命令 [环境]

* **user.thrift** Thrift的接口定义文件。

### 关于Thrift接口的定义

thrift服务接口我统一了输入和输出结构，并且每个服务提供了一个ping接口，格式如下：

    // 请求结构
    struct Request
    {
        1:i64    clientIp,     // 调用方IP地址
        2:i32    appId,        // 业务AppID
        3:string appKey,       // 业务AppKey
        4:string requestId,    // 请求ID，追踪调试用
        5:i64    requestTime,  // 请求时间/毫秒
        6:string data,         // 请求数据（json encode后）
    }
    
    // 输出结构
    struct Response
    {
        1:i32    code,         // 返回码（成功返回0，非0表示有错误发生）
        2:string msg,          // 错误消息
        3:string data,         // 接口返回数据（json encode后）
    }
    
    // 服务接口定义
    service UserService
    {
        // ping接口，用于检查服务是否可用
        i32 ping(1:i32 seq),
    
        // 用户注册
        Response registerUser(1:Request req),
    
        // 用户登录
        Response login(1:Request req),
    }
    
这样做的目的是：

1. 简化thrift的接口定义文件。
2. 所有输入参数和输出结果都使用json进行编码，方便扩展，有参数变更时不需要修改thrift文件。
3. 便于跟踪调试、权限控制、日志记录。
