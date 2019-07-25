package vip.ruoyun.httpbird.task;

/**
 * Created by EiT on 2015/8/30.
 */
public interface Task extends Runnable {

    void cancel();
}
