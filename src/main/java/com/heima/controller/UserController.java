package com.heima.controller;

import com.heima.pojo.Result;
import com.heima.pojo.User;
import com.heima.service.UserService;
import com.heima.utils.JwtUtil;
import com.heima.utils.Md5Util;
import com.heima.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$")String password) {
        // \s代表非空字符
            // 查询
            User u = userService.findByUserName(username);
            if(u == null) {
                userService.register(username, password);
                return Result.success();
            } else {
                return Result.error("用户名已被占用");
            }
    }

    @PostMapping("/login")
    public Result<String> login(@Pattern(regexp = "^\\S{5,16}$") String username, @Pattern(regexp = "^\\S{5,16}$")String password) {
        // 根据用户名查询用户
        User byUserName = userService.findByUserName(username);
        // 判断该用户是否存在
        if(byUserName == null) {
            return Result.error("用户名错误");
        }
        // 判断密码是否正确
        if(Md5Util.getMD5String(password).equals(byUserName.getPassword())) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", byUserName.getId());
            claims.put("username", byUserName.getUsername());
            String token = JwtUtil.genToken(claims);
            // 把token放到redis中
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.set(token, token, 1, TimeUnit.HOURS);

            return Result.success(token);
        }
        return Result.error("密码错误");
    }

    @GetMapping("/userInfo")
    public Result<User> userInfo(/*@RequestHeader(name = "Authorization") String token*/) {
        // 根据用户名查询用户
        /*Map<String, Object> map = JwtUtil.parseToken(token);
        String username = (String)map.get("username");*/
        log.info("获取用户信息");
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User u = userService.findByUserName(username);
        return Result.success(u);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Validated User user) {
        userService.update(user);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(/*@RequestParam*/@URL String avatarUrl) {
        userService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePwd")
    public Result updatePwd(@RequestBody Map<String, String> params, @RequestHeader("Authorization") String token) {
        // 1. 校验参数
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if(!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(newPwd))
            return Result.error("缺少必要的参数");

        // 原密码是否正确
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");
        User u = userService.findByUserName(username);
        if(!u.getPassword().equals(Md5Util.getMD5String(oldPwd))) {
            return Result.error("原密码错误");
        }
        if(!rePwd.equals(newPwd)) {
            return Result.error("两次密码填写不一样");
        }
        // 2. 调用service完成密码更新
        userService.updatePwd(newPwd);
        // 删除redis对应的token
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        operations.getOperations().delete(token);
        return Result.success();
    }
}
