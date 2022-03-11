package fr.istic.vv;

import com.github.javaparser.utils.SourceRoot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws IOException {
        if(args.length == 0) {
            System.err.println("Should provide the path to the source code");
            System.exit(1);
        }

        File file = new File(args[0]);
        if(!file.exists() || !file.isDirectory() || !file.canRead()) {
            System.err.println("Provide a path to an existing readable directory");
            System.exit(2);
        }

        SourceRoot root = new SourceRoot(file.toPath());
        List<Result> resultList = new ArrayList<>();
        PrivateFieldsWithNoGetter printer = new PrivateFieldsWithNoGetter(resultList);
        root.parse("", (localPath, absolutePath, result) -> {
            result.ifSuccessful(unit -> unit.accept(printer, null));
            return SourceRoot.Callback.Result.DONT_SAVE;
        });

        printToTxt(resultList, args.length > 1 ? args[1] : "result.txt");
    }

    private static void printToTxt(List<Result> resultList, String fileName) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("Private fields with no getter:\n");
            resultList.forEach(result -> {
                try {
                    myWriter.write(result.toString() + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
