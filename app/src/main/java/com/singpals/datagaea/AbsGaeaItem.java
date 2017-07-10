package com.singpals.datagaea;

import com.lib.mthdone.utils.IManager;

/**
 * $desc
 */

public abstract class AbsGaeaItem<T extends DataGaea> implements IManager {

    protected abstract void onSetDataGaea(T gaea);

}
