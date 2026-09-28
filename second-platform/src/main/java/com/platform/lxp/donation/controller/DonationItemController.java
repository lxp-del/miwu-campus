package com.platform.lxp.donation.controller;

import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.donation.entity.DonationItem;
import com.platform.lxp.donation.service.DonationItemService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
public class DonationItemController {

    @Resource
    private DonationItemService donationItemService;

    /**
     * 分页获取捐赠物品列表
     */
    @GetMapping
    public ResponseResult getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String campus,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String status
    ) {
        Map<String, Object> params = new java.util.HashMap<>();
        params.put("category", category);
        params.put("campus", campus);
        params.put("keyword", keyword);
        params.put("status", status);

        Map<String, Object> result = donationItemService.getList(page, pageSize, params);
        return ResponseResult.success(result);
    }

    /**
     * 获取单条捐赠物品详情
     */
    @GetMapping("/{id}")
    public ResponseResult getById(@PathVariable Long id) {
        DonationItem item = donationItemService.getById(id);
        if (item == null) {
            return ResponseResult.error("物品不存在");
        }
        return ResponseResult.success(item);
    }

    /**
     * 新增捐赠物品
     */
    @PostMapping
    public ResponseResult create(@RequestBody DonationItem donationItem) {
        DonationItem createdItem = donationItemService.create(donationItem);
        return ResponseResult.success(createdItem);
    }

    /**
     * 更新捐赠物品
     */
    @PutMapping("/{id}")
    public ResponseResult update(@PathVariable Long id, @RequestBody DonationItem donationItem) {
        donationItem.setId(id);
        DonationItem updatedItem = donationItemService.update(donationItem);
        return ResponseResult.success(updatedItem);
    }

    /**
     * 删除捐赠物品
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        donationItemService.delete(id);
        return ResponseResult.success();
    }
}