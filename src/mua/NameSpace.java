package mua;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class NameSpace {
    public static Map<String, Value> variables = new HashMap<String, Value>();

    public static enum ReadMode {
        inputScanner, stringArray, runList
    };

    public static Set<String> ops = new HashSet<String>() {
        private static final long serialVersionUID = 1L;
        {
            add("make");
            add("thing");
            add("colon");
            add("read");
            add("print");
            add("add");
            add("sub");
            add("mul");
            add("div");
            add("mod");
            add("erase");
            add("isname");
            add("run");
            add("eq");
            add("gt");
            add("lt");
            add("and");
            add("or");
            add("not");
            add("IF");
            add("isnumber");
            add("isword");
            add("islist");
            add("isbool");
            add("isempty");
        }
    };
}
