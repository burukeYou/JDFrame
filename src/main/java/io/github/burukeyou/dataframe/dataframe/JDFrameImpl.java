package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.JDFrame;
import io.github.burukeyou.dataframe.dataframe.item.FItem2;
import io.github.burukeyou.dataframe.dataframe.item.FItem3;
import io.github.burukeyou.dataframe.dataframe.item.FItem4;
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


/**
 * @author caizhihao
 */
public class JDFrameImpl<T> implements JDFrame<T> {

    protected Stream<T> data;

    public JDFrameImpl(List<T> list) {
        this.data = list.stream();
    }

    private static <T> JDFrameImpl<T> read(List<T> list) {
        return new JDFrameImpl<>(list);
    }

    /**
     * ===========================   原生stream包装相关  =====================================
     **/

    public JDFrameImpl<T> filter(Predicate<? super T> predicate) {
        data = data.filter(predicate);
        return this;
    }

    public <R, A> R collect(Collector<? super T, A, R> collector) {
        return data.collect(collector);
    }

    public Stream<T> toStream() {
        return data;
    }

    public List<T> toLists() {
        return data.collect(Collectors.toList());
    }


    /**
     * ===========================   排序相关  =====================================
     **/

    public JDFrameImpl<T> sortDesc(Comparator<T> comparator) {
        data = data.sorted(comparator.reversed());
        return this;
    }

    public <R extends Comparable<R>> JDFrameImpl<T> sortDesc(Function<T, R> function) {
        sortDesc(Comparator.comparing(function));
        return this;
    }

    /** ===========================   截取相关  ===================================== **/

    /**
     * 截取前n个
     */
    public JDFrameImpl<T> first(int n) {
        DFList<T> first = new DFList<>(toLists()).first(n);
        data = first.build().stream();
        return this;
    }

    /**
     * ===========================   筛选相关  =====================================
     **/

    public <R> JDFrameImpl<T> whereNotNull(Function<T, R> function) {
        data = data.filter(item -> {
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
        return this;
    }

    public <R extends Comparable<R>> JDFrameImpl<T> whereBetween(Function<T, R> function, R start, R end) {
        // 筛选条件都不存在默认不筛选
        if (start == null && end == null) {
            return this;
        }

        if (start == null) {
            data = data.filter(e -> function.apply(e).compareTo(end) <= 0);
        } else if (end == null) {
            data = data.filter(e -> function.apply(e).compareTo(start) >= 0);
        } else {
            data = data.filter(e -> function.apply(e).compareTo(start) >= 0 && function.apply(e).compareTo(end) <= 0);
        }
        return this;
    }

    /**
     * 区间筛选 （前开后闭）
     */
    public <R extends Comparable<R>> JDFrameImpl<T> whereBetweenR(Function<T, R> function, R start, R end) {
        // 筛选条件都不存在默认不筛选
        if (start == null && end == null) {
            return this;
        }

        if (start == null) {
            data = data.filter(e -> function.apply(e).compareTo(end) <= 0);
        } else if (end == null) {
            // 前开后闭
            data = data.filter(e -> function.apply(e).compareTo(start) > 0);
        } else {
            // 前开后闭
            data = data.filter(e -> function.apply(e).compareTo(start) > 0 && function.apply(e).compareTo(end) <= 0);
        }
        return this;
    }

    public <R extends Comparable<R>> JDFrameImpl<T> whereNotBetween(Function<T, R> function, R start, R end) {
        // 筛选条件不存在默认不筛选
        if (start == null || end == null) {
            return this;
        }

        data = data.filter(e -> function.apply(e).compareTo(start) < 0 || function.apply(e).compareTo(end) > 0);
        return this;
    }

    public <R> JDFrameImpl<T> whereIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }

        Set<R> set = new HashSet<>(list);
        data = data.filter(e -> set.contains(function.apply(e)));
        return this;
    }

