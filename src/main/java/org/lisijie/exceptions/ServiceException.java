package org.lisijie.exceptions;

import org.apache.thrift.TException;

/**
 * 服务异常类
 */
public class ServiceException extends TException {

    private int code;

    public ServiceException(String message) {
        this(message, -1);
    }

    public ServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
