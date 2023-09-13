package com.shuai.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shuai.mapper.UserMapper;
import com.shuai.pojo.po.User;
import com.shuai.pojo.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-04  15:01
 * @Description: TODO
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    /*
        1. 根据用户名去查询用户信息 以及 这个用户对应的权限信息
        2. 把对应的用户信息包括 权限信息 封装到 UsesDetails 对象中
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        User user = userMapper.selectOne(queryWrapper);
        // 如果没有查到该用户信息（即：user为null）则抛出异常
        if (user == null) {
            throw new RuntimeException("用户名或密码错误！");
        }



        // 查询用户对应的权限信息
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user,userVo);
        return userVo;
    }
}
