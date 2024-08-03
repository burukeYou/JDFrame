package io.github.burukeyou.dataframe.iframe;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import io.github.burukeyou.dataframe.iframe.function.ReplenishFunction;
import io.github.burukeyou.dataframe.iframe.function.SetFunction;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.item.FI3;
import io.github.burukeyou.dataframe.iframe.item.FI4;
import io.github.burukeyou.dataframe.iframe.support.Join;
import io.github.burukeyou.dataframe.iframe.support.JoinOn;
import io.github.burukeyou.dataframe.iframe.support.MaxMin;
import io.github.burukeyou.dataframe.util.BeanCopyUtil;
import io.github.burukeyou.dataframe.util.CollectorsPlusUtil;
import io.github.burukeyou.dataframe.util.FrameUtil;
import io.github.burukeyou.dataframe.util.ListUtils;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.*;

/**
 * @author caizhihao
 * @param <T>
 */
@Slf4j
@Getter
public abstract class AbstractDataFrameImpl<T> extends AbstractWindowDataFrame<T>  {

    protected AbstractDataFrameImpl() {

    }

    @Override
    public T[] toArray() {
        List<T> ts = toLists();
        if (ts.isEmpty() && fieldClass == null){
            // 为空拿不到泛型先返回null
            return null;
        }
        T[] arr = (T[]) Array.newInstance(fieldClass, ts.size());
        for (int i = 0; i < ts.size(); i++) {
            arr[i] = ts.get(i);
        }
        return arr;
    }

    @Override
    public T[] toArray(Class<T> elementClass) {
        List<T> ts = toLists();
        if (ts == null || ts.isEmpty()) {
            return (T[]) Array.newInstance(elementClass, 0);
        }
        T[] array = (T[]) Array.newInstance(elementClass, ts.size());
        for (int i = 0; i < ts.size(); i++) {
            array[i] = ts.get(i);
        }
        return array;
    }

    @Override
    public boolean contains(T other) {
        return toLists().contains(other);
    }

    @Override
    public  <U> boolean containsValue(Function<T,U> valueFunction, U value) {
        return stream().anyMatch(e -> {
            if (e == null) {
                return false;
            }

            U fieldValue = valueFunction.apply(e);
            if (fieldValue == null && value == null) {
                return true;
            }

            if (value != null) {
                return value.equals(fieldValue);
            } else {
                // value is null ,fieldValue is not null
                return false;
            }
        });
    }

    @Override
    public <U> String joining(Function<T, U> joinField,CharSequence delimiter, CharSequence prefix, CharSequence suffix) {
        return stream().map(joinField).filter(Objects::nonNull).map(Object::toString).collect(Collectors.joining(delimiter,prefix,suffix));
    }

    @Override
    public <U> String joining(Function<T, U> joinField, CharSequence delimiter) {
        return joining(joinField,delimiter,"","");
    }

