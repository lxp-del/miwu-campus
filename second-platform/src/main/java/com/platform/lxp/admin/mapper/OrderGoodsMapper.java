package com.platform.lxp.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.platform.lxp.admin.entity.pojo.OrderGoods;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author 来晓璞
 * @date 2026/3/31 20:12
 * @Description:
 */
@Mapper
public interface OrderGoodsMapper extends BaseMapper<OrderGoods> {

    @Select("SELECT * FROM order_goods WHERE id = #{id}")
    OrderGoods selectById(Long id);
}
