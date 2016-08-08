package com.eduoco.filewatcher.helper;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dbasak on 04/08/16.
 */
public class DirectoryWatcherRunnable implements Runnable {
    private final String base;
    private final String watch_dir;
    private FileWatchedProcessHandler fileWatchedProcess;
    private long sleepTime;

    public DirectoryWatcherRunnable(final String base, String watch_dir, FileWatchedProcessHandler fileWatchedProcess, long sleepTime) {

        this.base = base;
        this.watch_dir = watch_dir;
        this.fileWatchedProcess = fileWatchedProcess;
        this.sleepTime = sleepTime;
    }

    @Override
    public void run() {
        try {

            File file = new File(watch_dir);
            Map<String, Boolean> fileMapping = new HashMap<>();
            Map<String, Boolean> lastfileMapping = new HashMap<>();

            while (true) {
                if (file.exists()) {
                    List<File> files = Arrays.asList(file.listFiles());
                    fileMapping = new HashMap<>();
                    checktitudeDir(files, fileMapping, fileWatchedProcess);
                }

                info(fileMapping, lastfileMapping);

                fileWatchedProcess.setCurrentSnapshot(fileMapping);
                fileWatchedProcess.setLastSnapshot(lastfileMapping);
                fileWatchedProcess.process();

                lastfileMapping.putAll(fileMapping);

                Thread.sleep(sleepTime);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void info(Map<String, Boolean> fileMapping, Map<String, Boolean> lastfileMapping) {
        System.out.println("------------------");
        System.out.println("Last " + lastfileMapping);
        System.out.println("Nows " + fileMapping);
    }

    private void checktitudeDir(List<File> files, Map<String, Boolean> fileMapping, FileWatchedProcessHandler fileWatchedProcess) {

        for (File file : files) {
            if (file.isDirectory()) {
                String absolutePath = file.getAbsolutePath();

                if (base != null) {
                    absolutePath = absolutePath.replaceAll(base, "");
                }

                if (fileWatchedProcess != null) {
                    fileWatchedProcess.apply(file);
                }
                fileMapping.put(absolutePath, true);

                File[] files1 = file.listFiles();
                if (files1.length > 0) {
                    checktitudeDir(Arrays.asList(files1), fileMapping, fileWatchedProcess);
                }
            }
        }
    }
}
