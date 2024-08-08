package io.github.burukeyou.dataframe.iframe.impl;

import com.alibaba.fastjson.JSON;
import io.github.burukeyou.dataframe.iframe.IFrame;
import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.util.ClassUtil;
import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static java.lang.Character.UnicodeBlock.*;

@Getter
public abstract class AbstractCommonFrame<T> implements IFrame<T> {

    protected static final String MSG = "****";

    protected List<String> fieldList = new ArrayList<>();

    private  Map<String,Field> fieldNameMap = new HashMap<>();

    protected Class<?> fieldClass;

    protected int defaultScale = 2;
    protected RoundingMode defaultRoundingMode = RoundingMode.HALF_UP;

    protected abstract  List<T> dataList();

    protected int getOldRoundingMode(){
            switch (defaultRoundingMode){
                case UP:
                    return BigDecimal.ROUND_UP;
                case DOWN:
                    return BigDecimal.ROUND_DOWN;
                case CEILING:
                    return BigDecimal.ROUND_CEILING;
                case FLOOR:
                    return BigDecimal.ROUND_FLOOR;
                case HALF_UP:
                    return BigDecimal.ROUND_HALF_UP;
                case HALF_DOWN:
                    return BigDecimal.ROUND_HALF_DOWN;
                case HALF_EVEN:
                    return BigDecimal.ROUND_HALF_EVEN;
                case UNNECESSARY:
                    return BigDecimal.ROUND_UNNECESSARY;
            }
            throw new IllegalArgumentException("can not find round mode for " + defaultRoundingMode);
    }

    protected void initDefaultScale(int scale,RoundingMode roundingMode){
        this.defaultScale = scale;
        this.defaultRoundingMode = roundingMode;
    }

    protected  void transmitMember(AbstractCommonFrame<?> from, AbstractCommonFrame<?> toFrame) {
        toFrame.defaultScale = from.defaultScale;
        toFrame.defaultRoundingMode = from.defaultRoundingMode;
    }

    public List<String> getFieldList() {
        if (!fieldList.isEmpty()){
            return fieldList;
        }
        if (fieldClass == null){
            return Collections.emptyList();
        }

        List<Field> allFiled = ClassUtil.findAllFiled(fieldClass);
        for (Field field : allFiled) {
            String name = field.getName();
            fieldNameMap.put(name,field);
            fieldList.add(name);
        }
        return fieldList;
    }

    protected StringBuilder getShowString(int n) {
        if (fieldClass == null){
            return new StringBuilder();
        }

        if (isNormalType(fieldClass)) {
            return new StringBuilder(JSON.toJSONString(dataList()));
        }

        String[][] dataArr = buildPrintDataArr(n);
        if (dataArr == null || dataArr.length <= 0){
            return new StringBuilder("\n");
        }
        StringBuilder sb = new StringBuilder("\n");
        for (int i = 0; i < dataArr.length; i++) {
            for (int j = 0; j < dataArr[0].length; j++) {
                sb.append(dataArr[i][j].replace(MSG, "\t"));
            }
        }
        return sb;
    }

    protected String[][] buildPrintDataArr(int limit) {
        List<T> dataList = dataList();
        if (dataList.isEmpty()){
            return null;
        }

        List<String> filedList = getFieldList();
        int rowLen =  (Math.min(limit, dataList.size())) + 1;
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
            if (row > limit){
                break;
            }

            int tmpIndex = 0;
            for (String fieldName : filedList) {
                try {
                    Field field = fieldNameMap.get(fieldName);
                    field.setAccessible(true);
                    Object o = field.get(t);
                    dataArr[row][tmpIndex++] = o == null ? "" : o.toString();
                    dataArr[row][tmpIndex++] = MSG;
                } catch (IllegalAccessException e) {
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
                    maxStrLen = getStrLength(dataArr[j][i]);
                }
            }
            if (maxStrLen != -1) {
                for (int j = 0; j < rowLen; j++) {
                    int need = maxStrLen - getStrLength(dataArr[j][i]);
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

    public static int getStrLength(String str) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (isChineseChar(c)){
                count += 2;
            }else {
                count += 1;
            }
        }
        return count;
    }

    private static boolean isChineseChar(char checkChar) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(checkChar);
        if (CJK_UNIFIED_IDEOGRAPHS == ub||CJK_COMPATIBILITY_IDEOGRAPHS==ub||
                CJK_COMPATIBILITY_FORMS == ub|| CJK_RADICALS_SUPPLEMENT == ub||
                CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A == ub||CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B==ub) {
            return true;
        }
        return false;
    }


    protected boolean isNormalType(Class<?> fieldClass) {
        if (fieldClass.isPrimitive()){
            return true;
        }
        if (String.class.equals(fieldClass)){
            return true;
        }
        List<?> classes = Arrays.asList(Integer.class, Boolean.class, Double.class,
                Float.class, BigDecimal.class, Long.class,
                Byte.class, Short.class, Character.class
        );
        if (classes.contains(fieldClass)){
            return true;
        }
        if (fieldClass.isArray() || Collection.class.isAssignableFrom(fieldClass) || Map.class.isAssignableFrom(fieldClass)){
            return false;
        }
        if (fieldClass.getClassLoader().equals(this.getClass().getClassLoader())){
            return false;
        }
        return false;
    }

    protected Type[] getSuperClassActualTypeArguments(Class<?> clz){
        Type superclass = clz.getGenericSuperclass();
        if (superclass instanceof ParameterizedType){
            return  ((ParameterizedType)superclass).getActualTypeArguments();
        }
        return null;
    }

    protected Type[] getSuperInterfaceActualTypeArguments(Class<?> clz){
        Type[] genericInterfaces = clz.getGenericInterfaces();
        if (genericInterfaces[0] instanceof ParameterizedType){
            return  ((ParameterizedType)genericInterfaces[0]).getActualTypeArguments();
        }
        return null;
    }

    protected static <R extends Number> R bigDecimalToClassValue(BigDecimal value, Class<R> valueClass) {
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

    protected List<FI2<T,Integer>> rankingSameAsc(List<T> data, Comparator<T> comparator){
        return rankingSameAsc(data,comparator,data.size());
    }

    protected List<FI2<T,Integer>> rankingSameAsc(List<T> data, Comparator<T> comparator, int n) {
        if (data.isEmpty()){
            return Collections.emptyList();
        }
        if (n <= 0){
            throw new IllegalArgumentException("first N should greater than zero");
        }

        data.sort(comparator);
        int rank = 1;
        List<FI2<T,Integer>> tmpDataList = new ArrayList<>();
        tmpDataList.add(new FI2<>(data.get(0),1));
        for (int i = 1; i < data.size(); i++) {
            T pre = data.get(i-1);
            T cur = data.get(i);
            if (comparator.compare(pre,cur) != 0){
                rank += 1;
            }
            if (rank <= n){
                tmpDataList.add(new FI2<>(cur,rank));
            }else {
                break;
            }
        }
        return tmpDataList;
    }

}
