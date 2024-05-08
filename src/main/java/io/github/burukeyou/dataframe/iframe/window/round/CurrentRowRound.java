package io.github.burukeyou.dataframe.iframe.window.round;


import java.util.List;

/**
 * Current row
 * @author caizhihao
 */
public class CurrentRowRound extends WindowRound {

    @Override
    public <T> Integer getStartIndex(Integer currentRowIndex, List<T> windowList) {
        return currentRowIndex;
    }

    @Override
    public <T> Integer getEndIndex(Integer currentRowIndex, List<T> windowList) {
        return currentRowIndex;
    }
}