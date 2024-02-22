package io.github.burukeyou.dataframe.dataframe;

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

    public static <T> DFList<T> read(List<T> data){
        return new DFList<>(data);
    }

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

    /**
     * 取前N个，如果由于大小相同存在不止N个，则全部返回
     * @param n
     * @param comparator
     * @return
     */
    public DFList<T> firstWithSame(int n,Comparator<T> comparator){
        if (data.isEmpty()){
            return this;
        }

        if (n <= 0){
            throw new IllegalArgumentException("first N should greater than zero");
        }

        if (n >= data.size()){
            return this;
        }

        List<T> tempData = new ArrayList<>(data.subList(0, n));
        // 后面再加上和第N个大小相同的对象
        T t = data.get(n - 1);
        for(int i = n;i<data.size();i++){
            int compare = comparator.compare(t, data.get(i));
            if(compare == 0){
                tempData.add(data.get(i));
            }else{
                break;
            }
        }
        data = tempData;
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


    public DataFrame<T> toDataFrame(){
        return DataFrame.read(data);
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
