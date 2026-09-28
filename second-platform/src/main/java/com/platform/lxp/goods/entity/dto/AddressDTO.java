package com.platform.lxp.goods.entity.dto;



import lombok.Data;
import java.io.Serializable;
/**
 * @author 来晓璞
 * @date 2026/4/7 18:24
 * @Description:
 */

@Data
public class AddressDTO implements Serializable {

    private Integer id;

    private String name;

    private String phone;

    private String area;

    private String province;

    private String city;

    private String district;

    private String detailAddress;

    private Integer isDefault;
    private String region;
    private String detail;
    private String tag;
}
