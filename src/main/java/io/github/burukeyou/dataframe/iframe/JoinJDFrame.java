package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.support.DefaultJoin;
import io.github.burukeyou.dataframe.iframe.support.Join;
import io.github.burukeyou.dataframe.iframe.support.JoinOn;
import io.github.burukeyou.dataframe.iframe.support.VoidJoin;


public interface JoinJDFrame<T> extends JoinIFrame<T> {


    /**
     * inner join Frame
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> join(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    /**
     * inner join Frame
     *      If successfully associated with other Frame record, it will only be associated once
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> joinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);


    /**
     * inner join Frame
     *      such as {@link IFrame#join(IFrame, JoinOn, Join)}, but the default Join is {@link DefaultJoin},
     *      it will automatically map to a new Frame based on the same name
     * @param other         other frame
     * @param on            connection conditions
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> join(IFrame<K> other, JoinOn<T,K> on);


    /**
     * just Execute inner join operation， will not change the data of the frame
     * @param other         other frame
     * @param on            connection conditions
     * @param <K>           other Frame type
     */
    <K> JDFrame<T> joinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);


    /**
     *  just Execute inner join operation， will not change the data of the frame
     *      If successfully associated with other Frame record, it will only be associated once
     * @param other         other frame
     * @param on            connection conditions
     * @param <K>           other Frame type
     */
    <K> JDFrame<T> joinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);

    /**
     * left join Frame
     *      if connection conditions false, The callback value K for Join will be null， always keep T
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    /**
     * left join Frame
     *      if connection conditions false, The callback value K for Join will be null， always keep T
     *      If successfully associated with other Frame record, it will only be associated once
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> leftJoinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    /**
     * left join Frame
     *        such as {@link IFrame#leftJoin(IFrame, JoinOn, Join)}, but the default Join is {@link DefaultJoin},
     * @param other         other frame
     * @param on            connection conditions
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> leftJoin(IFrame<K> other, JoinOn<T,K> on);

    /**
     *  just Execute left join operation， will not change the data of the frame
     *      if connection conditions false, The callback value K for Join will be null， always keep T
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <K>           other Frame type
     */
    <K> JDFrame<T> leftJoinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);


    /**
     *  just Execute left join operation， will not change the data of the frame
     *      if connection conditions false, The callback value K for Join will be null， always keep T
     *      If successfully associated with other Frame record, it will only be associated once
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <K>           other Frame type
     */
    <K> JDFrame<T> leftJoinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);

    /**
     * right join Frame
     *      if connection conditions false, The callback value T for Join will be null， always keep K
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);

    /**
     * right join Frame
     *      if connection conditions false, The callback value T for Join will be null， always keep K
     *      If successfully associated with other Frame record, it will only be associated once
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> rightJoinOnce(IFrame<K> other, JoinOn<T,K> on, Join<T,K,R> join);


    /**
     * right join Frame
     *        such as {@link IFrame#rightJoin(IFrame, JoinOn, Join)}, but the default Join is {@link DefaultJoin},
     * @param other         other frame
     * @param on            connection conditions
     * @param <R>           new Frame type
     * @param <K>           other Frame type
     */
    <R,K> JDFrame<R> rightJoin(IFrame<K> other, JoinOn<T,K> on);

    /**
     *  just Execute right join operation， will not change the data of the frame
     *      if connection conditions false, The callback value T for Join will be null， always keep K
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <K>           other Frame type
     */
    <K> JDFrame<T> rightJoinVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);

    /**
     *  just Execute right join operation， will not change the data of the frame
     *      if connection conditions false, The callback value T for Join will be null， always keep K
     *      If successfully associated with other Frame record, it will only be associated once
     * @param other         other frame
     * @param on            connection conditions
     * @param join          Connection logic
     * @param <K>           other Frame type
     */
    <K> JDFrame<T> rightJoinOnceVoid(IFrame<K> other, JoinOn<T,K> on, VoidJoin<T,K> join);


}
