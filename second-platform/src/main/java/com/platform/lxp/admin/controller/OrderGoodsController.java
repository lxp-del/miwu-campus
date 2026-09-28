package com.platform.lxp.admin.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.lxp.admin.entity.dto.OrderGoodsDTO;
import com.platform.lxp.admin.entity.dto.OrderGoodsPageQueryDTO;

import com.platform.lxp.admin.entity.pojo.OrderGoods;
import com.platform.lxp.admin.entity.vo.GoodDetailVo;

import com.platform.lxp.admin.service.IOrderGoodsService;
import com.platform.lxp.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author 来晓璞
 * @date 2026/3/31 20:11
 * @Description:
 */
@Api(tags = "商品信息接口")
@RestController
@RequestMapping("/second/goods")
public class OrderGoodsController {

    @Resource
    private IOrderGoodsService orderGoodsService;

    @ApiOperation("商品新增")
    @PostMapping("/add")
    public ResponseResult add(@Valid @RequestBody OrderGoodsDTO dto) {
        orderGoodsService.addOrderGoods(dto);
        return ResponseResult.success();
    }

    @ApiOperation("商品名称")
    @GetMapping("/listAll")
    public ResponseResult orderGoodsListByName() {
        List<OrderGoods> list = orderGoodsService.list();
        return ResponseResult.success(list);
    }

    @ApiOperation("商品列表")
    @PostMapping("/list/type")
    public ResponseResult orderGoodsList(@RequestBody @Valid OrderGoodsPageQueryDTO dto) {
        ResponseResult pageResult = orderGoodsService.orderGoodsList(dto);
        return ResponseResult.success(pageResult);
    }

    @ApiOperation("查询详情数据")
    @GetMapping("/getById/{id}")
    public ResponseResult orderGoodsList(@PathVariable("id") String id) {
        GoodDetailVo vo = orderGoodsService.selectById(Long.parseLong(id));
        return ResponseResult.success(vo);
    }
}
