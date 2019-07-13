package analyzer;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length != 3) throw new InputMismatchException("Wrong amount of arguments!");


        String type = args[2];
        String pattern = args[1];
        File directory = new File(args[0]);
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        HashMap<String, Future<Boolean>> fileCheckers = new HashMap<>();
        for (File f : Objects.requireNonNull(directory.listFiles())) {
            try (InputStream inputStream = new FileInputStream(f)) {
                String content = new String(inputStream.readAllBytes());
                Future<Boolean> search = executor.submit(new KMPSearch(content, pattern));
                fileCheckers.put(f.getName(), search);
            } catch (IOException | OutOfMemoryError e) {
                System.out.println("Error while opening file: " + args[0] + "\n" + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        var filenames = (String[]) fileCheckers.keySet().toArray(new String[0]);
        Arrays.sort(filenames);
        for (String fileName : filenames) {
            if (fileCheckers.get(fileName).get()) {
                System.out.println(fileName + ": " + type);
            } else {
                System.out.println(fileName + ": " + "Unknown file type");
            }
        }
        executor.shutdown();
    }
}