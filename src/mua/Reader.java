package mua;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Reader {
    private Scanner in;

    // 定义表读入时的层数
    private int returnLayer;

    // 定义中缀表达式的运算优先级
    private static final Map<Character, Integer> priority = new HashMap<Character, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            put('(', 1);
            put('+', 2);
            put('-', 2);
            put('*', 3);
            put('/', 3);
            put('%', 3);
            put(')', 4);
        }
    };

    Reader() {
        this.in = new Scanner(System.in);
        this.returnLayer = 0;
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

    private Value read() {
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
                values.peek().push(readList(element));
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
                    } else {
                        values.push(new Stack<>());
                        values.peek().push(new Value(retValue));
                    }
                } else {
                    break;
                }
            }

            if (operations.size() == 0)
                break;
        }

        // 一般来说，最外层的返回值是没有意义的
        // 但是在括号运算符里需要用到
        return values.peek().peek();
    }

    private Value readList(String first) {
        // [a [b [c d] e]] with no space behind '[' and in front of ']'
        System.out.println("Now reading a list");
        ArrayList<Value> list = new ArrayList<>();
        first = first.substring(1);
        boolean isFirst = true;

        String element = first;
        do {
            if (!isFirst)
                element = in.next();
            isFirst = false;

            if (element.startsWith("[")) {
                list.add(readList(element));

                if (returnLayer > 0) {
                    returnLayer--;
                    break;
                }
            } else if (element.endsWith("]")) {
                list.add(new Value(element.substring(0, element.indexOf("]"))));
                // 这时有两种情况
                // 一种是x]，那么他会在接下来的while中正常退出
                // 一种是x]]]，后面连续接了若干个右括号
                // 这种时候不仅要return，还要告诉父亲现在也应当return
                // 并且还应当传递return的层数

                char[] c = element.toCharArray();
                for (Character ch : c)
                    if (ch == ']')
                        returnLayer++;

                returnLayer--;
                break;
            } else {
                list.add(new Value(element));
            }
        } while (true);
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
