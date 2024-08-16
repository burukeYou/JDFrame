package io.github.burukeyou.dataframe.iframe;

/**
 * @author          caizhihao
 */
public interface ConfigurableJDFrame<T> extends IFrame<T>,
                                                IJoinJDFrame<T>,
                                                IWhereJDFrame<T>,
                                                IGroupJDFrame<T>,
                                                IOverJDFrame<T>,
                                                IOperationJDFrame<T> {


}
