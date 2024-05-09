package io.github.burukeyou.dataframe.iframe.window.round;

import java.util.List;

/**
 * @author  caizhihao
 */
public abstract class WindowRound {

    /**
     *  校验参数合法
     */
    public void check(){}

    public boolean isFixedStartIndex(){
        return false;
    }

    public boolean isFixedEndIndex(){
        return false;
    }

    public abstract <T> Integer getStartIndex(Integer currentRowIndex, List<T> windowList);

    public abstract <T> Integer getEndIndex(Integer currentRowIndex, List<T> windowList);

    public boolean eq(Object obj) {
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
