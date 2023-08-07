package com.shuai.controller;

import com.shuai.util.QiniuUtils;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: fengxin
 * @CreateTime: 2022-11-29  19:49
 * @Description: 文件工具控制器：用于文件上传、短信验证、邮箱发送...等
 */
@Slf4j
@Transactional
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private QiniuUtils qiniuUtils;

    @PostMapping("/upload")
    public Result upload(@RequestBody MultipartFile file) throws IOException {
        log.info("传过来的file==>{}",file);
        //根据文件输入流原文件名获取url
        String url = qiniuUtils.upload(file.getInputStream(), file.getOriginalFilename());
        return Result.success("文件上传成功",url);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody MultipartFile file) throws IOException{
        log.info("传过来的file==>{}",file);
        //根据原文件名称删除七牛云的文件
        qiniuUtils.delete(file.getOriginalFilename());
        return Result.success("删除成功");
    }
}