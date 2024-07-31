package io.github.burukeyou.dataframe.util;

import java.lang.reflect.Field;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author caizhihao
 */
public class ClassUtil {

    private ClassUtil(){}


    /**
     * find all field of the class，even for the parent class
     * @param clazz         the target class to analyze
     */
    public static List<Field> findAllFiled(Class<?> clazz){
        List<Field> resultList = new ArrayList<>();
        Class<?> targetClass = clazz;
        do {
            Field[] fields = targetClass.getDeclaredFields();
            resultList.addAll(Arrays.asList(fields));
            targetClass = targetClass.getSuperclass();
        } while (targetClass != null && targetClass != Object.class);
        return resultList;
    }


}
