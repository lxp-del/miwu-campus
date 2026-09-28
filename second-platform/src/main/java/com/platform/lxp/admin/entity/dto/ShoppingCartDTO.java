package com.platform.lxp.admin.entity.dto;

import com.platform.lxp.common.Constants.GlobalConstants;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ShoppingCartDTO {

    @NotNull(message = "商品ID不能为空")
    private Long goodsId;

    /**
     * 地址信息
     */
    private String adrI = "1";

    /**
     * 支付方式
     */
    private String payType;

    /**
     * 数量（默认1）
     */
    private Integer quantity = GlobalConstants.CART_DEFAULT_QUANTITY;
}
