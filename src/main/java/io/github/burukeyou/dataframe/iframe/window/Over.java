package io.github.burukeyou.dataframe.iframe.window;

public class Over {

/*    public static void main(String[] args) {

        //Over.partitionBy(null).sortBy(null).collect(OverEnum.DENSE_RANK);
    }


    public static <T,P,R extends Comparable<R>> OverBuilder<T,P,R> partitionBy(Function<T,P>...partitionBy){
        return new OverBuilder<>(Arrays.asList(partitionBy));
    }

    public static <T,R  extends Comparable<R>> void sortAsc(Function<T,R> sortField)){

    }


*//*    public static <T,R  extends Comparable<R>> OverSortBuilder<T,R> sortAsc(Function<T,R>...sortField){
        return new OverSortBuilder<>(Arrays.asList(sortField));
    }*//*


    public static <T,R  extends Comparable<R>> OverSortBuilder<T,R> sortBy(Comparator<T> comparator){
        return new OverSortBuilder<>(comparator);
    }

    @Data
    public static class OverBuilder<T,P,R extends Comparable<R>> extends OverParam<T,P,R> {
        private List<Function<T,P>> partitionBy;
        private List<Function<T,R>> sortBy;

        private OverEnum overEnum;

        public OverBuilder(List<Function<T, P>> partitionBy, List<Function<T, R>> sortBy, OverEnum overEnum) {
            this.partitionBy = partitionBy;
            this.sortBy = sortBy;
            this.overEnum = overEnum;
        }

        public OverBuilder(List<Function<T, P>> partitionBy) {
            this.partitionBy = partitionBy;
        }

        public OverBuilder<T,P,R> sortBy(Function<T,R>...sortField){
            sortBy = Arrays.asList(sortField);
            return this;
        }

        public OverBuilder<T,P,R> collect(OverEnum overEnum){
            this.overEnum = overEnum;
            return this;
        }
    }

    public static class OverSortBuilder<T,R extends Comparable<R>> {
        private List<Function<T,R>> sortBy;
        private OverEnum overEnum;

        private Comparator<T> comparator;

        public OverSortBuilder(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        public OverSortBuilder(List<Function<T, R>> sortBy) {
            this.sortBy = sortBy;
        }

        public <P> OverBuilder<T,P,R> collect(OverEnum overEnum){
            this.overEnum = overEnum;
            return new OverBuilder<>(null,sortBy,this.overEnum);
        }
    }

    public static class OverComparatorBuilder<T> {
        private OverEnum overEnum;

        private Comparator<T> comparator;

        public OverComparatorBuilder(Comparator<T> comparator) {
            this.comparator = comparator;
        }

        public <P> OverComparatorBuilder<T> collect(OverEnum overEnum){
            this.overEnum = overEnum;
            return new OverBuilder<>(null,sortBy,this.overEnum);
        }
    }*/

}
