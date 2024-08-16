package io.github.burukeyou.dataframe.iframe.function;

import java.util.Objects;

/**
 * @author                  caizhihao
 * @param <T>
 */
@FunctionalInterface
public interface ConsumerPrevious<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param preElement     the pre iteration element
     * @param curElement     the current iteration element
     */
    void accept(T preElement, T curElement);

    /**
     * Returns a composed {@code ConsumerIndex} that performs, in sequence, this
     * operation followed by the {@code after} operation. If performing either
     * operation throws an exception, it is relayed to the caller of the
     * composed operation.  If performing this operation throws an exception,
     * the {@code after} operation will not be performed.
     *
     * @param after the operation to perform after this operation
     * @return a composed {@code ConsumerIndex} that performs in sequence this
     * operation followed by the {@code after} operation
     * @throws NullPointerException if {@code after} is null
     */
    default ConsumerPrevious<T> andThen(ConsumerPrevious<? super T> after) {
        Objects.requireNonNull(after);
        return (preElement,curElement) -> { accept(preElement,curElement); after.accept(preElement,curElement); };
    }

}
