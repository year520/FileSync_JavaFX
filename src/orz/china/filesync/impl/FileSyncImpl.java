package orz.china.filesync.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import orz.china.filesync.IFileSync;
import orz.china.filesync.beans.FileSyncBean;
import orz.china.filesync.beans.FileSyncProp;
import orz.china.filesync.filter.FileSyncFilter;
import orz.china.filesync.listener.FileSyncListener;
import orz.china.filesync.util.FileSyncUtil;

public class FileSyncImpl implements IFileSync {

    private FileSyncProp prop;
    private List<FileAlterationMonitor> monitorList;

    public FileSyncImpl(FileSyncProp prop) {
	this.prop = prop;
	monitorList = new ArrayList<FileAlterationMonitor>();
    }

    public void monitorAndSyncFile() throws Exception {
	List<FileSyncBean> beans = prop.getBeans();
	for (FileSyncBean bean : beans) {
	    for (String s : bean.getSourcePath()) {
		FileAlterationObserver filealterationobserver = new FileAlterationObserver(s, new FileSyncFilter(bean.getIncludeRole(), bean.getExcludeRole()));
		filealterationobserver.addListener(new FileSyncListener(s, bean.getTargetPath()));
		FileAlterationMonitor monitor = new FileAlterationMonitor(1000, filealterationobserver);
		monitorList.add(monitor);
		monitor.start();
	    }
	}
    }

    public void stop() throws Exception {
	if (null != monitorList && monitorList.size() > 0) {
	    for (FileAlterationMonitor monitor : monitorList) {
		monitor.stop();
	    }
	}
    }

    public FileSyncProp getProp() {
	return prop;
    }

    public void setProp(FileSyncProp prop) {
	this.prop = prop;
    }

    public void syncAllFile() throws Exception {
	List<FileSyncBean> beans = prop.getBeans();
	for (FileSyncBean bean : beans) {
	    FileSyncUtil.deleteFile(bean.getTargetPath());
	    for (String s : bean.getSourcePath()) {
		FileSyncUtil.copyDirectory(s, bean.getTargetPath(), new FileSyncFilter(bean.getIncludeRole(), bean.getExcludeRole()));
	    }
	}
    }

}
