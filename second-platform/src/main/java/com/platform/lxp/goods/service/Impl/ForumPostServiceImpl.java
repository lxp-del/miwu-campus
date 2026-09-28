package com.platform.lxp.goods.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.goods.entity.dto.PostAddDTO;
import com.platform.lxp.goods.entity.pojo.ForumPost;
import com.platform.lxp.goods.mapper.ForumPostMapper;
import com.platform.lxp.goods.service.IForumPostService;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author 来晓璞
 * @date 2026/4/6 12:53
 * @Description:
 */
@Service
public class ForumPostServiceImpl extends ServiceImpl<ForumPostMapper, ForumPost> implements IForumPostService {

    @Override
    public void addPost(PostAddDTO dto) {
        ForumPost post = new ForumPost();
        // 属性拷贝
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setImageUrl(dto.getImageUrl());
        post.setUserId(UserContext.getCurrentUser().getId());

        post.setCreateTime(new Date());
        post.setUpdateTime(new Date());

        // 使用MyBatis-Plus自带save方法入库
        this.save(post);
    }
}
