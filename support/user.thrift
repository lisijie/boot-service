namespace java org.lisijie.thrift
namespace php Service.Thrift

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