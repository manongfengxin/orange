package com.shuai.controller.admin;

import com.shuai.pojo.vo.GoodVo;
import com.shuai.service.GoodService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-13  07:51
 * @Description: 后台管理器
 */
@Slf4j
@RestController
@Transactional
@RequestMapping("/admin")
public class AdminController {
//
//    @Autowired
//    private GoodService goodService;
//
//
//    /**
//     * @description: 操作员添加商品
//     * @author: fengxin
//     * @date: 2023/8/13 10:37
//     * @param: [goodVo]
//     * @return: 是否成功
//     **/
//    @PostMapping("/addGood")
//    public Result addGood(@RequestBody GoodVo goodVo) {
//        log.info("传入：{}",goodVo);
//        return goodService.add(goodVo);
//    }
//
//    /**
//     * @description: 操作员修改上架商品
//     * @author: fengxin
//     * @date: 2023/8/13 10:44
//     * @param: [goodVo]
//     * @return: 是否成功
//     **/
//    @PutMapping("/updateGood")
//    public Result put(@RequestBody GoodVo goodVo) {
//        log.info("传入：{}", goodVo);
//        return goodService.put(goodVo);
//    }
//
//
//
//
//
//    /**
//     * @description: 下架商品下架指定商品
//     * @author: fengxin
//     * @date: 2023/8/14 14:47
//     * @param: [goodId]
//     * @return: 是否成功
//     **/
//    @DeleteMapping("/deleteGood")
//    public Result deleteGood(@RequestBody GoodVo goodVo) {
//        log.info("传入：{}",goodVo);
//        return goodService.deleteGood(goodVo.getGoodId());
//    }
}
