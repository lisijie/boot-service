package org.lisijie.common.utils;

import org.apache.commons.lang3.StringUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期处理工具类
 *
 * @author jesse.li
 */
public class DateUtil {

    /**
     * 日期转换为UNIX时间戳
     *
     * @param value 日期值
     * @param pattern 日期表达式
     * @return
     */
    public static int strToTime(String value, String pattern) {
        if(StringUtils.isBlank(value)){
            return 0;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(pattern);
        try {
            Date date = fmt.parse(value);
            return (int)(date.getTime()/1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int strToTime(String value) {
        return strToTime(value, "yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取当前的Unix时间戳/秒
     *
     * @return
     */
    public static int getTimestamp() {
        Date date = new Date();
        return (int) (date.getTime() / 1000);
    }
}
