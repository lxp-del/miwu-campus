package com.platform.lxp.admin.entity.vo;

import com.platform.lxp.admin.entity.pojo.SysUser;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/4/3 23:37
 * @Description: 商品详情信息视图层
 */
@Data
public class GoodDetailVo {

    /**
     * 商品ID
     */
    private Long id;

    /**
     * 用户信息
     */
    private SysUser sysUser;

    /**
     * 图片URL
     */
    private String imageUrls;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容详情
     */
    private String content;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * 话题标签
     */
    private String topic;

    /**
     * 发布时间差
     */
    private String dateDiffer;

    /**
     * 发布地址
     */
    private String publishAdr;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;
}
