package io.github.burukeyou.dataframe.iframe;

/**
 * @author          caizhihao
 */
public interface ConfigurableJDFrame<T> extends IFrame<T>,
                                                JoinJDFrame<T> ,
                                                WhereJDFrame<T>,
                                                GroupJDFrame<T>,
                                                OverJDFrame<T> ,
                                                OperationJDFrame<T> {


}
