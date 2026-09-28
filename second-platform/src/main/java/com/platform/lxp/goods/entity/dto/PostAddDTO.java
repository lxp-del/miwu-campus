package com.platform.lxp.goods.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
/**
 * @author 来晓璞
 * @date 2026/4/6 12:52
 * @Description:
 */
@Data
@ApiModel("帖子发布请求参数")
public class PostAddDTO {

    @ApiModelProperty("帖子标题")
    @NotBlank(message = "标题不能为空")
    private String title;

    @ApiModelProperty("帖子图片URL")
    private String imageUrl;

    @ApiModelProperty("帖子正文内容")
    @NotBlank(message = "内容不能为空")
    private String content;

    private String userId;
}
