package io.github.burukeyou.dataframe.iframe.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DataFrame （3 x 3 矩阵）
 * @param <T1>
 * @param <T2>
 * @param <T3>
 *
 * @author caizhihao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FI3<T1,T2,T3> implements FItem {

    private T1 c1;
    private T2 c2;
    private T3 c3;

    public T1 C1() {
        return c1;
    }

    public T2 C2() {
        return c2;
    }

    public T3 C3() {
        return c3;
    }
}
