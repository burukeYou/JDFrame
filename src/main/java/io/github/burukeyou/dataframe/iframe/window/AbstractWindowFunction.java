package io.github.burukeyou.dataframe.iframe.window;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.param.BaseWindowParam;
import io.github.burukeyou.dataframe.util.ListUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

public abstract class AbstractWindowFunction implements WindowFunction {


    @Override
    public <T, V> List<FI2<T, V>> execute(BaseWindowParam<T, V> param) {
        List<T> windowList = param.getWindowList();
        List<FI2<T, V>> result = new ArrayList<>();
        if (ListUtils.isEmpty(windowList)){
            return result;
        }

        List<Function<T,?>> partitionList = param.getPartitionBy();
        if (ListUtils.isEmpty(partitionList)){
            return doFunction(param);
        }

        // 获取每个窗口
        List<List<T>> allWindowList = new ArrayList<>();
        dfsFindWindow(allWindowList,windowList,partitionList,0);

        for (List<T> window : allWindowList) {
            param.setWindowList(window);
            List<FI2<T, V>> tmpList = doFunction(param);
            param.setWindowList(null);
            result.addAll(tmpList);
        }

        return result;
    }

    protected  <T> void dfsFindWindow(List<List<T>> result,
                                  List<T> windowList,
                                  List<Function<T, ?>> partitionList,
                                  int index){
        if (index >= partitionList.size()){
            result.add(windowList);
            return;
        }
        Function<T,?> partitionBy = partitionList.get(index);
        Map<?,List<T>> collect = windowList.stream().collect(groupingBy(partitionBy));
        for (List<T> window : collect.values()) {
            dfsFindWindow(result,window,partitionList,index+1);
        }
    }

    abstract <T,V> List<FI2<T, V>> doFunction(BaseWindowParam<T,V> param);


}
