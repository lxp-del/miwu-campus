package com.platform.lxp.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.lxp.admin.entity.pojo.SysUser;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.admin.mapper.SysUserMapper;
import com.platform.lxp.admin.util.IpUtil;
import com.platform.lxp.admin.utils.IPLocationUtils;
import com.platform.lxp.admin.utils.IpUtils;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.common.exception.BusinessException;
import com.platform.lxp.goods.entity.pojo.OrderComments;
import com.platform.lxp.goods.entity.vo.OrderCommentsVO;
import com.platform.lxp.goods.service.IOrderCommentService;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 来晓璞
 * @date 2026/4/6 11:36
 * @Description:
 */
@RestController
@RequestMapping("/commentss")
public class OrderCommentController {

    @Resource
    private IOrderCommentService orderCommentService;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private IPLocationUtils ipLocationUtils;

    @Resource
    private HttpServletRequest request;


    @GetMapping("/list/{goodId}")
    public ResponseResult page(@PathVariable("goodId") Long goodId) {

        LambdaQueryWrapper<OrderComments> queryWrapper = new LambdaQueryWrapper<OrderComments>().eq(OrderComments::getGoodId, goodId);
        List<OrderComments> list = orderCommentService.list(queryWrapper);
        List<OrderCommentsVO> voList = list.stream().map(item -> {
            OrderCommentsVO vo = new OrderCommentsVO();
            vo.setContent(item.getContent());
            SysUser sysUser = sysUserMapper.selectById(item.getUserId());
            if (sysUser != null) {
                vo.setName(sysUser.getName());
                vo.setAvatar(sysUser.getAvatar());
            } else {
                vo.setName("未知用户");
                vo.setAvatar("");
            }
            String timeAgo = getTimeAgo(item.getCreateTime());
            vo.setTime(timeAgo);
            vo.setAddress(item.getAddress());
            return vo;
        }).collect(Collectors.toList()); // 修正收集器

        return ResponseResult.success(voList); // 返回数据
    }

    /**
     * 新增评论
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody OrderComments orderComment) {

        orderComment.setUserId(UserContext.getCurrentUser().getId());
        orderComment.setCreateUser(UserContext.getCurrentUser().getId());

        // 获取 IP 地址
        String localIpByNetcard = IpUtil.getLocalIpByNetcard();
        orderComment.setAddress(localIpByNetcard);
        boolean save = orderCommentService.save(orderComment);
        if (!save) {
            throw new BusinessException("评论发布失败");
        }
        return ResponseResult.success();
    }


    private String getTimeAgo(Date createTime) {
        if (createTime == null) return "未知时间";

        long now = System.currentTimeMillis();
        long createTimeMillis = createTime.getTime();
        long diff = now - createTimeMillis; // 时间差（毫秒）

        long seconds = diff / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;
        long months = days / 30;
        long years = days / 365;

        if (years > 0) {
            return years + "年前";
        } else if (months > 0) {
            return months + "个月前";
        } else if (days > 0) {
            return days + "天前";
        } else if (hours > 0) {
            return hours + "小时前";
        } else if (minutes > 0) {
            return minutes + "分钟前";
        } else {
            return "刚刚";
        }
    }
}
