package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.param.BaseWindowParam;
import io.github.burukeyou.dataframe.util.MathUtils;

import java.math.BigDecimal;
import java.util.*;

public class CumeDistWindowFunction extends AbstractWindowFunction {

    @Override
    public  <T,V> List<FI2<T,V>> doFunction(BaseWindowParam<T,V> param) {
        List<T> windowList = param.getWindowList();
        Class<V> resultType = param.getResultType();
        Comparator<T> comparator = param.getComparator();
        List<FI2<T, Integer>> result = new ArrayList<>();
        int n = windowList.size();
        int rank = 1;
        Map<Integer,Integer> rankCountMap = new HashMap<>();
        for (int i = 1; i < windowList.size(); i++) {
            T pre = windowList.get(i-1);
            T cur = windowList.get(i);
            if (comparator.compare(pre,cur) != 0){
                // 次数的rank累积的计数最大
                rankCountMap.put(rank,i);
                rank = i + 1;
            }
            if (rank <= n){
                result.add(new FI2<>(cur, rank));
            }else {
                break;
            }
        }

        // 最大排名
        rankCountMap.computeIfAbsent(rank, k -> windowList.size());

        List<FI2<T, V>> resultList = new ArrayList<>();
        result.forEach(e -> {
            Integer count = rankCountMap.get(e.getC2());
            BigDecimal divide = MathUtils.divide(count, windowList.size(), 2);
            resultList.add(new FI2<>(e.getC1(),resultType.cast(divide)));
        });

        return resultList;
    }
}
