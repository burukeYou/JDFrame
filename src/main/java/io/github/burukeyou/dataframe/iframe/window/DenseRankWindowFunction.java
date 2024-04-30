package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.param.BaseWindowParam;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DenseRankWindowFunction extends AbstractWindowFunction {

    @Override
    public  <T,V> List<FI2<T,V>> doFunction(BaseWindowParam<T,V> param) {
        List<T> windowList = param.getWindowList();
        Class<V> resultType = param.getResultType();
        Comparator<T> comparator = param.getComparator();

        List<FI2<T, V>> result = new ArrayList<>();
        int n = windowList.size();
        int rank = 1;
        result.add(new FI2<>(windowList.get(0), resultType.cast(1)));
        for (int i = 1; i < windowList.size(); i++) {
            T pre = windowList.get(i-1);
            T cur = windowList.get(i);
            if (comparator.compare(pre,cur) != 0){
                rank += 1;
            }
            if (rank <= n){
                result.add(new FI2<>(cur, resultType.cast(rank)));
            }else {
                break;
            }
        }

        return null;
    }
}
