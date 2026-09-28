package com.platform.lxp.goods.entity.pojo;




import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;
/**
 * @author 来晓璞
 * @date 2026/4/7 18:22
 * @Description:
 */
@Data
@TableName("t_address")
public class Address {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 收货人姓名
     */
    private String name;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 省市区/详细地址 (冗余字段或组合字段，视业务而定)
     */
    private String area;

    private String region;
    private String detail;
    private String tag;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String district;

    /**
     * 详细街道地址
     */
    private String detailAddress;

    /**
     * 是否默认地址: 0-否, 1-是
     */
    private Integer isDefault;
}
