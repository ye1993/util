package org.madcoder.util.collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.ImmutableMap;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;

/**
 * Collection 工具类
 *
 * @author mad
 */
public class CollectionUtils {


    //数组 targets 是否包含 source
    public static boolean containsAny(Object source, Object... targets) {
        return Arrays.asList(targets).contains(source);
    }


    //检查多个集合是否有一个 空
    public static boolean isAnyEmpty(Collection<?>... collections) {
        return Arrays.stream(collections).anyMatch(CollectionUtil::isEmpty);
    }


    //过滤功能，集合根据predicate过滤
    public static <T> List<T> filterList(Collection<T> from, Predicate<T> predicate) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(predicate).collect(Collectors.toList());
    }


    //适用于去重复，对象去重
    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return distinct(from, keyMapper, (t1, t2) -> t1);
    }


    //也是去重，但是BinaryOperator<T> cover 返回发生重复时候只保留哪个
    public static <T, R> List<T> distinct(Collection<T> from, Function<T, R> keyMapper, BinaryOperator<T> cover) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(convertMap(from, keyMapper, Function.identity(), cover).values());
    }


    //对集合的多功能操作
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }

    //多集合多功能操作并带上过滤
    public static <T, U> List<U> convertList(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (CollUtil.isEmpty(from)) {
            return new ArrayList<>();
        }
        return from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toList());
    }


    //把集合转成set key值用func来定义
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func) {
        if (CollUtil.isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().map(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    //把集合转成set key值用func来定义，并增加过滤功能
    public static <T, U> Set<U> convertSet(Collection<T> from, Function<T, U> func, Predicate<T> filter) {
        if (CollUtil.isEmpty(from)) {
            return new HashSet<>();
        }
        return from.stream().filter(filter).map(func).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    //集合装成map，key在function定义
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, Function.identity());
    }

    //Supplier<? extends Map<K, T>> supplier  构建新类型的map
    public static <T, K> Map<K, T> convertMap(Collection<T> from, Function<T, K> keyFunc, Supplier<? extends Map<K, T>> supplier) {
        if (CollUtil.isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, Function.identity(), supplier);
    }


    //构建map，提取key用keyFunc，提取value 用valuefunc

    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1);
    }




    //有重复key时返回俩一个
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return convertMap(from, keyFunc, valueFunc, mergeFunction, HashMap::new);
    }


    //Supplier 可以替换类型
    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, Supplier<? extends Map<K, V>> supplier) {
        if (CollUtil.isEmpty(from)) {
            return supplier.get();
        }
        return convertMap(from, keyFunc, valueFunc, (v1, v2) -> v1, supplier);
    }


    public static <T, K, V> Map<K, V> convertMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc, BinaryOperator<V> mergeFunction, Supplier<? extends Map<K, V>> supplier) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.toMap(keyFunc, valueFunc, mergeFunction, supplier));
    }




    //生成一个 Map<K, List<T>> 的结果，其中键是通过 keyFunc 函数提取的
    public static <T, K> Map<K, List<T>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(t -> t, Collectors.toList())));
    }



    //生成一个 Map<K, List<T>> 的结果，其中键是通过 keyFunc 函数提取的,value是通过valueFunc
    public static <T, K, V> Map<K, List<V>> convertMultiMap(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream()
                .collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toList())));
    }


    // 返回Map<K, Set<V>> 结果，value有重复会去重
    public static <T, K, V> Map<K, Set<V>> convertMultiMap2(Collection<T> from, Function<T, K> keyFunc, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return new HashMap<>();
        }
        return from.stream().collect(Collectors.groupingBy(keyFunc, Collectors.mapping(valueFunc, Collectors.toSet())));
    }




    //返回ImmutableMap，但是key相同的情况下会报错
    public static <T, K> Map<K, T> convertImmutableMap(Collection<T> from, Function<T, K> keyFunc) {
        if (CollUtil.isEmpty(from)) {
            return Collections.emptyMap();
        }
        ImmutableMap.Builder<K, T> builder = ImmutableMap.builder();
        from.forEach(item -> builder.put(keyFunc.apply(item), item));
        return builder.build();
    }

//    public static boolean containsAny(Collection<?> source, Collection<?> candidates) {
//        return org.springframework.util.CollectionUtils.containsAny(source, candidates);
//    }


    //提取集合第一个
    public static <T> T getFirst(List<T> from) {
        return !CollectionUtil.isEmpty(from) ? from.get(0) : null;
    }



    //过滤以后返回第一个
    public static <T> T findFirst(List<T> from, Predicate<T> predicate) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        return from.stream().filter(predicate).findFirst().orElse(null);
    }


    //某种可比较类型的元素的集合中获取具有最大值的元素的值 getMaxValue(numbers,Function.identity())
    public static <T, V extends Comparable<? super V>> V getMaxValue(Collection<T> from, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        assert from.size() > 0; // 断言，避免告警
        T t = from.stream().max(Comparator.comparing(valueFunc)).get();
        return valueFunc.apply(t);
    }



    public static <T, V extends Comparable<? super V>> V getMinValue(List<T> from, Function<T, V> valueFunc) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        assert from.size() > 0; // 断言，避免告警
        T t = from.stream().min(Comparator.comparing(valueFunc)).get();
        return valueFunc.apply(t);
    }



    public static <T, V extends Comparable<? super V>> V getSumValue(List<T> from, Function<T, V> valueFunc, BinaryOperator<V> accumulator) {
        if (CollUtil.isEmpty(from)) {
            return null;
        }
        assert from.size() > 0; // 断言，避免告警
        return from.stream().map(valueFunc).reduce(accumulator).get();
    }

    //添加元素
    public static <T> void addIfNotNull(Collection<T> coll, T item) {
        if (item == null) {
            return;
        }
        coll.add(item);
    }

    public static <T> Collection<T> singleton(T deptId) {
        return deptId == null ? Collections.emptyList() : Collections.singleton(deptId);
    }

}
