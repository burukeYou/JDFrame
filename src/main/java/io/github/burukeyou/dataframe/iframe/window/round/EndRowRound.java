package io.github.burukeyou.dataframe.iframe.window.round;

import java.util.List;

/**
 *  The Last row
 */
public class EndRowRound extends WindowRound {

    @Override
    public boolean isFixedEndIndex() {
        return true;
    }

    @Override
    public <T> Integer getStartIndex(Integer currentRowIndex, List<T> windowList) {
        return null;
    }

    @Override
    public <T> Integer getEndIndex(Integer currentRowIndex, List<T> windowList) {
        return windowList.size() - 1;
    }
}