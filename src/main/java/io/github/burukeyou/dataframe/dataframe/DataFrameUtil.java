/*
package io.github.burukeyou.dataframe.dataframe;


import io.github.burukeyou.dataframe.SDFrame;
import io.github.burukeyou.dataframe.dataframe.item.FT2;
import io.github.burukeyou.dataframe.dataframe.item.FT3;
import io.github.burukeyou.dataframe.dataframe.item.FT4;
import io.github.burukeyou.dataframe.util.MathUtils;
import lombok.var;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class DataFrameUtil {

    private DataFrameUtil(){}



    */
/**
     * 根据列表的某一个列计算占比，并把占比赋值给其他一列
     * @param dataList          列表数据
     * @param function          计算的列
     * @param setFunction       占比赋值的列
     * @param <T>               列表类型
     * @param <R>               计算的列的类型
     *//*

    public static <T,R> void calcOccupyForList(List<T> dataList,
                                               Function<T,R> function,
                                               SetFunction<T,BigDecimal> setFunction){
        // 汇总该列的和
        BigDecimal sum = SDFrame.read(dataList).sum(function);
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

    */
/**
     * 对DataFrame的某一列进行计算占比，并把结果作为新的一列
     * @param dataList              数据列表
     * @param function              计算的列
     *//*

    public static <T,E> List<FT3<T,E, BigDecimal>> calcOccupyForItem2(List<FT2<T,E>> dataList,
                                                                      Function<FT2<T,E>,E> function){
        // 汇总该列的和
        BigDecimal sum = SDFrame.read(dataList).sum(function);
        return calcOccupyForItem2(dataList,function,sum);
    }

    */
/**
     * 对DataFrame的某一列进行计算占比，并把结果作为新的一列
     * @param dataList              数据列表
     * @param function              计算的列
     * @param sum                   手动指定计算占比的分母
     *//*

    public static <T,E> List<FT3<T,E, BigDecimal>> calcOccupyForItem2(List<FT2<T,E>> dataList,
                                                                      Function<FT2<T,E>,E> function,
                                                                      BigDecimal sum){
        List<FT3<T,E, BigDecimal>> result = new ArrayList<>(dataList.size());
        for (FT2<T,E> item : dataList) {
            E value = function.apply(item);
            BigDecimal occupy = null;
            if (value != null){
                occupy = MathUtils.proportion(new BigDecimal(value.toString()), sum, 2);
            }
            result.add(new FT3<>(item.getC1(), item.getC2(), occupy));
        }
        return result;
    }

    public static <T,E,R extends Number> List<FT4<T,E, R,BigDecimal>> calcOccupyForItem3(List<FT3<T,E, R>> dataList,
                                                                                         Function<FT3<T,E, R>, R> function){
        // 汇总该列的和
        BigDecimal sum = SDFrame.read(dataList).sum(function);
        List<FT4<T,E, R,BigDecimal>> result = new ArrayList<>(dataList.size());
        for (FT3<T,E, R> item : dataList) {
            R value = function.apply(item);
            BigDecimal occupy = null;
            if (value != null){
                occupy = MathUtils.proportion(new BigDecimal(value.toString()), sum, 2);
            }
            result.add(new FT4<>(item.getC1(), item.getC2(), item.getC3(),occupy));
        }
        return result;
    }

    public static void main(String[] args) {
        List<FT2<String,String>> dataList = new ArrayList<>();
        dataList.add(new FT2<>("4","1"));
        dataList.add(new FT2<>("5","2"));
        dataList.add(new FT2<>("8","3"));
        //List<DataFrameItem3<String,String, BigDecimal>> result = calcOccupyForItem2(dataList, DataFrameItem2::getC1);
        var resulT = calcOccupyForItem2(dataList, FT2::getC1);
        System.out.println();
    }
}
*/
