package com.platform.lxp.admin.controller;

import cn.hutool.core.util.ObjectUtil;
import com.platform.lxp.admin.entity.dto.SysUserDTO;
import com.platform.lxp.admin.entity.dto.WxLoginDTO;
import com.platform.lxp.admin.entity.pojo.SysUser;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.admin.service.ISysUserService;
import com.platform.lxp.common.Constants.MessageConstant;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.common.exception.BusinessException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author 来晓璞
 * @date 2026/3/30 18:28
 * @Description: 系统用户-Controlller
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    @Resource
    private ImageToAIController aiService;

    @Resource
    private ISysUserService sysUserService;

    @Resource
    private ImageToAIController imageToAIController;

    @ApiOperation("获取当前登录用户个人信息")
    @GetMapping("/userinfo")
    public ResponseResult getCurrentUser() {
        SysUser currentUser = UserContext.getCurrentUser();
        if (ObjectUtils.isEmpty(currentUser)) {
            throw new BusinessException("当前未登录，请重新登录");
        }
        return ResponseResult.success(currentUser);
    }


    @ApiOperation("获取用户列表信息")
    @GetMapping("/list")
    public ResponseResult findAll(@Valid SysUserDTO dto) {
        List<SysUser> sysUsers = sysUserService.selectList(dto);
        return ResponseResult.success(sysUsers);
    }


    /**
     * 拍照登录入口
     */
    @ApiOperation("学生拍照登录")
    @PostMapping("/login")
    public SysUser login(@RequestParam("image") MultipartFile file) throws Exception {

        // 1. AI识别姓名、学号
        String[] info = aiService.recognizeImage(file.getBytes());
        String name = info[0];
        String studentId = info[1];

        // 2. 学生登录（完全独立的业务类）
        return sysUserService.login(name, studentId);
    }

    @ApiOperation("AI识别商品 二手估价")
    @PostMapping("/estimate-price")
    public ResponseResult estimatePrice(@RequestParam("image") MultipartFile file) throws Exception {
        String price = imageToAIController.estimateGoodsPrice(file.getBytes());
        return ResponseResult.success(price);
    }

    @ApiOperation("修改用户信息")
    @PutMapping("/update")
    public ResponseResult update(@Valid @RequestBody SysUserDTO dto) {
        sysUserService.update(dto);
        return ResponseResult.success();
    }

    @ApiOperation("退出登录")
    @PostMapping("/logout")
    public ResponseResult logout() {
        return ResponseResult.success(MessageConstant.LOGOUT_SUCCESS);
    }

    @ApiOperation("微信登录")
    @PostMapping("/wx/login")
    public ResponseResult wxLogin(@RequestBody WxLoginDTO dto) {
        return ResponseResult.success(sysUserService.wxLogin(dto));
    }

    @ApiOperation("根据用户id获取详情数据")
    @GetMapping("/getById/{id}")
    public ResponseResult findById(@PathVariable("id") Long id) {
        return ResponseResult.success(sysUserService.getById(id));
    }
}
