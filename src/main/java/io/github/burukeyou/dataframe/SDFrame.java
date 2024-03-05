package io.github.burukeyou.dataframe;

import io.github.burukeyou.dataframe.dataframe.SDFrameImpl;

import java.util.List;

/**
 * Stream DataFrame
 *      前后的操作是连续的
 *
 * @author caizhihao
 */
public interface SDFrame<T> extends IFrame<T> {

    static <T> SDFrameImpl<T> read(List<T> list) {
        return new SDFrameImpl<>(list);
    }

}
