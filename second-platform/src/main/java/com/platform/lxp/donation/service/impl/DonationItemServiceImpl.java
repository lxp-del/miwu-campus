package com.platform.lxp.donation.service.impl;

import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.donation.entity.DonationItem;
import com.platform.lxp.donation.mapper.DonationItemMapper;
import com.platform.lxp.donation.service.DonationItemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DonationItemServiceImpl implements DonationItemService {

    @Resource
    private DonationItemMapper donationItemMapper;

    @Override
    public Map<String, Object> getList(int page, int pageSize, Map<String, Object> params) {
        int offset = (page - 1) * pageSize;
        List<DonationItem> items = donationItemMapper.selectList(params, offset, pageSize);
        int total = donationItemMapper.selectCount(params);

        Map<String, Object> result = new HashMap<>();
        result.put("items", items);
        result.put("total", total);
        result.put("page", page);
        result.put("pageSize", pageSize);
        result.put("totalPages", (total + pageSize - 1) / pageSize);

        return result;
    }

    @Override
    public DonationItem getById(Long id) {
        return donationItemMapper.selectById(id);
    }

    @Override
    public DonationItem create(DonationItem donationItem) {
        // 设置默认值
        if (donationItem.getPrice() == null) {
            donationItem.setPrice(java.math.BigDecimal.ZERO);
        }
        if (donationItem.getStatus() == null) {
            donationItem.setStatus("available");
        }

         String name = UserContext.getCurrentUser().getName();
        String avatar = UserContext.getCurrentUser().getAvatar();
        donationItem.setUserAvatar(avatar);
        donationItem.setUserName(name);


        donationItemMapper.insert(donationItem);
        return donationItem;
    }

    @Override
    public DonationItem update(DonationItem donationItem) {
        donationItemMapper.update(donationItem);
        return donationItem;
    }

    @Override
    public void delete(Long id) {
        donationItemMapper.deleteById(id);
    }
}
