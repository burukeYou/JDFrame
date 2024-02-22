package io.github.burukeyou.dataframe.dataframe.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DataFrame （4 x 4 矩阵）
 * @param <T1>
 * @param <T2>
 * @param <T3>
 * @param <T4>
 *
 * @author caizhihao
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class FItem4<T1, T2, T3, T4>  {

    private T1 c1;
    private T2 c2;
    private T3 c3;
    private T4 c4;

}
