package io.github.burukeyou.dataframe.iframe.window.round;

import java.util.List;

/**
 * The last n rows of the current row
 * @author caizhihao
 */
public class AfterRound extends WindowRound {

    protected int n = 0;

    public AfterRound() {
    }

    public AfterRound(int n) {
        this.n = n;
    }

    @Override
    public void check() {
        if (n < 0){
            throw new IllegalArgumentException("Boundary parameter values cannot be negative");
        }
    }

    @Override
    public <T> Integer getStartIndex(Integer currentRowIndex, List<T> windowList) {
        return null;
    }

    @Override
    public <T> Integer getEndIndex(Integer currentRowIndex, List<T> windowList) {
        int index = currentRowIndex + n;
        if (isIndexOutOfBounds(index,windowList)){
            index = windowList.size() - 1;
        }
        return index;
    }
}