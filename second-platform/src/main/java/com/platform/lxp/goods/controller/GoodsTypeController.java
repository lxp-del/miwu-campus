package com.platform.lxp.goods.controller;

import com.platform.lxp.admin.entity.pojo.GoodsTopic;
import com.platform.lxp.admin.entity.pojo.GoodsType;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.goods.mapper.GoodsTopicMapper;
import com.platform.lxp.goods.mapper.GoodsTypeMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 来晓璞
 * @date 2026/4/2 20:51
 * @Description: 商品所属类型
 */
@Api(tags = "商品类型")
@RestController
@RequestMapping("/goods/type")
public class GoodsTypeController {

    @Resource
    private GoodsTypeMapper goodsTypeMapper;

    @ApiOperation("查询商品类型列表")
    @GetMapping("/list")
    public ResponseResult list() {
        List<GoodsType> goodsTypes = goodsTypeMapper.selectList(null);
        return ResponseResult.success(goodsTypes);
    }
}
