package com.ruoyun.httpwings;

/**
 * Created by fanpu on 2017/9/21.
 */

public interface Request {

    //生成http响应对象
    public void createHttpClient() throws Exception;

    public void setSsl();

    public void setHeadProperty();

    //处理返回string的字段
    public void handleResult();

}
