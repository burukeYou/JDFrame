package io.github.burukeyou.dataframe.iframe;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IWhereJDFrame<T> extends IWhereFrame<T> {

    /**
     * filter by predicate
     * @param predicate         the predicate
     */
    JDFrame<T> where(Predicate<? super T> predicate);

    /**
     * Filter field values that are null, If it is string compatible, null and '' situations
     * @param function      the filter field
     * @param <R>           the filter field type
     */
    <R> JDFrame<T> whereNull(Function<T, R> function);

    /**
     * Filter field values that are not null,If it is string compatible, null and '' situations
     * @param function      the filter field
     * @param <R>           the filter field type
     */
    <R> JDFrame<T> whereNotNull(Function<T, R> function);

    /**
     * Screening within the interval,front closed and back closed.  [start,end]
     *             [start,end]
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> JDFrame<T> whereBetween(Function<T, R> function, R start, R end);

    /**
     * Screening within the interval , front open and back open  (start,end)
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> JDFrame<T> whereBetweenN(Function<T, R> function, R start, R end);

    /**
     * Screening within the interval , front open and back close  (start,end]
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> JDFrame<T> whereBetweenR(Function<T, R> function, R start, R end);

    /**
     * Screening within the interval , front close and back open  [start,end)
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> JDFrame<T> whereBetweenL(Function<T, R> function, R start, R end);


    /**
     * Out of range screening, (front closed and back closed)  [start,end]
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> JDFrame<T> whereNotBetween(Function<T, R> function, R start, R end);

    /**
     * Out of range screening, (front open and back open)  (start,end)
     * @param function          the filter field
     * @param start             start value
     * @param end               end value
     */
    <R extends Comparable<R>> JDFrame<T> whereNotBetweenN(Function<T, R> function, R start, R end);

    /**
     * The query value is within the specified range
     * @param function          the filter field
     * @param list              specified range
     */
    <R> JDFrame<T> whereIn(Function<T, R> function, List<R> list);

    /**
     * The query value is outside the specified range
     * @param function          the filter field
     * @param list              specified range
     */
    <R> JDFrame<T> whereNotIn(Function<T, R> function, List<R> list);

    /**
     * filter true by predicate
     */
    JDFrame<T> whereTrue(Predicate<T> predicate);

    /**
     * filter not true by predicate
     */
    JDFrame<T> whereNotTrue(Predicate<T> predicate);

    /**
     * Filter equals
     * @param function      the field
     * @param value         need value
     */
    <R> JDFrame<T> whereEq(Function<T, R> function, R value);

    /**
     * Filter not equals
     * @param function      the field
     * @param value         not need value
     */
    <R> JDFrame<T> whereNotEq(Function<T, R> function, R value);

    /**
     * Filter Greater than value
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> JDFrame<T> whereGt(Function<T, R> function, R value);

    /**
     * Filter Greater than or equal to
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> JDFrame<T> whereGe(Function<T, R> function, R value);

    /**
     * Filter LESS than value
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> JDFrame<T> whereLt(Function<T, R> function, R value);

    /**
     * Filter less than or equal to
     * @param function      the field
     * @param value         not need value
     */
    <R extends Comparable<R>> JDFrame<T> whereLe(Function<T, R> function, R value);

    /**
     * Fuzzy query contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> JDFrame<T> whereLike(Function<T, R> function, R value);

    /**
     * Fuzzy query not contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> JDFrame<T> whereNotLike(Function<T, R> function, R value);

    /**
     * prefix fuzzy query  contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> JDFrame<T> whereLikeLeft(Function<T, R> function, R value);

    /**
     * suffix fuzzy query  contains specified values
     * @param function              the field
     * @param value                 query value
     */
    <R> JDFrame<T> whereLikeRight(Function<T, R> function, R value);

    /**
     * Tests if the  field value starts with the specified prefix.
     * @param function              the field
     * @param value                 specified prefix
     */
    <R> JDFrame<T> whereStartsWith(Function<T, R> function, R value);

    /**
     * Tests if the  field value starts with the specified suffix.
     * @param function              the field
     * @param value                 specified suffix
     */
    <R> JDFrame<T> whereEndsWith(Function<T, R> function, R value);
}
