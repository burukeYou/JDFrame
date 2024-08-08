package io.github.burukeyou.dataframe.iframe.function;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

/**
 * support null value to compare.
 *          if values is null sort to the end in sort desc
 *          if values is null sort to the start in sort asc
 *
 * @author  caizhihao
 * @param <T>
 */
public interface NullComparator<T> extends Comparator<T> {

    int nulCompare(T left,T right);

    @Override
    default int compare(T left, T right){
        if (left == null && right == null){
            return 0;
        }
        if (left == null){
            return -1;
        }
        if (right == null){
            return 1;
        }
        return nulCompare(left,right);
    }

    /**
     *  simplify build  Comparator by keyExtractor
     */
    static <T, U extends Comparable<? super U>> NullComparator<T> comparing(Function<? super T, ? extends U> keyExtractor) {
        Objects.requireNonNull(keyExtractor);
        return (t1, t2) -> {
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

    static <T, U> NullComparator<T> comparing(
            Function<? super T, ? extends U> keyExtractor,
            Comparator<? super U> keyComparator) {
        Objects.requireNonNull(keyExtractor);
        Objects.requireNonNull(keyComparator);
        return (NullComparator<T> & Serializable)
                (c1, c2) -> keyComparator.compare(keyExtractor.apply(c1),
                        keyExtractor.apply(c2));
    }


    @Override
    default NullComparator<T> thenComparing(Comparator<? super T> other) {
        Objects.requireNonNull(other);
        return (NullComparator<T> & Serializable) (c1, c2) -> {
            int res = compare(c1, c2);
            return (res != 0) ? res : other.compare(c1, c2);
        };
    }


    @Override
    default <U> NullComparator<T> thenComparing(Function<? super T, ? extends U> keyExtractor, Comparator<? super U> keyComparator) {
        return thenComparing(comparing(keyExtractor, keyComparator));
    }

    @Override
    default <U extends Comparable<? super U>> NullComparator<T> thenComparing(Function<? super T, ? extends U> keyExtractor) {
        return thenComparing(comparing(keyExtractor));
    }

}
