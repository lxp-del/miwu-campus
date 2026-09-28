package com.platform.lxp.donation.mapper;

import com.platform.lxp.donation.entity.DonationItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DonationItemMapper {
    /**
     * 分页查询捐赠物品列表
     */
    List<DonationItem> selectList(@Param("params") Map<String, Object> params, @Param("offset") int offset, @Param("limit") int limit);

    /**
     * 查询捐赠物品总数
     */
    int selectCount(@Param("params") Map<String, Object> params);

    /**
     * 根据ID查询捐赠物品
     */
    DonationItem selectById(@Param("id") Long id);

    /**
     * 插入捐赠物品
     */
    int insert(DonationItem donationItem);

    /**
     * 更新捐赠物品
     */
    int update(DonationItem donationItem);

    /**
     * 删除捐赠物品
     */
    int deleteById(@Param("id") Long id);
}