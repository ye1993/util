package org.madcoder.util.string;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串工具类
 *
 * @author madcoder
 */
public class StrUtils {

    /**
     * 截取字符串，保留指定长度，超过部分用省略号表示。
     * @param str 要截取的字符串
     * @param maxLength 最大长度（包括省略号）
     * @return 截取后的字符串
     */
    public static String maxLength(CharSequence str, int maxLength) {
        // 调用 StringUtil.maxLength 方法截取字符串，长度减去 3 是为了留出省略号的位置
        //asdas -> a...
        return StrUtil.maxLength(str, maxLength - 3); // -3 的原因，是该方法会补充 ... 恰好
    }



    /**
     * 检查字符串是否以集合中任何一个元素作为前缀开头。
     * @param str 要检查的字符串
     * @param prefixes 前缀集合
     * @return 如果字符串以集合中任何一个元素作为前缀开头，则返回 true，否则返回 false
     */
    public static boolean startWithAny(String str, Collection<String> prefixes) {
        if (StrUtil.isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
            return false;
        }

        for (CharSequence suffix : prefixes) {
            if (StrUtil.startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 将包含数字的字符串按指定分隔符分割成长整型列表。
     * @param value 包含数字的字符串
     * @param separator 分隔符
     * @return 分割后的长整型列表
     */
    public static List<Long> splitToLong(String value, CharSequence separator) {
        long[] longs = StrUtil.splitToLong(value, separator);
        return Arrays.stream(longs).boxed().collect(Collectors.toList());
    }

    /**
     * 将包含数字的字符串按指定分隔符分割成整型列表。
     * @param value 包含数字的字符串
     * @param separator 分隔符
     * @return 分割后的整型列表
     */
    public static List<Integer> splitToInteger(String value, CharSequence separator) {
        int[] integers = StrUtil.splitToInt(value, separator);
        return Arrays.stream(integers).boxed().collect(Collectors.toList());
    }

}
