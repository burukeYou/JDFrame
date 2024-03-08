package io.github.burukeyou.dataframe.dataframe.support;

/**
 * @author  caizhihao
 * @param <T>
 * @param <K>
 * @param <R>
 */
public interface Join<T,K,R> {

    /**
     *
     * @param t     如果是右连接可能为null
     * @param k     如果是左连接可能为null
     * @return
     */
    R join(T t, K k);
}
