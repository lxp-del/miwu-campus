package com.platform.lxp.goods.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.lxp.goods.entity.dto.PostAddDTO;
import com.platform.lxp.goods.entity.pojo.ForumPost;

/**
 * @author 来晓璞
 * @date 2026/4/6 12:52
 * @Description:
 */
public interface IForumPostService extends IService<ForumPost> {
    void addPost(PostAddDTO dto);
}
