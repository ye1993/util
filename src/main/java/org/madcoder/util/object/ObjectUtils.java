package org.madcoder.util.object;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.function.Consumer;

/**
 * Object 工具类
 *
 * @author madcoder
 */
public class ObjectUtils {

    /**
     * 克隆对象并忽略指定属性。
     * <p>
     * 该方法通过对象的克隆实现，首先使用 {@link ObjectUtil#clone(Object)} 方法克隆原始对象，然后通过反射
     * 忽略指定属性的值。最后，通过传入的 {@link Consumer} 对象进行二次编辑。
     *
     * @param object   待克隆的对象
     * @param consumer 对克隆后的对象进行二次编辑的消费者接口
     * @param <T>      对象的类型
     * @return 克隆并编辑后的对象
     */
    public static <T> T cloneIgnoreId(T object, Consumer<T> consumer) {
        T result = ObjectUtil.clone(object);
        // 忽略 id 编号
        Field field = ReflectUtil.getField(object.getClass(), "id");
        if (field != null) {
            ReflectUtil.setFieldValue(result, field, null);
        }
        // 二次编辑
        if (result != null) {
            consumer.accept(result);
        }
        return result;
    }



    /**
     * 返回两个可比较对象中较大的那个对象。
     * 如果其中一个对象为 null，则返回另一个对象。
     *
     * @param obj1 第一个可比较对象
     * @param obj2 第二个可比较对象
     * @return 较大的那个对象，如果两个对象相等，则返回任意一个对象
     * @throws NullPointerException 如果两个对象都为 null，则抛出 NullPointerException
     * @param <T> 可比较对象的类型
     */
    public static <T extends Comparable<T>> T max(T obj1, T obj2) {
        if (obj1 == null) {
            return obj2;
        }
        if (obj2 == null) {
            return obj1;
        }
        return obj1.compareTo(obj2) > 0 ? obj1 : obj2;
    }


    /**
     * 返回给定对象数组中的第一个非空对象，如果所有对象都为null，则返回null。
     * @param array 要检查的对象数组
     * @param <T> 对象的类型
     * @return 给定对象数组中的第一个非空对象，如果所有对象都为null，则返回null
     */
    @SafeVarargs
    public static <T> T defaultIfNull(T... array) {
        for (T item : array) {
            if (item != null) {
                return item;
            }
        }
        return null;
    }


    /**
     * 检查给定对象是否等于数组中的任何一个对象。
     * @param obj 要检查的对象
     * @param array 对象数组
     * @param <T> 对象的类型
     * @return 如果给定对象等于数组中的任何一个对象，则返回true；否则返回false
     */
    @SafeVarargs
    public static <T> boolean equalsAny(T obj, T... array) {
        return Arrays.asList(array).contains(obj);
    }

}
