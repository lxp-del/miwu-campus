package com.platform.lxp.admin.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * @author 来晓璞
 * @date 2026/3/31 20:16
 * @Description:
 */
@Data
public class OrderGoodsDTO {

    /**
     * 图片URL列表
     */
    @NotBlank(message = "至少上传一张图片") // 注意：集合判空通常用 NotEmpty 而不是 NotBlank
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
}
