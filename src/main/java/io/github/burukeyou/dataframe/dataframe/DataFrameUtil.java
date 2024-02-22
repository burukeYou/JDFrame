package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.dataframe.item.FItem2;
import io.github.burukeyou.dataframe.dataframe.item.FItem3;
import io.github.burukeyou.dataframe.dataframe.item.FItem4;
import io.github.burukeyou.dataframe.util.ListUtils;
import io.github.burukeyou.dataframe.util.MathUtils;
import lombok.var;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataFrameUtil {

    private DataFrameUtil(){}

    /**
     *  占比列补充
     *          占比列在除不尽的情况下，可能存在精度缺失问题，导致加起来不等于100%
     *
     * @param dataList                  列表数据
     * @param rateGetFunction           占比列的获取函数
     * @param setFunction               占比列的赋值函数
     * @param rateColumnIsPercentage    占比列是否是百分数、否则默认是小数
     */
    public static <T> void fillRateColumn(List<T> dataList,
                                          Function<T,BigDecimal> rateGetFunction,
                                          SetFunction<T,BigDecimal> setFunction,
                                          boolean rateColumnIsPercentage){

        // 存在多个元素，才需要补充
        if (ListUtils.isEmpty(dataList) || dataList.size() <= 1){
            return;
        }

        BigDecimal sum = DataFrame.read(dataList).sum(rateGetFunction);
        T t = dataList.get(dataList.size() - 1);
        BigDecimal totalSum =  rateColumnIsPercentage ? new BigDecimal("100") : new BigDecimal("1");
        BigDecimal diff = totalSum.subtract(sum);
        if (diff.compareTo(BigDecimal.ZERO) != 0){
            BigDecimal lastValue = rateGetFunction.apply(t);
            if (lastValue != null){
                lastValue = lastValue.add(diff);
                setFunction.accept(t, lastValue);
            }
        }
    }

    public static <T> void fillRateDecimalColumn(List<T> dataList,
                                                    Function<T,BigDecimal> rateGetFunction,
                                                    SetFunction<T,BigDecimal> setFunction){
        fillRateColumn(dataList, rateGetFunction, setFunction, false);
    }


    public static <T> void fillRatePercentageColumn(List<T> dataList,
                                          Function<T,BigDecimal> rateGetFunction,
                                          SetFunction<T,BigDecimal> setFunction){
        fillRateColumn(dataList, rateGetFunction, setFunction, true);
    }

    /**
     * 根据列表的某一个列计算占比，并把占比赋值给其他一列
     * @param dataList          列表数据
     * @param function          计算的列
     * @param setFunction       占比赋值的列
     * @param <T>               列表类型
     * @param <R>               计算的列的类型
     */
    public static <T,R> void calcOccupyForList(List<T> dataList,
                                               Function<T,R> function,
                                               SetFunction<T,BigDecimal> setFunction){
        // 汇总该列的和
        BigDecimal sum = DataFrame.read(dataList).sum(function);
        calcOccupyForList(dataList, function, setFunction, sum);
    }

    public static <T,R> void calcOccupyForList(List<T> dataList,
                                               Function<T,R> function,
                                               SetFunction<T,BigDecimal> setFunction,
                                               BigDecimal sum){
        for (T e : dataList) {
            R value = function.apply(e);
            BigDecimal occupy = null;
            if (value != null){
                occupy = MathUtils.proportion(new BigDecimal(value.toString()), sum, 2);
                setFunction.accept(e, occupy);
            }
        }
    }

    /**
     * 对DataFrame的某一列进行计算占比，并把结果作为新的一列
     * @param dataList              数据列表
     * @param function              计算的列
     */
    public static <T,E> List<FItem3<T,E, BigDecimal>> calcOccupyForItem2(List<FItem2<T,E>> dataList,
                                                                         Function<FItem2<T,E>,E> function){
        // 汇总该列的和
        BigDecimal sum = DataFrame.read(dataList).sum(function);
        return calcOccupyForItem2(dataList,function,sum);
    }

    /**
     * 对DataFrame的某一列进行计算占比，并把结果作为新的一列
     * @param dataList              数据列表
     * @param function              计算的列
     * @param sum                   手动指定计算占比的分母
     */
    public static <T,E> List<FItem3<T,E, BigDecimal>> calcOccupyForItem2(List<FItem2<T,E>> dataList,
                                                                             Function<FItem2<T,E>,E> function,
                                                                             BigDecimal sum){
        List<FItem3<T,E, BigDecimal>> result = new ArrayList<>(dataList.size());
        for (FItem2<T,E> item : dataList) {
            E value = function.apply(item);
            BigDecimal occupy = null;
            if (value != null){
                occupy = MathUtils.proportion(new BigDecimal(value.toString()), sum, 2);
            }
            result.add(new FItem3<>(item.getC1(), item.getC2(), occupy));
        }
        return result;
    }

    public static <T,E,R extends Number> List<FItem4<T,E, R,BigDecimal>> calcOccupyForItem3(List<FItem3<T,E, R>> dataList,
                                                                                            Function<FItem3<T,E, R>, R> function){
        // 汇总该列的和
        BigDecimal sum = DataFrame.read(dataList).sum(function);
        List<FItem4<T,E, R,BigDecimal>> result = new ArrayList<>(dataList.size());
        for (FItem3<T,E, R> item : dataList) {
            R value = function.apply(item);
            BigDecimal occupy = null;
            if (value != null){
                occupy = MathUtils.proportion(new BigDecimal(value.toString()), sum, 2);
            }
            result.add(new FItem4<>(item.getC1(), item.getC2(), item.getC3(),occupy));
        }
        return result;
    }

    public static void main(String[] args) {
        List<FItem2<String,String>> dataList = new ArrayList<>();
        dataList.add(new FItem2<>("4","1"));
        dataList.add(new FItem2<>("5","2"));
        dataList.add(new FItem2<>("8","3"));
        //List<DataFrameItem3<String,String, BigDecimal>> result = calcOccupyForItem2(dataList, DataFrameItem2::getC1);
        var resulT = calcOccupyForItem2(dataList, FItem2::getC1);
        System.out.println();
    }
}
