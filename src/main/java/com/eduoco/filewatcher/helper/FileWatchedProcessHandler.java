package com.eduoco.filewatcher.helper;

import java.io.File;
import java.util.Map;

/**
 * Created by dbasak on 03/08/16.
 */
public interface FileWatchedProcessHandler {

    void apply(File file);
    void setLastSnapshot(Map<String, Boolean> snapshot);
    void setCurrentSnapshot(Map<String, Boolean> snapshot);
    void process();

}
