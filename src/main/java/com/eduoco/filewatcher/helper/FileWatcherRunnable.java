package com.eduoco.filewatcher.helper;

import com.eduoco.filewatcher.helper.FileWatchedProcessHandler;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dbasak on 04/08/16.
 */
public class FileWatcherRunnable implements Runnable {
    private final String base_dir;
    private final String watch_dir;
    private final FileWatchedProcessHandler fileWatchedProcess;
    private long sleepTime;

    public FileWatcherRunnable(String base_dir, String watch_dir, FileWatchedProcessHandler fileWatchedProcess, long sleepTime) {

        this.base_dir = base_dir;
        this.watch_dir = watch_dir;
        this.fileWatchedProcess = fileWatchedProcess;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        File file = new File(watch_dir);
        Map<String, Boolean> fileMapping = new HashMap<>();
        Map<String, Boolean> lastfileMapping = new HashMap<>();

        while (true) {
            if (file.exists()) {
                List<File> files = Arrays.asList(file.listFiles());
                fileMapping = new HashMap<>();
                checktitude(files, fileMapping, fileWatchedProcess);
            }

            System.out.println("------------------");
            System.out.println("Last " + lastfileMapping);
            System.out.println("Nows " + fileMapping);
            lastfileMapping = new HashMap<>(fileMapping);
            System.out.println("------------------");

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checktitude(final List<File> files, final Map<String, Boolean> fileMapping, FileWatchedProcessHandler fileWatchedProcess) {

        for (File file : files) {
            if (file.isDirectory()) {
                checktitude(Arrays.asList(file.listFiles()), fileMapping, fileWatchedProcess);
            } else {

                String absolutePath = file.getAbsolutePath();

                if (base_dir != null) {
                    absolutePath = absolutePath.replaceAll(base_dir, "");
                }

                if (fileWatchedProcess != null) {
                    fileWatchedProcess.apply(file);
                }
                fileMapping.put(absolutePath, true);
            }
        }
    }

}
