package io.github.burukeyou.dataframe;

import io.github.burukeyou.dataframe.dataframe.JDFrameImpl;

import java.util.List;

/**
 *
 *
 * @author  caizhihao
 */
public interface JDFrame<T> extends IFrame<T> {

     static <T> JDFrame<T> read(List<T> list) {
        return new JDFrameImpl<>(list);
    }


}
