package com.shuai.controller;

import com.shuai.common.RedisKey;
import com.shuai.handler.UserThreadLocal;
import com.shuai.service.SearchHistoryService;
import com.shuai.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-23  13:44
 * @Description: TODO
 */
@RestController
@RequestMapping("/searchHistory")
public class SearchHistoryController {

    @Autowired
    private SearchHistoryService searchHistoryService;

    /**
     * @description: 获取指定模块的搜索历史记录
     * @author: fengxin
     * @date: 2023/8/23 14:39
     * @param: [type] = [商城，论坛] （不传，默认：商城）
     * @return: 搜索历史记录
     **/
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "商城",name = "type")String type) {
        // 1. 获取当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 2. 判断是 商城 还是 论坛的搜索历史记录
        String userKey;
        if (Objects.equals(type,"论坛")) {
            userKey = RedisKey.POST_SEARCH + userId;
        }else {
            userKey = RedisKey.GOOD_SEARCH + userId;
        }
        // 2. 获取搜索历史记录
        List<String> searchHistory = searchHistoryService.getSearchHistory(userKey);
        return Result.success(type + "的搜索历史记录",searchHistory);
    }

    /**
     * @description: 删除当前用户指定搜索记录（单条）
     * @author: fengxin
     * @date: 2023/8/23 15:39
     * @param: [keyword, type]
     * @return: 是否成功
     **/
    @DeleteMapping("/delete")
    public Result delete(@RequestParam("keyword")String keyword,
                         @RequestParam(defaultValue = "商城",name = "type")String type) {
        // 1. 获取当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 2. 判断是 商城 还是 论坛的搜索历史记录
        String userKey;
        if (Objects.equals(type,"论坛")) {
            userKey = RedisKey.POST_SEARCH + userId;
        }else {
            userKey = RedisKey.GOOD_SEARCH + userId;
        }
        // 3. 删除当前用户指定的搜索记录
        Boolean aBoolean = searchHistoryService.deleteSearchHistory(userKey, keyword);
        if (aBoolean) {
            return Result.success("删除成功！");
        }
        return Result.fail("删除失败！请联系后台");
    }

    /**
     * @description: 清空指定模块的所有搜索记录
     * @author: fengxin
     * @date: 2023/8/23 15:45
     * @param: [type]
     * @return: 是否成功
     **/
    @DeleteMapping("/clear")
    public Result clear(@RequestParam(defaultValue = "商城",name = "type")String type) {
        // 1. 获取当前用户id
        Long userId = UserThreadLocal.get().getId();
        // 2. 判断是 商城 还是 论坛的搜索历史记录
        String userKey;
        if (Objects.equals(type,"论坛")) {
            userKey = RedisKey.POST_SEARCH + userId;
        }else {
            userKey = RedisKey.GOOD_SEARCH + userId;
        }
        // 3. 清空当前用户指定模块的搜索记录
        Boolean aBoolean = searchHistoryService.clearSearchHistory(userKey);
        if (aBoolean) {
            return Result.success("已清空所有搜索记录！");
        }
        return Result.fail("清空失败！请联系后台");
    }


}