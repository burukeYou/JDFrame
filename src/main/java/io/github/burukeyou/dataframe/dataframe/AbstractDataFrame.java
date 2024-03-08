package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.IFrame;
import io.github.burukeyou.dataframe.dataframe.item.FT2;
import io.github.burukeyou.dataframe.dataframe.item.FT3;
import io.github.burukeyou.dataframe.dataframe.item.FT4;
import io.github.burukeyou.dataframe.dataframe.support.Join;
import io.github.burukeyou.dataframe.dataframe.support.JoinOn;
import io.github.burukeyou.dataframe.util.CollectorsPlusUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author caizhihao
 * @param <T>
 */
@Slf4j
@Getter
public abstract class AbstractDataFrame<T> implements IFrame<T> {

    private static final String MSG = "****";

    protected List<String> fieldList = new ArrayList<>();

    protected AbstractDataFrame() {

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
        return stream().filter(e -> value.equals(function.apply(e)));
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

    private static <R extends Number> R bigDecimalToClassValue(BigDecimal value, Class<R> valueClass) {
        if (value == null) {
           return null;
        }
        if (BigDecimal.class.equals(valueClass)){
            return (R)value;
        }
        else if (Byte.class.equals(valueClass)) {
            return valueClass.cast(value.byteValue());
        } else if (Short.class.equals(valueClass)) {
            return valueClass.cast(value.shortValue());
        } else if (Integer.class.equals(valueClass) || int.class.equals(valueClass)) {
            return (R)Integer.valueOf(value.intValue());
        } else if (Long.class.equals(valueClass) || long.class.equals(valueClass)) {
            return (R)Long.valueOf(value.longValue());
        } else if (Float.class.equals(valueClass)) {
            return(R)Float.valueOf(value.floatValue());
        } else if (Double.class.equals(valueClass)) {
            return (R)Double.valueOf(value.doubleValue());
        } else {
            throw new IllegalArgumentException("Unsupported Number class: " + valueClass.getName());
        }
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
                .divide(BigDecimal.valueOf(bigDecimalList.size()));
    }

    public <R extends Comparable<R>> MaxMin<R> maxMinValue(Function<T, R> function) {
        MaxMin<T> maxAndMin = maxMin(function);
        return new MaxMin<>(function.apply(maxAndMin.getMax()), function.apply(maxAndMin.getMin()));
    }

    public <R extends Comparable<R>> MaxMin<T> maxMin(Function<T, R> function) {
        List<T> itemList = stream().filter(e -> function.apply(e) != null).collect(toList());
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

    public long count() {
        return stream().count();
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

    public <R> Stream<T> streamFilterNull(Function<T,R> function){
        return stream().filter(e -> function.apply(e) != null);
    }

    @Override
    public Iterator<T> iterator() {
        return toLists().iterator();
    }

    protected  <F> List<String> buildFieldList(F f){
        List<String> filedList = new ArrayList<>();
        Arrays.stream(f.getClass().getDeclaredFields()).forEach(field -> {
            field.setAccessible(true);
            filedList.add(field.getName());
        });
        return filedList;
    }

    @Override
    public List<String> columns() {
        return fieldList;
    }

    @Override
    public void print(){
        String[][] dataArr = buildPrintDataArr(-1);
        if (dataArr == null){
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataArr.length; i++) {
            for (int j = 0; j < dataArr[0].length; j++) {
                sb.append(dataArr[i][j].replace(MSG, "\t"));
            }
        }
        log.info("\n{}",sb);
    }

    private String[][] buildPrintDataArr(int limit) {
        List<T> dataList = toLists();
        if (dataList.isEmpty()){
            return null;
        }

        List<String> filedList = getFieldList();
        int rowLen = dataList.size() + 1;
        int colLen = filedList.size() * 2 + 1;

        String[][] dataArr = new String[rowLen][colLen];


        int index1 = 0;
        for (String field : filedList) {
            dataArr[0][index1++] = field;
            dataArr[0][index1++] = MSG;
        }
        dataArr[0][index1] = "\n";

        index1 = 0;
        if (dataList.isEmpty()) {
            for (String field : filedList) {
                dataArr[1][index1++] = "";
                dataArr[1][index1++] = MSG;
            }
        }

        int row = 1;
        for (T t : dataList) {
            int tmpIndex = 0;
            for (String fieldName : filedList) {
                try {
                    Field field = t.getClass().getDeclaredField(fieldName);
                    field.setAccessible(true);
                    Object o = field.get(t);
                    dataArr[row][tmpIndex++] = o == null ? "" : o.toString();
                    dataArr[row][tmpIndex++] = MSG;
                } catch (NoSuchFieldException | IllegalAccessException e) {
                   throw new RuntimeException(e);
                }
            }
            dataArr[row][tmpIndex] = "\n";
            row++;
        }

        // 格式对齐
        for (int i = 0; i < colLen -1; i++) {
            int maxStrLen = -1;
            for (int j = 0; j < rowLen; j++) {
                if (Objects.equals(dataArr[j][i], MSG)) {
                    continue;
                }
                if (dataArr[j][i].length() > maxStrLen) {
                    maxStrLen = dataArr[j][i].length();
                }
            }
            if (maxStrLen != -1) {
                for (int j = 0; j < rowLen; j++) {
                    int need = maxStrLen - dataArr[j][i].length();
                    if (need > 0 ) {
                        dataArr[j][i] = dataArr[j][i] + getSpace(need);
                    }
                }
            }
        }
        return dataArr;
    }

    private String getSpace(int need) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < need; i++) {
            sb.append(" ");
        }
        return sb.toString();
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
}
