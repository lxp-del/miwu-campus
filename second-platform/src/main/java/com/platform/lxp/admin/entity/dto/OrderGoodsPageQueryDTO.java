package com.platform.lxp.admin.entity.dto;

import lombok.Data;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

@Data
public class OrderGoodsPageQueryDTO implements Serializable {

    //页码
    @NotNull(message = "页码不能为空")
    private int pageNum;

    //每页记录数
    @NotNull(message = "每页记录数不能为空")
    private int pageSize;

    //商品类型
    @NotNull(message = "商品类型不能为空")
    private Integer typeId;
}
