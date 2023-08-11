package com.shuai.handler;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.shuai.pojo.vo.UserVo;
import com.shuai.util.JwtUtil;
import com.shuai.util.RedisKey;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//将拦截器放入mvc配置中
@Slf4j
@Component
public class LoginHandler implements HandlerInterceptor {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
                // 方法执行之前进行拦截
        /**
         * 1. 判断请求是否是请求 controller方法
         * 2. 有些接口不需要登录拦截，需要开发自定义的注解 @NoAuth => 此注解标识的，表示不需要登录验证
         * 3. 拿到 token
         * 4. 去 redis 进行token认证 ，获得 user信息
         * 5. 如果 token 认证通过就放行，不通过则返回未登录
         * 6. 得到了用户信息，放入ThreadLocal中
         */

        log.info("请求路径：{}",request.getRequestURL());
        log.info("request==>{}",request);
        log.info("handler==>{}",handler);
        log.info("token==>{}",request.getHeader("Authorization"));

        if (!(handler instanceof HandlerMethod)){//如果不是访问的controller方法就直接放行
            return true;
        }

        //如过方法上有@NoAuth注解就直接放行
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        if (handlerMethod.hasMethodAnnotation(NoAuth.class)){
            return true;
        }

        String token = request.getHeader("Authorization");
        log.info("token==>{}",token);
        token = token.replace("Bearer","");
        boolean verify = JwtUtil.verify(token);
        if (!verify){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(Result.fail("未登录")));
            return false;
        }
        String userJson = (String) redisTemplate.opsForValue().get(RedisKey.TOKEN + token);
        if (StringUtils.isBlank(userJson)){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write(JSON.toJSONString(Result.fail("未登录!")));
            return false;
        }
        UserVo uservo = JSON.parseObject(userJson, UserVo.class);
        UserThreadLocal.put(uservo);
        return true;

    }
}
