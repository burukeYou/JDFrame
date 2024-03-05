package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.IFrame;
import io.github.burukeyou.dataframe.dataframe.item.FT2;
import io.github.burukeyou.dataframe.dataframe.item.FT3;
import io.github.burukeyou.dataframe.dataframe.item.FT4;
import io.github.burukeyou.dataframe.util.CollectorsPlusUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;
import static java.util.stream.Collectors.counting;

/**
 * @author caizhihao
 * @param <T>
 */
public abstract class AbstractDataFrame<T> implements IFrame<T> {

    protected AbstractDataFrame<T> read(List<T> dataList){
        return readDF(dataList);
    }

    /**
     * ===========================   筛选相关  =====================================
     **/
    public <R> IFrame<T> whereNotNull(Function<T, R> function) {
        Stream<T> stream = stream().filter(item -> {
            R r = function.apply(item);
            if (r == null) {
                return false;
            }
            if (r instanceof String) {
                return !"".equals(r);
            } else {
                return true;
            }
        });
        return returnThis(stream);
    }

    public <R extends Comparable<R>> IFrame<T> whereBetween(Function<T, R> function, R start, R end) {
        if (start == null && end == null) {
            return this;
        }
        Stream<T> stream = null;
        if (start == null) {
            stream = stream().filter(e -> function.apply(e).compareTo(end) <= 0);
        } else if (end == null) {
            stream = stream().filter(e -> function.apply(e).compareTo(start) >= 0);
        } else {
            stream = stream().filter(e -> function.apply(e).compareTo(start) >= 0 && function.apply(e).compareTo(end) <= 0);
        }
        return returnThis(stream);
    }

    
    public <R extends Comparable<R>> IFrame<T> whereBetweenR(Function<T, R> function, R start, R end) {
        // 筛选条件都不存在默认不筛选
        if (start == null && end == null) {
            return this;
        }
        Stream<T> stream = null;
        if (start == null) {
            stream = stream().filter(e -> function.apply(e).compareTo(end) <= 0);
        } else if (end == null) {
            // 前开后闭
            stream = stream().filter(e -> function.apply(e).compareTo(start) > 0);
        } else {
            // 前开后闭
            stream = stream().filter(e -> function.apply(e).compareTo(start) > 0 && function.apply(e).compareTo(end) <= 0);
        }
        return returnThis(stream);
    }


    
    public <R extends Comparable<R>> IFrame<T> whereNotBetween(Function<T, R> function, R start, R end) {
        if (start == null || end == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> function.apply(e).compareTo(start) < 0 || function.apply(e).compareTo(end) > 0);
        return returnThis(stream);
    }

    
    public <R> IFrame<T> whereIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        Set<R> set = new HashSet<>(list);
        Stream<T> stream = stream().filter(e -> set.contains(function.apply(e)));
        return returnThis(stream);
    }

    
    public <R> IFrame<T> whereNotIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }
        Set<R> set = new HashSet<>(list);
        Stream<T> stream = stream().filter(e -> !set.contains(function.apply(e)));
        return returnThis(stream);
    }


    
    public IFrame<T> whereTrue(Predicate<T> predicate) {
        return returnThis(stream().filter(predicate));
    }

    
    public IFrame<T> whereNotTrue(Predicate<T> predicate) {
        return whereTrue(predicate.negate());
    }

    
    public <R> IFrame<T> whereEq(Function<T, R> function, R value) {
        if (null == value) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> value.equals(function.apply(e)));
        return  returnThis(stream);
    }

    
    public <R> IFrame<T> whereNotEq(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> !value.equals(function.apply(e)));
        return returnThis(stream);
    }

    
    public <R extends Comparable<R>> IFrame<T> whereGt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> function.apply(e).compareTo(value) > 0);
        return returnThis(stream);
    }

    
    public <R extends Comparable<R>> IFrame<T> whereGe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> function.apply(e).compareTo(value) >= 0);
        return returnThis(stream);
    }

    
    public <R extends Comparable<R>> IFrame<T> whereLt(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> function.apply(e).compareTo(value) < 0);
        return returnThis(stream);
    }

    
    public <R extends Comparable<R>> IFrame<T> whereLe(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> function.apply(e).compareTo(value) <= 0);
        return returnThis(stream);
    }

    
    public <R> IFrame<T> whereLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> String.valueOf(function.apply(e)).contains(String.valueOf(value)));
        return returnThis(stream);
    }

    
    public <R> IFrame<T> whereNotLike(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> !String.valueOf(function.apply(e)).contains(String.valueOf(value)));
        return returnThis(stream);
    }

    
    public <R> IFrame<T> whereLikeLeft(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> String.valueOf(function.apply(e)).startsWith(String.valueOf(value)));
        return returnThis(stream);
    }

    
    public <R> IFrame<T> whereLikeRight(Function<T, R> function, R value) {
        if (value == null) {
            return this;
        }
        Stream<T> stream = stream().filter(e -> String.valueOf(function.apply(e)).endsWith(String.valueOf(value)));
        return returnThis(stream);
    }

    /**
     * ===========================   汇总相关  =====================================
     **/
    
    public <R> BigDecimal sum(Function<T, R> function) {
        return stream().map(function).filter(Objects::nonNull).collect(CollectorsPlusUtil.summingBigDecimal(e -> {
            if (e instanceof BigDecimal) {
                return (BigDecimal) e;
            } else {
                return new BigDecimal(String.valueOf(e));
            }
        }));
    }

    // todo
    
    public <R> Integer sumInt(Function<T, R> function){
        return stream().map(function).filter(Objects::nonNull).mapToInt(e -> {
            if (e instanceof Integer) {
                return (Integer) e;
            } else {
                return new Integer(String.valueOf(e));
            }
        }).sum();
    }

    
    public <R> BigDecimal avg(Function<T, R> function) {
        List<BigDecimal> bigDecimalList = stream().map(function).filter(Objects::nonNull).map(e -> {
            if (e instanceof BigDecimal) {
                return (BigDecimal) e;
            } else {
                return new BigDecimal(String.valueOf(e));
            }
        }).collect(toList());

        if (bigDecimalList.isEmpty()) {
            return null;
        }
        return bigDecimalList.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(bigDecimalList.size()), 2, RoundingMode.HALF_UP);
    }

    
    public <R extends Comparable<R>> MaxMin<R> maxAndMinValue(Function<T, R> function) {
        List<R> valueList = stream().map(function).filter(Objects::nonNull).collect(toList());
        if (valueList.isEmpty()) {
            return new MaxMin<>(null, null);
        }
        R max = valueList.get(0);
        R min = valueList.get(0);
        for (int i = 1; i < valueList.size(); i++) {
            R cur = valueList.get(i);
            if (cur.compareTo(max) >= 0) {
                max = cur;
            }
            if (cur.compareTo(min) <= 0) {
                min = cur;
            }
        }
        return new MaxMin<>(max, min);
    }

    
    public <R extends Comparable<R>> MaxMin<T> maxAndMin(Function<T, R> function) {
        T max = max(function);
        T min = min(function);
        return new MaxMin<>(max, min);
    }


    
    public <R extends Comparable<R>> R maxValue(Function<T, R> function) {
        Optional<R> value = stream().map(function).filter(Objects::nonNull).max(Comparator.comparing(e -> e));
        return value.orElse(null);
    }

    
    public <R extends Comparable<R>> T max(Function<T, R> function) {
        Optional<T> max = stream().filter(Objects::nonNull).max(Comparator.comparing(function));
        return max.orElse(null);
    }

    
    public <R extends Comparable<R>> R minValue(Function<T, R> function) {
        Optional<R> value = stream().map(function).filter(Objects::nonNull).min(Comparator.comparing(e -> e));
        return value.orElse(null);
    }

    
    public <R extends Comparable<R>> T min(Function<T, R> function) {
        Optional<T> min = stream().filter(Objects::nonNull).min(Comparator.comparing(function));
        return min.orElse(null);
    }

    public int count() {
        return (int) stream().count();
    }

    /** ===========================   分组相关  ===================================== **/


    
    public <K> IFrame<FT2<K, BigDecimal>> groupBySum(Function<T, K> K,
                                                     ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT2<K, BigDecimal>> collect = group(K, tBigDecimalCollector);
        return readDF(collect);
    }

    
    public <K, J> IFrame<FT3<K, J, BigDecimal>> groupBySum(Function<T, K> K,
                                                           Function<T, J> K2,
                                                           ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT3<K, J, BigDecimal>> collect = group(K, K2, tBigDecimalCollector);
        return readDF(collect);
    }


    
    public <K, J, H> IFrame<FT4<K, J, H, BigDecimal>> groupBySum(Function<T, K> K,
                                                                 Function<T, J> J,
                                                                 Function<T, H> H,
                                                                 ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT4<K, J, H, BigDecimal>> collect = group(K, J, H, tBigDecimalCollector);
        return readDF(collect);
    }

    
    public <K> IFrame<FT2<K, Long>> groupByCount(Function<T, K> K) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Long> collect = stream().collect(groupingBy(K, counting));
        return readDF(convertToDataFrameItem2(collect));
    }

    
    public <K, J> IFrame<FT3<K, J, Long>> groupByCount(Function<T, K> K,
                                                       Function<T, J> J) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Long>> collect = stream().collect(groupingBy(K, groupingBy(J, counting)));
        return readDF(convertToDataFrameItem3(collect));
    }

    
    public <K, J, H> IFrame<FT4<K, J, H, Long>> groupByCount(Function<T, K> K,
                                                             Function<T, J> J,
                                                             Function<T, H> H) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Map<H, Long>>> collect = stream().collect(groupingBy(K, groupingBy(J, groupingBy(H, counting))));
        return readDF(convertToDataFrameItem4(collect));
    }

    
    public <K> IFrame<FT3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> K, ToBigDecimalFunction<T> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT2<K, BigDecimal>> sumList = readDF(dataList).group(K, tBigDecimalCollector);
        List<FT2<K, Long>> countList =  read(dataList).groupByCount(K).toLists();
        Map<K, Long> countMap = countList.stream().collect(toMap(FT2::getC1, FT2::getC2));
        List<FT3<K, BigDecimal, Long>> collect = sumList.stream().map(e -> new FT3<>(e.getC1(), e.getC2(), countMap.get(e.getC1()))).collect(Collectors.toList());
        return readDF(collect);
    }

    
    public <K, J> IFrame<FT4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> K,
                                                                      Function<T, J> J,
                                                                      ToBigDecimalFunction<T> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FT3<K, J, BigDecimal>> sumList = readDF(dataList).group(K, J, tBigDecimalCollector);
        List<FT3<K, J, Long>> countList =  read(dataList).groupByCount(K, J).toLists();
        // 合并sum和count字段
        Map<String, FT3<K, J, Long>> countMap = countList.stream().collect(toMap(e -> e.getC1() + "_" + e.getC2(), Function.identity()));
        List<FT4<K, J, BigDecimal, Long>> collect = sumList.stream().map(e -> {
            FT3<K, J, Long> countItem = countMap.get(e.getC1() + "_" + e.getC2());
            return new FT4<>(e.getC1(), e.getC2(), e.getC3(), countItem.getC3());
        }).collect(Collectors.toList());
        return readDF(collect);
    }

    
    public <K> IFrame<FT2<K, BigDecimal>> groupByAvg(Function<T, K> K,
                                                     ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FT2<K, BigDecimal>> collect = group(K, tBigDecimalCollector);
        return readDF(collect);
    }

    
    public <K, J> IFrame<FT3<K, J, BigDecimal>> groupByAvg(Function<T, K> K,
                                                           Function<T, J> J,
                                                           ToBigDecimalFunction<T> value) {

        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FT3<K, J, BigDecimal>> collect = group(K, J, tBigDecimalCollector);
        return readDF(collect);
    }

    
    public <K, J, H> IFrame<FT4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> K,
                                                                 Function<T, J> J,
                                                                 Function<T, H> H,
                                                                 ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FT4<K, J, H, BigDecimal>> collect = group(K, J, H, tBigDecimalCollector);
        return readDF(collect);
    }


    
    public <K, V extends Comparable<V>> IFrame<FT2<K, T>> groupByMax(Function<T, K> K,
                                                                     Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(K, collectingAndThen(toList(), e -> e.stream().min(Comparator.comparing(value)).orElse(null))));
        return readDF(convertToDataFrameItem2(collect));
    }

    
    public <K, V extends Comparable<V>> IFrame<FT2<K, T>> groupByMin(Function<T, K> K,
                                                                     Function<T, V> value) {
        Map<K, T> collect = stream().collect(groupingBy(K, collectingAndThen(toList(), e -> e.stream().min(Comparator.comparing(value)).orElse(null))));
        return readDF(convertToDataFrameItem2(collect));
    }

    
    public <K, V extends Comparable<V>> IFrame<FT2<K, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                        Function<T, V> value) {
        Map<K, MaxMin<V>> map = stream().collect(groupingBy(K, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value))));
        return readDF(convertToDataFrameItem2(map));
    }

    
    public <K, J, V extends Comparable<V>> IFrame<FT3<K, J, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                              Function<T, J> J,
                                                                                              Function<T, V> value) {
        Map<K, Map<J, MaxMin<V>>> map = stream().collect(groupingBy(K, groupingBy(J, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value)))));
        return readDF(convertToDataFrameItem3(map));
    }

    
    public <K, V extends Comparable<V>> IFrame<FT2<K, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                   Function<T, V> value) {
        Map<K, MaxMin<T>> map = stream().collect(groupingBy(K, collectingAndThen(toList(), getListGroupMaxMinFunction(value))));
        return readDF(convertToDataFrameItem2(map));
    }

    
    public <K, J, V extends Comparable<V>> IFrame<FT3<K, J, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                         Function<T, J> J,
                                                                                         Function<T, V> value) {
        Map<K, Map<J, MaxMin<T>>> map = stream().collect(groupingBy(K, groupingBy(J, collectingAndThen(toList(), getListGroupMaxMinFunction(value)))));
        return readDF(convertToDataFrameItem3(map));
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


    /**
     * 一级分组
     *
     * @param K                    一级分组K
     * @param tBigDecimalCollector 聚合方式
     */
    protected  <K, V> List<FT2<K, V>> group(Function<T, K> K, Collector<T, ?, V> tBigDecimalCollector) {
        Map<K, V> resultMap = stream().collect(groupingBy(K, tBigDecimalCollector));
        return convertToDataFrameItem2(resultMap);
    }

    /**
     * 二级分组
     *
     * @param K                    一级分组K
     * @param J                   二级分组K
     * @param tBigDecimalCollector 聚合方式
     */
    protected <K, J, V> List<FT3<K, J, V>> group(Function<T, K> K, Function<T, J> J, Collector<T, ?, V> tBigDecimalCollector) {
        Map<K, Map<J, V>> map = stream().collect(groupingBy(K, groupingBy(J, tBigDecimalCollector)));
        return convertToDataFrameItem3(map);
    }

    /**
     * 三级分组
     *
     * @param K             一级分组K
     * @param J            二级分组K
     * @param H            三级分组K
     * @param collectorType 聚合方式
     */
    protected <K, J, H, V> List<FT4<K, J, H, V>> group(Function<T, K> K, Function<T, J> J, Function<T, H> H, Collector<T, ?, V> collectorType) {
        Map<K, Map<J, Map<H, V>>> map = stream().collect(groupingBy(K, groupingBy(J, groupingBy(H, collectorType))));
        return convertToDataFrameItem4(map);
    }


    protected <K, V> List<FT2<K, V>> convertToDataFrameItem2(Map<K, V> resultMap) {
        return resultMap.entrySet().stream().map(e -> new FT2<>(e.getKey(), e.getValue())).collect(toList());
    }

    protected <K, J, V> List<FT3<K, J, V>> convertToDataFrameItem3(Map<K, Map<J, V>> map) {
        return map.entrySet().stream()
                .flatMap(et ->
                        et.getValue().entrySet().stream()
                                .map(subEt -> new FT3<>(et.getKey(), subEt.getKey(), subEt.getValue()))
                                .collect(toList())
                                .stream()
                )
                .collect(toList());
    }

    protected <K, J, H, V> List<FT4<K, J, H, V>> convertToDataFrameItem4(Map<K, Map<J, Map<H, V>>> map) {
        List<FT4<K, J, H, V>> collect = map.entrySet().stream()
                .flatMap(et ->
                        et.getValue().entrySet().stream()
                                .flatMap(subEt -> subEt.getValue().entrySet().stream().map(sub2Et -> new FT4<>(et.getKey(), subEt.getKey(), sub2Et.getKey(), sub2Et.getValue())).collect(toList()).stream())
                                .collect(toList())
                                .stream()
                )
                .collect(toList());
        return collect;
    }

    protected abstract IFrame<T> returnThis(Stream<T> stream);

    protected abstract <R> AbstractDataFrame<R> readDF(List<R> dataList);
}
