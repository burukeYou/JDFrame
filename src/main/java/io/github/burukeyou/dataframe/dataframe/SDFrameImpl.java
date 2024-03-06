package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.SDFrame;
import io.github.burukeyou.dataframe.dataframe.item.FT2;
import io.github.burukeyou.dataframe.dataframe.item.FT3;
import io.github.burukeyou.dataframe.dataframe.item.FT4;
import io.github.burukeyou.dataframe.util.CollectorsPlusUtil;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;


/**
 * @author caizhihao
 */
public class SDFrameImpl<T>  extends AbstractDataFrame<T> implements SDFrame<T> {

    protected Stream<T> data;

    public SDFrameImpl(List<T> list) {
        this.data = list.stream();
    }

    public <R> SDFrameImpl<R> read(List<R> list) {
        return new SDFrameImpl<>(list);
    }

    @Override
    public List<T> toLists() {
        return data.collect(toList());
    }

    @Override
    public  Stream<T> stream(){
        return data;
    }

    /**
     * ===========================   排序相关  =====================================
     **/

    @Override
    public SDFrameImpl<T> sortDesc(Comparator<T> comparator) {
        data = stream().sorted(comparator.reversed());
        return this;
    }

    @Override
    public <R extends Comparable<R>> SDFrameImpl<T> sortDesc(Function<T, R> function) {
        sortDesc(Comparator.comparing(function));
        return this;
    }

    @Override
    public SDFrameImpl<T> sortAsc(Comparator<T> comparator) {
        data = stream().sorted(comparator);
        return this;
    }

    @Override
    public <R extends Comparable<R>> SDFrameImpl<T> sortAsc(Function<T, R> function) {
        sortAsc(Comparator.comparing(function));
        return this;
    }


    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    public SDFrameImpl<T> first(int n) {
        DFList<T> first = new DFList<>(toLists()).first(n);
        data = first.build().stream();
        return this;
    }

    /**
     * ===========================   筛选相关  =====================================
     **/
    public <R> SDFrame<T> whereNotNull(Function<T, R> function) {
        return returnThis(whereNotNullStream(function));
    }

    public <R extends Comparable<R>> SDFrame<T> whereBetween(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        return returnThis(whereBetweenStream(function,start,end));
    }


    public <R extends Comparable<R>> SDFrame<T> whereBetweenR(Function<T, R> function, R start, R end) {
        // 筛选条件都不存在默认不筛选
        if (start == null && end == null) {
            return this;
        }
        return returnThis(whereBetweenRStream(function,start,end));
    }


    public <R extends Comparable<R>> SDFrame<T> whereNotBetween(Function<T, R> function, R start, R end) {
        if (start == null || end == null) {
            return this;
        }
        return returnThis(whereNotBetweenStream(function,start,end));
    }


