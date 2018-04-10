/**
 * @(#)RegisterCheckItem.java 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 * ht:  Copyright (c) 2017
 * <br> Company:厦门畅享信息技术有限公司
 * <br> @author 吴宸勖
 * <br> 2017/3/13 19:54
 */

package com.example.demo.enums;

import lombok.Getter;

/**
 * 证件类型
 *
 * @author panyz
 */
@Getter
public enum TplType implements IEnum {

    HEAD("head", "二代身份证"),
    FOOTER("footer", "港澳通行证");


    public String value;

    public String name;

    TplType(String value, String name) {
        this.value = value;
        this.name = name;
    }
}

