package io.github.burukeyou.dataframe.dataframe.collection;


import io.github.burukeyou.dataframe.dataframe.DFList;

import java.util.Comparator;
import java.util.List;

public class DFSortList<T> extends DFList<T> {

    private Comparator<T> comparator;

    public DFSortList(List<T> data, Comparator<T> comparator) {
        super(data);
        if (data == null){
            return;
        }
        try {
            data.sort(comparator);
        } catch (NullPointerException e) {
            throw new NullPointerException("排序的值存在null无法进行排序");
        }
    }


}
