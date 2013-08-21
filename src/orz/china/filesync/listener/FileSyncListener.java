package orz.china.filesync.listener;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListener;
import org.apache.commons.io.monitor.FileAlterationObserver;

import orz.china.filesync.util.FileSyncUtil;

public class FileSyncListener implements FileAlterationListener {

    private String target;
    private String source;

    public FileSyncListener(File source, File target) {
	this.source = source.getAbsolutePath();
	this.target = target.getAbsolutePath();
    }

    public FileSyncListener(String source, String target) {
	this.source = new File(source).getAbsolutePath();
	this.target = new File(target).getAbsolutePath();
    }

    public void onDirectoryChange(File file) {
    }

    public void onDirectoryCreate(File file) {
    }

    public void onDirectoryDelete(File file) {

    }

    public void onFileChange(File file) {
	String filePath = file.getAbsolutePath();
	FileSyncUtil.copyFile(filePath, filePath.replace(source, target));
    }

    public void onFileCreate(File file) {
	String filePath = file.getAbsolutePath();
	FileSyncUtil.copyFile(filePath, filePath.replace(source, target));
    }

    public void onFileDelete(File file) {
	String filePath = file.getAbsolutePath();
	FileSyncUtil.deleteFile(filePath.replace(source, target));
    }

    public void onStart(FileAlterationObserver filealterationobserver) {
    }

    public void onStop(FileAlterationObserver filealterationobserver) {

    }

    public String getTarget() {
	return target;
    }

    public void setTarget(String target) {
	this.target = target;
    }

    public String getSource() {
	return source;
    }

    public void setSource(String source) {
	this.source = source;
    }

}
