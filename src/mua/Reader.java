package mua;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Reader {
    private Scanner in;

    Reader() {
        this.in = new Scanner(System.in);
    }

    public static void main(String[] args) {
        Reader reader = new Reader();
        reader.readAll();
    }

    private void readAll() {
        while (in.hasNext()) {
            read();
        }
    }

    private void read() {
        // 处理逻辑如下
        // 操作符栈存储操作符，values栈存储操作量
        // 一个操作符可能有多个操作量，再对应用一个栈存放
        // 始终保持两个栈的大小相等
        // 如果一个操作符后面接了数量正好的操作数，那么就可以算了
        // 否则，他需要用到后面的操作的某个返回值，而后面读入的操作符又会新开一个栈
        // 操作运算完后，对应的values会释放，返回值再压栈，供前一个操作符使用
        Stack<Operation> operations = new Stack<Operation>();
        Stack<Stack<Value>> values = new Stack<>();

        while (true) {
            String element = in.next();

            if (element.startsWith("[")) {
                readList(element);
            } else if (element.startsWith("(")) {
                readExpr();
            } else if (element.startsWith(":")) {
                operations.push(Operation.colon);
                values.push(new Stack<>());
                values.peek().push(new Value(element.substring(1)));
            } else {
                if (NameSpace.ops.contains(element)) {
                    operations.push(Operation.valueOf(element));
                    values.push(new Stack<Value>());
                } else {
                    values.peek().push(new Value(element));
                }
            }

            while (!operations.empty()) {
                if (operations.peek().getOpNum() == values.peek().size()) {
                    String[] args = new String[operations.peek().getOpNum()];
                    for (int i = 0; i < operations.peek().getOpNum(); ++i) {
                        args[i] = values.peek().pop().getElement();
                    }
                    values.pop();

                    String retValue = operations.peek().calc(args);
                    operations.pop();

                    if (!values.empty()) {
                        values.peek().push(new Value(retValue));
                    }
                } else {
                    break;
                }
            }

            if (operations.size() == 0 && values.size() == 0)
                break;
        }
    }

    private Value readList(String first) {
        // [a [b [c d] e]] with no space behind '[' and in front of ']'
        ArrayList<Value> list = new ArrayList<>();
        first = first.substring(1);
        boolean isFirst = true;

        String element = first;
        do {
            if (!isFirst)
                element = in.next();

            if (element.startsWith("[")) {
                list.add(readList(element));
            } else {
                list.add(new Value(element));
            }
        } while (!element.endsWith(")"));
        Value v = new Value("");
        v.type = Value.Type.list;
        for (Value value : list) {
            v.addListVal(value);
        }
        return v;
    }

    private void readExpr() {

    }
}
