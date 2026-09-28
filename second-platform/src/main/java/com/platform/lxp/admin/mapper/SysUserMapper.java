package com.platform.lxp.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.lxp.admin.entity.pojo.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 来晓璞
 * @date 2026/3/30 18:23
 * @Description: 系统用户-Mapper
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    SysUser selectById(Long id);

}
