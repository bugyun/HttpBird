package com.ruoyun.httpwings;

/**
 * Created by fanpu on 2017/9/21.
 */

public class NetWork {


    public Response performRequest(Request request) {
        request.createHttpClient();
        request.setSsl();
        request.setHeadProperty();
        return new Response();
    }


}