    public <R> SDFrame<T> whereIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        return returnThis(whereInStream(function,list));
    }


    public <R> SDFrame<T> whereNotIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        return returnThis(whereNotInStream(function,list));
    }


    public SDFrame<T> whereTrue(Predicate<T> predicate) {
        return returnThis(stream().filter(predicate));
    }


    public SDFrame<T> whereNotTrue(Predicate<T> predicate) {
        return whereTrue(predicate.negate());
    }


    public <R> SDFrame<T> whereEq(Function<T, R> function, R value) {
        if (null == value) {
            return this;
        }
        return  returnThis(whereEqStream(function,value));
    }


    public <R> SDFrame<T> whereNotEq(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereNotEqStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereGt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereGtStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereGe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereGeStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereLt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLtStream(function,value));
    }


    public <R extends Comparable<R>> SDFrame<T> whereLe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLeStream(function,value));
    }


    public <R> SDFrame<T> whereLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLikeStream(function,value));
    }


    public <R> SDFrame<T> whereNotLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereNotLikeStream(function,value));
    }


    public <R> SDFrame<T> whereLikeLeft(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        return returnThis(whereLikeLeftStream(function,value));
    }


    public <R> SDFrame<T> whereLikeRight(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> String.valueOf(function.apply(e)).endsWith(String.valueOf(value)));
        return returnThis(stream);
    }



    /** ===========================   分组相关  ===================================== **/



    public <K> SDFrame<FT2<K, BigDecimal>> groupBySum(Function<T, K> K,
                                                     ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT2<K, BigDecimal>> collect = group(K, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J> SDFrame<FT3<K, J, BigDecimal>> groupBySum(Function<T, K> K,
                                                           Function<T, J> K2,
                                                           ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT3<K, J, BigDecimal>> collect = group(K, K2, tBigDecimalCollector);
        return returnDF(collect);
    }



    public <K, J, H> SDFrame<FT4<K, J, H, BigDecimal>> groupBySum(Function<T, K> K,
                                                                 Function<T, J> J,
                                                                 Function<T, H> H,
                                                                 ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT4<K, J, H, BigDecimal>> collect = group(K, J, H, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K> SDFrame<FT2<K, Long>> groupByCount(Function<T, K> K) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Long> collect = stream().collect(groupingBy(K, counting));
        return returnDF(convertToDataFrameItem2(collect));
    }


    public <K, J> SDFrame<FT3<K, J, Long>> groupByCount(Function<T, K> K,
                                                       Function<T, J> J) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Long>> collect = stream().collect(groupingBy(K, groupingBy(J, counting)));
        return returnDF(convertToDataFrameItem3(collect));
    }


    public <K, J, H> SDFrame<FT4<K, J, H, Long>> groupByCount(Function<T, K> K,
                                                             Function<T, J> J,
                                                             Function<T, H> H) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Map<H, Long>>> collect = stream().collect(groupingBy(K, groupingBy(J, groupingBy(H, counting))));
        return returnDF(convertToDataFrameItem4(collect));
    }


    public <K> SDFrame<FT3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> K, ToBigDecimalFunction<T> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT2<K, BigDecimal>> sumList = returnDF(dataList).group(K, tBigDecimalCollector);
        List<FT2<K, Long>> countList =  read(dataList).groupByCount(K).toLists();
        Map<K, Long> countMap = countList.stream().collect(toMap(FT2::getC1, FT2::getC2));
        List<FT3<K, BigDecimal, Long>> collect = sumList.stream().map(e -> new FT3<>(e.getC1(), e.getC2(), countMap.get(e.getC1()))).collect(Collectors.toList());
        return returnDF(collect);
    }


    public <K, J> SDFrame<FT4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> K,
                                                                      Function<T, J> J,
                                                                      ToBigDecimalFunction<T> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT3<K, J, BigDecimal>> sumList = returnDF(dataList).group(K, J, tBigDecimalCollector);
        List<FT3<K, J, Long>> countList =  read(dataList).groupByCount(K, J).toLists();
        // 合并sum和count字段
        Map<String, FT3<K, J, Long>> countMap = countList.stream().collect(toMap(e -> e.getC1() + "_" + e.getC2(), Function.identity()));
        List<FT4<K, J, BigDecimal, Long>> collect = sumList.stream().map(e -> {
            FT3<K, J, Long> countItem = countMap.get(e.getC1() + "_" + e.getC2());
            return new FT4<>(e.getC1(), e.getC2(), e.getC3(), countItem.getC3());
        }).collect(Collectors.toList());
        return returnDF(collect);
    }


    public <K> SDFrame<FT2<K, BigDecimal>> groupByAvg(Function<T, K> K,
                                                     ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FT2<K, BigDecimal>> collect = group(K, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J> SDFrame<FT3<K, J, BigDecimal>> groupByAvg(Function<T, K> K,
                                                           Function<T, J> J,
                                                           ToBigDecimalFunction<T> value) {

        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FT3<K, J, BigDecimal>> collect = group(K, J, tBigDecimalCollector);
        return returnDF(collect);
    }


    public <K, J, H> SDFrame<FT4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> K,
                                                                 Function<T, J> J,
                                                                 Function<T, H> H,
                                                                 ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FT4<K, J, H, BigDecimal>> collect = group(K, J, H, tBigDecimalCollector);
        return returnDF(collect);
    }



    public <K, V extends Comparable<V>> SDFrame<FT2<K, T>> groupByMax(Function<T, K> K,
                                                                     Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(K, collectingAndThen(toList(), e -> e.stream().min(Comparator.comparing(value)).orElse(null))));
        return returnDF(convertToDataFrameItem2(collect));
    }


    public <K, V extends Comparable<V>> SDFrame<FT2<K, T>> groupByMin(Function<T, K> K,
                                                                     Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(K, collectingAndThen(toList(), e -> e.stream().min(Comparator.comparing(value)).orElse(null))));
        return returnDF(convertToDataFrameItem2(collect));
    }


    public <K, V extends Comparable<V>> SDFrame<FT2<K, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                        Function<T, V> value) {
        Map<K, MaxMin<V>> map = stream().collect(groupingBy(K, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value))));
        return returnDF(convertToDataFrameItem2(map));
    }


    public <K, J, V extends Comparable<V>> SDFrame<FT3<K, J, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                              Function<T, J> J,
                                                                                              Function<T, V> value) {
        Map<K, Map<J, MaxMin<V>>> map = stream().collect(groupingBy(K, groupingBy(J, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value)))));
        return returnDF(convertToDataFrameItem3(map));
    }


    public <K, V extends Comparable<V>> SDFrame<FT2<K, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                   Function<T, V> value) {
        Map<K, MaxMin<T>> map = stream().collect(groupingBy(K, collectingAndThen(toList(), getListGroupMaxMinFunction(value))));
        return returnDF(convertToDataFrameItem2(map));
    }


    public <K, J, V extends Comparable<V>> SDFrame<FT3<K, J, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                         Function<T, J> J,
                                                                                         Function<T, V> value) {
        Map<K, Map<J, MaxMin<T>>> map = stream().collect(groupingBy(K, groupingBy(J, collectingAndThen(toList(), getListGroupMaxMinFunction(value)))));
        return returnDF(convertToDataFrameItem3(map));
    }

    private <V extends Comparable<V>> Function<List<T>, MaxMin<V>> getListGroupMaxMinValueFunction(Function<T, V> value) {
        return list -> {
            if (list == null || list.isEmpty()) {
                return null;
            }
            MaxMin<V> maxMin = new MaxMin<>();
            maxMin.setMax(list.stream().max(Comparator.comparing(value)).map(value).orElse(null));
            maxMin.setMin(list.stream().min(Comparator.comparing(value)).map(value).orElse(null));
            return maxMin;
        };
    }

    private <V extends Comparable<V>> Function<List<T>, MaxMin<T>> getListGroupMaxMinFunction(Function<T, V> value) {
        return list -> {
            if (list == null || list.isEmpty()) {
                return new MaxMin<>();
            }
            MaxMin<T> maxMin = new MaxMin<>();
            maxMin.setMax(list.stream().max(Comparator.comparing(value)).orElse(null));
            maxMin.setMin(list.stream().min(Comparator.comparing(value)).orElse(null));
            return maxMin;
        };
    }

    protected SDFrame<T> returnThis(Stream<T> stream) {
        this.data = stream;
        return this;
    }

    protected <R> SDFrameImpl<R> returnDF(List<R> dataList) {
        return new SDFrameImpl<>(dataList);
    }
}
