package com.zhixin;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 文件夹监控类
 * @author
 */
public class FileListener extends FileAlterationListenerAdaptor {

    private static final Logger log = LogManager.getLogger(FileListener.class);

    /**
     * 文件创建执行
     */
    public void onFileCreate(File file) {
        log.info("[新建]:" + file.getAbsolutePath());
    }
    /**
     * 文件创建修改
     */
    public void onFileChange(File file) {
        log.info("[修改]:" + file.getAbsolutePath());
    }
    /**
     * 文件删除
     */
    public void onFileDelete(File file) {
        log.info("[删除]:" + file.getAbsolutePath());
    }
    /**
     * 目录创建
     */
    public void onDirectoryCreate(File directory) {
        log.info("[新建]:" + directory.getAbsolutePath());
    }
    /**
     * 目录修改
     */
    public void onDirectoryChange(File directory) {
        log.info("[修改]:" + directory.getAbsolutePath());
    }
    /**
     * 目录删除
     */
    public void onDirectoryDelete(File directory) {
        log.info("[删除]:" + directory.getAbsolutePath());
    }
    public void onStart(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStart(observer);
    }
    public void onStop(FileAlterationObserver observer) {
        // TODO Auto-generated method stub
        super.onStop(observer);
    }

    static final class FileFilterImpl implements FileFilter {

        /**
         * @return return true:返回所有目录下所有文件详细(包含所有子目录)
         * @return return false:返回主目录下所有文件详细(不包含所有子目录)
         */

        public boolean accept(File file) {
            // TODO Auto-generated method stub
            log.info("文件路径: " + file);
            log.info("最后修改时间： " + new Date(file.lastModified()));
            return true;
        }
    }


    public static void servicerun(String rootDir) throws Exception {

        FileAlterationObserver observer = new FileAlterationObserver(rootDir, new FileFilterImpl());
        // 构造收听类
        FileListener listener = new FileListener();
        // 为观察对象添加收听对象
        observer.addListener(listener);
        // 配置Monitor，第一个参数单位是毫秒，是监听的间隔；第二个参数就是绑定我们之前的观察对象。
        FileAlterationMonitor fileMonitor = new FileAlterationMonitor(10000,
                new FileAlterationObserver[]{observer});

        System.out.println("---开始监控:"+rootDir);
        // 启动开始监听
        fileMonitor.start();

    }
}
