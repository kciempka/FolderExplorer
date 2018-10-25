package pl.qnet.sample;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Comparator;

/**
 * The Folder Explorer program implements an application that
 * simply displays directory content to the standard output.
 *
 * @author Jakub Ciempka
 * @version 1.0
 * @since 2018-10-25
 */
public class Du {

    //output message
    private static Output appOutput = new ConsoleOutput();

    public static void main(String[] args) {

        appOutput.sendOutputString("Welcome in Folder Explorer");

        //Folder explorer
        Explorer explorer = new Explorer();
        String folderPath = (args.length > 0) ? args[0] : null;
        try {
            //check args, if null then use current directory
            if (folderPath == null || folderPath.isEmpty()) {
                folderPath = new java.io.File(".").getCanonicalPath();
                appOutput.sendOutputString("No input argument, using current directory.");
            } else {
                appOutput.sendOutputString("Using:" + folderPath);
            }

            explorer.justExplore(folderPath, appOutput);

            appOutput.sendOutputString("Folder Explorer end");

        } catch (IOException e) {
            //just printstacktrace
            //ToDo - add logger
            e.printStackTrace();
        }
    }

    /**
     *  Explorer responsible for display directory content
     */
    static class Explorer {
        private void justExplore(String folderPath, Output output) {
            File fileSystemObtainedFile = new File(folderPath);
            if (fileSystemObtainedFile != null && fileSystemObtainedFile.listFiles() != null) {
                Arrays.stream(fileSystemObtainedFile.listFiles()).map(
                        i -> {
                            try {
                                //convert to FileItem
                                return new FileItem(i.isFile() ? "File" : "Dir", i.getCanonicalPath(), i.isFile() ? (i.length() / 1024) : Files.walk(i.toPath()).mapToLong(p -> p.toFile().length()).sum() / 1024);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                ).sorted(new Comparator<FileItem>() {
                    //sort file items
                    public int compare(FileItem o1, FileItem o2) {
                        int result;
                        if (o1.size == o2.size) {
                            result = 0;
                        } else if (o1.size > o2.size) {
                            result = 1;
                        } else {
                            result = -1;
                        }
                        return -1 * result;
                    }
                }).forEachOrdered(
                        //send toString to output
                        i -> output.sendOutputString(i.toString())
                );
            } else {
                output.sendOutputString("Invalid path");
            }
        }
    }


    static class FileItem {
        private String type;
        private String name;
        private Long size;

        public FileItem(String type, String name, Long size) {
            this.type = type;
            this.name = name;
            this.size = size;
        }

        public String getType() {
            return type;
        }

        public String getName() {
            return name;
        }

        public Long getSize() {
            return size;
        }

        @Override
        public String toString() {
            return (String.format("%4s", type) + " " +String.format("%-60s", name) + " " + size + "KB").toUpperCase();
        }
    }

}
