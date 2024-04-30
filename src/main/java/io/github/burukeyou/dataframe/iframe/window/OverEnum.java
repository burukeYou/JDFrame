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
    RANK,

    /**
     * Rank of current row within its partition, without gaps
     */
    DENSE_RANK,

    /**
     *  Number of current row within its partition
     */
    ROW_NUMBER,

    /**
     * Percentage rank value
     */
    PERCENT_RANK,

    /**
     * Cumulative distribution value
     */
    CUME_DIST,

    /**
     * Value of argument from row lagging current row within partition
     */
    LAG,

    /**
     * Value of argument from row leading current row within partition
     */
    LEAD,

    /**
     * Value of argument from first row of window frame
     */
    FIRST_VALUE,

    /**
     * Value of argument from last row of window frame
     */
    LAST_VALUE,

    /**
     * Value of argument from N-th row of window frame
     */
    NTH_VALUE,

    /**
     * Bucket number of current row within its partition.
     */
   // NTILE,


    SUM,

    AVG,

    MAX,

    MIN,

    COUNT,

    /**
     *
     */
    //PROPORTION
    
}
