package analyzer;

import java.util.concurrent.Callable;

public class KMPSearch implements Callable {

    private String text;
    private String pattern;

    KMPSearch(String text, String pattern) {
        this.text = text;
        this.pattern = pattern;
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


    @Override
    public Boolean call() {

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
