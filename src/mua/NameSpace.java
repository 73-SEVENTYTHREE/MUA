package mua;

import java.util.Set;
import java.util.Stack;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;

public class NameSpace {
    public static Map<String, Value> variables = new HashMap<String, Value>() {
        private static final long serialVersionUID = 1L;
        {
            put("pi", new Value("3.14159"));
        }
    };

    public Map<String, Value> localVariables = new HashMap<String, Value>();

    public static enum ReadMode {
        inputScanner, stringArray, runList
    };

    public static Stack<Integer> jumpRun = new Stack<>();

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
            add("RETURN");
            add("export");
            add("random");
            add("INT");
            add("sqrt");
            add("readlist");
            add("word");
            add("sentence");
            add("list");
            add("join");
            add("first");
            add("last");
            add("butfirst");
            add("butlast");
            add("erall");
            add("poall");
            add("save");
            add("load");
        }
    };
}
