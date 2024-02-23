package io.github.burukeyou.dataframe;

import java.util.List;

/**
 * 支持连续流
 *
 * @author  caizhihao
 */
public interface JDFluentFrame<T> extends JDFrame<T> {

    static <T> JDFluentFrame<T> read(List<T> list) {
        return null;
    }

}
