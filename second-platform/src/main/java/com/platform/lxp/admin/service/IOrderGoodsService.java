package com.platform.lxp.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.lxp.admin.entity.dto.OrderGoodsDTO;
import com.platform.lxp.admin.entity.dto.OrderGoodsPageQueryDTO;
import com.platform.lxp.admin.entity.pojo.OrderGoods;

import com.platform.lxp.admin.entity.vo.GoodDetailVo;

import com.platform.lxp.common.ResponseResult;

import javax.validation.Valid;

/**
 * @author 来晓璞
 * @date 2026/3/31 20:12
 * @Description:
 */
public interface IOrderGoodsService extends IService<OrderGoods> {

    /**
     * 新增商品
     */
    public void addOrderGoods(OrderGoodsDTO dto);

    ResponseResult orderGoodsList(@Valid OrderGoodsPageQueryDTO dto);

    /**
     * 查询详情数据
     * @param id
     * @return
     */
    public GoodDetailVo selectById(Long id);
}
