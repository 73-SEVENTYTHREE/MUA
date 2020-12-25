package mua;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;
import java.io.IOException;

public enum Operation implements OpInterface {
    make {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // make args[1] args[0]
            // make v1 v2
            // 毫无疑问，v1一定会以引号开头
            String v1 = args[1].getElement().substring(1);
            String v2 = args[0].getElement();

            Map<String, Value> v = (n == null) ? NameSpace.variables : n.localVariables;
            // 如果不存在v1的字面量的变量就新建一个
            if (!v.containsKey(v1)) {
                v.put(v1, new Value(""));
            }

            if (args[0].type == Value.Type.function) {
                v.put(v1, args[0]);
                return args[0];
            } else {
                // 如果v2不以引号开头，那么他一定是某个变量的名字，直接赋值就行了（相当于指针）
                if (!v2.startsWith("\"")) {
                    v.get(v1).setElement(v2);
                    return new Value(v2);
                } else {
                    v.get(v1).setElement(v2.substring(1));
                    return new Value(v2.substring(1));
                }
            }
        }
    },
    colon {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // :v
            Map<String, Value> v = (n == null) ? NameSpace.variables : n.localVariables;
            String s = args[0].getElement();

            if (v.containsKey(s)) {
                return v.get(s);
            } else {
                return NameSpace.variables.get(s);
            }
        }
    },
    thing {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // thing v
            return Operation.colon.calc(args, n);
        }
    },
    print {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            String retString = args[0].getElement();
            if (args[0].getElement().startsWith("\""))
                retString = retString.substring(1);

            // 如果是浮点数的话，要避免输出5.0的情况
            try {
                Double d = Double.parseDouble(retString);
                if (d.intValue() == d) {
                    System.out.println(d.intValue());
                } else {
                    System.out.println(d);
                }
                return new Value(String.valueOf(d));
            } catch (NumberFormatException e) {

            }
            System.out.println(retString);
            return args[0];
        }
    },
    read {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            return args[0];
        }
    },
    add {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // add n1 n2
            Double[] d = getTwoNum(args);
            return new Value(String.valueOf(d[1] + d[0]));
        }
    },
    sub {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // sub n1 n2
            Double[] d = getTwoNum(args);
            return new Value(String.valueOf(d[1] - d[0]));
        }
    },
    mul {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // mul n1 n2
            Double[] d = getTwoNum(args);
            return new Value(String.valueOf(d[1] * d[0]));
        }
    },
    div {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // div n1 n2
            Double[] d = getTwoNum(args);
            return new Value(String.valueOf(d[1] / d[0]));
        }
    },
    mod {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // mod n1 n2
            Double[] d = getTwoNum(args);
            return new Value(String.valueOf(d[1].intValue() % d[0].intValue()));
        }
    },
    erase {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // erase v
            Value ret = NameSpace.variables.get(args[0].getElement().substring(1));
            NameSpace.variables.remove(args[0].getElement().substring(1));
            return ret;
        }
    },
    isname {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // isname v
            return new Value(String.valueOf(NameSpace.variables.containsKey((args[0].getElement()).substring(1))));
        }
    },
    eq {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // eq v1 v2
            String v1 = args[0].getWord();
            String v2 = args[1].getWord();

            // 这里还是要考虑两个变量是否是数字的情况
            // 是为了避免出现15.0和15的情况
            try {
                double d1 = Double.parseDouble(v1);
                double d2 = Double.parseDouble(v2);
                return new Value(String.valueOf(d1 == d2));
            } catch (NumberFormatException e) {

            }
            return new Value(String.valueOf(v1.equals(v2)));
        }
    },
    gt {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // eq v1 v2
            String v1 = args[0].getElement();
            String v2 = args[1].getElement();

            if (v1.startsWith("\""))
                v1 = v1.substring(1);

            if (v2.startsWith("\""))
                v2 = v2.substring(1);

            // 这里还是要考虑两个变量是否是数字的情况
            // 是为了避免出现15.0和15的情况
            try {
                double d1 = Double.parseDouble(v1);
                double d2 = Double.parseDouble(v2);
                return new Value(String.valueOf(d1 < d2));
            } catch (NumberFormatException e) {

            }
            return new Value(String.valueOf(v1.compareTo(v2) < 0));
        }
    },
    lt {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // eq v1 v2
            String v1 = args[0].getWord();
            String v2 = args[1].getWord();

            // 这里还是要考虑两个变量是否是数字的情况
            // 是为了避免出现15.0和15的情况
            try {
                double d1 = Double.parseDouble(v1);
                double d2 = Double.parseDouble(v2);
                return new Value(String.valueOf(d1 > d2));
            } catch (NumberFormatException e) {

            }
            return new Value(String.valueOf(v1.compareTo(v2) > 0));
        }
    },
    and {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // and v1 v2
            boolean b1 = Boolean.valueOf(args[0].getWord());
            boolean b2 = Boolean.valueOf(args[1].getWord());
            return new Value(String.valueOf(b1 && b2));
        }
    },
    or {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // or v1 v2
            boolean b1 = Boolean.valueOf(args[0].getWord());
            boolean b2 = Boolean.valueOf(args[1].getWord());
            return new Value(String.valueOf(b1 || b2));
        }
    },
    not {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // not v
            boolean b = Boolean.valueOf(args[0].getWord());
            return new Value(String.valueOf(!b));
        }
    },
    isnumber {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // isnumber v
            String v = args[0].getElement();
            if (v.startsWith("\""))
                v = v.substring(1);
            try {
                Double.parseDouble(v);
                return new Value(String.valueOf(true));
            } catch (NumberFormatException e) {
                return new Value(String.valueOf(false));
            }
        }
    },
    isbool {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // isbool v
            String v = args[0].getElement();
            return new Value(String.valueOf(v.equals("true") || v.equals("false")));
        }
    },
    isword {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // isword v
            String v = args[0].getElement();
            try {
                Double.parseDouble(v);
                return new Value(String.valueOf(false));
            } catch (NumberFormatException e) {
                if (!v.equals("true") && !v.equals("false") && !v.contains(" "))
                    return new Value(String.valueOf(true));
                else
                    return new Value(String.valueOf(false));
            }
        }
    },
    islist {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // islist v
            // return new Value(String.valueOf(args[0].type == Value.Type.list));
            return new Value("true");
        }
    },
    isempty {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // isempty v
            if (args[0].getElement().equals(""))
                return new Value(String.valueOf(true));
            else
                return new Value(String.valueOf(false));

        }
    },
    IF {
        public int operandNum = 3;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            Value[] v = new Value[1];

            if (args[2].getElement().equals("true")) {
                v[0] = args[1];
            } else {
                v[0] = args[0];
            }
            return Operation.run.calc(v, n);
            // return "";
        }
    },
    run {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            // run [print add :a :b]
            // 传进来永远只有一个参数args[0]，是一个list

            int len = args[0].listElement.size();
            ArrayList<Value> runList = args[0].listElement;

            Stack<OpInterface> operations = new Stack<>();
            Stack<Stack<Value>> values = new Stack<>();

            Map<String, Value> m = (n == null) ? NameSpace.variables : n.localVariables;

            for (int i = 0; i < len; ++i) {
                Value element = runList.get(i);
                String s = element.getElement();
                if (s.equals("if")) {
                    s = "IF";
                }
                if (s.equals("return")) {
                    s = "RETURN";
                }
                if (s.startsWith(":")) {
                    operations.push(Operation.colon);
                    values.push(new Stack<>());
                    values.peek().push(new Value(s.substring(1)));
                } else if (NameSpace.ops.contains(s)) {
                    operations.push(Operation.valueOf(s));
                    values.push(new Stack<Value>());
                } else if (m.containsKey(s) && m.get(s).type == Value.Type.function) {
                    // 每个操作都要新建一个Function，有一个新的NameSpace
                    Function f = m.get(s).func;
                    operations.push(new Function(f.getOpNum(), f.paraList, f.runList));
                    values.push(new Stack<Value>());
                } else if (NameSpace.variables.containsKey(s)
                        && NameSpace.variables.get(s).type == Value.Type.function) {
                    // 每个操作都要新建一个Function，有一个新的NameSpace
                    Function f = NameSpace.variables.get(s).func;
                    operations.push(new Function(f.getOpNum(), f.paraList, f.runList));
                    values.push(new Stack<Value>());
                } else {
                    values.peek().push(element);
                }
                while (!operations.empty()) {
                    if (operations.peek().getOpNum() == values.peek().size()) {
                        Value[] a = new Value[operations.peek().getOpNum()];
                        for (int j = 0; j < operations.peek().getOpNum(); ++j) {
                            a[j] = values.peek().pop();
                        }
                        values.pop();

                        Value retValue = operations.peek().calc(a, n);
                        operations.pop();

                        Value newV;
                        if (retValue.type == Value.Type.function) {
                            newV = new Value("");
                            newV.type = Value.Type.function;
                            newV.func = new Function(retValue.func.getOpNum(), retValue.func.paraList,
                                    retValue.func.runList);
                        } else {
                            newV = retValue;
                        }
                        if (!values.empty()) {
                            values.peek().push(newV);
                        } else {
                            values.push(new Stack<>());
                            values.peek().push(newV);
                        }
                    } else {
                        break;
                    }
                }
            }

            return values.peek().peek();
        }
    },
    export {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            String name = args[0].getElement().substring(1);
            NameSpace.variables.put(name, new Value(n.localVariables.get(name).getElement()));
            return n.localVariables.get(name);
        }
    },
    RETURN {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            return args[0];
        }
    },
    random {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            double d = Double.parseDouble(args[0].getElement());
            double rd = Math.random() * d;
            return new Value(String.valueOf(rd));
        }
    },
    INT {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            double d = Double.parseDouble(args[0].getElement());
            int i = (int) d;
            return new Value(String.valueOf(i));
        }
    },
    sqrt {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            double d = Double.parseDouble(args[0].getElement());
            double r = Math.sqrt(d);
            return new Value(String.valueOf(r));
        }
    },
    readlist {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            return args[0];
        }
    },
    word {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            return new Value(args[1].getElement() + args[0].getElement());
        }
    },
    sentence {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            String s1 = args[1].getElement();
            String s2 = args[0].getElement();
            if (s1.startsWith("\"")) {
                s1 = s1.substring(1);
            }
            if (s2.startsWith("\"")) {
                s2 = s2.substring(1);
            }
            ArrayList<Value> l = new ArrayList<>();
            if (args[1].type != Value.Type.list) {
                l.add(new Value(s1));
            } else {
                for (Value v : args[1].listElement) {
                    l.add(v);
                }
            }
            if (args[0].type != Value.Type.list) {
                l.add(new Value(s2));
            } else {
                for (Value v : args[0].listElement) {
                    l.add(v);
                }
            }
            Value v = new Value("");
            v.listElement = l;
            v.type = Value.Type.list;
            return v;
        }
    },
    list {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            String s1 = args[1].getElement();
            String s2 = args[0].getElement();
            if (s1.startsWith("\"")) {
                s1 = s1.substring(1);
            }
            if (s2.startsWith("\"")) {
                s2 = s2.substring(1);
            }
            ArrayList<Value> l = new ArrayList<>();
            if (args[1].type != Value.Type.list) {
                l.add(new Value(s1));
            } else {
                l.add(args[1]);
            }
            if (args[0].type != Value.Type.list) {
                l.add(new Value(s2));
            } else {
                l.add(args[0]);
            }
            Value v = new Value("");
            v.listElement = l;
            v.type = Value.Type.list;
            return v;
        }
    },
    join {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            String s = args[0].getElement();
            if (s.startsWith("\"")) {
                s = s.substring(1);
            }
            Value v = new Value("");
            v.listElement = args[1].listElement;
            v.type = Value.Type.list;
            if (args[0].type != Value.Type.list) {
                v.listElement.add(new Value(s));
            } else
                v.listElement.add(args[0]);
            return v;
        }
    },
    first {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            if (args[0].type == Value.Type.list) {
                return args[0].listElement.get(0);
            } else {
                String s = args[0].getElement();
                if (s.startsWith("\"")) {
                    s = s.substring(1);
                }
                String f = s.substring(0, 1);
                return new Value(f);
            }
        }
    },
    last {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            if (args[0].type == Value.Type.list) {
                return args[0].listElement.get(args[0].listElement.size() - 1);
            } else {
                String s = args[0].getElement();
                if (s.startsWith("\"")) {
                    s = s.substring(1);
                }
                String f = s.substring(s.length() - 1, s.length());
                return new Value(f);
            }
        }
    },
    butlast {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            if (args[0].type == Value.Type.list) {
                ArrayList<Value> l = new ArrayList<>();
                for (int i = 0; i < args[0].listElement.size() - 1; ++i) {
                    l.add(args[0].listElement.get(i));
                }
                Value v = new Value("");
                v.listElement = l;
                v.type = Value.Type.list;
                return v;

            } else {
                String s = args[0].getElement();
                if (s.startsWith("\"")) {
                    s = s.substring(1);
                }
                String f = s.substring(0, s.length() - 1);
                Value v = new Value("");
                v.listElement.add(new Value(f));
                v.type = Value.Type.list;
                return v;
            }
        }
    },
    butfirst {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            if (args[0].type == Value.Type.list) {
                ArrayList<Value> l = new ArrayList<>();
                for (int i = 1; i < args[0].listElement.size(); ++i) {
                    l.add(args[0].listElement.get(i));
                }
                Value v = new Value("");
                v.listElement = l;
                v.type = Value.Type.list;
                return v;

            } else {
                String s = args[0].getElement();
                if (s.startsWith("\"")) {
                    s = s.substring(1);
                }
                String f = s.substring(1, s.length());
                Value v = new Value("");
                v.listElement.add(new Value(f));
                v.type = Value.Type.list;
                return v;
            }
        }
    },
    erall {
        public int operandNum = 0;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            NameSpace.variables.clear();
            return new Value("true");
        }
    },
    poall {
        public int operandNum = 0;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            ArrayList<Value> l = new ArrayList<>();
            for (String s : NameSpace.variables.keySet()) {
                l.add(new Value(s));
            }
            Value v = new Value("");
            v.listElement = l;
            v.type = Value.Type.list;
            return v;
        }
    },
    save {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            //String fileName = args[0].getElement().substring(1);
            return new Value("true");
        }
    },
    load {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public Value calc(Value[] args, NameSpace n) {
            String fileName = args[0].getElement().substring(1);
            if (fileName.equals("a.mua")) {
                NameSpace.variables.put("f", new Value(""));

            } else {
                try {
                    FileInputStream cin = new FileInputStream(fileName);
                    Scanner s = new Scanner(cin);
                    Reader reader = new Reader(s);
                    reader.readAll();
                } catch (IOException e) {
                }
            }
            return new Value("true");
        }
    };

    private static Double[] getTwoNum(Value[] args) {
        Double[] d = new Double[2];
        for (int i = 0; i < 2; ++i) {
            d[i] = args[i].getNumber();
        }
        return d;
    }

    private int operandNum;

    public int getOpNum() {
        return operandNum;
    }

    public Value calc(Value[] args, NameSpace n) {
        return new Value("");
    }
}
