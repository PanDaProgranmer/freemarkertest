/*
 * @(#) IEnum
 * 版权声明 厦门畅享信息技术有限公司, 版权所有 违者必究
 *
 * <br> Copyright:  Copyright (c) 2017
 * <br> Company:厦门畅享信息技术有限公司
 * <br> @author panyz
 * <br> 2017-11-22 10:29:33
 */

package com.example.demo.enums;

/**
 *
 * @author 吴宸勖
 */
public interface IEnum {
    String getValue();

    default boolean eq(String value) {
        return getValue().equals(value);
    }

    static <E extends Enum<E> & IEnum> E getEnum(Class<E> clazz, String value) {
        for (E e : clazz.getEnumConstants()) {
            if (e.eq(value)) {
                return e;
            }
        }
        return null;
    }

    static <E extends Enum<E> & IEnum> E getEnum(Class<E> clazz, String value, E def) {
        E ret = getEnum(clazz, value);
        return ret == null ? def : ret;
    }

    default boolean in(IEnum... enums) {
        for (IEnum e : enums) {
            if (e.equals(this)) {
                return true;
            }
        }
        return false;
    }

    static boolean in(String value, IEnum... enums) {
        for (IEnum e : enums) {
            if (e.eq(value)) {
                return true;
            }
        }
        return false;
    }

    default boolean notIn(IEnum... enums) {
        return !in(enums);
    }

    static boolean notIn(String value, IEnum... enums) {
        return !in(value, enums);
    }
}