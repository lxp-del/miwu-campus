package com.platform.lxp.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.platform.lxp.admin.local.UserContext;
import com.platform.lxp.common.ResponseResult;
import com.platform.lxp.goods.entity.dto.AddressDTO;
import com.platform.lxp.goods.entity.pojo.Address;
import com.platform.lxp.goods.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 来晓璞
 * @date 2026/4/7 18:26
 * @Description:
 */
@RestController
@RequestMapping("/api/address")
public class AddressController {

    @Resource
    private AddressService addressService;

    /**
     * 新增地址
     */
    @PostMapping("/add")
    public ResponseResult add(@RequestBody AddressDTO dto) {
        Address address = new Address();
        BeanUtils.copyProperties(dto, address);

        address.setCreateUser(UserContext.getCurrentUser().getId().toString());
        addressService.save(address);
        return ResponseResult.success();
    }

    /**
     * 修改地址
     */
    @PostMapping("/update")
    public ResponseResult update(@RequestBody AddressDTO dto) {
        Address address = new Address();
        BeanUtils.copyProperties(dto, address);
        addressService.updateById(address);
        return ResponseResult.success();
    }

    /**
     * 删除地址
     */
    @GetMapping("/delete")
    public ResponseResult delete(@RequestParam Integer id) {
        addressService.removeById(id);
        return ResponseResult.success();
    }

    @GetMapping("/moren/{id}")
    public ResponseResult moren(@PathVariable("id") Integer id) {
        LambdaUpdateWrapper<Address> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Address::getIsDefault, 0);
        addressService.update(updateWrapper);
        Address address = addressService.getById(id);
        address.setIsDefault(1);
        addressService.updateById(address);
        return ResponseResult.success();
    }

    /**
     * 获取地址详情
     */
    @GetMapping("/info")
    public ResponseResult info(@RequestParam Integer id) {
        Address address = addressService.getById(id);
        return ResponseResult.success(address);
    }

    /**
     * 获取地址列表
     */
    @GetMapping("/list")
    public ResponseResult list(AddressDTO dto) {
        LambdaQueryWrapper<Address> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(Address::getCreateUser, UserContext.getCurrentUser().getId().toString());
        // 按创建时间倒序排列
        wrapper.orderByDesc(Address::getCreateTime);

        List<Address> addressList = addressService.list(wrapper);
        return ResponseResult.success(addressList);
    }


}
