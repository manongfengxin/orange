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
    public Result addCart(@RequestBody Cart cart) {
        log.info("前端传入数据：{}",cart);
        return cartService.addCart(cart.getGoodId(),cart.getGoodNumber());
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
    public Result updateGoodsNumber(@RequestBody Cart cart) {
        log.info("前端传入数据：{}",cart);
        return cartService.updateGoodNumber(cart.getGoodId(),cart.getGoodNumber());
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

}
