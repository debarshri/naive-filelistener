package com.eduoco.filewatcher;

import com.eduoco.filewatcher.helper.DirectoryWatcherRunnable;
import com.eduoco.filewatcher.helper.FileWatchedProcessHandler;
import com.eduoco.filewatcher.helper.FileWatcherRunnable;

import java.io.File;
import java.util.Map;

public class FileWatcher {

    public static void main(String[] args) throws InterruptedException {

        /*
        FileWatcher.watcher("test", "/Users/dbasak/IdeaProjects/test/", file -> {
            System.out.println("------");
            System.out.println(file.getName());
            System.out.println("------");
        });

        FileWatcher.directoryWatcher("test", "/Users/dbasak/IdeaProjects/test/", file -> {
            System.out.println("------");
            System.out.println(file.getName());
            System.out.println("------");
        });
*/

        FileWatcher.directoryWatcher("test", "/Users/dbasak/IdeaProjects/test/", new WatchHandler(), 1000);
    }

    public static void directoryWatcher(String watch_dir, String base_dir, FileWatchedProcessHandler fileWatchedProcess) throws InterruptedException {
        new Thread(new DirectoryWatcherRunnable(base_dir,watch_dir, fileWatchedProcess,1000)).start();
    }

    public static void directoryWatcher(String watch_dir, String base_dir, FileWatchedProcessHandler fileWatchedProcess, long sleepTime) throws InterruptedException {
        new Thread(new DirectoryWatcherRunnable(base_dir,watch_dir, fileWatchedProcess,sleepTime)).start();
    }

    public static void watcher(final String watch_dir, final String base_dir, final FileWatchedProcessHandler fileWatchedProcess) throws InterruptedException {
        new Thread(new FileWatcherRunnable(base_dir,watch_dir,fileWatchedProcess, 1000)).start();
    }

    public static void watcher(final String watch_dir, final String base_dir, final FileWatchedProcessHandler fileWatchedProcess, long sleepTime) throws InterruptedException {
        new Thread(new FileWatcherRunnable(base_dir,watch_dir,fileWatchedProcess, sleepTime)).start();
    }

    private static class WatchHandler implements FileWatchedProcessHandler {
        private Map<String, Boolean> lastSnapshot;
        private Map<String, Boolean> setCurrentSnapshot;

        @Override
        public void apply(File file) {
            System.out.println(file.getName());
        }

        @Override
        public void setLastSnapshot(Map<String, Boolean> snapshot) {
            this.lastSnapshot = snapshot;
        }

        @Override
        public void setCurrentSnapshot(Map<String, Boolean> snapshot) {
            this.setCurrentSnapshot = snapshot;
        }

        @Override
        public void process() {
            setCurrentSnapshot.keySet().removeAll(lastSnapshot.keySet());
            System.out.println(setCurrentSnapshot);
        }
    }
}
