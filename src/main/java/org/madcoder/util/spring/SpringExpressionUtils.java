package org.madcoder.util.spring;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Spring EL 表达式的工具类
 *
 * @author madCoder
 */
public class SpringExpressionUtils {

    /**
     * spel表达式解析器
     */
    private static final ExpressionParser EXPRESSION_PARSER = new SpelExpressionParser();
    /**
     * 参数名发现器
     */
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    private SpringExpressionUtils() {
    }

    /**
     * 解析表达式字符串并返回结果。
     * @param joinPoint 切点
     * @param expressionString 表达式字符串
     * @return 解析结果
     */
    public static Object parseExpression(ProceedingJoinPoint joinPoint, String expressionString) {
        Map<String, Object> result = parseExpressions(joinPoint, Collections.singletonList(expressionString));
        return result.get(expressionString);
    }


    /**
     * 解析表达式字符串列表并返回结果映射。
     * @param joinPoint 切点
     * @param expressionStrings 表达式字符串列表
     * @return 解析结果映射
     */
    public static Map<String, Object> parseExpressions(ProceedingJoinPoint joinPoint, List<String> expressionStrings) {
        // 如果为空，则不进行解析
        if (CollUtil.isEmpty(expressionStrings)) {
            return MapUtil.newHashMap();
        }

        // 第一步，构建解析的上下文 EvaluationContext
        // 通过 joinPoint 获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 使用 spring 的 ParameterNameDiscoverer 获取方法形参名数组
        String[] paramNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        // Spring 的表达式上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        // 给上下文赋值
        if (ArrayUtil.isNotEmpty(paramNames)) {
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
        }

        // 第二步，逐个参数解析
        Map<String, Object> result = MapUtil.newHashMap(expressionStrings.size(), true);
        expressionStrings.forEach(key -> {
            Object value = EXPRESSION_PARSER.parseExpression(key).getValue(context);
            result.put(key, value);
        });
        return result;
    }

    /**
     * 解析表达式字符串列表并返回结果映射。
     * @param joinPoint 切点
     * @param info 附加信息
     * @param expressionStrings 表达式字符串列表
     * @return 解析结果映射
     */
    public static Map<String, Object> parseExpression(JoinPoint joinPoint, Object info, List<String> expressionStrings) {
        // 如果为空，则不进行解析
        if (CollUtil.isEmpty(expressionStrings)) {
            return MapUtil.newHashMap();
        }

        // 第一步，构建解析的上下文 EvaluationContext
        // 通过 joinPoint 获取被注解方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        // 使用 spring 的 ParameterNameDiscoverer 获取方法形参名数组
        String[] parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
        // Spring 的表达式上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        if (ArrayUtil.isNotEmpty(parameterNames)) {
            //获取方法参数值
            Object[] args = joinPoint.getArgs();
            for (int i = 0; i < args.length; i++) {
                // 替换 SP EL 里的变量值为实际值， 比如 #user --> user对象
                context.setVariable(parameterNames[i], args[i]);
            }
            context.setVariable("info", info);
        }
        // 第二步，逐个参数解析
        Map<String, Object> result = MapUtil.newHashMap(expressionStrings.size(), true);
        expressionStrings.forEach(key -> {
            Object value = EXPRESSION_PARSER.parseExpression(key).getValue(context);
            result.put(key, value);
        });
        return result;
    }

}
