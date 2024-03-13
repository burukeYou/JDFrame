package io.github.burukeyou.dataframe.iframe;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 *
 * @param <T>           元素类型
 *
 * @author caizhihao
 */
@Data
@AllArgsConstructor
public class DFList<T> {

    private List<T> data;

    /**
     * 取前N个
     * @param n
     * @return
     */
    public DFList<T> first(int n) {
        if (data.isEmpty()){
            return this;
        }

        if (n <= 0){
            throw new IllegalArgumentException("first N should greater than zero");
        }

        if (n >= data.size()){
            return this;
        }

        data = data.subList(0,n);
        return this;
    }

    public DFList<T> rankingDesc(Comparator<T> comparator, int n) {
        return rankingAsc(comparator.reversed(),n);
    }

    public DFList<T> rankingAsc(Comparator<T> comparator, int n) {
        if (data.isEmpty()){
            return this;
        }
        if (n <= 0){
            throw new IllegalArgumentException("first N should greater than zero");
        }

        sortAsc(comparator);

        int rank = 1;
        List<T> tmpDataList = new ArrayList<>();
        tmpDataList.add(data.get(0));
        for (int i = 1; i < data.size(); i++) {
            T pre = data.get(i-1);
            T cur = data.get(i);
            if (comparator.compare(pre,cur) != 0){
                rank += 1;
            }
            if (rank <= n){
                tmpDataList.add(cur);
            }else {
                break;
            }
        }
        data = tmpDataList;
        return this;
    }



    public T first() {
        if (data.isEmpty()){
            return null;
        }

        return data.get(0);
    }

    /**
     * 取后N个
     * @param n
     * @return
     */
    public DFList<T> last(int n) {
        if (n <= 0){
            throw new IllegalArgumentException("last N should greater than zero");
        }

        if (n >= data.size()){
            return this;
        }

        int start = data.size() - n + 1;
        data = data.subList(start,data.size());
        return this;
    }

    /**
     * 降序
     */
   /* public DataFrameList<T> sortDesc(){
        data.sort(Comparator.reverseOrder());
        return this;
    }*/

    public DFList<T> sortDesc(Comparator<T> comparator){
        data = data.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        return this;
    }


    /**
     * 升序
     */
/*    public DataFrameList<T> sortAsc(){
        Collections.sort(data);
        return this;
    }*/

    public DFList<T> sortAsc(Comparator<T> comparator){
        data = data.stream().sorted(comparator).collect(Collectors.toList());
        return this;
    }


    public List<T> build(){
        return data;
    }

    public <K> Map<K,T> toMap(Function<T,K> function){
        return data.stream().collect(Collectors.toMap(function, e -> e));
    }

    public <K,V> Map<K,V> toMap(Function<T,K> function,Function<T,V> function2){
        return data.stream().collect(Collectors.toMap(function, function2));
    }


}
