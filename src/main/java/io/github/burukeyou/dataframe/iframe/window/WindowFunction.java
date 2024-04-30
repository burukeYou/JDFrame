package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.param.BaseWindowParam;

import java.util.List;

/**
 * WindowFunction
 *
 * @author  caizhihao
 */
public interface WindowFunction {

    <T,V> List<FI2<T, V>> execute(BaseWindowParam<T,V> param);

}
