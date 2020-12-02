package mua;

/* import java.util.ArrayList;
import java.util.List;
import java.util.Arrays; */
import java.util.Map;

public enum Operation implements OpInterface {
    make {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
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
                return v2;
            } else {
                // 如果v2不以引号开头，那么他一定是某个变量的名字，直接赋值就行了（相当于指针）
                if (!v2.startsWith("\"")) {
                    v.get(v1).setElement(v2);
                    return v2;
                } else {
                    v.get(v1).setElement(v2.substring(1));
                    return v2.substring(1);
                }
            }
        }
    },
    colon {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // :v
            Map<String, Value> v = (n == null) ? NameSpace.variables : n.localVariables;
            if (v.containsKey(args[0].getElement()))
                return v.get(args[0].getElement()).getElement();
            else
                return NameSpace.variables.get(args[0].getElement()).getElement();
        }
    },
    thing {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // thing v
            Map<String, Value> v = (n == null) ? NameSpace.variables : n.localVariables;
            if (v.containsKey(args[0].getElement()))
                return v.get(args[0].getElement()).getElement();
            else
                return NameSpace.variables.get(args[0].getElement()).getElement();
        }
    },
    print {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
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
                return (String.valueOf(d));
            } catch (NumberFormatException e) {

            }
            System.out.println(retString);
            return retString;
        }
    },
    read {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            return args[0].getElement();
        }
    },
    add {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // add n1 n2
            Double[] d = getTwoNum(args);
            return String.valueOf(d[1] + d[0]);
        }
    },
    sub {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // sub n1 n2
            Double[] d = getTwoNum(args);
            return String.valueOf(d[1] - d[0]);
        }
    },
    mul {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // mul n1 n2
            Double[] d = getTwoNum(args);
            return String.valueOf(d[1] * d[0]);
        }
    },
    div {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // div n1 n2
            Double[] d = getTwoNum(args);
            return String.valueOf(d[1] / d[0]);
        }
    },
    mod {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // mod n1 n2
            Double[] d = getTwoNum(args);
            return String.valueOf(d[1].intValue() % d[0].intValue());
        }
    },
    erase {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // erase v
            String ret = NameSpace.variables.get(args[0].getElement().substring(1)).getElement();
            NameSpace.variables.remove(args[0].getElement().substring(1));
            return ret;
        }
    },
    isname {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // isname v
            return String.valueOf(NameSpace.variables.containsKey((args[0].getElement()).substring(1)));
        }
    },
    eq {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // eq v1 v2
            String v1 = args[0].getWord();
            String v2 = args[1].getWord();

            // 这里还是要考虑两个变量是否是数字的情况
            // 是为了避免出现15.0和15的情况
            try {
                double d1 = Double.parseDouble(v1);
                double d2 = Double.parseDouble(v2);
                return String.valueOf(d1 == d2);
            } catch (NumberFormatException e) {

            }
            return String.valueOf(v1.equals(v2));
        }
    },
    gt {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
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
                return String.valueOf(d1 < d2);
            } catch (NumberFormatException e) {

            }
            return String.valueOf(v1.compareTo(v2) < 0);
        }
    },
    lt {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // eq v1 v2
            String v1 = args[0].getWord();
            String v2 = args[1].getWord();

            // 这里还是要考虑两个变量是否是数字的情况
            // 是为了避免出现15.0和15的情况
            try {
                double d1 = Double.parseDouble(v1);
                double d2 = Double.parseDouble(v2);
                return String.valueOf(d1 > d2);
            } catch (NumberFormatException e) {

            }
            return String.valueOf(v1.compareTo(v2) > 0);
        }
    },
    and {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // and v1 v2
            boolean b1 = Boolean.valueOf(args[0].getWord());
            boolean b2 = Boolean.valueOf(args[1].getWord());
            return String.valueOf(b1 && b2);
        }
    },
    or {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // or v1 v2
            boolean b1 = Boolean.valueOf(args[0].getWord());
            boolean b2 = Boolean.valueOf(args[1].getWord());
            return String.valueOf(b1 || b2);
        }
    },
    not {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // not v
            boolean b = Boolean.valueOf(args[0].getWord());
            return String.valueOf(!b);
        }
    },
    isnumber {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // isnumber v
            String v = args[0].getElement();
            if (v.startsWith("\""))
                v = v.substring(1);
            try {
                Double.parseDouble(v);
                return String.valueOf(true);
            } catch (NumberFormatException e) {
                return String.valueOf(false);
            }
        }
    },
    isbool {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // isbool v
            String v = args[0].getElement();
            return String.valueOf(v.equals("true") || v.equals("false"));
        }
    },
    isword {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // isword v
            String v = args[0].getElement();
            try {
                Double.parseDouble(v);
                return String.valueOf(false);
            } catch (NumberFormatException e) {
                if (!v.equals("true") && !v.equals("false") && !v.contains(" "))
                    return String.valueOf(true);
                else
                    return String.valueOf(false);
            }
        }
    },
    islist {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // islist v
            if (args[0].getElement().contains(" "))
                return String.valueOf(true);
            else
                return String.valueOf(false);
        }
    },
    isempty {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // isempty v
            if (args[0].getElement().equals(""))
                return String.valueOf(true);
            else
                return String.valueOf(false);

        }
    },
    IF {
        public int operandNum = 3;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            String[] s = new String[1];
            if (args[2].getElement().equals("true")) {
                s[0] = args[1].getElement();
            } else {
                s[0] = args[0].getElement();
            }
            // return Operation.run.calc(s, n);
            return "";
        }
    },
    run {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            // run [print add :a :b]
            // 执行的时候，接受到的参数是"print add :a :b"
            // 然后把它按空格分成数组，然后再把整个数组传递给read
            // 如果有一段代码想要调用run来运行
            // 需要给把待运行的代码按空格分词
            String[] srcExp = args[0].getElement().split("\\s+");

            /*
             * ArrayList<String> tempArrayList = new
             * ArrayList<String>(Arrays.asList(srcExp)); List<String> tempList =
             * tempArrayList.subList(1, tempArrayList.size()); srcExp = tempList.toArray(new
             * String[0]);
             */

            Value v = new Reader(null).read(NameSpace.ReadMode.runList, srcExp, n);
            return v.getElement();
        }
    },
    RETURN {
        public int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(Value[] args, NameSpace n) {
            return args[0].getElement();
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

    public String calc(Value[] args, NameSpace n) {
        return "";
    }
}
