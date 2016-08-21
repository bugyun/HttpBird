package com.zyh.httpbird.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruoyun on 16/8/16.
 * 任务队列管理器
 */
public class JobQueue {


    //顺序执行的队列
    private List inOrderQuenue = new ArrayList();
    //同时进行的队列
    private List disorderQuenue = new ArrayList();


    public void addInOrderQuenue(Object object) {
        inOrderQuenue.add(object);
    }

    public void addDisorderQuenue(Object object) {
        disorderQuenue.add(object);
    }

    //顺序执行的notif，通知执行下一个任务
    public void notif() {
        Object object = inOrderQuenue.remove(0);
        //线程池执行runable
    }


}
