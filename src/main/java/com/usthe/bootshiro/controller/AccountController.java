package com.usthe.bootshiro.controller;

import com.usthe.bootshiro.domain.vo.Message;
import com.usthe.bootshiro.service.AccountService;
import com.usthe.bootshiro.util.*;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.UUID;

/**
 *   post新增,get读取,put完整更新,patch部分更新,delete删除
 * @author tomsun28
 * @date 14:40 2018/3/8
 */
@RestController
@RequestMapping("/account")
public class AccountController {


    @Autowired
    private AccountService accountService;

    /**
     * description 登录签发 JWT ,这里已经在 passwordFilter 进行了登录认证
     *
     * @param request 1
     * @param response 2
     * @return com.usthe.bootshiro.domain.vo.Message
     */
    @ApiOperation(value = "用户登录", notes = "POST用户登录签发JWT")
    @PostMapping("/login")
    public Message accountLogin(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String> params = RequestResponseUtil.getRequestBodyMap(request);
        String appId = params.get("appId");
        // 根据appId获取其对应所拥有的角色(这里设计为角色对应资源，没有权限对应资源)
        String roles = accountService.loadAccountRole(appId);
        // 时间以秒计算,token有效刷新时间是token有效过期时间的2倍
        long refreshPeriodTime = 36000L;
        String jwt = JsonWebTokenUtil.issueJWT(UUID.randomUUID().toString(), appId,
                "token-server", refreshPeriodTime >> 1, roles, null, SignatureAlgorithm.HS512);

        return new Message().ok(1003, "issue jwt success").addData("jwt", jwt);
    }

}
