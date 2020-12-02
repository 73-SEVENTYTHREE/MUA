package mua;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

import mua.NameSpace.ReadMode;

public class Reader {
    private Scanner in;

    // 定义表读入时的层数
    private int returnLayer;

    // 定义中缀表达式跳过的层数
    private int jumpReadExpr;

    // 定义中缀表达式中前缀表达式中中缀表达式跳过的层数
    private int jumpRead;

    // 定义中缀表达式的运算优先级
    private static final Map<String, Integer> priority = new HashMap<String, Integer>() {
        private static final long serialVersionUID = 1L;
        {
            put("(", 1);
            put("+", 2);
            put("-", 2);
            put("*", 3);
            put("/", 3);
            put("%", 3);
            put(")", 4);
        }
    };

    Reader(Scanner i) {
        this.in = i;
        this.returnLayer = 0;
    }

    public void readAll() {
        while (in.hasNext()) {
            read(NameSpace.ReadMode.inputScanner, null, null);
        }
    }

    public Value read(NameSpace.ReadMode mode, String[] exp, NameSpace n) {
        // 处理逻辑如下
        // 操作符栈存储操作符，values栈存储操作量
        // 一个操作符可能有多个操作量，再对应用一个栈存放
        // 始终保持两个栈的大小相等
        // 如果一个操作符后面接了数量正好的操作数，那么就可以算了
        // 否则，他需要用到后面的操作的某个返回值，而后面读入的操作符又会新开一个栈
        // 操作运算完后，对应的values会释放，返回值再压栈，供前一个操作符使用
        Stack<OpInterface> operations = new Stack<OpInterface>();
        Stack<Stack<Value>> values = new Stack<>();

        if (mode == ReadMode.runList && exp.length == 1) {
            return new Value(exp[0]);
        }

        int cnt = 0;
        jumpReadExpr = 0;
        while (true) {
            String element;
            if (mode == NameSpace.ReadMode.inputScanner) {
                element = in.next();
            } else {
                element = exp[cnt++];
            }

            if (element.startsWith("[")) {
                values.peek().push(readList(element));
            } else if (element.startsWith("(")) {
                if (mode == NameSpace.ReadMode.inputScanner) {
                    values.peek().push(readExpr(NameSpace.ReadMode.inputScanner, element, null, n));
                } else {
                    // 这种情况实在是过于复杂了
                    // 他是指(add (1+2) 3)这种情况里，add以后再去递归地读一个中缀表达式
                    // 要传的参数是：以element作为first，以element后面的所有元素作为srcExp
                    ArrayList<String> tempArrayList = new ArrayList<>(Arrays.asList(exp));
                    List<String> tempList = tempArrayList.subList(cnt, tempArrayList.size());
                    String[] srcExp = tempList.toArray(new String[0]);
                    values.peek().push(readExpr(NameSpace.ReadMode.stringArray, element, srcExp, n));
                    jumpReadExpr += jumpRead;
                    jumpReadExpr--;
                }
            } else if (element.startsWith(":")) {
                operations.push(Operation.colon);
                values.push(new Stack<>());
                values.peek().push(new Value(element.substring(1)));
            } else {
                if (element.equals("if"))
                    element = "IF";
                if (element.equals("return"))
                    element = "RETURN";

                if (NameSpace.ops.contains(element)) {
                    operations.push(Operation.valueOf(element));
                    values.push(new Stack<Value>());
                } else if (NameSpace.variables.containsKey(element)
                        && NameSpace.variables.get(element).type == Value.Type.function) {
                    // 每个操作都要新建一个Function，有一个新的NameSpace
                    Function f = NameSpace.variables.get(element).func;
                    operations.push(new Function(f.getOpNum(), f.paraList, f.runList));
                    values.push(new Stack<Value>());
                } else {
                    values.peek().push(new Value(element));
                }
            }

            while (!operations.empty()) {
                if (operations.peek().getOpNum() == values.peek().size()) {
                    Value[] args = new Value[operations.peek().getOpNum()];
                    for (int i = 0; i < operations.peek().getOpNum(); ++i) {
                        args[i] = values.peek().pop();
                    }
                    values.pop();

                    String retValue = operations.peek().calc(args, n);
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

            if (mode == ReadMode.runList) {
                if (cnt == exp.length)
                    break;
            } else if (operations.size() == 0)
                break;
        }

        jumpReadExpr += cnt;

        // 一般来说，最外层的返回值是没有意义的
        // 但是在括号运算符里需要用到
        return values.peek().peek();
    }

    private Value readList(String first) {
        // [a [b [c d] e]] with no space behind '[' and in front of ']'
        ArrayList<Value> list = new ArrayList<>();
        boolean isFirst = true;
        if (first.equals("[")) {
            isFirst = false;
        } else {
            first = first.substring(1);
        }

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
                if (element.equals("]")) {
                    //returnLayer++;
                    break;
                }
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
        if (v.listElement.size() == 2) {
            if (v.listElement.get(0).type == Value.Type.list && v.listElement.get(1).type == Value.Type.list) {
                v.type = Value.Type.function;
                v.func = new Function(v.listElement.get(0).listElement.size(), v.listElement.get(0),
                        v.listElement.get(1));
                v.func.runList = v.listElement.get(1);
            }
        }
        return v;
    }

    private Value readExpr(NameSpace.ReadMode mode, String first, String[] srcExp, NameSpace n) {
        String exp = first;
        int left = 0, right = 0;
        int cnt = 0;
        jumpRead = 0;

        for (Character c : first.toCharArray()) {
            if (c.equals('('))
                left++;
            if (c.equals(')'))
                right++;
        }

        while (left != right) {
            String next;
            if (mode == NameSpace.ReadMode.inputScanner) {
                next = in.next();
            } else {
                next = srcExp[cnt++];
            }
            for (Character c : next.toCharArray()) {
                if (c.equals('('))
                    left++;
                if (c.equals(')'))
                    right++;
            }
            exp = exp + " " + next;
        }

        // add space
        for (int i = 0; i < exp.length(); ++i) {
            char c = exp.charAt(i);
            if (priority.containsKey(String.valueOf(c))) {
                if (c == '-') {
                    int j = 1;
                    char pre = exp.charAt(i - 1);
                    while (pre == ' ') {
                        pre = exp.charAt(i - j);
                        j++;
                    }
                    if (priority.containsKey(String.valueOf(pre)))
                        continue;
                }
                StringBuilder sb = new StringBuilder(exp);
                sb.insert(i, " ");
                sb.insert(i + 2, " ");
                i += 2;
                exp = sb.toString();
            }
        }

        exp = exp.replace(":", " colon ");

        // 第一个是空，后面每个都分割好了
        String[] words = exp.split("\\s+");

        Stack<String> op = new Stack<>();
        Stack<Double> num = new Stack<>();

        for (int i = 1; i < words.length; ++i) {
            String s = words[i];
            try {
                Double d = Double.parseDouble(s);
                num.push(d);
            } catch (NumberFormatException e) {
                if (NameSpace.ops.contains(s)) {
                    ArrayList<String> tempArrayList = new ArrayList<String>(Arrays.asList(words));
                    List<String> tempList = tempArrayList.subList(i, tempArrayList.size());
                    String[] tempStr = tempList.toArray(new String[0]);
                    Value v = read(NameSpace.ReadMode.stringArray, tempStr, n);
                    num.push(v.getNumber());
                    i += jumpReadExpr;
                    i--;// 因为外面还有一个i++
                } else {
                    if (s.equals("(")) {
                        op.push(s);
                    } else if (s.equals(")")) {
                        while (!op.peek().equals("(")) {
                            num.push(calc2Num(num, op.pop()));
                        }
                        op.pop();
                    } else {
                        if (op.empty())
                            op.push(s);
                        else {
                            while (!op.empty()) {
                                String temp = op.peek();
                                if (priority.get(temp) >= priority.get(s))
                                    num.push(calc2Num(num, op.pop()));
                                else
                                    break;
                            }
                            op.push(s);
                        }
                    }
                }
            }
        }

        jumpRead = words.length - 1;
        return new Value(String.valueOf(num.pop()));
    }

    private double calc2Num(Stack<Double> operand, String op) {
        double b = operand.pop();
        double a = operand.pop();
        if (op.equals("+"))
            return a + b;
        else if (op.equals("-"))
            return a - b;
        else if (op.equals("*"))
            return a * b;
        else if (op.equals("/"))
            return a / b;
        else
            return a % b;
    }
}
