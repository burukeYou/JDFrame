package io.github.burukeyou.dataframe.dataframe.function;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;

/**
 *  DataFrame 内嵌函数
 */
public class TimeFunction {

    private TimeFunction(){}

    /**
     * 获取日期字符串对应的所属年份
     * @param time          日期字符串   2022-02-22 11:09:38
     * @return              年份        2022
     */
    public static int getTimeYear(String time) {
        // 不关注时分秒去掉
        String[] split = time.split(" ");
        String yearMonthDate = split[0];
        return LocalDate.parse(yearMonthDate).getYear();
    }

    /**
     * 获取日期字符串对应的所属年-月
     * @param time          日期字符串   2022-02-22 11:09:38
     * @return              年-月       2022-02
     */
    public static String getTimeYearMonth(String time) {
        return time.substring(0,time.lastIndexOf('-'));
    }

    public static YearMonth getTimeYearMonth(LocalDateTime dateTime) {
        return YearMonth.of(dateTime.getYear(),dateTime.getMonth());
    }

    public static String getTimeYearMonthStr(LocalDateTime dateTime) {
        return YearMonth.of(dateTime.getYear(),dateTime.getMonth()).toString();
    }

    /**
     * 获取日期字符串对应的所属年-月-日
     * @param time          日期字符串   2022-02-22 11:09:38
     * @return              年-月       2022-02-22
     */
    public static String getTimeDate(String time) {
        return time.substring(0,time.lastIndexOf(' '));
    }



}
