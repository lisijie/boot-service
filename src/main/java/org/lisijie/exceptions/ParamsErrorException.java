package org.lisijie.exceptions;

/**
 * 参数错误异常
 * @author jesse.li
 */
public class ParamsErrorException extends ServiceException {

    public ParamsErrorException(String message) {
        super("请求参数错误: " + message, ErrorCode.PARAMS_ERROR);
    }
}
