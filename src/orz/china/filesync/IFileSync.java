package orz.china.filesync;

public interface IFileSync {

    void monitorAndSyncFile() throws Exception;

    void syncAllFile() throws Exception;
    void stop() throws Exception;
}
