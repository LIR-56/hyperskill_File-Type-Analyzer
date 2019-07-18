package analyzer;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        if (args.length != 2) throw new InputMismatchException("Wrong amount of arguments!");

        ArrayList<Pair<String, String>> patterns;
        patterns = readPatterns(new File(args[1]));
        if (patterns == null) return;

        File directory = new File(args[0]);
        ExecutorService executor = Executors.newFixedThreadPool(10);
        HashMap<String, Future<String>> fileCheckers = new HashMap<>();

        for (File f : directory.listFiles()) {
            try (InputStream inputStream = new FileInputStream(f)) {
                String content = new String(inputStream.readAllBytes());
                Future<String> search = executor.submit(new DefineClassTypeWithKMPByDict(content, patterns));
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
            System.out.println(fileName + ": " + fileCheckers.get(fileName).get());
        }
        executor.shutdown();
    }


    private static ArrayList<Pair<String, String>> readPatterns(File patternsDataBase) {
        ArrayList<Pair<String, String>> patterns;
        try (InputStream inputStream = new FileInputStream(patternsDataBase)) {
            String[] parsedContent = new String(inputStream.readAllBytes()).split("\n");

            patterns = new ArrayList<>(parsedContent.length);
            for (int i = parsedContent.length-1; i >= 0; i--) {
                String[] parsedRaw = parsedContent[i].split(";");
                patterns.add(new Pair<>(parsedRaw[1].substring(1, parsedRaw[1].length() - 1),
                        parsedRaw[2].substring(1, parsedRaw[2].length() - 1)));
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return patterns;
    }
}

class Pair<T, U> {
    private final T first;
    private final U last;

    Pair(T first, U last) {
        this.first = first;
        this.last = last;
    }

    T getFirst() {
        return first;
    }

    U getLast() {
        return last;
    }

}