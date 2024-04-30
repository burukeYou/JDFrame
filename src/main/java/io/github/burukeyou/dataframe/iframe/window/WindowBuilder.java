package io.github.burukeyou.dataframe.iframe.window;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;

/**
 * @author caizhiao
 */

public class WindowBuilder<T>   implements  Window<T> {

    private List<Function<T,?>> groupBy;
    protected Comparator<T> comparator;


    public WindowBuilder(Comparator<T> comparator) {
        this.comparator = comparator;
    }

    public WindowBuilder(List<Function<T, ?>> groupBy, Comparator<T> comparator) {
        this.comparator = comparator;
        this.groupBy = groupBy;
    }


    @Override
    public Comparator<T> getComparator() {
        return comparator;
    }

    public List<Function<T, ?>> getGroupBy() {
        return groupBy;
    }

    static <T,U> WindowBuilder<T> groupBy(Function<T,U> groupField){
        return new WindowBuilder<>(Collections.singletonList(groupField),null);
    }

    static <T,U> WindowBuilder<T> groupBy(Function<T,U>...groupField){
        return new WindowBuilder<>(Arrays.asList(groupField),null);

    }


    public <U extends Comparable<? super U>> Window<T> sortAsc(Function<T,U> sortField) {
        if (this.comparator == null){
            this.comparator = Comparator.comparing(sortField);
        }else {
            this.comparator.thenComparing(Comparator.comparing(sortField));
        }
        return this;
    }


    public <U extends Comparable<? super U>> Window<T> sortDesc(Function<T,U> sortField) {
        if (this.comparator == null){
            this.comparator = Comparator.comparing(sortField).reversed();
        }else {
            this.comparator.thenComparing(Comparator.comparing(sortField).reversed());
        }
        return this;
    }





    public static void main(String[] args) {
        Comparator<User> userComparator = Comparator.comparing(User::getAge).thenComparing(User::getName);

    }

    @Data
    public static class User {
        private String name;
        private Integer age;
        private LocalDateTime createTime;
    }

}
