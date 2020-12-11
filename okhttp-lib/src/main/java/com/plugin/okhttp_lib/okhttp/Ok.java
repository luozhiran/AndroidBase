package com.plugin.okhttp_lib.okhttp;



import okhttp3.Callback;

public  class Ok  {
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PATCH = "PATCH";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";
    public static final String MOVE = "MOVE";
    private ItgRequest itgRequest;

    protected ItgRequest request() {
        if (itgRequest == null) {
            itgRequest = ItgRequest.of();
        }
        return itgRequest;
    }

    protected void releaseBuild() {
        itgRequest = null;
    }


}
