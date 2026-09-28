package com.platform.lxp.donation.service;

import com.platform.lxp.donation.entity.DonationItem;

import java.util.List;
import java.util.Map;

public interface DonationItemService {
    /**
     * 分页获取捐赠物品列表
     */
    Map<String, Object> getList(int page, int pageSize, Map<String, Object> params);

    /**
     * 根据ID获取捐赠物品详情
     */
    DonationItem getById(Long id);

    /**
     * 新增捐赠物品
     */
    DonationItem create(DonationItem donationItem);

    /**
     * 更新捐赠物品
     */
    DonationItem update(DonationItem donationItem);

    /**
     * 删除捐赠物品
     */
    void delete(Long id);
}