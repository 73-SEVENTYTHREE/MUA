package mua;

import java.util.Iterator;
import java.util.Scanner;
import java.util.Stack;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to MUA");
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

    // private NameSpace namespace = new NameSpace();

    private static void read(Scanner in) {
        Stack<Operation> operations = new Stack<Operation>();
        Stack<Value> values = new Stack<Value>();

        int j = 0;
        while (true) {
            System.out.println("processing the " + j++ + "th word");
            int expectedValues = 0;
            Iterator<Operation> it = operations.iterator();
            while (it.hasNext())
                expectedValues += it.next().getOpNum();

            if (operations.size() + values.size() - expectedValues == 1)
                break;

            String element = in.next();

            System.out.println(element);
            if (Main.ops.contains(element)) {
                System.out.println("into ops:" + element);
                operations.push(Operation.valueOf(element));
            } else {
                System.out.println("into values:" + element);
                values.push(new Value(element));
            }
        }

        while (!operations.empty()) {
            Operation op = operations.pop();
            String[] args = new String[3];
            for (int i = 0; i < op.getOpNum(); ++i) {
                args[i] = values.pop().getElement();
            }
            values.push(new Value(op.calc(args)));
        }
    }
}