package com.shuai.controller;

import com.shuai.handler.NoAuth;
import com.shuai.util.QiniuUtils;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

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


    /**
     * @description: 上传文件
     * @author: fengxin
     * @date: 2023/8/27 17:25
     * @param: [file]
     * @return: 文件的部分 url（需后端提供拼接）
     **/
    @PostMapping("/upload")
    public Result upload(@RequestBody MultipartFile file) throws IOException {
        log.info("传过来的file==>{}",file);
        if (Objects.equals(file,null)) {
            return Result.fail("上传的图片不能为空！");
        }
        //根据文件输入流原文件名获取url
        String url = qiniuUtils.upload(file.getInputStream(), file.getOriginalFilename());
        log.info("文件上传成功==>{}",url);
        return Result.success("文件上传成功",url);
    }

    /**
     * @description: 上传文件(一次性最多五张)
     * @author: fengxin
     * @date: 2023/8/28 22:20
     * @param: [files]
     * @return: 文件的部分 url（多个）
     **/
    @PostMapping("/uploadMany")
    public Result uploadMany(@RequestBody MultipartFile[] files) throws IOException {
        log.info("传过来的file==>{}", (Object) files);
        String[] urls = new String[5];
        if (Objects.equals(files,null)) {
            return Result.fail("上传的图片不能为空！");
        }
        if (files.length > 5) {
            return Result.fail("上传图片一次最多五张！");
        }
        int i = 0;
        for (MultipartFile file :files) {
            //根据文件输入流原文件名获取url
            String url = qiniuUtils.upload(file.getInputStream(), file.getOriginalFilename());
            urls[i++] = url;
        }
        log.info("文件上传成功==>{}", (Object) urls);
        return Result.success("文件上传成功",urls);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestBody MultipartFile file) throws IOException{
        log.info("传过来的file==>{}",file);
        //根据原文件名称删除七牛云的文件
        qiniuUtils.delete(file.getOriginalFilename());
        return Result.success("删除成功");
    }
}