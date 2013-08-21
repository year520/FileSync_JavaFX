package orz.china.filesync.beans;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("FileSyncProp")
public class FileSyncProp {
    @XStreamAlias("fileSyncbeans")
    List<FileSyncBean> beans;
    @XStreamAlias("syncTime")
    long syncTime = 1000;

    public void addBean(FileSyncBean bean) {
	if (null == beans)
	    beans = new ArrayList<FileSyncBean>();
	beans.add(bean);
    }

    public List<FileSyncBean> getBeans() {
	return beans;
    }

    public void setBeans(List<FileSyncBean> beans) {
	this.beans = beans;
    }

    public long getSyncTime() {
	return syncTime;
    }

    public void setSyncTime(long syncTime) {
	this.syncTime = syncTime;
    }

}