package analyzer;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class DefineClassTypeWithKMPByDict implements Callable {
    private final String text;
    private final ArrayList<Pair<String, String>> patterns;

    DefineClassTypeWithKMPByDict(String text, ArrayList<Pair<String, String>> patterns) {
        this.text = text;
        this.patterns = patterns;
    }

    @Override
    public String call() {
        for(Pair<String, String> i : patterns) {
            if(new KMPSearch(text, i.getFirst()).call()) {
                return i.getLast();
            }
        }
        return "Unknown file type";
    }
}
