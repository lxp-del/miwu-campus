package com.platform.lxp.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.platform.lxp.admin.entity.pojo.SysUser;

import com.platform.lxp.admin.mapper.SysUserMapper;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.goods.entity.dto.PostAddDTO;
import com.platform.lxp.goods.entity.pojo.ForumPost;
import com.platform.lxp.goods.entity.vo.ForumPostVO;
import com.platform.lxp.goods.service.IForumPostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 来晓璞
 * @date 2026/4/6 12:52
 * @Description:
 */
@Api(tags = "帖子管理接口")
@RestController
@RequestMapping("/forum/post")
public class ForumPostController {

    @Resource
    private IForumPostService forumPostService;

    @Resource
    private SysUserMapper sysUserMapper;

    @ApiOperation("发布新帖子")
    @PostMapping("/add")
    public ResponseResult add(@Validated @RequestBody PostAddDTO dto) {

        forumPostService.addPost(dto);
        return ResponseResult.success();
    }

    @ApiOperation("获取帖子详情")
    @GetMapping("/detail/{id}")
    public ResponseResult detail(@PathVariable Long id) {
        return ResponseResult.success(forumPostService.getById(id));
    }

    @ApiOperation("获取帖子列表")
    @GetMapping("/list")
    public ResponseResult list() {
        List<ForumPost> postList = forumPostService.list(
                new LambdaQueryWrapper<ForumPost>()
                        .orderByDesc(ForumPost::getCreateTime)
        );
         List<ForumPostVO> voList = postList.stream().map(item -> {
            ForumPostVO vo = new ForumPostVO();
             BeanUtils.copyProperties(item, vo);

              SysUser sysUser = sysUserMapper.selectById(item.getUserId());
            vo.setCreateSysUser(sysUser);
            vo.setLikes(10L);
            vo.setViews(201L);
            return vo;
        }).collect(Collectors.toList());
        return ResponseResult.success(voList);
    }
}
