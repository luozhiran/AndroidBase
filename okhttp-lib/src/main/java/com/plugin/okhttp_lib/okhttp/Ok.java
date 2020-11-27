package com.plugin.okhttp_lib.okhttp;



import okhttp3.Callback;

public abstract class Ok  {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PATCH = "PATCH";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String MOVE = "MOVE";
    private ItgRequest itgRequest;


    public abstract void go(Callback callback);

    protected ItgRequest request() {
        if (itgRequest == null) {
            itgRequest = ItgRequest.of(this);
        }
        return itgRequest;
    }

    protected void releaseBuild() {
        itgRequest = null;
    }


}
