package com.usthe.bootshiro.shiro.filter;

import com.alibaba.fastjson.JSON;
import com.usthe.bootshiro.domain.vo.Message;
import com.usthe.bootshiro.shiro.token.JwtToken;
import com.usthe.bootshiro.util.RequestResponseUtil;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Map;
import java.util.stream.Stream;

/**
 *  支持restful url 的过滤链  JWT json web token 过滤器，无状态验证
 * @author tomsun28
 * @date 0:04 2018/4/20
 */
public class BonJwtFilter extends AbstractPathMatchingFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BonJwtFilter.class);

    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object mappedValue) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);

        boolean isJwtPost = (null != subject && !subject.isAuthenticated()) && isJwtSubmission(servletRequest);
        // 判断是否为JWT认证请求
        if (isJwtPost) {
            AuthenticationToken token = createJwtToken(servletRequest);
            try {
                subject.login(token);
                return this.checkRoles(subject,mappedValue);
            }catch (AuthenticationException e) {

                Message message = new Message().error(1007,"auth error Jwt");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
                return false;

            } catch (Exception e) {
                // 告知客户端JWT错误1005,需重新登录申请jwt
                Message message = new Message().error(1007,"error jwt");
                RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
                return false;
            }
        }else {
            // 请求未携带jwt 判断为无效请求
            Message message = new Message().error(1111,"error request");
            RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        Subject subject = getSubject(servletRequest,servletResponse);

        // 未认证的情况上面已经处理  这里处理未授权
        if (subject != null && subject.isAuthenticated()){
            //  已经认证但未授权的情况
            // 告知客户端JWT没有权限访问此资源
            Message message = new Message().error(1008,"no permission");
            RequestResponseUtil.responseWrite(JSON.toJSONString(message),servletResponse);
        }
        // 过滤链终止
        return false;
    }

    private boolean isJwtSubmission(ServletRequest request) {

        String jwt = ((HttpServletRequestWrapper)request).getHeader("authorization");
        String appId = ((HttpServletRequestWrapper)request).getHeader("appId");
        return (request instanceof HttpServletRequest)
                && !StringUtils.isEmpty(jwt)
                && !StringUtils.isEmpty(appId);
    }

    private AuthenticationToken createJwtToken(ServletRequest request) {

        Map<String,String> maps = RequestResponseUtil.getRequestHeaders(request);
        String appId = maps.get("appId");
        String ipHost = request.getRemoteAddr();
        String jwt = maps.get("authorization");
        String deviceInfo = maps.get("deviceInfo");

        return new JwtToken(ipHost,deviceInfo,jwt,appId);
    }

    /**
     * description 验证当前用户是否属于mappedValue任意一个角色
     *
     * @param subject 1
     * @param mappedValue 2
     * @return boolean
     */
    private boolean checkRoles(Subject subject, Object mappedValue){
        String[] rolesArray = (String[]) mappedValue;
        return rolesArray == null || rolesArray.length == 0 || Stream.of(rolesArray).anyMatch(role -> subject.hasRole(role.trim()));
    }

}
