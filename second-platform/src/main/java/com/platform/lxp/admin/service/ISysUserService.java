package com.platform.lxp.admin.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.platform.lxp.admin.entity.dto.SysUserDTO;
import com.platform.lxp.admin.entity.dto.WxLoginDTO;
import com.platform.lxp.admin.entity.pojo.SysUser;
import com.platform.lxp.common.ResponseResult;

import javax.validation.Valid;
import java.util.List;

/**
 * @author 来晓璞
 * @date 2026/3/30 18:24
 * @Description:
 */
public interface ISysUserService extends IService<SysUser> {

    /**
     * 查询用户列表
     * @return
     */
    public List<SysUser> selectList(SysUserDTO dto);

    /**
     * 登录
     * @param name
     * @param studentId
     * @return
     */
    SysUser login(String name, String studentId);

    /**
     * 用户校验
     * @param token
     * @return
     */
    public SysUser validateToken(String token);

    void update(@Valid SysUserDTO dto);

    ResponseResult wxLogin(WxLoginDTO dto);
}
