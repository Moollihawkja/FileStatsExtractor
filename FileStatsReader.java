package com.company;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;


public class FileStatsReader {

    private List<String> fileStatResults;
    private ExecutorService threadPool;
    private boolean isDone = false;
    private Exception exception;

    public FileStatsReader() {
        //Creating a synchronized list so that fileStatsResults can be mutated in a thread safe manner
        fileStatResults = Collections.synchronizedList(new ArrayList<>());
        threadPool = Executors.newCachedThreadPool();
        exception = null;F
    }

    public void printStats(ArrayList<File> files, Runnable callback) {
        getAllFileStats(files, callback);
    }

    public List<String> getStatResults() {
        List<String> results = new ArrayList<>();
        synchronized (fileStatResults) {
            Iterator i = fileStatResults.iterator();
            while (i.hasNext()) {
                results.add((String) i.next());
            }
        }
        return results;
    }

    public boolean wasSuccessful() {
        return isDone;
    }

    public Exception getException() {
        return exception;
    }

    private void getAllFileStats(ArrayList<File> files, Runnable callback) {
        for(File file: files) {
            threadPool.submit(() -> {
                String fileStats = getFileStats(file);
                fileStatResults.add(fileStats);
            });
        }
        try {
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
            isDone = true;
        }
        catch (Exception ex) {
            Thread.currentThread().interrupt();
            exception = ex;
        }
        finally {
            callback.run();
        }

    }

    private String getFileStats(File file){
        StringJoiner stats = new StringJoiner("\n");
        String error = "";
        try {
            stats.add(String.format("File Name:     %s", file.getName()));
            if (file.isDirectory()) {
                error = "Is a directory";
            } else if (file.isFile()) {

                stats.add(String.format("Parent Dir:    %s", file.getParent()));
                stats.add(String.format("File Length:   %d bytes", file.length()));
                stats.add(String.format("Last Modified: %s", new Date(file.lastModified())));
            } else {
                error = "File does not exist";
            }
            if (!error.isEmpty()) {
                stats.add(String.format("Error:         %s", error));
            }
        }
        catch (Exception ex){
            String exception = ex.getMessage();
            stats.add(String.format("Exception:     %s", exception));
        }
        return stats.toString();
    }
}
