package io.github.burukeyou.dataframe.iframe.function;

import java.util.Objects;

/**
 * @author                  caizhihao
 * @param <T>
 */
@FunctionalInterface
public interface ConsumerNext<T> {

    /**
     * Performs this operation on the given argument.
     *
     * @param curElement     the current iteration element
     * @param nextElement    the next iteration element
     */
    void accept(T curElement, T nextElement);

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
    default ConsumerNext<T> andThen(ConsumerNext<? super T> after) {
        Objects.requireNonNull(after);
        return (curElement,nextElement) -> { accept(curElement,nextElement); after.accept(curElement,nextElement); };
    }

}
