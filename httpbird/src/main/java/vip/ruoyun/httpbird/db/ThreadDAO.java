/*
 * @Title ThreadDAO.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-8 ����10:55:21
 * @version 1.0
 */
package vip.ruoyun.httpbird.db;


import vip.ruoyun.httpbird.entities.FileInfo;

/**
 * 数据访问接口
 */
public interface ThreadDAO {
    /**
     * 插入线程信息
     */
    void insertFileInfo(FileInfo threadInfo);

    /**
     * 删除线程信息
     */
    void deleteFileInfo(String url);

    /**
     * 更新线程下载进度
     */
    void updateFileInfo(FileInfo fileinfo);

    /**
     * 查询文件的线程信息
     */
    FileInfo getFileInfo(String url);

    /**
     * 线程信息是否存在
     */
    boolean isExists(String url);
}
