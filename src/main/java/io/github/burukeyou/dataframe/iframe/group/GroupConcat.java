package io.github.burukeyou.dataframe.iframe.group;

import java.util.function.Function;

/**
 * Group Concat Param Builder
 *
 * @author          caizhihao
 * @param <T>
 */
public interface GroupConcat<T> {

    /**
     * build concat by delimiter param
     * @param concatField           concat value
     * @param delimiter             concat delimiter
     */
    static <T,V> GroupConcat<T> concatBy(Function<T,V> concatField,CharSequence delimiter){
       return new GroupConcatImpl<>(concatField,delimiter);
    }

    /**
     * build concat by prefix,delimiter ,suffix
     * @param concatField       concat value
     * @param delimiter         concat delimiter
     * @param prefix            concat prefix
     * @param suffix            concat suffix
     */
    static <T,V> GroupConcat<T> concatBy(Function<T,V> concatField,CharSequence delimiter,CharSequence prefix, CharSequence suffix){
        return new GroupConcatImpl<>(concatField,delimiter,prefix,suffix);
    }
}
