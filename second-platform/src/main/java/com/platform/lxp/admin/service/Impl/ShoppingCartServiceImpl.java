package com.platform.lxp.admin.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.util.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.lxp.admin.entity.dto.ShoppingCartDTO;
import com.platform.lxp.admin.entity.pojo.OrderGoods;
import com.platform.lxp.admin.entity.pojo.ShoppingCart;
import com.platform.lxp.admin.entity.vo.CartVO;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.admin.mapper.ShoppingCartMapper;
import com.platform.lxp.admin.service.IOrderGoodsService;
import com.platform.lxp.admin.service.ShoppingCartService;
import com.platform.lxp.common.Constants.GlobalConstants;
import com.platform.lxp.common.Constants.MessageConstant;
import com.platform.lxp.common.exception.BusinessException;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
        implements ShoppingCartService {

    @Resource
    private IOrderGoodsService orderGoodsService;

    // 加入购物车
    @Override
    public void addCart(ShoppingCartDTO dto) {
        Long userId = UserContext.getCurrentUser().getId();

        OrderGoods good = orderGoodsService.getById(dto.getGoodsId());

        if (ObjectUtils.isEmpty(good)) {
            throw new BusinessException(MessageConstant.GOODS_NOT_FOUND);
        }

        ShoppingCart cart = new ShoppingCart();
        BeanUtil.copyProperties(dto, cart);

        //当前系统登录用户
        cart.setUserId(userId);

        boolean save = this.save(cart);
        if (!save) {
            throw new BusinessException(MessageConstant.GOODS_buy_fail);
        }
    }

    // 获取购物车列表（带商品信息）
    @Override
    public List<CartVO> getMyCart() {
        Long userId = UserContext.getCurrentUser().getId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId)
                .orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> cartList = this.list(wrapper);


        return cartList.stream().map(cart -> {
            OrderGoods goods = orderGoodsService.getById(cart.getGoodsId());
            CartVO vo = new CartVO();
            vo.setTitle(goods.getTitle());
            vo.setPrice(goods.getPrice());
            vo.setQuantity(cart.getQuantity());
            vo.setImageUrls(goods.getImageUrls());
            vo.setGoodId(cart.getGoodsId());
            return vo;
        }).collect(Collectors.toList());
    }
}
