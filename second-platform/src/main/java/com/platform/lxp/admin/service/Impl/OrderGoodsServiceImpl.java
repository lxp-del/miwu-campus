package com.platform.lxp.admin.service.Impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.platform.lxp.admin.entity.dto.OrderGoodsDTO;
import com.platform.lxp.admin.entity.dto.OrderGoodsPageQueryDTO;
import com.platform.lxp.admin.entity.pojo.GoodsTopic;
import com.platform.lxp.admin.entity.pojo.OrderGoods;
import com.platform.lxp.admin.entity.pojo.SysUser;
import com.platform.lxp.admin.entity.vo.CommentVO;
import com.platform.lxp.admin.entity.vo.GoodDetailVo;
import com.platform.lxp.admin.entity.vo.OrderGoodsVO;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.admin.mapper.OrderGoodsMapper;
import com.platform.lxp.admin.mapper.SysUserMapper;
import com.platform.lxp.admin.service.IOrderGoodsService;
import com.platform.lxp.admin.utils.TimeUtil;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.common.exception.BusinessException;

import com.platform.lxp.goods.mapper.GoodsTopicMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

/**
 * @author 来晓璞
 * @date 2026/3/31 20:12
 * @Description:
 */
@Service
public class OrderGoodsServiceImpl extends ServiceImpl<OrderGoodsMapper, OrderGoods> implements IOrderGoodsService {


    @Resource
    private OrderGoodsMapper orderGoodsMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private GoodsTopicMapper goodsTopicMapper;

//    @Resource
//    private GoodsCommentService goodsCommentService;

    /**
     * 新增
     *
     * @param dto
     */
    @Override
    public void addOrderGoods(OrderGoodsDTO dto) {


        if (ObjectUtil.isNotEmpty(dto)) {
            OrderGoods orderGoods = new OrderGoods();
            orderGoods.setCreateUser(UserContext.getCurrentUser().getId());
            BeanUtils.copyProperties(dto, orderGoods);
            int insert = orderGoodsMapper.insert(orderGoods);
            if (insert != 1) {
                throw new BusinessException("商品新增失败");
            }
        }

    }

    @Override
    public ResponseResult orderGoodsList(OrderGoodsPageQueryDTO dto) {

        // 1. PageHelper 分页
        PageHelper.startPage(dto.getPageNum(), dto.getPageSize());

        // 2. 根据商品类型查询 OrderGoods
        LambdaQueryWrapper<OrderGoods> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderGoods::getTypeId, dto.getTypeId());
        wrapper.orderByDesc(OrderGoods::getCreateTime);

        List<OrderGoods> orderGoodsList = orderGoodsMapper.selectList(wrapper);
        PageInfo<OrderGoods> pageInfo = new PageInfo<>(orderGoodsList);

        // 3. 封装 VO
        List<OrderGoodsVO> voList = orderGoodsList.stream().map(item -> {
            OrderGoodsVO vo = new OrderGoodsVO();
            BeanUtil.copyProperties(item, vo);

            vo.setTitle(item.getTitle());
            vo.setPrice(item.getPrice().toString());
            // 发布时间差
            vo.setPublishTimeAgo(TimeUtil.getTimeBefore(item.getCreateTime()));
            //封面取第一张图片
            String imageUrlsStr = item.getImageUrls();
            if (imageUrlsStr != null && !imageUrlsStr.trim().isEmpty()) {
                String[] urlArray = imageUrlsStr.split(",");
                if (urlArray.length > 0) {
                    vo.setImage(urlArray[0].trim());
                }
            }

            //标签选第一个进行插入
            if (StringUtils.isNotBlank(item.getTopic())) {
                List<String> topicIds = Arrays.asList(item.getTopic().split(","));
                List<GoodsTopic> goodsTopics = goodsTopicMapper.selectBatchIds(topicIds);
                if (goodsTopics != null && !goodsTopics.isEmpty()) {
                    GoodsTopic firstTopic = goodsTopics.get(0);
                    if (firstTopic != null) {
                        vo.setTag(firstTopic.getTopic());
                    }
                }
            }
            SysUser sysUser = sysUserMapper.selectById(item.getCreateUser());
            if (sysUser != null) {
                vo.setSellerNickname(sysUser.getName());
                vo.setSellerAvatar(sysUser.getAvatar());
            }
            return vo;
        }).collect(Collectors.toList());

        // 4. 封装分页 VO 返回
        PageInfo<OrderGoodsVO> voPageInfo = new PageInfo<>();
        BeanUtil.copyProperties(pageInfo, voPageInfo);
        voPageInfo.setList(voList);

        return ResponseResult.success(voPageInfo);
    }


    /**
     * 查询详情数据
     *
     * @param id
     * @return
     */
    @Override
    public GoodDetailVo selectById(Long id) {

        OrderGoods orderGoods = orderGoodsMapper.selectById(id);
        if (ObjectUtil.isEmpty(orderGoods)) {
            throw new BusinessException("当前信息不存在");
        }

        GoodDetailVo vo = new GoodDetailVo();
        BeanUtils.copyProperties(orderGoods, vo);

        //商品创建人信息
        vo.setSysUser(sysUserMapper.selectById(orderGoods.getCreateUser()));

        //topic采取逗号拼接
        List<GoodsTopic> goodsTopics = goodsTopicMapper.selectBatchIds(Arrays.asList(orderGoods.getTopic().split(",")));
        if (ObjectUtil.isNotEmpty(goodsTopics)) {
            String topicStr = goodsTopics.stream()
                    .map(GoodsTopic::getTopic)
                    .collect(Collectors.joining(","));

            vo.setTopic(topicStr);
        }


        return vo;
    }

}
