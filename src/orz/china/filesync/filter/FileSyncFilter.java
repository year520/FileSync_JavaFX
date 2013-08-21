package orz.china.filesync.filter;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.commons.io.filefilter.FileFileFilter;

public class FileSyncFilter extends FileFileFilter {

    private static final long serialVersionUID = 8767906439203856335L;
    private Pattern include;
    private Pattern exclude;

    public FileSyncFilter(String include, String exclude) {
	if (null != include && !"".equals(include))
	    this.include = Pattern.compile(include);
	if (null != exclude && !"".equals(exclude))
	    this.exclude = Pattern.compile(exclude);
    }

    public boolean accept(File file) {
	String filePath = file.getAbsolutePath();
	if (null != include) {
	    if (include.matcher(filePath).find()) {
		return true;
	    }
	}
	if (null != exclude) {
	    if (exclude.matcher(filePath).find()) {
		return false;
	    }
	}
	return true;
    }

}
