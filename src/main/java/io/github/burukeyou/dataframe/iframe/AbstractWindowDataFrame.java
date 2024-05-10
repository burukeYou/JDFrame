package io.github.burukeyou.dataframe.iframe;

import io.github.burukeyou.dataframe.iframe.item.FI2;
import io.github.burukeyou.dataframe.iframe.window.SupplierFunction;
import io.github.burukeyou.dataframe.iframe.window.Window;
import io.github.burukeyou.dataframe.iframe.window.WindowBuilder;
import io.github.burukeyou.dataframe.iframe.window.round.Round;
import io.github.burukeyou.dataframe.util.FieldValueList;
import io.github.burukeyou.dataframe.util.ListUtils;
import io.github.burukeyou.dataframe.util.MathUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

/**
 * @author  caizhihao
 * @param <T>
 */
public abstract class AbstractWindowDataFrame<T> extends AbstractCommonFrame<T>{

    protected final Window<T> EMPTY_WINDOW = new WindowBuilder<>(Round.START_ROW,Round.END_ROW);

    protected Window<T> window;


    protected  <V> List<FI2<T, V>> overAbject(Window<T> overParam,
                                              SupplierFunction<T,V> supplier) {
        ((WindowBuilder<T>)overParam).initDefault();
        List<T> windowList = toLists();
        List<FI2<T, V>> result = new ArrayList<>();
        if (ListUtils.isEmpty(windowList)){
            return result;
        }

        Comparator<T> comparator = overParam.getComparator();
        List<Function<T,?>> partitionList = overParam.partitions();
        if (ListUtils.isEmpty(partitionList)){
            if (comparator != null){
                windowList.sort(comparator);
            }
            return supplier.get(windowList);
        }

        // 获取每个窗口
        List<List<T>> allWindowList = new ArrayList<>();
        dfsFindWindow(allWindowList,windowList,partitionList,0);

        for (List<T> data : allWindowList) {
            if (comparator != null){
                data.sort(comparator);
            }
            List<FI2<T, V>> tmpList = supplier.get(data);
            result.addAll(tmpList);
        }

        return result;
    }

    protected  void dfsFindWindow(List<List<T>> result,
                                  List<T> windowList,
                                  List<Function<T, ?>> partitionList,
                                  int index){
        if (index >= partitionList.size()){
            result.add(windowList);
            return;
        }
        Function<T,?> partitionBy = partitionList.get(index);
        Map<?,List<T>> collect = windowList.stream().collect(groupingBy(partitionBy));
        for (List<T> window : collect.values()) {
            dfsFindWindow(result,window,partitionList,index+1);
        }
    }

    protected List<FI2<T, Integer>> windowFunctionForRowNumber(Window<T> overParam) {
        SupplierFunction<T,Integer> supplier = windowList -> {
            List<FI2<T, Integer>> result = new ArrayList<>();
            int index = 1;
            for (T t : windowList) {
                result.add(new FI2<>(t,index++));
            }
            return result;
        };
        return overAbject(overParam, supplier);
    }

