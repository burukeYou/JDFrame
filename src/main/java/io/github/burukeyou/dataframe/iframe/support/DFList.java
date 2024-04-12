package io.github.burukeyou.dataframe.iframe.support;

import io.github.burukeyou.dataframe.iframe.JDFrame;
import io.github.burukeyou.dataframe.iframe.SDFrame;
import lombok.Data;

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
public class DFList<T> {

    private List<T> data;

    public DFList(List<T> data) {
        this.data = data;
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

    public DFList<T> sortDesc(Comparator<T> comparator){
        data = data.stream().sorted(comparator.reversed()).collect(Collectors.toList());
        return this;
    }



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

    public SDFrame<T> toSDFrame(){
        return SDFrame.read(this.data);
    }

    public JDFrame<T> toJDFrame(){
        return JDFrame.read(this.data);
    }

}
