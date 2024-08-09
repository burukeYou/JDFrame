package io.github.burukeyou.dataframe.iframe.function;

import java.io.Serializable;
import java.util.function.Function;

/**
 * support compare two different element
 *      support null value to compare , if null indicates lower priority
 *
 * @param <L>           left  element
 * @param <R>           right  element
 *
 * @author  caizhihao
 */
public interface CompareTwo<L,R> {

    /**
     * compare two different element
     * @param left         left  element
     * @param right        right   element
     */
    int compare(L left, R right);

    /**
     * Build JoinOn based on fields
     * @param leftField            left  element field
     * @param rightField           right element field
     */
    static <L,R, U extends Comparable<? super U>> CompareTwo<L,R> on(Function<L,U> leftField, Function<R,U> rightField){
        return (t,k) -> {{
            if (t == null && k == null){
                return 0;
            }
            if (t == null){
                return -1;
            }
            if (k == null){
                return 1;
            }
            U leftFieldValue = leftField.apply(t);
            U rightFieldValue = rightField.apply(k);

            if (leftFieldValue == null && rightFieldValue == null){
                return 0;
            }
            if (leftFieldValue == null){
                return -1;
            }
            if (rightFieldValue == null){
                return 1;
            }
            return leftFieldValue.compareTo(rightFieldValue);
        }};
    }

    /**
     * when equal compare other field
     * @param leftField             left element field
     * @param rightField            left  element field
     */
    default <U extends Comparable<? super U>> CompareTwo<L,R> thenOn(Function<L,U> leftField, Function<R,U> rightField){
        CompareTwo<L, R> other = on(leftField, rightField);
        return (CompareTwo<L,R> & Serializable) (c1, c2) -> {
            int res = compare(c1, c2);
            return (res != 0) ? res : other.compare(c1, c2);
        };
    }

    /**
     * when equal compare other field
     * @param other             other compare
     */
    default CompareTwo<L,R> thenCompare(CompareTwo<L,R> other) {
        return (CompareTwo<L,R> & Serializable) (c1, c2) -> {
            int res = compare(c1, c2);
            return (res != 0) ? res : other.compare(c1, c2);
        };
    }
}
