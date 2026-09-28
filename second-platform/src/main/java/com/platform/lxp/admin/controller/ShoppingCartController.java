package com.platform.lxp.admin.controller;

import com.platform.lxp.admin.entity.dto.ShoppingCartDTO;
import com.platform.lxp.admin.entity.vo.CartVO;
import com.platform.lxp.admin.service.ShoppingCartService;
import com.platform.lxp.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@Api(tags = "购物车接口")
@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    @Resource
    private ShoppingCartService shoppingCartService;

    // 加入购物车
    @ApiOperation(value = "加入购物车")
    @PostMapping("/add")
    public ResponseResult addCart(@Valid @RequestBody ShoppingCartDTO dto) {
        shoppingCartService.addCart(dto);
        return ResponseResult.success("加入购物车成功");
    }

    // 获取我的购物车列表
    @ApiOperation(value = "获取我的购物车列表")
    @GetMapping("/list")
    public ResponseResult getCartList() {
        List<CartVO> list = shoppingCartService.getMyCart();
        return ResponseResult.success(list);
    }
}
