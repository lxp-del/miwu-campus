package com.platform.lxp.admin.entity.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/3/31 20:06
 * @Description: 商品表
 */
@Data
@TableName("order_goods")
public class OrderGoods {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

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
     * 商品状态
     */
    private Integer status;

    /**
     * 是否发布 (0:未发布, 1:已发布)
     */
    private Boolean isPublished;

    /**
     * 所属类型ID (关联类型表)
     */
    private Integer typeId;

    /**
     * 查看权限 (1:公开, 2:仅好友用户, 3:仅自己)
     */
    private Byte viewPermission;

    /**
     * 发货方式 (1:快递, 2:自提, 3:无需物流)
     */
    private Byte deliveryMethod;

    /**
     * 创建人
     */
    private Long createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改人
     */
    private String updateUser;

    /**
     * 修改时间
     */
    private Date updateTime;



}
