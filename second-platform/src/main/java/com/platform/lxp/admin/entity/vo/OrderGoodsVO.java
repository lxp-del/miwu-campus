package com.platform.lxp.admin.entity.vo;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderGoodsVO {

    // 商品ID
    private Long id;

    // 商品图片
    private String image;

    // 商品标题（名称）
    private String title;

    // 价格
    private String price;

    // 发布多久了（几分钟前、几小时前、几天前）
    private String publishTimeAgo;

    // 话题标签
    private String topic;

    // 卖家昵称
    private String sellerNickname;

    // 卖家头像
    private String sellerAvatar;

    // 发布时间（原时间，用于前端计算或后端转成时间差）
    private LocalDateTime createTime;

    //发布标签（选择第一个）
    private String tag;

    private String createUser;

    /**
     * 该商品的【树形评论列表】
     * 前端直接遍历展示：评论 + 回复
     */
    private List<CommentVO> commentList;


}
