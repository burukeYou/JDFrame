package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.SupplierFunction;
import io.github.burukeyou.dataframe.iframe.window.Window;
import io.github.burukeyou.dataframe.util.ListUtils;
import io.github.burukeyou.dataframe.util.MathUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author  caizhihao
 * @param <T>
 */
public abstract class AbstractWindowDataFrame<T> extends AbstractCommonFrame<T>{

    protected Window<T> window;

    public void setWindow(Window<T> window) {
        this.window = window;
    }

    protected  <V> List<FI2<T, V>> overAbject(Window<T> overParam,
                                              SupplierFunction<T,V> supplier) {
        List<T> windowList = toLists();
        List<FI2<T, V>> result = new ArrayList<>();
        if (ListUtils.isEmpty(windowList)){
            return result;
        }

        Comparator<T> comparator = overParam.getComparator();
        List<Function<T,?>> partitionList = overParam.partitions();
        if (ListUtils.isEmpty(partitionList)){
            if (comparator != null){
                windowList.sort(comparator);
            }
            return supplier.get(windowList);
        }

        // 获取每个窗口
        List<List<T>> allWindowList = new ArrayList<>();
        dfsFindWindow(allWindowList,windowList,partitionList,0);

        for (List<T> window : allWindowList) {
            if (comparator != null){
                window.sort(comparator);
            }
            List<FI2<T, V>> tmpList = supplier.get(window);
            result.addAll(tmpList);
        }

        return result;
    }

    protected  void dfsFindWindow(List<List<T>> result,
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

    protected List<FI2<T, Integer>> windowFunctionForRowNumber(Window<T> overParam) {
        SupplierFunction<T,Integer> supplier = windowList -> {
            List<FI2<T, Integer>> result = new ArrayList<>();
            int index = 1;
            for (T t : windowList) {
                result.add(new FI2<>(t,index++));
            }
            return result;
        };
        return overAbject(overParam, supplier);
    }

    protected  List<FI2<T, Integer>> windowFunctionForRank(Window<T> overParam) {
        SupplierFunction<T,Integer> supplier = (windowList) -> {
            Comparator<T> comparator = overParam.getComparator();
            List<FI2<T, Integer>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            result.add(new FI2<>(windowList.get(0), 1));
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (comparator.compare(pre,cur) != 0){
                    rank = i + 1;
                }
                if (rank <= n){
                    result.add(new FI2<>(cur, rank));
                }else {
                    break;
                }
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected List<FI2<T, Integer>> windowFunctionForDenseRank(Window<T> overParam) {
        SupplierFunction<T,Integer> supplier = (windowList) -> {
            List<FI2<T, Integer>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            result.add(new FI2<>(windowList.get(0), 1));
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (overParam.getComparator().compare(pre,cur) != 0){
                    rank += 1;
                }
                if (rank <= n){
                    result.add(new FI2<>(cur, rank));
                }else {
                    break;
                }
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected  List<FI2<T, BigDecimal>> windowFunctionForPercentRank(Window<T> overParam) {
        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            // (rank-1) / (rows-1)
            List<FI2<T, BigDecimal>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            result.add(new FI2<>(windowList.get(0), new BigDecimal("0.00")));
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (overParam.getComparator().compare(pre,cur) != 0){
                    rank = i + 1;
                }
                if (rank <= n){
                    BigDecimal divide = MathUtils.divide((rank - 1), windowList.size() - 1, 2);
                    result.add(new FI2<>(cur, divide));
                }else {
                    break;
                }
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected  List<FI2<T, BigDecimal>> windowFunctionForCumeDist(Window<T> overParam) {
        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            List<FI2<T, Integer>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            Map<Integer,Integer> rankCountMap = new HashMap<>();
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (overParam.getComparator().compare(pre,cur) != 0){
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
            List<FI2<T, BigDecimal>> resultList = new ArrayList<>();
            result.forEach(e -> {
                Integer count = rankCountMap.get(e.getC2());
                BigDecimal divide = MathUtils.divide(count, windowList.size(), 2);
                resultList.add(new FI2<>(e.getC1(),divide));
            });
            return resultList;
        };

        return overAbject(overParam,supplier);
    }

    protected <F> List<FI2<T, F>> windowFunctionForLag(Window<T> overParam, Function<T, F> field, int n) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            List<FI2<T, F>> result = new ArrayList<>();
            for (int i = 0; i < windowList.size(); i++) {
                int preIndex = i - n;
                F value = null;
                if (preIndex >= 0 && preIndex < windowList.size()){
                    value = field.apply(windowList.get(preIndex));
                }
                result.add(new FI2<>(windowList.get(i),value));
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected <F> List<FI2<T, F>> windowFunctionForLead(Window<T> overParam, Function<T, F> field, int n) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            List<FI2<T, F>> result = new ArrayList<>();
            for (int i = 0; i < windowList.size(); i++) {
                int afterIndex = i + n;
                F value = null;
                if (afterIndex >= 0 && afterIndex < windowList.size()){
                    value = field.apply(windowList.get(afterIndex));
                }
                result.add(new FI2<>(windowList.get(i),value));
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected <F> List<FI2<T, F>> windowFunctionForNthValue(Window<T> overParam, Function<T, F> field, int n) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            int index;
            if (n == -1){
                index = windowList.size() - 1;
            }else {
                index = n - 1;
            }
            if (index >= 0 && index < windowList.size()){
                F value = field.apply( windowList.get(index));
                return windowList.stream().map(e -> new FI2<>(e, value)).collect(toList());
            }else {
                return windowList.stream().map(e -> new FI2<T,F>(e,null)).collect(toList());
            }
        };
        return overAbject(overParam,supplier);
    }

    protected <F> List<FI2<T, BigDecimal>> windowFunctionForSum(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            BigDecimal value = SDFrame.read(windowList).sum(field);
            return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
        };
        return overAbject(overParam,supplier);
    }

    protected <F> List<FI2<T, BigDecimal>> windowFunctionForAvg(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            BigDecimal value = SDFrame.read(windowList).avg(field);
            return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
        };
        return overAbject(overParam,supplier);
    }

    protected <F extends Comparable<? super F>>  List<FI2<T, F>>  windowFunctionForMaxValue(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            F value = SDFrame.read(windowList).maxValue(field);
            return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
        };
        return overAbject(overParam,supplier);
    }

    protected <F extends Comparable<? super F>> List<FI2<T, F>> windowFunctionForMinValue(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            F value = SDFrame.read(windowList).minValue(field);
            return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
        };
        return overAbject(overParam,supplier);
    }

    protected List<FI2<T, Integer>> windowFunctionForCount(Window<T> overParam) {
        SupplierFunction<T,Integer> supplier = (windowList) -> {
            int count = windowList.size();
            return windowList.stream().map(e -> new FI2<>(e,count)).collect(toList());
        };
        return overAbject(overParam,supplier);
    }


}
