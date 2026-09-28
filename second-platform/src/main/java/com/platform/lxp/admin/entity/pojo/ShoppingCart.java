package com.platform.lxp.admin.entity.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

// @Description: 购物车表
@Data
@TableName("shopping_cart")
public class ShoppingCart {

    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户ID
    private Long userId;

    // 商品ID
    private Long goodsId;

    // 商品数量
    private Integer quantity;

    // 加入购物车时的商品单价
    private BigDecimal price;

    // 是否选中 0-未选中 1-选中
    private Integer isChecked;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 地址信息
     */
    @TableField("adr_id")
    private String adrId;

    /**
     * 支付方式
     */
    @TableField("pay_type")
    private String payType;
}
