package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.param.BaseWindowParam;

import java.util.Comparator;
import java.util.List;

public class LeadWindowFunction extends AbstractWindowFunction {

    @Override
    public  <T,V> List<FI2<T,V>> doFunction(BaseWindowParam<T,V> param) {
        List<T> windowList = param.getWindowList();
        Class<V> resultType = param.getResultType();
        Comparator<T> comparator = param.getComparator();

        return null;
    }
}
