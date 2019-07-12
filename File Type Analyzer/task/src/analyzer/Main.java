package analyzer;

import java.io.*;
import java.util.HashMap;
import java.util.InputMismatchException;

public class Main {
    public static void main(String[] args) {
        if (args.length != 4) throw new InputMismatchException("Wrong amount of arguments!");

        String alg = args[0];
        HashMap<String, String> dict = new HashMap<>();
        dict.put(args[2], args[3]);
        long t = System.nanoTime();
        try (InputStream inputStream = new FileInputStream(args[1])) {
            String content = new String(inputStream.readAllBytes());
            for (String s :
                    dict.keySet()) {
                if (alg.equals("--naive")) {
                    if (content.contains(s)) {
                        System.out.println(dict.get(s));
                        System.out.println("It took "+ (System.nanoTime()-t)/1000000000.0 + " seconds");
                        return;
                    }
                } else if (alg.equals("--KMP")) {
                    if(KMPSearch(content, s)) {
                        System.out.println(dict.get(s));
                        System.out.println("It took "+ (System.nanoTime()-t)/1000000000.0 + " seconds");
                        return;
                    }
                }
            }
        } catch (IOException | OutOfMemoryError e) {
            System.out.println("Error while openning file: " + args[0] + "\n" + e.getMessage());
        }
        System.out.println("Unknown file type");
        System.out.println("It took "+ (System.nanoTime()-t)/1000000000.0 + " seconds");
    }

    private static int[] prefixFunction(String str) {

        int[] prefixFunc = new int[str.length()];

        for (int i = 1; i < str.length(); i++) {

            int j = prefixFunc[i - 1];

            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefixFunc[j - 1];
            }

            if (str.charAt(i) == str.charAt(j)) {
                j += 1;
            }

            prefixFunc[i] = j;
        }

        return prefixFunc;
    }

    private static boolean KMPSearch(String text, String pattern) {

        int[] prefixFunc = prefixFunction(pattern);
        int j = 0;

        for (int i = 0; i < text.length(); i++) {

            while (j > 0 && text.charAt(i) != pattern.charAt(j)) {
                j = prefixFunc[j - 1];
            }

            if (text.charAt(i) == pattern.charAt(j)) {
                j += 1;
            }

            if (j == pattern.length()) {
                return  true;
            }
        }

        return false;
    }

}
