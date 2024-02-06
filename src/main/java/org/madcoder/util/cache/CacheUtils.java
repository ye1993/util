package org.madcoder.util.cache;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.madcoder.core.KeyValue;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * Cache 工具类
 *
 * @author madcoder
 */
public class CacheUtils {

    public static <K, V> LoadingCache<K, V> buildAsyncReloadingCache(Duration duration, CacheLoader<K, V> loader) {
        return CacheBuilder.newBuilder()
                // 只阻塞当前数据加载线程，其他线程返回旧值
                .refreshAfterWrite(duration)
                // 通过 asyncReloading 实现全异步加载，包括 refreshAfterWrite 被阻塞的加载线程
                .build(CacheLoader.asyncReloading(loader, Executors.newCachedThreadPool()));

    }


    public static void main(String[] args) throws ExecutionException {
        LoadingCache<String, String> stringStringLoadingCache = CacheUtils.buildAsyncReloadingCache(
                Duration.ofMinutes(1),// 过期时间 1 分钟
                new CacheLoader<String, String>() {

                    @Override
                    public String load(String key) throws Exception {
                        //例子 正常从数据库查询的值
                        return "A";
                    }
                }


        );
        String b = stringStringLoadingCache.get("B");  //第一次会走buildAsyncReloadingCache里的方法
        System.out.println(b);
        String a = stringStringLoadingCache.get("B"); //第二次直接从stringStringLoadingCache 里拿
        System.out.println(a);

    }
}
