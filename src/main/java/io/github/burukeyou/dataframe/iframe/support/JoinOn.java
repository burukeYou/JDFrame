package io.github.burukeyou.dataframe.iframe.support;

import java.util.function.Function;

/**
 * Build Join On Condition
 *
 * @param <L>           left table element
 * @param <R>           right  table element
 *
 * @author  caizhiho
 */
public interface JoinOn<L,R> {

    /**
     * Determine whether the association is successful
     * @param left         left table element
     * @param right        right  table element
     */
    boolean on(L left, R right);

    /**
     * Build JoinOn based on fields
     * @param leftField            left table join on field
     * @param rightField           right table join on field
     */
    static <T,K,V1,V2> JoinOn<T,K> on(Function<T,V1> leftField, Function<K,V2> rightField){
        return (t,k) -> {{
            if (t == null || k == null){
                return false;
            }
            V1 leftFieldValue = leftField.apply(t);
            V2 rightFieldValue = rightField.apply(k);

            if (leftFieldValue == null && rightFieldValue == null){
                return true;
            }

            if (leftFieldValue == null){
                // field2Value is not null so return false
                return false;
            }
            return leftFieldValue.equals(rightFieldValue);
        }};
    }

    /**
     * Building  Multi field Join on
     * @param leftField             left table join on field
     * @param rightField            left table join on field
     */
    default <V1,V2> JoinOn<L,R> thenOn(Function<L,V1> leftField, Function<R,V2> rightField){
        return (t,k) -> on(t, k) && on(leftField, rightField).on(t, k);
    }

}
