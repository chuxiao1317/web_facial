package com.chuxiao.web_facial.util;

import java.math.BigDecimal;

public class BigCompare<T> {

    public int compare(T from, T to) {
        //返回-1、0、1——返回1代表左边大，返回0代表相等，返回-1代表右边大
        int res = new BigDecimal(from.toString()).compareTo(new BigDecimal(to.toString()));
        return res < 0 ? -1 : (res == 0 ? 0 : 1);
    }
}
