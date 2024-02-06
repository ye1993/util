package org.madcoder.util.json;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON 工具类
 *
 * @author madcoder
 */
@UtilityClass
@Slf4j
public class JsonUtils {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.registerModules(new JavaTimeModule()); // 解决 LocalDateTime 的序列化
    }

    /**
     * 初始化 objectMapper 属性
     * <p>
     * 通过这样的方式，使用 Spring 创建的 ObjectMapper Bean
     *
     * @param objectMapper ObjectMapper 对象
     */
    public static void init(ObjectMapper objectMapper) {
        JsonUtils.objectMapper = objectMapper;
    }

    @SneakyThrows
    public static String toJsonString(Object object) {
        return objectMapper.writeValueAsString(object);
    }

    @SneakyThrows
    public static byte[] toJsonByte(Object object) {
        return objectMapper.writeValueAsBytes(object);
    }


    /**
     * 将对象转换为格式化的 JSON 字符串。
     *
     * @param object 要转换的对象
     * @return 格式化的 JSON 字符串
     */
    @SneakyThrows
    public static String toJsonPrettyString(Object object) {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }



    /**
     * 将 JSON 字符串转换为指定类型的对象。
     *
     * @param text  JSON 字符串
     * @param clazz 目标对象的类
     * @param <T>   目标对象的类型
     * @return 转换后的对象，如果转换失败则返回 null
     * @throws RuntimeException 如果转换过程中出现异常
     */
    public static <T> T parseObject(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return null;
        }
        try {
            return objectMapper.readValue(text, clazz);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象。
     *
     * @param text JSON 字符串
     * @param type 目标对象的 Type
     * @param <T>  目标对象的类型
     * @return 转换后的对象，如果转换失败则返回 null
     * @throws RuntimeException 如果转换过程中出现异常
     */
    public static <T> T parseObject(String text, Type type) {
        if (StrUtil.isEmpty(text)) {
            return null;
        }
        try {
            //TypeReference
            return objectMapper.readValue(text, objectMapper.getTypeFactory().constructType(type));
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将字符串解析成指定类型的对象
     * 使用 {@link #parseObject(String, Class)} 时，在@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS) 的场景下，
     * 如果 text 没有 class 属性，则会报错。此时，使用这个方法，可以解决。
     *
     * @param text 字符串
     * @param clazz 类型
     * @return 对象
     */
    public static <T> T parseObject2(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return null;
        }
        return JSONUtil.toBean(text, clazz);
    }


    /**
     * 将字节数组转换为指定类型的对象
     *
     * @param bytes 字节数组
     * @param clazz 目标类型的 Class 对象
     * @param <T>   目标类型
     * @return 反序列化后的对象，如果字节数组为空则返回 null
     * @throws RuntimeException 如果反序列化过程中出现异常
     */
    public static <T> T parseObject(byte[] bytes, Class<T> clazz) {
        if (ArrayUtil.isEmpty(bytes)) {
            return null;
        }
        try {
            return objectMapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error("json parse err,json:{}", bytes, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串转换为指定类型的对象，支持复杂类型
     *
     * @param text           JSON 字符串
     * @param typeReference  指定类型的 TypeReference 对象
     * @param <T>            目标类型
     * @return 反序列化后的对象，如果 JSON 字符串为空则返回 null
     * @throws RuntimeException 如果反序列化过程中出现异常
     */
    public static <T> T parseObject(String text, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(text, typeReference);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 数组字符串转换为指定类型的 List。
     * <p>
     * 例如，给定 JSON 数组字符串和目标对象类型，通过该方法可以得到一个包含目标对象的 List。
     *
     * @param text         JSON 数组字符串
     * @param clazz        目标对象的类型
     * @param <T>          目标对象的泛型
     * @return 包含目标对象的 List，如果 JSON 解析失败或输入字符串为空，则返回空列表
     */
    public static <T> List<T> parseArray(String text, Class<T> clazz) {
        if (StrUtil.isEmpty(text)) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(text, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 将 JSON 字符串解析为 JsonNode 对象。
     * <p>
     * 例如，给定 JSON 字符串，通过该方法可以得到相应的 JsonNode 对象表示整个 JSON 结构。
     *
     * @param text JSON 字符串
     * @return JsonNode 对象，表示整个 JSON 结构，如果 JSON 解析失败或输入字符串为空，则抛出 RuntimeException
     */
    public static JsonNode parseTree(String text) {
        try {
            return objectMapper.readTree(text);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    public static JsonNode parseTree(byte[] text) {
        try {
            return objectMapper.readTree(text);
        } catch (IOException e) {
            log.error("json parse err,json:{}", text, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 判断字符串是否为合法的 JSON 格式。
     * <p>
     * 例如，给定一个字符串，通过该方法可以判断它是否符合 JSON 格式的语法规则。
     *
     * @param text 待判断的字符串
     * @return 如果字符串为合法的 JSON 格式，则返回 true；否则返回 false
     */
    public static boolean isJson(String text) {
        return JSONUtil.isTypeJSON(text);
    }

}
