package com.platform.lxp.admin.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 所属类型
 */
@Data
@TableName("goods_type")
public class GoodsType {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 类型名称
     */
    private String label;
}
