package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.param.BaseWindowParam;

import java.util.ArrayList;
import java.util.List;

/**
 * @author caihzihao
 */
public class RowNumberWindowFunction extends AbstractWindowFunction {

    @Override
    public  <T,V> List<FI2<T,V>> doFunction(BaseWindowParam<T,V> param) {
        List<T> windowList = param.getWindowList();
        Class<V> resultType = param.getResultType();
        List<FI2<T, V>> result = new ArrayList<>();
        int index = 1;
        for (T t : windowList) {
            result.add(new FI2<>(t,resultType.cast(index++)));
        }
        return result;
    }
}
