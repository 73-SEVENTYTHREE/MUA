package mua;

import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            read(in);
        }
    }

    private static void read(Scanner in) {
        // 处理逻辑如下
        // 操作符栈存储操作符，values栈存储操作量
        // 一个操作符可能有多个操作量，再对应用一个栈存放
        // 始终保持两个栈的大小相等
        // 如果一个操作符后面接了数量正好的操作数，那么就可以算了
        // 否则，他需要用到后面的操作的某个返回值，而后面读入的操作符又会新开一个栈
        // 操作运算完后，对应的values会释放，返回值再压栈，供前一个操作符使用
        Stack<Operation> operations = new Stack<Operation>();
        Stack<Stack<String>> values = new Stack<>();

        while (true) {
            String element = in.next();

            if (element.startsWith(":")) {
                operations.push(Operation.colon);
                values.push(new Stack<>());
                values.peek().push(element.substring(1));
            } else {
                if (NameSpace.ops.contains(element)) {
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