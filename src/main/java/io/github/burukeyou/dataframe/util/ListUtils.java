package io.github.burukeyou.dataframe.util;

import java.util.Collection;

public class ListUtils {

    private ListUtils(){}

    public static boolean isEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }
}
