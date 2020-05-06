package com.usthe.bootshiro.util;


import com.alibaba.fastjson.JSON;
import org.apache.shiro.web.util.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * http request response 过滤XSS SQL 数据工具类
 * @author tomsun28
 * @date 10:13 2018/2/14
 */
public class RequestResponseUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestResponseUtil.class);
    private static final String STR_BODY = "body";


    /**
     * description 获取request中的body json 数据转化为map
     *
     * @param request 1
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getRequestBodyMap(ServletRequest request) {
        Map<String ,String > dataMap = new HashMap<>(16);
        // 判断是否已经将 inputStream 流中的 body 数据读出放入 attribute
        if (request.getAttribute(STR_BODY) != null) {
            // 已经读出则返回attribute中的body
            return (Map<String,String>)request.getAttribute(STR_BODY);
        } else {
            try {
                Map<String, Object> maps = JSON.parseObject(request.getInputStream(),Map.class);
                maps.forEach((key, value) -> dataMap.put(key, String.valueOf(value)));
                request.setAttribute(STR_BODY,dataMap);
            }catch (IOException e) {
                e.printStackTrace();
            }
            return dataMap;
        }
    }


    /**
     * description 取request头中的已经被防止XSS，SQL注入过滤过的 key value数据封装到map 返回
     *
     * @param request 1
     * @return java.util.Map<java.lang.String,java.lang.String>
     */
    public static Map<String,String> getRequestHeaders(ServletRequest request) {
        Map<String,String> headerMap = new HashMap<>(16);
        Enumeration enums = ((HttpServletRequestWrapper)request).getHeaderNames();
        while (enums.hasMoreElements()) {
            String name = (String) enums.nextElement();
            String value = ((HttpServletRequestWrapper)request).getHeader(name);
            if (null != value && !"".equals(value)) {
                headerMap.put(name,value);
            }
        }
        return headerMap;
    }


    /**
     * description 封装response  统一json返回
     *
     * @param outStr 1
     * @param response 2
     */
    public static void responseWrite(String outStr, ServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=utf-8");
        PrintWriter printWriter;
        try {
            printWriter = WebUtils.toHttp(response).getWriter();
            printWriter.write(outStr);
        }catch (Exception e) {
            LOGGER.error(e.getMessage(),e);
        }
    }


}
