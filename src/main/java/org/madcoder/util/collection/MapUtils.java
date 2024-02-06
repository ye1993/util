package org.madcoder.util.collection;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import org.madcoder.core.KeyValue;

import java.util.*;
import java.util.function.Consumer;

/**
 * Map 工具类
 *
 * @author madcoder
 */
public class MapUtils {

    /**
     * 从哈希表表中，获得 keys 对应的所有 value 数组
     *
     * @param multimap 哈希表
     * @param keys keys
     * @return value 数组
     */
    public static <K, V> List<V> getList(Multimap<K, V> multimap, Collection<K> keys) {
        List<V> result = new ArrayList<>();
        keys.forEach(k -> {
            Collection<V> values = multimap.get(k);
            if (CollectionUtil.isEmpty(values)) {
                return;
            }
            result.addAll(values);
        });
        return result;
    }



    /**
     * 从哈希表查找到 key 对应的 value，然后进一步处理
     * 注意，如果查找到的 value 为 null 时，不进行处理
     *
     * @param map 哈希表
     * @param key key
     * @param consumer 进一步处理的逻辑
     */
    public static <K, V> void findAndThen(Map<K, V> map, K key, Consumer<V> consumer) {
        if (CollUtil.isEmpty(map)) {
            return;
        }
        V value = map.get(key);
        if (value == null) {
            return;
        }
        consumer.accept(value);
    }

    public static void main(String[] args) {
        List<KeyValue<String, String>> kv = new ArrayList<>();
        KeyValue<String,String> k1 = new KeyValue<>();
        k1.setKey("1");
        k1.setValue("1");
        KeyValue<String,String> k2 = new KeyValue<>();
        k2.setKey("2");
        k2.setValue("2");
        KeyValue<String,String> k3 = new KeyValue<>();
        k3.setKey("3");
        k3.setValue("3");
        kv.add(k1);
        kv.add(k2);
        kv.add(k3);

        Map<String, String> stringStringMap = convertMap(kv);
        System.out.println(stringStringMap);
    }

    public static <K, V> Map<K, V> convertMap(List<KeyValue<K, V>> keyValues) {
        Map<K, V> map = Maps.newLinkedHashMapWithExpectedSize(keyValues.size());
        keyValues.forEach(keyValue -> map.put(keyValue.getKey(), keyValue.getValue()));
        return map;
    }

}
