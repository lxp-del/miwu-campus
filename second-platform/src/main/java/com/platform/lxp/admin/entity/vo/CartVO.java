package com.platform.lxp.admin.entity.vo;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class CartVO {

    private Long goodId;
    private String title;      // 商品标题
    private String imageUrls;  // 商品图片
    private BigDecimal price;  // 单价
    private Integer quantity;  // 数量

}
