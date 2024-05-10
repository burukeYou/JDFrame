package io.github.burukeyou.dataframe.iframe.window.round;

/**
 * WindowRange Builder
 *
 * @author caizhihao
 */
public class Range {

    private Range(){}

    /**
     *  The first row of the window
     */
    public static final WindowRange START_ROW = new StartRowRange();

    /**
     *  The first 0 row of the current row
     */
    public static final WindowRange BEFORE_ROW = new BeforeRange(0);

    /**
     *  The current row of the window
     */
    public static final WindowRange CURRENT_ROW = new CurrentRowRange();

    /**
     *  The last 0 row of the current row
     */
    public static final WindowRange AFTER_ROW = new AfterRange(0);


    /**
     *  The last row of the window
     */
    public static final WindowRange END_ROW = new EndRowRange();


    /**
     *  The first n row of the current row
     */
    public static WindowRange BEFORE(int n){
        return new BeforeRange(n);
    }

    /**
     *  The last n row of the current row
     */
    public static WindowRange AFTER(int n){
        return new AfterRange(n);
    }

}
