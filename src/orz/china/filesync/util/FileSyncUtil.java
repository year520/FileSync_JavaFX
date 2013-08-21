package orz.china.filesync.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileSyncUtil {

    private static Logger log = Logger.getLogger(FileSyncUtil.class);

    public static void copyFile(String source, String target) {
	copyFile(new File(source), new File(target));
    }

    public static void deleteFile(String source) {
	log.info("delete file:" + source);
	deleteFile(new File(source));
    }

    public static void copyFile(File source, File target) {
	try {
	    log.info("copy file:source:" + source.getAbsolutePath() + "--- target:" + target.getAbsolutePath());
	    FileUtils.copyFile(source, target);
	} catch (IOException e) {
	    log.error("copy error!" + e.getMessage() + "\n source:" + source.getAbsolutePath() + "\n target:" + target.getAbsolutePath());
	}
    }

    public static void deleteFile(File source) {
	log.info("delete file:" + source.getAbsolutePath());
	FileUtils.deleteQuietly(source);
    }

    public static void copyDirectory(String source, String target) {
	try {
	    log.info("copy Directory:source:" + source + "--- target:" + target);
	    FileUtils.copyDirectory(new File(source), new File(target));
	} catch (IOException e) {
	    log.error("error happen:", e);
	}
    }

    public static void copyDirectory(String source, String target, FileFilter filter) {
	try {
	    log.info("copy Directory:source:" + source + "--- target:" + target + "  filter:" + filter.getClass().getSimpleName());
	    FileUtils.copyDirectory(new File(source), new File(target), filter);
	} catch (IOException e) {
	    log.error("error happen:", e);
	}
    }
}
