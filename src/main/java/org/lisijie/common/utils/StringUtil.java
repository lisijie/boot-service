package org.lisijie.common.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Locale;

/**
 * 字符串处理工具类
 *
 * @author jesse.li
 */
public class StringUtil {

    /**
     * 首字母大写
     *
     * @param s
     * @return
     */
    public static String ucfirst(String s) {
        if (!Character.isUpperCase(s.charAt(0))) {
            return s.substring(0, 1).toUpperCase(Locale.ENGLISH) + s.substring(1);
        }
        return s;
    }

    /**
     * 每个单词的首字母大写
     *
     * @return
     */
    public static String ucwords(String s) {
        if (s.contains(" ")) {
            String[] ss = s.split(" ");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < ss.length; i++) {
                sb.append(" ");
                sb.append(ucfirst(ss[i]));
            }
            return sb.substring(1);
        }
        return ucfirst(s);
    }

    /**
     * 转成纯小写的风格
     *
     * 用于字段对象属性到数据库字段的转换
     * 例如:
     *  userName => user_name
     *
     * @return
     */
    public static String toLowerFiled(String s) {
        StringBuilder sb = new StringBuilder();

        int start = 0;
        for (int i = 0; i < s.length(); i++) {
            if (Character.isUpperCase(s.charAt(i))) {
                if (i == 0) {
                    sb.append(s.substring(i, 1).toLowerCase(Locale.ENGLISH));
                } else {
                    sb.append(s.substring(start, i));
                    sb.append("_");
                    sb.append(s.substring(i, i + 1).toLowerCase(Locale.ENGLISH));
                }
                start = i + 1;
            }
        }
        if (start < s.length()) {
            sb.append(s.substring(start, s.length()));
        }

        return sb.toString();
    }

    /**
     * 汉字转换为拼音
     * @param s
     * @return
     */
    public static String toPinyin(String s) {
        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        StringBuilder sb = new StringBuilder();

        char[] chars = s.toCharArray();
        for (char c : chars) {
            try {
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c, format);
                if (pinyin == null) {
                    sb.append(c);
                } else if (pinyin.length > 0) {
                    sb.append(pinyin[0]);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }

        return sb.toString();
    }
}