    protected  List<FI2<T, Integer>> windowFunctionForRank(Window<T> overParam) {
        checkWindow(overParam);

        SupplierFunction<T,Integer> supplier = (windowList) -> {
            List<FI2<T, Integer>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            result.add(new FI2<>(windowList.get(0), 1));
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (overParam.getComparator().compare(pre,cur) != 0){
                    rank = i + 1;
                }
                if (rank <= n){
                    result.add(new FI2<>(cur, rank));
                }else {
                    break;
                }
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected List<FI2<T, Integer>> windowFunctionForDenseRank(Window<T> overParam) {
        checkWindow(overParam);

        SupplierFunction<T,Integer> supplier = (windowList) -> {
            List<FI2<T, Integer>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            result.add(new FI2<>(windowList.get(0), 1));
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (overParam.getComparator().compare(pre,cur) != 0){
                    rank += 1;
                }
                if (rank <= n){
                    result.add(new FI2<>(cur, rank));
                }else {
                    break;
                }
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected  List<FI2<T, BigDecimal>> windowFunctionForPercentRank(Window<T> overParam) {
        checkWindow(overParam);

        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            // (rank-1) / (rows-1)
            List<FI2<T, BigDecimal>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            result.add(new FI2<>(windowList.get(0), BigDecimal.ZERO));
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (overParam.getComparator().compare(pre,cur) != 0){
                    rank = i + 1;
                }
                if (rank <= n){
                    BigDecimal divide = MathUtils.divide((rank - 1), windowList.size() - 1, defaultScale,defaultRoundingMode);
                    result.add(new FI2<>(cur, divide));
                }else {
                    break;
                }
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected  List<FI2<T, BigDecimal>> windowFunctionForCumeDist(Window<T> overParam) {
        checkWindow(overParam);

        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            List<FI2<T, Integer>> result = new ArrayList<>();
            int n = windowList.size();
            int rank = 1;
            Map<Integer,Integer> rankCountMap = new HashMap<>();
            for (int i = 1; i < windowList.size(); i++) {
                T pre = windowList.get(i-1);
                T cur = windowList.get(i);
                if (overParam.getComparator().compare(pre,cur) != 0){
                    // 次数的rank累积的计数最大
                    rankCountMap.put(rank,i);
                    rank = i + 1;
                }
                if (rank <= n){
                    result.add(new FI2<>(cur, rank));
                }else {
                    break;
                }
            }
            // 最大排名
            rankCountMap.computeIfAbsent(rank, k -> windowList.size());
            List<FI2<T, BigDecimal>> resultList = new ArrayList<>();
            result.forEach(e -> {
                Integer count = rankCountMap.get(e.getC2());
                BigDecimal divide = MathUtils.divide(count, windowList.size(), defaultScale,defaultRoundingMode);
                resultList.add(new FI2<>(e.getC1(),divide));
            });
            return resultList;
        };

        return overAbject(overParam,supplier);
    }

    private void checkWindow(Window<T> overParam) {
        Comparator<T> comparator = overParam.getComparator();
        if (comparator == null){
            throw new IllegalArgumentException("please specify window sort");
        }
    }

    /**
     * 获取当前行的前N行的值
     */
    protected <F> List<FI2<T, F>> windowFunctionForLag(Window<T> overParam, Function<T, F> field, int n) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            List<FI2<T, F>> result = new ArrayList<>();
            for (int i = 0; i < windowList.size(); i++) {
                int preIndex = i - n;
                if (preIndex < 0){
                    result.add(new FI2<>(windowList.get(i),null));
                    continue;
                }

                FI2<Integer, Integer> indexRange = getIndexRange(overParam, i, windowList);
                if (!isInRange(indexRange,preIndex)){
                    preIndex = -1;
                }

                F value = null;
                if (preIndex >= 0 && preIndex < windowList.size()){
                    value = field.apply(windowList.get(preIndex));
                }
                result.add(new FI2<>(windowList.get(i),value));
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    /**
     * 获取当前行的后N行的值
     */
    protected <F> List<FI2<T, F>> windowFunctionForLead(Window<T> overParam, Function<T, F> field, int n) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            List<FI2<T, F>> result = new ArrayList<>();
            for (int i = 0; i < windowList.size(); i++) {
                int afterIndex = i + n;

                FI2<Integer, Integer> indexRange = getIndexRange(overParam, i, windowList);
                if (!isInRange(indexRange,afterIndex)){
                    afterIndex = -1;
                }

                F value = null;
                if (afterIndex >= 0 && afterIndex < windowList.size()){
                    value = field.apply(windowList.get(afterIndex));
                }
                result.add(new FI2<>(windowList.get(i),value));
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    /**
     *  获取窗口内第N行的值
     */
    protected <F> List<FI2<T, F>> windowFunctionForNthValue(Window<T> overParam, Function<T, F> field, int n) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            int index;
            if (n == -1){
                // 获取窗口最后一行
                index = windowList.size() - 1;
            }else {
                index = n - 1;
            }

            if (index < 0 || index >= windowList.size()){
                return windowList.stream().map(e -> new FI2<T,F>(e,null)).collect(toList());
            }

            List<FI2<T, F>> result = new ArrayList<>();
            for (int i = 0; i < windowList.size(); i++) {
                F value = null;
                FI2<Integer, Integer> indexRange = getIndexRange(overParam, i, windowList);
                if (indexRange.getC1() < 0){
                    // 重新设置窗口开始边界
                    indexRange.setC1(0);
                }

                if (n != -1){
                    // 获取窗口内的第n行
                    index = indexRange.getC1() + n - 1;
                }else {
                    index = indexRange.getC2();
                }
                if (index >= 0 && index < windowList.size() && isInRange(indexRange,index)){
                    value = field.apply(windowList.get(index));
                }
                result.add(new FI2<>(windowList.get(i),value));
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    public <V> FI2<Integer,Integer> getIndexRange(Window<T> overParam, int currentIndex,List<V> windowList){
        Integer startIndex = overParam.getStartRound().getStartIndex(currentIndex, windowList);
        Integer endIndex = overParam.getEndRound().getEndIndex(currentIndex, windowList);
        return new FI2<>(startIndex, endIndex);
    }

    public boolean isInRange(FI2<Integer,Integer> round,int index){
        return index >= round.getC1() && index <= round.getC2();
    }

    public boolean isAllRow(Window<T> overParam){
        return Round.START_ROW.equals(overParam.getStartRound()) && Round.END_ROW.equals(overParam.getEndRound());
    }

    protected <F> List<FI2<T, BigDecimal>> windowFunctionForSum(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            if (isAllRow(overParam)){
                BigDecimal value = SDFrame.read(windowList).sum(field);
                return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
            }
            return slidingWindowSum(windowList,overParam,field);
        };
        return overAbject(overParam,supplier);
    }

    public <F> List<FI2<T, BigDecimal>> slidingWindowSum(List<T> nums, Window<T> overParam, Function<T, F> field) {
        FI2<Integer, Integer> firstSlidingWindow = getFirstSlidingWindow(nums, overParam);
        Integer startIndex = firstSlidingWindow.getC1();
        Integer endIndex = firstSlidingWindow.getC2();

        // 计算第一个窗口的和
        BigDecimal windowSum = BigDecimal.ZERO;
        for (int i = startIndex; i <= endIndex && i < nums.size(); i++) {
            if (i >= 0){
                windowSum = windowSum.add(getBigDecimalValue(nums.get(i),field));
            }
        }
        List<FI2<T, BigDecimal>> dataList = new ArrayList<>();

        dataList.add(new FI2<>(nums.get(0),windowSum));

        // 滑动窗口并计算后续窗口的和 移动次数
        int index = 1;
        while (dataList.size() < nums.size()) {
            if (!overParam.getEndRound().isFixedEndIndex()){
                ++endIndex;
                if (endIndex >= 0 && endIndex < nums.size()){
                    windowSum = windowSum.add(getBigDecimalValue(nums.get(endIndex),field));
                }
            }

            if (!overParam.getStartRound().isFixedStartIndex()){
                if (startIndex >= 0 && startIndex < nums.size()){
                    windowSum = windowSum.subtract(getBigDecimalValue(nums.get(startIndex),field));
                }
                startIndex++;
            }

            if (endIndex >= 0){
                dataList.add(new FI2<>(nums.get(index++),windowSum));
            }
        }
        return dataList;
    }

    public <F> BigDecimal getBigDecimalValue(T obj,Function<T, F> field){
        F apply = field.apply(obj);
        if (apply == null){
            return BigDecimal.ZERO;
        }
        if (apply instanceof BigDecimal) {
            return (BigDecimal) apply;
        } else {
            return new BigDecimal(String.valueOf(apply));
        }
    }

    protected <F> List<FI2<T, BigDecimal>> windowFunctionForAvg(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,BigDecimal> supplier = (windowList) -> {
            if (isAllRow(overParam)){
                BigDecimal value = SDFrame.read(windowList).avg(field);
                return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
            }

            return slidingWindowAvg(windowList,overParam,field);
        };
        return overAbject(overParam,supplier);
    }

    public <F> List<FI2<T, BigDecimal>> slidingWindowAvg(List<T> nums, Window<T> overParam, Function<T, F> field) {
        FI2<Integer, Integer> firstSlidingWindow = getFirstSlidingWindow(nums, overParam);
        Integer startIndex = firstSlidingWindow.getC1();
        Integer endIndex = firstSlidingWindow.getC2();

        // 计算第一个窗口
        BigDecimal windowSum = BigDecimal.ZERO;
        int windowSize = 0;
        for (int i = startIndex; i <= endIndex && i < nums.size(); i++) {
            if (i >= 0){
                windowSize++;
                windowSum = windowSum.add(getBigDecimalValue(nums.get(i),field));
            }
        }
        List<FI2<T, BigDecimal>> dataList = new ArrayList<>();
        dataList.add(new FI2<>(nums.get(0),MathUtils.divide(windowSum,new BigDecimal(windowSize),defaultScale,defaultRoundingMode)));

        // 滑动窗口并计算后续窗口的和 窗口大小
        int index = 1;
        while (dataList.size() < nums.size()) {
            // 滑动右窗口
            if (!overParam.getEndRound().isFixedEndIndex()){
                ++endIndex;
                if (endIndex >= 0 && endIndex < nums.size()){
                    windowSum = windowSum.add(getBigDecimalValue(nums.get(endIndex),field));
                }
            }

            // 滑动左窗口
            if (!overParam.getStartRound().isFixedStartIndex()){
                if (startIndex >= 0 && startIndex < nums.size()){
                    windowSum = windowSum.subtract(getBigDecimalValue(nums.get(startIndex),field));
                }
                startIndex++;
            }

            windowSize = getActualWindowSize(nums,startIndex,endIndex);
            if (endIndex >= 0){
                dataList.add(new FI2<>(nums.get(index++),MathUtils.divide(windowSum,new BigDecimal(windowSize),defaultScale,defaultRoundingMode)));
            }
        }
        return dataList;
    }

    private Integer getActualWindowSize(List<T> nums, Integer startIndex, Integer endIndex) {
        if (endIndex < 0 || startIndex >= nums.size()){
            return 0;
        }
        if (startIndex < 0 && endIndex >= nums.size()){
            return nums.size();
        }

        int left = startIndex < 0 ? 0 : startIndex;
        int right = endIndex >= nums.size() ? nums.size() - 1 : endIndex;
        return right - left + 1;
    }


    public  FI2<Integer, Integer> getFirstSlidingWindow(List<T> windowList,Window<T> overParam) {
        return getIndexRange(overParam, 0, windowList);
    }

    public <F extends Comparable<? super F>> void updateSlidingWindowMaxQueue(LinkedList<Integer> queue,FieldValueList<T, F> obj, int i){
        // 移除比当前元素小的. 就是之前比他（i）先进的但是比他小的已经没有意义了不会再获取他们，只会可能获取到i
        while (!queue.isEmpty() && obj.get(queue.peekLast()).compareTo(obj.get(i)) < 0){
            queue.removeLast();
        }
        // 添加队列
        queue.add(i);
    }

    public <F extends Comparable<? super F>> void updateSlidingWindowMinQueue(LinkedList<Integer> queue,FieldValueList<T, F> obj, int i){
        // 移除比当前元素大的. 就是之前比他（i）先进的但是比他大的已经没有意义了不会再获取他们，只会可能获取到i
        while (!queue.isEmpty() && obj.get(queue.peekLast()).compareTo(obj.get(i)) > 0){
            queue.removeLast();
        }
        // 添加队列
        queue.add(i);
    }

    public <F extends Comparable<? super F>> List<FI2<T, F>> slidingWindowForMaxValue(List<T> nums, Window<T> overParam, Function<T, F> field) {
        FI2<Integer, Integer> firstSlidingWindow = getFirstSlidingWindow(nums, overParam);
        Integer startIndex = firstSlidingWindow.getC1();
        Integer endIndex = firstSlidingWindow.getC2();
        FieldValueList<T, F> obj = new FieldValueList<>(nums, field);

        // 双端队列，存放窗口内的元素的索引。 单调递减
        LinkedList<Integer> queue = new LinkedList<>();

        // 初始化第一个窗口
        for (int i = startIndex; i <= endIndex && i < nums.size(); i++) {
            if (i < 0){
                continue;
            }
            updateSlidingWindowMaxQueue(queue,obj,i);
        }

        List<FI2<T, F>> dataList = new ArrayList<>();
        Integer maxIndex = queue.peekFirst();
        dataList.add(new FI2<>(nums.get(0),obj.get(maxIndex)));

        // 滑动窗口
        int index = 1;
        while (dataList.size() < nums.size()) {
            if (!overParam.getEndRound().isFixedEndIndex()){
                ++endIndex;
                if (endIndex >= 0 && endIndex < nums.size()){
                    updateSlidingWindowMaxQueue(queue,obj,endIndex);
                }
            }

            if (!overParam.getStartRound().isFixedStartIndex()){
                startIndex++;
            }

            // 窗口边界更新了，将越界的最大元素移除掉， 只要最大值没有越界就可以获取到，
            // 那些不是最大值的虽然可能已经出窗口了但是获取不到不用管
            while(!queue.isEmpty() && queue.peekFirst() < startIndex){
                queue.removeFirst();
            }

            if (endIndex >= 0){
                dataList.add(new FI2<>(nums.get(index++),obj.get(queue.peekFirst())));

            }
        }
        return dataList;
    }

    public <F extends Comparable<? super F>> List<FI2<T, F>> slidingWindowForMinValue(List<T> nums, Window<T> overParam, Function<T, F> field) {
        FI2<Integer, Integer> firstSlidingWindow = getFirstSlidingWindow(nums, overParam);
        Integer startIndex = firstSlidingWindow.getC1();
        Integer endIndex = firstSlidingWindow.getC2();
        FieldValueList<T, F> obj = new FieldValueList<>(nums, field);

        // 双端队列，存放窗口内的元素的索引。 单调递增
        LinkedList<Integer> queue = new LinkedList<>();

        // 初始化第一个窗口
        for (int i = startIndex; i <= endIndex && i < nums.size(); i++) {
            if (i < 0){
                continue;
            }
            updateSlidingWindowMinQueue(queue,obj,i);
        }

        List<FI2<T, F>> dataList = new ArrayList<>();
        Integer maxIndex = queue.peekFirst();
        dataList.add(new FI2<>(nums.get(0),obj.get(maxIndex)));

        // 滑动窗口
        int index = 1;
        while (dataList.size() < nums.size()) {
            if (!overParam.getEndRound().isFixedEndIndex()){
                ++endIndex;
                if (endIndex >= 0 && endIndex < nums.size()){
                    updateSlidingWindowMinQueue(queue,obj,endIndex);
                }
            }

            if (!overParam.getStartRound().isFixedStartIndex()){
                startIndex++;
            }

            while(!queue.isEmpty() && queue.peekFirst() < startIndex){
                queue.removeFirst();
            }

            if (endIndex >= 0){
                dataList.add(new FI2<>(nums.get(index++),obj.get(queue.peekFirst())));
            }
        }
        return dataList;
    }

    protected <F extends Comparable<? super F>>  List<FI2<T, F>>  windowFunctionForMaxValue(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            if (isAllRow(overParam)){
                F value = SDFrame.read(windowList).maxValue(field);
                return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
            }
            return slidingWindowForMaxValue(windowList,overParam,field);
        };
        return overAbject(overParam,supplier);
    }

    protected <F extends Comparable<? super F>> List<FI2<T, F>> windowFunctionForMinValue(Window<T> overParam, Function<T, F> field) {
        SupplierFunction<T,F> supplier = (windowList) -> {
            if (isAllRow(overParam)){
                F value = SDFrame.read(windowList).minValue(field);
                return windowList.stream().map(e -> new FI2<>(e,value)).collect(toList());
            }
            return slidingWindowForMinValue(windowList,overParam,field);
        };
        return overAbject(overParam,supplier);
    }

    protected List<FI2<T, Integer>> windowFunctionForCount(Window<T> overParam) {
        SupplierFunction<T,Integer> supplier = (windowList) -> {
            if (isAllRow(overParam)){
                int count = windowList.size();
                return windowList.stream().map(e -> new FI2<>(e,count)).collect(toList());
            }
            List<FI2<T, Integer>> result = new ArrayList<>();
            for (int i = 0; i < windowList.size(); i++) {
                FI2<Integer, Integer> indexRange = getIndexRange(overParam, i, windowList);
                if (indexRange.getC1() <= 0){
                    indexRange.setC1(0);
                }
                if (indexRange.getC2() > windowList.size() - 1){
                    indexRange.setC2(windowList.size() - 1);
                }
                Integer value = indexRange.getC2() -  indexRange.getC1() + 1;
                result.add(new FI2<>(windowList.get(i),value));
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

    protected List<FI2<T, Integer>> windowFunctionForNtile(Window<T> overParam, int n) {
        if (n <= 0){
            throw new IllegalArgumentException("incorrect arguments to ntile for " + n);
        }

        SupplierFunction<T,Integer> supplier = (windowList) -> {
            List<FI2<T, Integer>> result = new ArrayList<>();

            // 能均匀分
            if (windowList.size() % n == 0){
                int groupSize = windowList.size() / n;
                int bucket = 1;
                int index = 0;
                for (T t : windowList) {
                    index++;
                    if (index > groupSize){
                        bucket++;
                        index = 1;
                    }
                    result.add(new FI2<>(t,bucket));
                }
                return result;
            }

            // 不能均匀分
            // 如果不能平均分配，则优先分配较小编号的桶，并且各个桶中能放的行数最多相差1。
            // 先分配组1，再分配组2，再分配组3。 如此循环直到将数字分配完
            int[] arr = new int[n];
            int count = windowList.size();
            int index = 0;
            while (count > 0){
                if (index >= arr.length){
                    index = 0;
                }
                arr[index++]++;
                count--;
            }

            // 去消耗每个桶的数字，如果消耗完则桶编号增加
            int bucket = 1;
            for (int i = 0; i < windowList.size(); i++) {
                arr[bucket-1]--;
                result.add(new FI2<>(windowList.get(i),bucket));
                if (arr[bucket-1] <= 0){
                    bucket++;
                }
            }
            return result;
        };
        return overAbject(overParam,supplier);
    }

}
