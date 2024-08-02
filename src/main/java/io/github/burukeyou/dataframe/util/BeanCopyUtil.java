package io.github.burukeyou.dataframe.util;

import com.alibaba.fastjson.JSON;

public class BeanCopyUtil {

    private BeanCopyUtil(){}

    public static <T> T copyProperties(Object source,Class<T> targetClass)  {
        if (source == null){
            return null;
        }
        T t = JSON.parseObject(JSON.toJSONString(source), targetClass);
        return t;
    }

}
