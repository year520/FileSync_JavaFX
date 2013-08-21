package orz.china.filesync.beans;

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("fileSyncBean")
public class FileSyncBean {
    @XStreamAlias("source")
    private List<String> sourcePath;
    @XStreamAlias("target")
    private String targetPath;
    @XStreamAlias("include")
    private String includeRole;
    @XStreamAlias("exclude")
    private String excludeRole;

    public FileSyncBean() {
	sourcePath = new ArrayList<String>();
    }

    public List<String> getSourcePath() {
	return sourcePath;
    }

    public void setSourcePath(List<String> sourcePath) {
	this.sourcePath = sourcePath;
    }

    public String getTargetPath() {
	return targetPath;
    }

    public void setTargetPath(String targetPath) {
	this.targetPath = targetPath;
    }

    public String getIncludeRole() {
	return includeRole;
    }

    public void setIncludeRole(String includeRole) {
	this.includeRole = includeRole;
    }

    public String getExcludeRole() {
	return excludeRole;
    }

    public void setExcludeRole(String excludeRole) {
	this.excludeRole = excludeRole;
    }

    public String toString() {
	return "TargetPath:" + this.targetPath + ",IncludeRole:" + this.includeRole + ",ExcludeRole:" + this.excludeRole + ",SourcePath:" + String.valueOf(this.sourcePath);
    }
}