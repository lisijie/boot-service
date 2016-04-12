package org.lisijie.services.user.handler;

import com.alibaba.fastjson.JSONObject;
import org.apache.thrift.TException;
import org.lisijie.common.BaseServiceHandler;
import org.lisijie.common.ServiceResponse;
import org.lisijie.common.Validate;
import org.lisijie.exceptions.ParamsErrorException;
import org.lisijie.thrift.Request;
import org.lisijie.thrift.Response;
import org.lisijie.thrift.UserService;
import org.lisijie.thrift.annotation.ThriftHandler;

/**
 * 用户服务接口
 */
@ThriftHandler("/user")
public class UserServiceHandler extends BaseServiceHandler implements UserService.Iface {

    /**
     * 用户注册
     * @param req
     * @return
     * @throws TException
     */
    @Override
    public Response registerUser(Request req) throws TException {
        return null;
    }

    /**
     * 用户登录
     * @param req
     * @return
     * @throws TException
     */
    @Override
    public Response login(Request req) throws TException {
        JSONObject params = this.resolveRequest(req).params();
        ServiceResponse response = this.createResponse();

        Validate.notEmpty(params, "username", "password");

        String username = params.getString("username");
        String password = params.getString("password");

        if ("admin".equals(username) && "123456".equals(password)) {
            response.assign("token", "xxxxxxxxx"); // 登录token
        } else {
            throw new ParamsErrorException("帐号或密码错误");
        }

        return response.getResponse();
    }
}
