package io.github.burukeyou.dataframe.iframe.window.round;

import java.util.List;

/**
 *  The first row
 */
public class StartRowRange implements WindowRange {

    @Override
    public boolean isFixedStartIndex() {
        return true;
    }

    @Override
    public <T> Integer getStartIndex(Integer currentRowIndex, List<T> windowList) {
        return 0;
    }

    @Override
    public <T> Integer getEndIndex(Integer currentRowIndex, List<T> windowList) {
        return null;
    }
}