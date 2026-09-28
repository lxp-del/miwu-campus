package com.platform.lxp.goods.controller;

import com.platform.lxp.admin.entity.pojo.GoodsTopic;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.goods.mapper.GoodsTopicMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 来晓璞
 * @date 2026/4/2 20:50
 * @Description: 商品话题
 */
@Api(tags = "商品话题")
@RestController
@RequestMapping("/goods/topic")
public class GoodsTopicController {

    @Resource
    private GoodsTopicMapper goodsTopicMapper;

    @ApiOperation("查询商品话题列表")
    @GetMapping("/list")
    public ResponseResult list() {
        List<GoodsTopic> goodsTopics = goodsTopicMapper.selectList(null);
        return ResponseResult.success(goodsTopics);
    }
}
