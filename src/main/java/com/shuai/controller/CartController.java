package com.shuai.controller;

import com.shuai.pojo.po.Cart;
import com.shuai.pojo.vo.CartVo;
import com.shuai.service.CartService;
import com.shuai.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: fengxin
 * @CreateTime: 2023-08-14  15:53
 * @Description: 购物车管理器
 */
@Slf4j
@RestController
@Transactional
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    /**
     * @description: 添加购物车
     * @author: fengxin
     * @date: 2023/8/14 16:03
     * @param: goodId, goodNumber
     * @return: 是否成功
     **/
    @PostMapping("/add")
    public Result addCart(@RequestParam("goodId")String goodId,
                          @RequestParam("goodNumber")Integer goodNumber) {
        log.info("前端传入数据：{}和{}",goodId,goodNumber);
        return cartService.addCart(goodId,goodNumber);
    }

    /**
     * @description: 通过用户id查询用户的购物车列表
     * @author: fengxin
     * @date: 2023/8/14 16:08
     * @param: []
     * @return: 购物车列表
     **/
    @GetMapping("/list")
    public Result getCartList() {
        return cartService.getCartList();
    }

    /**
     * @description: 修改购物车商品数量
     * @author: fengxin
     * @date: 2023/8/14 16:09
     * @param: [goodId, goodNumber]
     * @return: 是否成功
     **/
    @PutMapping("/update")
    public Result updateGoodsNumber(@RequestParam("goodId")String goodId,
                                    @RequestParam("goodNumber")Integer goodNumber) {
        log.info("前端传入数据：{}和{}",goodId,goodNumber);
        return cartService.updateGoodNumber(goodId,goodNumber);
    }

    /**
     * @description: 删除指定的购物车商品
     * @author: fengxin
     * @date: 2023/8/14 16:15
     * @param: [goodId]
     * @eturn: 是否成功
     **/
    @DeleteMapping("/delete")
    public Result deleteCart(@RequestParam("goodId")String goodId) {
        log.info("前端传入数据：{}",goodId);
        return cartService.deleteCart(goodId);
    }

//    /**
//     * @description: 清空购物车（所有）
//     * @author: fengxin
//     * @date: 2023/7/31 15:57
//     * @param: [userId]
//     * @return: void
//     **/
//    @DeleteMapping("/deleteAll")
//    public String deleteShoppingCarAll(@RequestParam("userId")Long userId) {
//        log.info("调用deleteShoppingCar接口，前端传入数据：{}",userId);
//        if (userId == null) {
//            return "清空购物车失败";
//        }
//        return shoppingCarService.deleteShoppingCarAll(userId);
//    }
}
