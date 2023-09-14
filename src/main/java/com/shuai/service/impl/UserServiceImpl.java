package com.shuai.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.shuai.common.RedisKey;
import com.shuai.handler.UserThreadLocal;
import com.shuai.mapper.UserMapper;
import com.shuai.pojo.bo.WxAuth;
import com.shuai.pojo.po.User;
import com.shuai.pojo.vo.UserVo;
import com.shuai.pojo.vo.WxUserInfo;
import com.shuai.service.UserService;
import com.shuai.service.WxService;
import com.shuai.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @Author: fengxin
 * @CreateTime: 2023-04-19  22:43
 * @Description: TODO
 */

@Slf4j
@Service
@Transactional
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {



    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserMapper userMapper;

//    @Autowired
//    private RedisTemplate<String,Object> redisTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Value("${weixin.appid}")
    private String appid;

    @Value("${weixin.secret}")
    private String secret;

    @Autowired
    private WxService wxService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /* 注册用户名+并设置密码
     * @description:
     * @author: fengxin
     * @date: 2023/4/20 10:38
     * @param: [uservo]：{username,password}
     * @return: com.improve.shell.util.Result
     **/
    @Override
    public Result loginByUser(UserVo uservo) {
        // 1. 通过用户名获取用户记录
        User userRecord = selectUserByUsername(uservo.getUsername());
        // 2. 判断用户存不存在
        if (null != userRecord){
            // 2.1 用户存在：
            // 2.1.1 将密码进行MD5加密
            uservo.setPassword(MD5Utils.digest(uservo.getPassword()));
            // 2.1.2 判断密码是否正确
            if (Objects.equals(uservo.getPassword(), userRecord.getPassword())){
                // 设置用户id、传入登录模块
                uservo.setId(userRecord.getId());
                return this.login(uservo);
            }else {
                return Result.fail("账号或密码错误！");
            }
        }else {
            // 2.2 用户不存在：
            return Result.fail("该用户名还未注册！");
        }
    }

    @Override
    public Result accountLogin(UserVo uservo) {
        //AuthenticationManager authenticate进行用户认证
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(uservo.getUsername(),uservo.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证没通过，给出对应的提示
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("登录失败");
        }
        //如果认证通过了，使用userid生成一个jwt jwt存入ResponseResult返回
        UserVo userVo = (UserVo) authenticate.getPrincipal();
        return login(userVo);
    }

    /* 注册用户名+并设置密码
     * @description:
     * @author: fengxin
     * @date: 2023/4/20 10:38
     * @param: [uservo]：{username,password}
     * @return: com.improve.shell.util.Result
     **/
    @Override
    public Result register(UserVo uservo) {
        // 1. 通过用户名查询数据库有没有已注册的用户记录
        User userRecord = selectUserByUsername(uservo.getUsername());
        // 2. 判断用户存不存在
        if (null != userRecord){
            // 2.1 用户存在：
            return Result.fail("该用户名已被注册！");
        }
        // 2.2 用户不存在：
        User user = new User();
        // 3. 将密码进行MD5加密
//        uservo.setPassword(MD5Utils.digest(uservo.getPassword()));
        uservo.setPassword(passwordEncoder.encode(uservo.getPassword()));
        // 4. 将uservo的值全部赋值给user：为了用user存入数据库
        BeanUtils.copyProperties(uservo,user);
        // 4.1 获取并设置注册时间
        user.setCreateTime(TimeUtil.getNowTime());
        // 4.2 设置默认昵称、头像
        uservo.setNickname("昵称" + TimeUtil.getNowTimeString());
        uservo.setAvatar("https://pic.imgdb.cn/item/64c617ea1ddac507cc683d71.webp");
        // 向数据库添加新注册的用户
        userMapper.insert(user);
        // 添加成功后通过设置uservo的id属性为数据库的id
        uservo.setId(user.getId());
        return Result.success("注册成功，请登录！");
    }

    /*
     * (抽取方法)：通过用户名从数据库获取用户记录
     * @param: [uservo]
     * @return: User：数据库中一条用户记录 或 null
     **/
    private User selectUserByUsername(String username) {
        // 1.使用MySQL Plus 的 QueryWrapper
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        // 2.写入查询条件：username
        queryWrapper.eq("username", username);
        // 3.查询并返回记录
        return userMapper.selectOne(queryWrapper);
    }


    /*
     * 签发token
     * @param: [uservo]
     * @return: com.improve.shell.util.Result
     **/
    private Result login(UserVo uservo) {
        // 1. 以用户id 签发token
        String token = JwtUtil.sign(uservo.getId());
        uservo.setOpenid(null);
        uservo.setWxUnionId(null);
        uservo.setPassword(null);
        uservo.setToken(token);
        // 2. 需要把token 存入redis，value存为uservo，下次用户访问登录资源时，可以根据token拿到用户的详细信息（存储 7 天）
//        redisTemplate.opsForValue().set(RedisKey.TOKEN + token, JSON.toJSONString(uservo),7, TimeUnit.DAYS);
        redisUtil.setCacheObject(RedisKey.TOKEN + token, JSON.toJSONString(uservo),7, TimeUnit.DAYS);
        return Result.success("登录成功",uservo);
    }

    /*-------------------------------------------------微信登录--------------------------------------------------------------*/

    @Override
    public Result getSessionId(String code){
        /**
         * 1. 拼接url，微信登录凭证校验接口
         * 2. 发起http调用，获取微信的返回结构
         * 3. 存到redis
         * 4. 生成sessionId返回给前端，作为用户登录的标识
         * 5. 生成sessionId，当用户点击登录时，可以标识用户身份
         */
        String replaceUrl = "https://api.weixin.qq.com/sns/jscode2session?appid="+
                appid + "&secret=" + secret + "&js_code=" + code + "&grant_type=authorization_code";
        String res = HttpUtil.get(replaceUrl);
        log.info("调用微信接口返回的值 ==> {}", res);

        String uuid = UUID.randomUUID().toString();
//        redisTemplate.opsForValue().set(RedisKey.WX_SESSION_ID + uuid, res, 30, TimeUnit.MINUTES);
        redisUtil.setCacheObject(RedisKey.WX_SESSION_ID + uuid, res, 30, TimeUnit.MINUTES);

        Map<String, String> map = new HashMap<>();
        map.put("sessionId", uuid);
        log.info("sessionId ==> {}", uuid);
        return Result.success("session获取成功",map);
    }

    @Override
    public Result authLogin(WxAuth wxAuth){
        /**
         * 1. 通过 wxAuth中的值,对它进行解密
         * 2. 解密完成后，会获取到微信用户信息，其中包含openId 、性别、 昵称、 头像等信息
         * 3. openId 是唯一的，需要去user表中查询openId是否存在（存在则登录成功，不存在则先注册）
         * 4. 使用jwt技术，生成token 提供给前端 token令牌，用户下次将携带token访问
         * 5. 后端通过对token的验证，知道是 此用户是否处于登录状态 以及是哪个用户登录的
         */

        try {
            // 三个参数，
            String wxRes = wxService.WxDecrypt(wxAuth.getEncryptedData(),wxAuth.getIv(),wxAuth.getSessionId());
            WxUserInfo wxUserInfo = JSON.parseObject(wxRes, WxUserInfo.class);
            // 从redis中拿到openId
//            String json = (String) redisTemplate.opsForValue().get(RedisKey.WX_SESSION_ID + wxAuth.getSessionId());
            String json = redisUtil.getCacheObject(RedisKey.WX_SESSION_ID + wxAuth.getSessionId());

            JSONObject jsonObject = JSON.parseObject(json);
            String openid = (String) jsonObject.get("openid");
            // 给 uservo 赋值
            wxUserInfo.setOpenid(openid);
            UserVo uservo = new UserVo();
            uservo.from(wxUserInfo);
            // 从数据库查询此用户是否存在
            User user = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getOpenid, openid));
            if (user == null) { // 不存在：
                // 注册
                this.register(uservo);
            }
            User userInfo = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getOpenid, openid));
            uservo.setId(userInfo.getId());
            // 登录
            return this.login(uservo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.fail("登录失败");
    }

    @Override
    public Result userinfo(boolean refresh) {
        /**
         * 1.获取目前登录的用户信息
         * 2.refresh 如果为true 代表刷新 重新生成 token 和 redis里面重新存储 续期
         * 3.false 直接返回用户信息(从redis中查询出来), 直接返回
         */

        UserVo uservo = UserThreadLocal.get();
        if (refresh) {
            String token = JwtUtil.sign(uservo.getId());
            uservo.setToken(token);
//            redisTemplate.opsForValue().set(RedisKey.TOKEN + token, JSON.toJSONString(uservo), 7, TimeUnit.DAYS);
            redisUtil.setCacheObject(RedisKey.TOKEN + token, JSON.toJSONString(uservo),7, TimeUnit.DAYS);
        }
        return Result.success("返回用户信息", uservo);
    }

    /* 登出 */
    @Override
    public Result logout() {
        // 拿到用户的 token
        String token = UserThreadLocal.get().getToken();
        // 塑造 key
        String key = RedisKey.TOKEN + token;
        // 删除此 key
//        redisTemplate.delete(key);
        redisUtil.deleteObject(key);
        return Result.success("登出成功！");
    }


    @Override
    public Result updateInfo(User user) {
        // 0. 拿到当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 1. 组装user
        user.setId(userId);
        // 2. 修改
        int i = userMapper.updateById(user);
        if (i > 0) {
            return Result.success("个人信息修改成功！");
        }else {
            return Result.fail("个人信息修改失败！请联系服务器");
        }
    }
}
