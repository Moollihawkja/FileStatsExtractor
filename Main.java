package com.company;

import java.io.File;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        //Validate the arguments
        if (args.length != 1) {
            System.err.println("Please enter the paths as a comma delimited string. This app takes only a single argument");
            return;
        }
        System.out.println("Extracting File Stats for:");
        System.out.println(args[0]);

        try {
            //Convert the string of filepaths to an ArrayList of the Files
            ArrayList<File> files = new ArrayList<>();
            for (String filePath : args[0].split(",")) {
                File file = new File(filePath.trim());
                files.add(file);
            }

            //Getting all the file stats and print results to stdout when done
            FileStatsReader fileStatsReader = new FileStatsReader();
            fileStatsReader.printStats(files, () -> {
                if (!fileStatsReader.wasSuccessful()){
                    System.err.println(fileStatsReader.getException().getMessage());
                }
                else {
                    for (String results : fileStatsReader.getStatResults()) {
                        System.out.println("------------------------------------");
                        System.out.println(results);
                        System.out.println("------------------------------------\n");
                    }
                }
            });
        }
        catch (Exception ex) {
            System.err.println(String.format("Encountered Exception: %s", ex.getMessage()));
        }
    }
}
