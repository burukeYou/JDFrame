package io.github.burukeyou.dataframe.util;

import java.util.Collection;

public class ListUtils {

    private ListUtils(){}

    public static boolean isEmpty(Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    public static boolean isNotEmpty(Collection<?> coll) {
        return !isEmpty(coll);
    }

}
