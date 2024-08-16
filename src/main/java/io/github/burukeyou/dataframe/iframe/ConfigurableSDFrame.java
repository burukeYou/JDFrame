package io.github.burukeyou.dataframe.iframe;

/**
 * @author          caizhihao
 */
public interface ConfigurableSDFrame<T> extends IFrame<T>,
                                                IWhereSDFrame<T>,
                                                IJoinSDFrame<T>,
                                                IGroupSDFrame<T>,
                                                IOverSDFrame<T>,
                                                IOperationSDFrame<T> {


}
