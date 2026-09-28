package com.platform.lxp.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;

import com.platform.lxp.admin.entity.dto.ShoppingCartDTO;
import com.platform.lxp.admin.entity.pojo.ShoppingCart;
import com.platform.lxp.admin.entity.vo.CartVO;
import java.util.List;

public interface ShoppingCartService extends IService<ShoppingCart> {
    // 加入购物车
    void addCart(ShoppingCartDTO dto);

    // 获取我的购物车列表
    List<CartVO> getMyCart();
}
