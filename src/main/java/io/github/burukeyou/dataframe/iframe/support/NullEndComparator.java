package io.github.burukeyou.dataframe.iframe.support;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * support null value to compare.  if values is null sort to the end
 *
 * @author  caizhihao
 * @param <T>
 */
public interface NullEndComparator<T> extends Comparator<T> {

    /**
     *  simplify build  Comparator by keyExtractor
     */
    static <T, U extends Comparable<? super U>> Comparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (t1, t2) -> {
            if (t1 == null && t2 == null){
                return 0;
            }
            if (t1 == null){
                return -1;
            }
            if (t2 == null){
                return 1;
            }
            U t1Value = keyExtractor.apply(t1);
            U t2Value = keyExtractor.apply(t2);
            if (t1Value == null && t2Value == null){
                return 0;
            }
            if (t1Value == null){
                return -1;
            }
            if (t2Value == null){
                return 1;
            }
            return t1Value.compareTo(t2Value);
        };
    }

}
