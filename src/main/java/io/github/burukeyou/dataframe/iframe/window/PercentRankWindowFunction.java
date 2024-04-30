package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.param.BaseWindowParam;
import io.github.burukeyou.dataframe.util.MathUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PercentRankWindowFunction extends AbstractWindowFunction {

    @Override
    public  <T,V> List<FI2<T,V>> doFunction(BaseWindowParam<T,V> param) {
        List<T> windowList = param.getWindowList();
        Class<V> resultType = param.getResultType();
        Comparator<T> comparator = param.getComparator();
        // (rank-1) / (rows-1)
        List<FI2<T, V>> result = new ArrayList<>();
        int n = windowList.size();
        int rank = 1;
        result.add(new FI2<>(windowList.get(0), resultType.cast(new BigDecimal("0.00"))));
        for (int i = 1; i < windowList.size(); i++) {
            T pre = windowList.get(i-1);
            T cur = windowList.get(i);
            if (comparator.compare(pre,cur) != 0){
                rank = i + 1;
            }
            if (rank <= n){
                BigDecimal divide = MathUtils.divide((rank - 1), windowList.size() - 1, 2);
                result.add(new FI2<>(cur, resultType.cast(divide)));
            }else {
                break;
            }
        }
        return result;
    }
}