    public <R> JDFrameImpl<T> whereNotIn(Function<T, R> function, List<R> list) {
        if (list == null || list.isEmpty()) {
            return this;
        }

        Set<R> set = new HashSet<>(list);
        data = data.filter(e -> !set.contains(function.apply(e)));
        return this;
    }

    public JDFrameImpl<T> whereTrue(Predicate<T> predicate) {
        data = data.filter(predicate);
        return this;
    }

    public JDFrameImpl<T> whereNotTrue(Predicate<T> predicate) {
        data = data.filter(predicate.negate());
        return this;
    }

    public <R> JDFrameImpl<T> whereEq(Function<T, R> function, R value) {
        if (null == value) {
            return this;
        }

        data = data.filter(e -> value.equals(function.apply(e)));
        return this;
    }

    public <R> JDFrameImpl<T> whereNotEq(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> !value.equals(function.apply(e)));
        return this;
    }


    public <R extends Comparable<R>> JDFrameImpl<T> whereGt(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> function.apply(e).compareTo(value) > 0);
        return this;
    }

    public <R extends Comparable<R>> JDFrameImpl<T> whereGe(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> function.apply(e).compareTo(value) >= 0);
        return this;
    }

    public <R extends Comparable<R>> JDFrameImpl<T> whereLt(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> function.apply(e).compareTo(value) < 0);
        return this;
    }

    public <R extends Comparable<R>> JDFrameImpl<T> whereLe(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> function.apply(e).compareTo(value) <= 0);
        return this;
    }


    public <R> JDFrameImpl<T> whereLike(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> String.valueOf(function.apply(e)).contains(String.valueOf(value)));
        return this;
    }

    public <R> JDFrameImpl<T> whereNotLike(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> !String.valueOf(function.apply(e)).contains(String.valueOf(value)));
        return this;
    }

    public <R> JDFrameImpl<T> whereLikeLeft(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> String.valueOf(function.apply(e)).startsWith(String.valueOf(value)));
        return this;
    }

    public <R> JDFrameImpl<T> whereLikeRight(Function<T, R> function, R value) {
        // 筛选条件不存在默认不筛选
        if (value == null) {
            return this;
        }

        data = data.filter(e -> String.valueOf(function.apply(e)).endsWith(String.valueOf(value)));
        return this;
    }

    /**
     * ===========================   汇总相关  =====================================
     **/

    public <R> BigDecimal sum(Function<T, R> function) {
        return data.map(function).filter(Objects::nonNull).collect(CollectorsPlusUtil.summingBigDecimal(e -> {
            if (e instanceof BigDecimal) {
                return (BigDecimal) e;
            } else {
                return new BigDecimal(String.valueOf(e));
            }
        }));
    }

    public <R> Integer sumInt(Function<T, R> function){
        return data.map(function).filter(Objects::nonNull).mapToInt(e -> {
            if (e instanceof Integer) {
                return (Integer) e;
            } else {
                return new Integer(String.valueOf(e));
            }
        }).sum();
    }

    public <R> BigDecimal avg(Function<T, R> function) {
        List<BigDecimal> bigDecimalList = data.map(function).filter(Objects::nonNull).map(e -> {
            if (e instanceof BigDecimal) {
                return (BigDecimal) e;
            } else {
                return new BigDecimal(String.valueOf(e));
            }
        }).collect(toList());

        if (bigDecimalList.isEmpty()) {
            return null;
        }

        // 计算平均值
        return bigDecimalList.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(bigDecimalList.size()), 2, RoundingMode.HALF_UP);
    }

    public <R extends Comparable<R>> MaxMin<R> maxAndMinValue(Function<T, R> function) {
        List<R> valueList = data.map(function).filter(Objects::nonNull).collect(toList());
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
        Optional<R> value = data.map(function).filter(Objects::nonNull).max(Comparator.comparing(e -> e));
        return value.orElse(null);
    }

    public <R extends Comparable<R>> T max(Function<T, R> function) {
        Optional<T> max = data.filter(Objects::nonNull).max(Comparator.comparing(function));
        return max.orElse(null);
    }

    public <R extends Comparable<R>> R minValue(Function<T, R> function) {
        Optional<R> value = data.map(function).filter(Objects::nonNull).min(Comparator.comparing(e -> e));
        return value.orElse(null);
    }

    public <R extends Comparable<R>> T min(Function<T, R> function) {
        Optional<T> min = data.filter(Objects::nonNull).min(Comparator.comparing(function));
        return min.orElse(null);
    }

    public int count() {
        return (int) data.count();
    }

    /** ===========================   分组相关  ===================================== **/

    /**
     * 分组求和
     *
     * @param K     分组的字段
     * @param value 聚合的字段
     */
    public <K> DFList<FItem2<K, BigDecimal>> groupBySum(Function<T, K> K,
                                                        ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FItem2<K, BigDecimal>> collect = group(K, tBigDecimalCollector);
        return new DFList<>(collect);
    }

    /**
     * 分组求和
     *
     * @param K     分组K
     * @param K2    二级分组K
     * @param value 聚合字段
     */
    public <K, J> DFList<FItem3<K, J, BigDecimal>> groupBySum(Function<T, K> K,
                                                              Function<T, J> K2,
                                                              ToBigDecimalFunction<T> value) {

        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FItem3<K, J, BigDecimal>> collect = group(K, K2, tBigDecimalCollector);
        return new DFList<>(collect);
    }

    /**
     * 分组求和
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param H    三级分组K
     * @param value 聚合字段
     */
    public <K, J, H> DFList<FItem4<K, J, H, BigDecimal>> groupBySum(Function<T, K> K,
                                                                    Function<T, J> J,
                                                                    Function<T, H> H,
                                                                    ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FItem4<K, J, H, BigDecimal>> collect = group(K, J, H, tBigDecimalCollector);
        return new DFList<>(collect);
    }

    /**
     * 分组求数量
     *
     * @param K 分组K
     */
    public <K> DFList<FItem2<K, Long>> groupByCount(Function<T, K> K) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Long> collect = data.collect(groupingBy(K, counting));
        return new DFList<>(convertToDataFrameItem2(collect));
    }

    /**
     * 分组求数量
     *
     * @param K  分组K
     * @param J 二级分组K
     */
    public <K, J> DFList<FItem3<K, J, Long>> groupByCount(Function<T, K> K,
                                                            Function<T, J> J) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Long>> collect = data.collect(groupingBy(K, groupingBy(J, counting)));
        return new DFList<>(convertToDataFrameItem3(collect));
    }

    /**
     * 分组求数量
     *
     * @param K 分组K
     *          二级分组K
     *          三级分组K
     */
    public <K, J, H> DFList<FItem4<K, J, H, Long>> groupByCount(Function<T, K> K,
                                                                    Function<T, J> J,
                                                                    Function<T, H> H) {
        Collector<Object, ?, Long> counting = counting();
        Map<K, Map<J, Map<H, Long>>> collect = data.collect(groupingBy(K, groupingBy(J, groupingBy(H, counting))));
        return new DFList<>(convertToDataFrameItem4(collect));
    }


    /**
     * 分组求和及数量
     *
     * @param K     分组的字段
     * @param value 求和的字段
     * @return              FItem3<K, 和, 数量>
     */
    public <K> DFList<FItem3<K, BigDecimal,Long>> groupBySumCount(Function<T, K> K, ToBigDecimalFunction<T> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FItem2<K, BigDecimal>> sumList = read(dataList).group(K, tBigDecimalCollector);
        List<FItem2<K, Long>> countList =  read(dataList).groupByCount(K).build();

        Map<K, Long> countMap = countList.stream().collect(toMap(FItem2::getC1, FItem2::getC2));
        List<FItem3<K, BigDecimal, Long>> collect = sumList.stream().map(e -> new FItem3<>(e.getC1(), e.getC2(), countMap.get(e.getC1()))).collect(Collectors.toList());
        return new DFList<>(collect);
    }

    /**
     * 分组求和及数量
     *
     * @param K             分组K
     * @param J             二级分组K
     * @param value         求和字段
     * @return              FItem4<K,K2, 和, 数量>
     */
    public <K, J> DFList<FItem4<K, J, BigDecimal, Long>> groupBySumCount(Function<T, K> K,
                                                                           Function<T, J> J,
                                                                           ToBigDecimalFunction<T> value) {
        List<T> dataList = toLists();
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.summingBigDecimal(value);
        List<FItem3<K, J, BigDecimal>> sumList = read(dataList).group(K, J, tBigDecimalCollector);
        List<FItem3<K, J, Long>> countList =  read(dataList).groupByCount(K, J).build();

        // 合并sum和count字段
        Map<String, FItem3<K, J, Long>> countMap = countList.stream().collect(toMap(e -> e.getC1() + "_" + e.getC2(), Function.identity()));
        List<FItem4<K, J, BigDecimal, Long>> collect = sumList.stream().map(e -> {
            FItem3<K, J, Long> countItem = countMap.get(e.getC1() + "_" + e.getC2());
            return new FItem4<>(e.getC1(), e.getC2(), e.getC3(), countItem.getC3());
        }).collect(Collectors.toList());

        return new DFList<>(collect);
    }


    /**
     * 分组求平均值
     *
     * @param K     分组的字段
     * @param value 聚合的字段
     */
    public <K> DFList<FItem2<K, BigDecimal>> groupByAvg(Function<T, K> K,
                                                        ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FItem2<K, BigDecimal>> collect = group(K, tBigDecimalCollector);
        return new DFList<>(collect);
    }

    /**
     * 分组求平均
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param value 聚合字段
     */
    public <K, J> DFList<FItem3<K, J, BigDecimal>> groupByAvg(Function<T, K> K,
                                                                Function<T, J> J,
                                                                ToBigDecimalFunction<T> value) {

        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FItem3<K, J, BigDecimal>> collect = group(K, J, tBigDecimalCollector);
        return new DFList<>(collect);
    }

    /**
     * 分组求平均
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param H    三级分组K
     * @param value 聚合字段
     */
    public <K, J, H> DFList<FItem4<K, J, H, BigDecimal>> groupByAvg(Function<T, K> K,
                                                                        Function<T, J> J,
                                                                        Function<T, H> H,
                                                                        ToBigDecimalFunction<T> value) {
        Collector<T, ?, BigDecimal> tBigDecimalCollector = CollectorsPlusUtil.averagingBigDecimal(value, 2, BigDecimal.ROUND_HALF_UP);
        List<FItem4<K, J, H, BigDecimal>> collect = group(K, J, H, tBigDecimalCollector);
        return new DFList<>(collect);
    }


    /**
     * 分组求最大
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    public <K, V extends Comparable<V>> DFList<FItem2<K, T>> groupByMax(Function<T, K> K,
                                                                        Function<T, V> value) {
        Map<K, T> collect = data.collect(groupingBy(K, collectingAndThen(toList(), e -> e.stream().min(Comparator.comparing(value)).orElse(null))));
        return new DFList<>(convertToDataFrameItem2(collect));
    }

    /**
     * 分组求最小
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    public <K, V extends Comparable<V>> DFList<FItem2<K, T>> groupByMin(Function<T, K> K,
                                                                        Function<T, V> value) {
        Map<K, T> collect = data.collect(groupingBy(K, collectingAndThen(toList(), e -> e.stream().min(Comparator.comparing(value)).orElse(null))));
        return new DFList<>(convertToDataFrameItem2(collect));
    }

    /**
     * 分组求最大和最小值
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    public <K, V extends Comparable<V>> DFList<FItem2<K, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                           Function<T, V> value) {
        Map<K, MaxMin<V>> map = data.collect(groupingBy(K, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value))));
        return new DFList<>(convertToDataFrameItem2(map));
    }

    /**
     * 分组求最大和最小值
     *
     * @param K     分组K
     * @param J    二级分组K
     * @param value 聚合字段
     */
    public <K, J, V extends Comparable<V>> DFList<FItem3<K, J, MaxMin<V>>> groupByMaxAndMinValue(Function<T, K> K,
                                                                                                   Function<T, J> J,
                                                                                                   Function<T, V> value) {
        Map<K, Map<J, MaxMin<V>>> map = data.collect(groupingBy(K, groupingBy(J, collectingAndThen(toList(), getListGroupMaxMinValueFunction(value)))));
        return new DFList<>(convertToDataFrameItem3(map));
    }

    /**
     * 分组求最大和最小
     *
     * @param K     分组K
     * @param value 聚合字段
     */
    public <K, V extends Comparable<V>> DFList<FItem2<K, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                      Function<T, V> value) {
        Map<K, MaxMin<T>> map = data.collect(groupingBy(K, collectingAndThen(toList(), getListGroupMaxMinFunction(value))));
        return new DFList<>(convertToDataFrameItem2(map));
    }

    /**
     * 分组求最大和最小
     *
     * @param K     分组K
     * @param J   二级分组K
     * @param value 聚合字段
     */
    public <K, J, V extends Comparable<V>> DFList<FItem3<K, J, MaxMin<T>>> groupByMaxAndMin(Function<T, K> K,
                                                                                              Function<T, J> J,
                                                                                              Function<T, V> value) {
        Map<K, Map<J, MaxMin<T>>> map = data.collect(groupingBy(K, groupingBy(J, collectingAndThen(toList(), getListGroupMaxMinFunction(value)))));
        return new DFList<>(convertToDataFrameItem3(map));
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
    private <K, V> List<FItem2<K, V>> group(Function<T, K> K, Collector<T, ?, V> tBigDecimalCollector) {
        Map<K, V> resultMap = data.collect(groupingBy(K, tBigDecimalCollector));
        return convertToDataFrameItem2(resultMap);
    }

    /**
     * 二级分组
     *
     * @param K                    一级分组K
     * @param J                   二级分组K
     * @param tBigDecimalCollector 聚合方式
     */
    private <K, J, V> List<FItem3<K, J, V>> group(Function<T, K> K, Function<T, J> J, Collector<T, ?, V> tBigDecimalCollector) {
        Map<K, Map<J, V>> map = data.collect(groupingBy(K, groupingBy(J, tBigDecimalCollector)));
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
    private <K, J, H, V> List<FItem4<K, J, H, V>> group(Function<T, K> K, Function<T, J> J, Function<T, H> H, Collector<T, ?, V> collectorType) {
        Map<K, Map<J, Map<H, V>>> map = data.collect(groupingBy(K, groupingBy(J, groupingBy(H, collectorType))));
        return convertToDataFrameItem4(map);
    }


    private <K, V> List<FItem2<K, V>> convertToDataFrameItem2(Map<K, V> resultMap) {
        return resultMap.entrySet().stream().map(e -> new FItem2<>(e.getKey(), e.getValue())).collect(toList());
    }

    private <K, J, V> List<FItem3<K, J, V>> convertToDataFrameItem3(Map<K, Map<J, V>> map) {
        return map.entrySet().stream()
                .flatMap(et ->
                        et.getValue().entrySet().stream()
                                .map(subEt -> new FItem3<>(et.getKey(), subEt.getKey(), subEt.getValue()))
                                .collect(toList())
                                .stream()
                )
                .collect(toList());
    }

    private <K, J, H, V> List<FItem4<K, J, H, V>> convertToDataFrameItem4(Map<K, Map<J, Map<H, V>>> map) {
        List<FItem4<K, J, H, V>> collect = map.entrySet().stream()
                .flatMap(et ->
                        et.getValue().entrySet().stream()
                                .flatMap(subEt -> subEt.getValue().entrySet().stream().map(sub2Et -> new FItem4<>(et.getKey(), subEt.getKey(), sub2Et.getKey(), sub2Et.getValue())).collect(toList()).stream())
                                .collect(toList())
                                .stream()
                )
                .collect(toList());
        return collect;
    }

}
