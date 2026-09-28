package com.platform.lxp.admin.entity.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 商品话题
 */
@Data
@TableName("goods_topic")
public class GoodsTopic {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 话题标签
     */
    private String topic;
}
