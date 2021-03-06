# FileStatsExtractor
A little command line tool to print out the file stats of a given list of file paths

## Usage
Just run the following command in terminal after building project as 'fileAnalyzer'.
The only argument is a string that contains a comma delimited list of file paths
```
java fileAnalyzer 'path/to/file/1.ext, path/to/file/2.ext, ... , path/to/file/n.ext'
```

## Implementation
1. In FileStatsReader.java, using `Collections.synchronizedList` so that the results are collected in a thread safe manner.
2. In FileStatsReader.java, created a thread pool using `java.util.concurrent.ExecutorService`, however the current thread will be locked until all threads spawned to collect the file stats are done. This code would need to be changed if that was not the desired behavior. 
3. In FileStatsReader.java, a `newCachedThreadPool` is created instead of `newFixedThreadPool`, since getting the stats of the files should be very fast

## Sample output
```
Extracting File Stats for:
/Users/path/Pictures/pic1.jpg, /Users/path/Pictures/pic2.jpg, NonExistentFile
------------------------------------
File Name:     NonExistentFile
Error:         File does not exist
------------------------------------

------------------------------------
File Name:     pic1.jpg
Parent Dir:    /Users/path/Pictures
File Length:   27860 bytes
Last Modified: Sat Apr 19 21:56:01 MST 2014
------------------------------------

------------------------------------
File Name:     pic2.jpg
Parent Dir:    /Users/path/Pictures
File Length:   30819 bytes
Last Modified: Sat Apr 19 22:47:32 MST 2014
------------------------------------
```

