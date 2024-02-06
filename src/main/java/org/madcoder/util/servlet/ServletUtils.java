package org.madcoder.util.servlet;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.servlet.ServletUtil;
import org.madcoder.util.json.JsonUtils;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 客户端工具类
 *
 * @author madcoder
 */
public class ServletUtils {

    /**
     * 将对象写入 HTTP 响应中，以 JSON 格式。
     * @param response HttpServletResponse 对象，用于写入响应
     * @param object 要写入响应的对象
     */
    @SuppressWarnings("deprecation") // 必须使用 APPLICATION_JSON_UTF8_VALUE，否则会乱码
    public static void writeJSON(HttpServletResponse response, Object object) {
        String content = JsonUtils.toJsonString(object);
        ServletUtil.write(response, content, MediaType.APPLICATION_JSON_UTF8_VALUE);
    }

    /**
     * 将字节数组作为附件写入 HTTP 响应中。
     * @param response HttpServletResponse 对象，用于写入响应
     * @param filename 附件的文件名
     * @param content 要写入响应的字节数组
     * @throws IOException 写入过程中可能抛出 IOException 异常
     */
    public static void writeAttachment(HttpServletResponse response, String filename, byte[] content) throws IOException {
        // 设置 header 和 contentType
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        // 输出附件
        IoUtil.write(response.getOutputStream(), false, content);
    }

    /**
     * 获取 HTTP 请求的 User-Agent 头部信息。
     * @param request HttpServletRequest 对象，用于获取请求头部信息
     * @return 返回 User-Agent 头部信息，如果未找到则返回空字符串
     */
    public static String getUserAgent(HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        return ua != null ? ua : "";
    }

    /**
     * 获取当前请求的 HttpServletRequest 对象。
     * @return 返回当前请求的 HttpServletRequest 对象，如果无法获取则返回 null
     */
    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (!(requestAttributes instanceof ServletRequestAttributes)) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

    /**
     * 获取当前请求的 User-Agent。
     * @return 返回当前请求的 User-Agent，如果无法获取则返回 null
     */
    public static String getUserAgent() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return getUserAgent(request);
    }

    /**
     * 获取客户端 IP 地址。
     * @return 返回客户端 IP 地址，如果无法获取则返回 null
     */
    public static String getClientIP() {
        HttpServletRequest request = getRequest();
        if (request == null) {
            return null;
        }
        return ServletUtil.getClientIP(request);
    }

    /**
     * 判断请求是否为 JSON 格式。
     * @param request Servlet 请求对象
     * @return 如果请求为 JSON 格式则返回 true，否则返回 false
     */
    public static boolean isJsonRequest(ServletRequest request) {
        return StrUtil.startWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE);
    }

    /**
     * 获取 HTTP 请求的请求体内容。
     * @param request HTTP 请求对象
     * @return 请求体内容，如果获取失败则返回 null
     */
    public static String getBody(HttpServletRequest request) {
        return ServletUtil.getBody(request);
    }

    /**
     * 获取 HTTP 请求的请求体内容，并以字节数组形式返回。
     * @param request HTTP 请求对象
     * @return 请求体内容的字节数组，如果获取失败则返回 null
     */
    public static byte[] getBodyBytes(HttpServletRequest request) {
        return ServletUtil.getBodyBytes(request);
    }

    public static String getClientIP(HttpServletRequest request) {
        return ServletUtil.getClientIP(request);
    }


    /**
     * 获取 HTTP 请求的参数映射。
     * @param request HTTP 请求对象
     * @return 参数映射，键为参数名，值为参数值；如果获取失败或参数为空，则返回空映射
     */
    public static Map<String, String> getParamMap(HttpServletRequest request) {
        return ServletUtil.getParamMap(request);
    }
}
