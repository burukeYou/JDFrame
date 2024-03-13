package io.github.burukeyou.dataframe.iframe.support;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

public class DefaultJoin<T,K,R> implements Join<T,K,R> {

    @Override
    public R join(T t, K k) {
        try {
            ParameterizedType parameterizedType = (ParameterizedType)this.getClass().getGenericSuperclass();
            Class<R> rClass = (Class<R>) parameterizedType.getActualTypeArguments()[2];
            R r = rClass.newInstance();
            for (Field field : r.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object fieldValue = null;
                if (t != null){
                    Field tFeild = t.getClass().getDeclaredField(fieldName);
                    if (tFeild != null){
                        tFeild.setAccessible(true);
                        fieldValue = tFeild.get(t);
                    }
                }
                if (k != null && fieldValue == null){
                    Field kFeild = k.getClass().getDeclaredField(fieldName);
                    if (kFeild != null){
                        kFeild.setAccessible(true);
                        fieldValue = kFeild.get(t);
                    }
                }
                field.set(r,fieldValue);
            }
            return r;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
}