    @Override
    public <K, V> Map<K, V> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends V> valueMapper) {
        // 原生stream 的 toMap存在两个问题。 1-value不能为null否则空指针异常 2-不能重复key，否则 Duplicate key 异常所以宁愿手写
        List<T> list = toLists();
        if (ListUtils.isEmpty(list)){
            return Collections.emptyMap();
        }
        Map<K, V> map = new HashMap<>(list.size());
        for (T t : list) {
            map.put(keyMapper.apply(t),valueMapper.apply(t));
        }
        return map;
    }

    @Override
    public <K, K2, V> Map<K, Map<K2, V>> toMap(Function<? super T, ? extends K> keyMapper, Function<? super T, ? extends K2> key2Mapper, Function<? super T, ? extends V> valueMapper) {
        Map<? extends K, List<T>> oldMap = stream().collect(groupingBy(keyMapper));
        Map<K, Map<K2, V>> map = new HashMap<>(oldMap.size());
        oldMap.forEach((key,list) -> map.put(key,from(list.stream()).toMap(key2Mapper, valueMapper)));
        return map;
    }

    protected  <R> Stream<T> whereNullStream(Function<T, R> function) {
        return stream().filter(item -> {
            R r = function.apply(item);
            if (r == null) {
                return true;
            }
            if (r instanceof String) {
                return "".equals(r);
            } else {
                return false;
            }
        });
    }

    protected  <R> Stream<T> whereNotNullStream(Function<T, R> function) {
        return stream().filter(item -> {
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
    }

    public <R extends Comparable<R>> Stream<T> whereBetweenStream(Function<T, R> function, R start, R end) {
        Stream<T> stream = streamFilterNull(function);
        if (start == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(end) <= 0);
        } else if (end == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(start) >= 0);
        } else {
            stream = stream.filter(e -> function.apply(e).compareTo(start) >= 0 && function.apply(e).compareTo(end) <= 0);
        }
        return stream;
    }

    public <R extends Comparable<R>> Stream<T> whereBetweenNStream(Function<T, R> function, R start, R end) {
        Stream<T> stream = streamFilterNull(function);
        if (start == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(end) < 0);
        } else if (end == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(start) > 0);
        } else {
            stream = stream.filter(e -> function.apply(e).compareTo(start) > 0 && function.apply(e).compareTo(end) < 0);
        }
        return stream;
    }

    public <R extends Comparable<R>> Stream<T> whereBetweenRStream(Function<T, R> function, R start, R end) {
        // 前开后闭
        Stream<T> stream = streamFilterNull(function);
        if (start == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(end) <= 0);
        } else if (end == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(start) > 0);
        } else {
            stream = stream.filter(e -> function.apply(e).compareTo(start) > 0 && function.apply(e).compareTo(end) <= 0);
        }
        return stream;
    }

    public <R extends Comparable<R>> Stream<T> whereBetweenLStream(Function<T, R> function, R start, R end) {
        // 前闭后开
        Stream<T> stream = streamFilterNull(function);
        if (start == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(end) < 0);
        } else if (end == null) {
            stream = stream.filter(e -> function.apply(e).compareTo(start) >= 0);
        } else {
            stream = stream.filter(e -> function.apply(e).compareTo(start) >= 0 && function.apply(e).compareTo(end) < 0);
        }
        return stream;
    }

    public <R extends Comparable<R>> Stream<T> whereNotBetweenStream(Function<T, R> function, R start, R end) {
        return streamFilterNull(function).filter(e -> function.apply(e).compareTo(start) <= 0 || function.apply(e).compareTo(end) >= 0);
    }

    public <R extends Comparable<R>> Stream<T> whereNotBetweenNStream(Function<T, R> function, R start, R end) {
        return streamFilterNull(function).filter(e -> function.apply(e).compareTo(start) < 0 || function.apply(e).compareTo(end) > 0);
    }

    public <R> Stream<T> whereInStream(Function<T, R> function, List<R> list) {
        Set<R> set = new HashSet<>(list);
        return stream().filter(e -> set.contains(function.apply(e)));
    }

    public <R> Stream<T> whereNotInStream(Function<T, R> function, List<R> list) {
        Set<R> set = new HashSet<>(list);
        return stream().filter(e -> !set.contains(function.apply(e)));
    }


    public <R> Stream<T> whereEqStream(Function<T, R> function, R value) {
        return stream().filter(e -> {
            if (e == null) {
                return false;
            }

            R fieldValue = function.apply(e);
            if (fieldValue == null && value == null) {
                return true;
            }

            if (value != null) {
                return value.equals(fieldValue);
            } else {
                // value is null ,fieldValue is not null
                return false;
            }
        });
    }

    public <R> Stream<T> whereNotEqStream(Function<T, R> function, R value) {
        return stream().filter(e -> !value.equals(function.apply(e)));
    }

    public <R extends Comparable<R>> Stream<T> whereGtStream(Function<T, R> function, R value) {
        return streamFilterNull(function).filter(e -> function.apply(e).compareTo(value) > 0);
    }

    public <R extends Comparable<R>> Stream<T> whereGeStream(Function<T, R> function, R value) {
        return streamFilterNull(function).filter(e -> function.apply(e).compareTo(value) >= 0);
    }

    public <R extends Comparable<R>> Stream<T> whereLtStream(Function<T, R> function, R value) {
        return streamFilterNull(function).filter(e -> function.apply(e).compareTo(value) < 0);
    }

    public <R extends Comparable<R>> Stream<T> whereLeStream(Function<T, R> function, R value) {
        return streamFilterNull(function).filter(e -> function.apply(e).compareTo(value) <= 0);
    }


    public <R> Stream<T> whereLikeStream(Function<T, R> function, R value) {
       return streamFilterNull(function).filter(e -> String.valueOf(function.apply(e)).contains(String.valueOf(value)));
    }

    public <R> Stream<T> whereNotLikeStream(Function<T, R> function, R value) {
       return streamFilterNull(function).filter(e -> !String.valueOf(function.apply(e)).contains(String.valueOf(value)));
    }

    public <R> Stream<T> whereLikeLeftStream(Function<T, R> function, R value) {
        return streamFilterNull(function).filter(e -> String.valueOf(function.apply(e)).startsWith(String.valueOf(value)));
    }

    public <R> Stream<T> whereLikeRightStream(Function<T, R> function, R value) {
       return streamFilterNull(function).filter(e -> String.valueOf(function.apply(e)).endsWith(String.valueOf(value)));
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
                .divide(BigDecimal.valueOf(bigDecimalList.size()), defaultScale, defaultRoundingMode);
    }

    public <R extends Comparable<? super R>> MaxMin<R> maxMinValue(Function<T, R> function) {
        MaxMin<T> maxAndMin = maxMin(function);
        return new MaxMin<>(getApplyValue(function,maxAndMin.getMax()), getApplyValue(function,maxAndMin.getMin()));
    }

    public <R extends Comparable<? super R>> MaxMin<T> maxMin(Function<T, R> function) {
        List<T> itemList = stream().filter(e -> e != null && function.apply(e) != null).collect(toList());
        if (itemList.isEmpty()){
            return new MaxMin<>(null,null);
        }
        T max = itemList.get(0);
        T min = itemList.get(0);
        for (int i = 1; i < itemList.size(); i++) {
            T cur = itemList.get(i);
            R curValue = function.apply(cur);
            R maxValue = function.apply(max);
            R minValue = function.apply(min);
            if (curValue.compareTo(maxValue) >= 0) {
                max = cur;
            }
            if (curValue.compareTo(minValue) <= 0) {
                min = cur;
            }
        }
        return new MaxMin<>(max, min);
    }

    public <R extends Comparable<? super R>> R maxValue(Function<T, R> function) {
        Optional<R> value = stream().map(function).filter(Objects::nonNull).max(Comparator.comparing(e -> e));
        return value.orElse(null);
    }


    public <R extends Comparable<R>> T max(Function<T, R> function) {
        Optional<T> max = stream().filter(e -> function.apply(e) != null).max(Comparator.comparing(function));
        return max.orElse(null);
    }


    public <R extends Comparable<? super R>> R minValue(Function<T, R> function) {
        Optional<R> value = stream().map(function).filter(Objects::nonNull).min(Comparator.comparing(e -> e));
        return value.orElse(null);
    }


    public <R extends Comparable<R>> T min(Function<T, R> function) {
        Optional<T> min = stream().filter(e -> function.apply(e) != null).min(Comparator.comparing(function));
        return min.orElse(null);
    }

    public long count() {
        return stream().count();
    }

    @Override
    public boolean isEmpty() {
        return count() <= 0;
    }

    @Override
    public boolean isNotEmpty() {
        return count() > 0;
    }

    protected  <K> List<FI2<K, List<T>>> groupKey(Function<? super T, ? extends K> K) {
        return FrameUtil.toListFI2(stream().collect(groupingBy(K)));
    }

    /**
     * 一级分组
     *
     * @param K                    一级分组K
     * @param tBigDecimalCollector 聚合方式
     */
    protected  <K, V> List<FI2<K, V>> groupKey(Function<T, K> K, Collector<T, ?, V> tBigDecimalCollector) {
        Map<K, V> resultMap = stream().collect(groupingBy(K, tBigDecimalCollector));
        return FrameUtil.toListFI2(resultMap);
    }

    /**
     * 二级分组
     *
     * @param K                    一级分组K
     * @param J                   二级分组K
     * @param tBigDecimalCollector 聚合方式
     */
    protected <K, J, V> List<FI3<K, J, V>> groupKey(Function<T, K> K, Function<T, J> J, Collector<T, ?, V> tBigDecimalCollector) {
        Map<K, Map<J, V>> map = stream().collect(groupingBy(K, groupingBy(J, tBigDecimalCollector)));
        return FrameUtil.toListFI3(map);
    }

    /**
     * 三级分组
     *
     * @param K             一级分组K
     * @param J            二级分组K
     * @param H            三级分组K
     * @param collectorType 聚合方式
     */
    protected <K, J, H, V> List<FI4<K, J, H, V>> groupKey(Function<T, K> K, Function<T, J> J, Function<T, H> H, Collector<T, ?, V> collectorType) {
        Map<K, Map<J, Map<H, V>>> map = stream().collect(groupingBy(K, groupingBy(J, groupingBy(H, collectorType))));
        return FrameUtil.toListFI4(map);
    }




    protected  <K, J, V extends Comparable<V>> Map<K, Map<J, T>> groupToMap(Function<T, K> key, Function<T, J> key2,Function<List<T>, T> getListMaxFunction) {
        return stream().collect(groupingBy(key, groupingBy(key2, collectingAndThen(toList(), getListMaxFunction))));
    }

    protected  <V extends Comparable<? super V>> Function<List<T>, T> getListMaxFunction(Function<T, V> value) {
        return e -> e.stream().filter(a ->  value.apply(a) != null).max(Comparator.comparing(value)).orElse(null);
    }

    protected  <V extends Comparable<? super V>> Function<List<T>, T> getListMinFunction(Function<T, V> value) {
        return e -> e.stream().min(Comparator.comparing(value)).orElse(null);
    }

    protected <V extends Comparable<? super V>> Function<List<T>, MaxMin<V>> getListGroupMaxMinValueFunction(Function<T, V> value) {
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

    protected <V extends Comparable<? super V>> Function<List<T>, MaxMin<T>> getListGroupMaxMinFunction(Function<T, V> value) {
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


    public <R> Stream<T> streamFilterNull(Function<T,R> function){
        return stream().filter(e -> e != null && function.apply(e) != null);
    }

    @Override
    public Iterator<T> iterator() {
        return toLists().iterator();
    }


    @Override
    public List<String> columns() {
        return getFieldList();
    }


    @Override
    public <R> List<R> col(Function<T, R> function) {
        return toLists().stream().map(function).collect(toList());
    }

    @Override
    public List<T> page(int page, int pageSize) {
        if (page < 0 || pageSize < 1) {
            throw new IllegalArgumentException("Page and pageSize must be positive integers.");
        }

        if (page == 0){
            page = 1;
        }
        int startIndex = (page - 1) * pageSize;
        int count = (int)count();
        if (startIndex >= count) {
            return Collections.emptyList();
        }
        int endIndex = Math.min(startIndex + pageSize, count);
        return toLists().subList(startIndex, endIndex);
    }

    @Override
    public String toString() {
        return getShowString(15).toString();
    }

    @Override
    public void show(){
        show(15);
    }

    @Override
    public void show(int n){
        StringBuilder sb = getShowString(n);
        System.out.println(sb);
    }


    protected  <R, K> List<R> joinList(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        List<R> resultList = new ArrayList<>();
        for (T cur :this){
            for (K k : other) {
                if(on.on(cur,k)){
                    resultList.add(join.join(cur,k));
                }
            }
        }
        return resultList;
    }

    protected  <R, K> List<R> leftJoinList(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        List<R> resultList = new ArrayList<>();
        for (T cur :this){
            for (K k : other) {
                if(on.on(cur,k)){
                    resultList.add(join.join(cur,k));
                }else {
                    resultList.add(join.join(cur,null));
                }
            }
        }
        return resultList;
    }

    protected  <R, K> List<R> rightJoinList(IFrame<K> other, JoinOn<T, K> on, Join<T, K, R> join) {
        List<R> resultList = new ArrayList<>();
        for (K k : other) {
            for (T cur :this){
                if(on.on(cur,k)){
                    resultList.add(join.join(cur,k));
                }else {
                    resultList.add(join.join(null,k));
                }
            }
        }
        return resultList;
    }

    @Override
    public List<T> head(int n) {
        List<T> tsList = toLists();
        if (tsList.isEmpty()){
            return Collections.emptyList();
        }

        if (n >= tsList.size()){
            return tsList;
        }
        return tsList.subList(0,n);
    }

    @Override
    public List<T> tail(int n) {
        List<T> tsList = toLists();
        if (tsList.isEmpty()){
            return Collections.emptyList();
        }

        if (n >= tsList.size()){
            return tsList;
        }
        return tsList.subList(tsList.size()-1-n+1,tsList.size());
    }


    @Override
    public T head() {
        List<T> ts = toLists();
        return ts.isEmpty() ? null : ts.get(0);
    }

    @Override
    public T tail() {
        List<T> ts = toLists();
        return ts.isEmpty() ? null : ts.get(ts.size()-1);
    }

    @Override
    public List<T> subList(Integer startIndex, Integer endIndex) {
        List<T> ts = toLists();
        if (startIndex == null || startIndex < 0){
            startIndex = 0;
        }
        if (endIndex == null || endIndex > ts.size()){
            endIndex = ts.size();
        }
        return ts.subList(startIndex,endIndex);
    }

    protected static <T, C> List<T> replenish(List<T> itemDTOList,
                                              Function<T, C> collectDim,
                                              List<C> allDim,
                                              Function<C,T> getEmptyObject){
        allDim = new ArrayList<>(new HashSet<>(allDim));
        // 计算差集，然后补充
        List<C> collect = itemDTOList.stream().map(collectDim).collect(toList());
        collect = new ArrayList<>(new HashSet<>(collect));
        // 计算差集，然后补充
        allDim.removeAll(collect);
        List<T> collect1 = allDim.stream().map(getEmptyObject).collect(toList());
        itemDTOList.addAll(collect1);
        return itemDTOList;
    }


    /**
     * 分组计算差集， 然后将差集补充到该分组内
     *
     *      将原始集合(itemDTOList) 按照groupDim维度进分组， 然后将每个分组内的所有collectDim字段进行汇总
     *      汇总后 与 allAbscissa进行计算差集，这些差集就是需要补充的条目， 然后将这些差集按照getEmptyObject逻辑生成空对象添加到该分组内
     *
     * @param itemDTOList           原始集合
     * @param groupDim              分组的维度字段
     * @param collectDim            组内收集的数据字段
     * @param allDim                组内需要展示的所有维度
     * @param getEmptyObject        生成空对象的逻辑
     *
     * @param <T>                   原始集合的类型
     * @param <G>                   分组的类型
     * @param <C>                   组内收集的类型
     *
     * @return 补充后的集合
     */
    public static  <T,G, C> List<T> replenish(List<T> itemDTOList,
                                              Function<T, G> groupDim,
                                              Function<T, C> collectDim,
                                              List<C> allDim,
                                              ReplenishFunction<G,C,T> getEmptyObject){
        // 计算差集，然后补充
        Map<G, List<T>> nameItemListMap = itemDTOList.stream().collect(groupingBy(groupDim));
        nameItemListMap.forEach((name, itemList) -> {
            List<C> tmpAll = new ArrayList<>(allDim);
            List<C> abasicssaList = itemList.stream().map(collectDim).collect(toList());
            tmpAll.removeAll(abasicssaList);
            if (ListUtils.isNotEmpty(tmpAll)) {
                List<T> missingList = tmpAll.stream().map(e -> getEmptyObject.apply(name, e)).collect(toList());
                itemList.addAll(missingList);
            }
        });

        return nameItemListMap.values().stream().flatMap(Collection::stream).collect(toList());
    }


    protected  static <T,G, C> List<T> replenish(List<T> itemDTOList,
                                                 Function<T, G> groupDim,
                                                 Function<T, C> collectDim,
                                                 ReplenishFunction<G,C,T> getEmptyObject) {
        //Map<G, List<C>> nameAbscissaMap = itemDTOList.stream().collect(groupingBy(groupDim, Collectors.collectingAndThen(toList(), e -> e.stream().map(collectDim).collect(toList()))));
        //List<C> allDim = mergeCollection(nameAbscissaMap.values());
        List<C> allDim = itemDTOList.stream().map(collectDim).filter(Objects::nonNull).collect(toList());
        allDim = new ArrayList<>(new HashSet<>(allDim));
        return replenish(itemDTOList,groupDim,collectDim,allDim,getEmptyObject);
    }

    protected static <C> List<C> mergeCollection(Collection<List<C>> values) {
        List<C> allAbscissa = values.stream().flatMap(Collection::stream).collect(toList());
        allAbscissa = new HashSet<>(allAbscissa).stream().collect(toList());
        return allAbscissa;
    }

    protected <R> R getApplyValue(Function<T, R> fun,T obj){
        return obj == null ? null : fun.apply(obj);
    }

    protected  <F> Stream<T> fi2Stream(Stream<FI2<T, F>> stream, SetFunction<T, F> setFunction){
        return stream.map(e -> {
            setFunction.accept(e.getC1(),e.getC2());
            return e.getC1();
        });
    }

    protected Stream<FI2<T, String>> explodeStringStream(Function<T, String> getFunction, String delimiter){
        return stream().flatMap(e -> {
            String fieldValue = getFunction.apply(e);
            if (StringUtils.isBlank(fieldValue)) {
                return Stream.of(new FI2<>(e, fieldValue));
            }
            fieldValue = fieldValue.trim();
            fieldValue = StringUtils.strip(fieldValue, "[]");
            String[] arrText = fieldValue.split(delimiter);
            int length = arrText.length;
            if (length <= 1){
                return Stream.of(new FI2<>(e, getFunction.apply(e)));
            }
            return  Arrays.stream(arrText).map(text -> new FI2<>(BeanCopyUtil.copyProperties(e, (Class<T>)e.getClass()),  StringUtils.strip(text, "\""))).collect(toList()).stream();
        });
    }

    protected Stream<FI2<T, String>> explodeJsonArrayStream(Function<T, String> getFunction){
        return stream().flatMap(e -> {
            String fieldValue = getFunction.apply(e);
            if (StringUtils.isBlank(fieldValue) || !JSON.isValidArray(fieldValue)) {
                return Stream.of(new FI2<>(e, fieldValue));
            }
            JSONArray objects = JSON.parseArray(fieldValue);
            if(objects.isEmpty()){
                return Stream.of(new FI2<>(e, fieldValue));
            }
            if (objects.size() == 1){
                return Stream.of(new FI2<>(e, objects.get(0).toString()));
            }
            return  objects.stream().map(text -> new FI2<>(BeanCopyUtil.copyProperties(e, (Class<T>)e.getClass()), text.toString())).collect(toList()).stream();
        });
    }

    protected  <E> Stream<FI2<T,E>> explodeCollectionStream(Function<T, ? extends Collection<E>> getFunction){
        return stream().flatMap(e -> {
            Object fieldValue = getFunction.apply(e);
            if (fieldValue == null) {
                return Stream.of(new FI2<>(e, null));
            }

            Class<?> fieldValueClass = fieldValue.getClass();
            if (!Collection.class.isAssignableFrom(fieldValueClass)) {
                return Stream.of(new FI2<>(e, null));
            }
            Collection<Object> objects = (Collection<Object>) fieldValue;
            if (objects.isEmpty()) {
                return Stream.of(new FI2<>(e, null));
            } else if (objects.size() == 1) {
                return Stream.of(new FI2<>(e, (E)objects.iterator().next()));
            }
            return objects.stream().map(text -> new FI2<>(BeanCopyUtil.copyProperties(e, (Class<T>) e.getClass()), (E) text)).collect(toList()).stream();
        });
    }


    protected  <E> Stream<FI2<T,E>> explodeCollectionArrayStream(Function<T,?> getFunction,Class<E> elementClass){
        return stream().flatMap(e -> {
            Object fieldValue = getFunction.apply(e);
            if (fieldValue == null) {
                return Stream.of(new FI2<>(e, null));
            }

            Class<?> fieldValueClass = fieldValue.getClass();
            if (!fieldValueClass.isArray() && !Collection.class.isAssignableFrom(fieldValueClass)) {
                return Stream.of(new FI2<>(e, null));
            }

            Stream<Object> stream = null;
            if (fieldValueClass.isArray()) {
                Object[] objects = (Object[]) fieldValue;
                stream = Arrays.stream(objects);

                if (objects.length == 0) {
                    return Stream.of(new FI2<>(e, null));
                } else if (objects.length == 1) {
                    return Stream.of(new FI2<>(e, elementClass.cast(objects[0])));
                }
            }else if (Collection.class.isAssignableFrom(fieldValueClass)) {
                Collection<Object> objects = (Collection<Object>) fieldValue;
                if (objects.isEmpty()) {
                    return Stream.of(new FI2<>(e, null));
                } else if (objects.size() == 1) {
                    return Stream.of(new FI2<>(e, elementClass.cast(objects.iterator().next())));
                }
                stream = objects.stream();
            }
            return stream.map(text -> new FI2<>(BeanCopyUtil.copyProperties(e, (Class<T>) e.getClass()), elementClass.cast(text))).collect(toList()).stream();
        });
    }


}
