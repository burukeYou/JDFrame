package io.github.burukeyou.dataframe.iframe.group;

import java.util.Comparator;
import java.util.function.Function;

/**
 * @author          caizhihao
 * @param <T>
 */
public interface GroupConcat<T> extends GroupWindow<T> {

   static <T> GroupConcat<T> concat(Function<T,?> concatField,CharSequence delimiter){
       return new GroupConcatImpl<>(concatField,delimiter);
   }

    /**
     * Sort windows in ascending order according to specified fields
     * @param sortField                 sort field
     */
    <U extends Comparable<? super U>> GroupConcat<T> sortAsc(Function<T,U> sortField);

    /**
     * Sort windows in descending order according to specified fields
     * @param sortField          sort field
     */
    <U extends Comparable<? super U>> GroupConcat<T> sortDesc(Function<T,U> sortField);

    /**
     *  sort window by  Comparator
     * @param comparator        sort comparator
     */
    GroupConcat<T> sort(Comparator<T> comparator);

}
