package io.github.burukeyou.dataframe.iframe.window.round;

import java.util.List;

/**
 * Window Range
 *        Can be generated through {@link Range} construction
 *        the window range is a sliding window, so we can specify some boundaries and sliding situations of the window
 *
 * @author  caizhihao
 */
public interface WindowRange {

    /**
     *  Verify that the parameters are valid
     */
    default void check(){}

    /**
     * is the start boundary fixed
     */
    default boolean isFixedStartIndex(){
        return false;
    }

    /**
     * is the end boundary fixed
     */
    default boolean isFixedEndIndex(){
        return false;
    }

    /**
     * get the window  sliding start boundary
     * @param currentRowIndex           the current row index
     * @param windowList                the current window data
     */
    <T> Integer getStartIndex(Integer currentRowIndex, List<T> windowList);

    /**
     * get the window  sliding end boundary
     * @param currentRowIndex           the current row index
     * @param windowList                the current window data
     */
    <T> Integer getEndIndex(Integer currentRowIndex, List<T> windowList);

    default boolean eq(Object obj) {
         if (obj == null){
             return false;
         }

         if (this == obj){
             return true;
         }

         if (this.getClass() == obj.getClass()){
             return true;
         }
         return false;
    }
}
