package io.github.burukeyou.dataframe.iframe;

/**
 * @author          caizhihao
 */
public interface ConfigurableSDFrame<T> extends IFrame<T>,
                                                WhereSDFrame<T>,
                                                JoinSDFrame<T>,
                                                GroupSDFrame<T>,
                                                OverSDFrame<T> {


}
