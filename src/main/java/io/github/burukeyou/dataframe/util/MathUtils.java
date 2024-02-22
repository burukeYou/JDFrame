package io.github.burukeyou.dataframe.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 *  数学工具
 */
public class MathUtils {

    private MathUtils() {
    }

    public static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /**
     *  小数转百分比
     */
    public static BigDecimal percentage(BigDecimal dividend){
        if (dividend == null){
            return null;
        }
        return dividend.multiply(ONE_HUNDRED).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal percentage(BigDecimal dividend,int scale){
        if (dividend == null){
            return null;
        }
        return dividend.multiply(ONE_HUNDRED).setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 占比计算
     * @param dividend 被除数
     * @param divisor 除数
     * @param scale 精度
     * @return (dividend 除以 divisor) 乘以 100
     */
    public static BigDecimal proportion(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (dividend == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        return dividend.divide(divisor,8,RoundingMode.HALF_UP).multiply(ONE_HUNDRED).setScale(scale, RoundingMode.HALF_UP);
    }

    public static <T1,T2> BigDecimal proportion(T1 dividend, T2 divisor, int scale) {
        if (dividend == null || divisor == null) {
            return null;
        }

        BigDecimal dividendBig = toBigDecimal(dividend);
        BigDecimal divisorBig = toBigDecimal(divisor);
        if (divisorBig.compareTo(BigDecimal.ZERO) == 0){
            return null;
        }
        return proportion(dividendBig,divisorBig,scale);
    }

    /**
     * 除法计算
     * @param dividend 被除数
     * @param divisor 除数
     * @param scale 精度
     * @return 被除数÷除数=商
     */
    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int scale) {
        if (dividend == null || divisor == null || divisor.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return dividend.divide(divisor, 8, RoundingMode.HALF_UP).setScale(scale, RoundingMode.HALF_UP);
    }

    public static <T1,T2> BigDecimal divide(T1 dividend, T2 divisor, int scale) {
        if (dividend == null || divisor == null) {
            return null;
        }
        BigDecimal dividendBig = toBigDecimal(dividend);
        BigDecimal divisorBig = toBigDecimal(divisor);
        if (divisorBig.compareTo(BigDecimal.ZERO) == 0){
            return null;
        }

        return dividendBig.divide(divisorBig, 8, RoundingMode.HALF_UP).setScale(scale, RoundingMode.HALF_UP);
    }

    public static <T> BigDecimal toBigDecimal(T value){
        if (value == null){
            return null;
        }

        if (value instanceof BigDecimal){
            return (BigDecimal)value;
        }

        if (value instanceof Integer){
            return new BigDecimal((Integer)value);
        }

        return new BigDecimal(String.valueOf(value));
    }

    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return divide(dividend, divisor, 8);
    }

    /**
     * 计算增长率 (结果X100)
     *          （当前值 - 上次值）/上次值 * 100%
     * @param nowValue      当前值
     * @param preValue      上次值
     * @param scale
     * @return
     */
    public static BigDecimal calcGrowthRate(BigDecimal nowValue, BigDecimal preValue,int scale) {
        return doCalcGrowthRate(nowValue, preValue, scale, true);
    }

    /**
     * 计算增长率  不乘100
     *          （当前值 - 上次值）/上次值 * 100%
     * @param nowValue      当前值
     * @param preValue      上次值
     * @param scale
     * @return
     */
    public static BigDecimal calcGrowthRateNoMulti100(BigDecimal nowValue, BigDecimal preValue, int scale) {
        return doCalcGrowthRate(nowValue, preValue, scale, false);
    }

    /**
     * 计算增长率
     *          （当前值 - 上次值）/上次值 * 100%
     * @param nowValue      当前值
     * @param preValue      上次值
     * @param scale
     * @return
     */
    public static BigDecimal doCalcGrowthRate(BigDecimal nowValue, BigDecimal preValue,int scale, boolean multi100) {
        if (nowValue == null || preValue == null || preValue.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        BigDecimal subtract = nowValue.subtract(preValue);
        BigDecimal growRate = subtract.divide(preValue,8, BigDecimal.ROUND_HALF_UP);
        if (multi100) {
            growRate = growRate.multiply(ONE_HUNDRED);
        }
        return growRate.setScale(scale, RoundingMode.HALF_UP);
    }


    /**
     * 计算波动率
     */
    public static BigDecimal calcVolatilityRate(List<BigDecimal> valuesList){
        // 平均值
        BigDecimal avg = avg(valuesList);

        // 计算每个离差率
        List<BigDecimal> dispersionRateList = valuesList.stream().map(e -> {
            // ｜(e-avg)/avg｜
            return e.subtract(avg).divide(avg, 8, BigDecimal.ROUND_HALF_UP).abs();
        }).collect(Collectors.toList());

        // 平均离差率 （即波动率）
        BigDecimal result = avg(dispersionRateList);

        if (result.compareTo(BigDecimal.ZERO) < 0){
            result = result.abs();
        }

        // 转成百分数
        result = result.multiply(ONE_HUNDRED).setScale(2, RoundingMode.HALF_UP);
        return result;
    }

    /**
     * 平均值
     * @param bigDecimals
     * @param roundingMode
     * @param scale
     * @return
     */
    public static BigDecimal average(List<BigDecimal> bigDecimals, RoundingMode roundingMode, int scale) {
        BigDecimal sum = bigDecimals.stream()
                .map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(bigDecimals.size()), roundingMode).setScale(scale, roundingMode);
    }

    private static BigDecimal avg(List<BigDecimal> valuesList) {
        return valuesList.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(valuesList.size()), 8, RoundingMode.HALF_UP);
    }

    public static String smartScale(BigDecimal olBValue) {
        if (olBValue == null){
            return "";
        }

        if (olBValue.compareTo(BigDecimal.ZERO) == 0){
            return "0";
        }

        BigDecimal bValue = olBValue.setScale(2, RoundingMode.HALF_UP);
        if (bValue.compareTo(BigDecimal.ZERO) == 0){
            // 尝试保留4位小数
            bValue = olBValue.setScale(4, RoundingMode.HALF_UP);
            if (bValue.compareTo(BigDecimal.ZERO) == 0){
                // 尝试保留5位小数
                bValue = olBValue.setScale(5, RoundingMode.HALF_UP);
            }
        }
        return bValue.toString();
    }
}
