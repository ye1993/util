package org.madcoder.util.number;

import cn.hutool.core.util.StrUtil;

/**
 * 数字的工具类，补全 {@link cn.hutool.core.util.NumberUtil} 的功能
 *
 * @author madcoder
 */
public class NumberUtils {


    /**
     * 将字符串转换为 Long 类型。
     * <p>
     * 例如，给定一个字符串，通过该方法可以将其转换为 Long 类型的数据。
     *
     * @param str 待转换的字符串
     * @return 如果字符串非空且可以成功转换为 Long 类型，则返回转换后的 Long 值；否则返回 null
     */
    public static Long parseLong(String str) {
        return StrUtil.isNotEmpty(str) ? Long.valueOf(str) : null;
    }

}
