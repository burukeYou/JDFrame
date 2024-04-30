package io.github.burukeyou.dataframe.iframe.window;

/**
 * Window Function
 *
 * @author caizhihao
 */

public enum OverEnum {

    /**
     * Rank of current row within its partition, with gaps
     */
    RANK(new RankWindowFunction()),

    /**
     * Rank of current row within its partition, without gaps
     */
    DENSE_RANK(new DenseRankWindowFunction()),

    /**
     *  Number of current row within its partition
     */
    ROW_NUMBER(new RowNumberWindowFunction()),

    /**
     * Percentage rank value
     */
    PERCENT_RANK(new PercentRankWindowFunction()),

    /**
     * Cumulative distribution value
     */
    CUME_DIST(new CumeDistWindowFunction()),

    /**
     * Value of argument from row lagging current row within partition
     */
    LAG(new LagWindowFunction()),

    /**
     * Value of argument from row leading current row within partition
     */
    LEAD(new LeadWindowFunction()),

    /**
     * Value of argument from first row of window frame
     */
    FIRST_VALUE(new FirstValueWindowFunction()),

    /**
     * Value of argument from last row of window frame
     */
    LAST_VALUE(new LastValueWindowFunction()),

    /**
     * Value of argument from N-th row of window frame
     */
    NTH_VALUE(new NthValueWindowFunction()),

    /**
     * Bucket number of current row within its partition.
     */
   // NTILE,

/*

    SUM,

    AVG,

    MAX,

    MIN,

    COUNT,
*/

    /**
     *
     */
    //PROPORTION

    ;

    private WindowFunction windowFunction;

    OverEnum(WindowFunction windowFunction) {
        this.windowFunction = windowFunction;
    }

    public WindowFunction getWindowFunction() {
        return windowFunction;
    }
}
