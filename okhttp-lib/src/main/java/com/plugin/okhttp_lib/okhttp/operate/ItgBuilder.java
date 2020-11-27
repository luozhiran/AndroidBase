package com.plugin.okhttp_lib.okhttp.operate;

import com.plugin.okhttp_lib.okhttp.Ok;
import com.plugin.okhttp_lib.okhttp.operateimpl.MultiBuilderImpl;
import com.plugin.okhttp_lib.okhttp.operateimpl.SingleBuilderImpl;


/**
 * 使用静态代理模式
 */
public abstract class ItgBuilder implements SingleBuilder, MultiBuilder, Builder {

    private SingleBuilderImpl singleBuilder;
    private MultiBuilderImpl multiBuilder;


    public MultiBuilderImpl getMultiBuilder() {
        if (singleBuilder != null)
            throw new IllegalArgumentException("You already used a single mode request");
        if (multiBuilder == null) {
            multiBuilder = new MultiBuilderImpl(this);
        }
        return multiBuilder;
    }

    public SingleBuilderImpl getSingleBuilder() {
        if (multiBuilder != null)
            throw new IllegalArgumentException("You already multiple module requests");
        if (singleBuilder == null) {
            singleBuilder = new SingleBuilderImpl(this);
        }
        return singleBuilder;
    }

    public boolean isMulti() {
        if (multiBuilder != null) {
            return true;
        } else {
            return false;
        }
    }

    public abstract Ok ok();


}
