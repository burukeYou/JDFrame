package io.github.burukeyou.dataframe.iframe.window.round;

import java.util.List;

/**
 * The first n rows of the current row
 * @author caizhihao
 */
public class BeforeRound extends WindowRound {

    protected int n = 0;

    public BeforeRound() {
    }

    public BeforeRound(int n) {
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
        return  currentRowIndex - n;
    }

    @Override
    public <T> Integer getEndIndex(Integer currentRowIndex, List<T> windowList) {
        return null;
    }

}