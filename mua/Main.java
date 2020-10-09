package mua;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            read(in);
        }
    }

    public static ArrayList<String> ops = new ArrayList<String>() {
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
        }
    };

    private static void read(Scanner in) {
        Stack<Operation> operations = new Stack<Operation>();

        Stack<Stack<String>> values = new Stack<>();

        int j = 0;
        while (true) {
            String element = in.next();

            if (element.startsWith(":")) {
                operations.push(Operation.colon);
                values.push(new Stack<>());
                values.peek().push(element.substring(1));
            } else {
                if (Main.ops.contains(element)) {
                    operations.push(Operation.valueOf(element));
                    values.push(new Stack<String>());
                } else {
                    values.peek().push(element);
                }
            }

            while (!operations.empty()) {
                if (operations.peek().getOpNum() == values.peek().size()) {
                    String[] args = new String[operations.peek().getOpNum()];
                    for (int i = 0; i < operations.peek().getOpNum(); ++i) {
                        args[i] = values.peek().pop();
                    }
                    values.pop();

                    String retValue = operations.peek().calc(args);
                    operations.pop();

                    if (!values.empty()) {
                        values.peek().push(retValue);
                    }
                } else {
                    break;
                }
            }

            if (operations.size() == 0 && values.size() == 0)
                break;
        }
    }
}