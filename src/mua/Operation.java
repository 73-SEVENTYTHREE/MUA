package mua;

public enum Operation {
    make {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // make args[1] args[0]
            // make v1 v2
            // 毫无疑问，v1一定会以引号开头
            String v1 = args[1].substring(1);
            String v2 = args[0];
            // 如果不存在v1的字面量的变量就新建一个
            if (!NameSpace.variables.containsKey(v1)) {
                NameSpace.variables.put(v1, new Value(""));
            }

            // 如果v2不以引号开头，那么他一定是某个变量的名字，直接赋值就行了（相当于指针）
            if (!v2.startsWith("\"")) {
                NameSpace.variables.get(v1).setElement(v2);
                return v2;
            } else {
                NameSpace.variables.get(v1).setElement(v2.substring(1));
                return v2.substring(1);
            }
        }
    },
    colon {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // :v
            return NameSpace.variables.get(args[0]).getElement();
        }
    },
    thing {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // thing v
            return NameSpace.variables.get(args[0]).getElement();
        }
    },
    print {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            String retString = args[0];
            if (args[0].startsWith("\""))
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

        public String calc(String[] args) {
            return args[0];
        }
    },
    add {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
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

        public String calc(String[] args) {
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

        public String calc(String[] args) {
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

        public String calc(String[] args) {
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

        public String calc(String[] args) {
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

        public String calc(String[] args) {
            // erase v
            String ret = NameSpace.variables.get(args[0]).getElement();
            NameSpace.variables.get(args[0]).setElement("");
            return ret;
        }
    },
    isname {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // isname v
            return String.valueOf(NameSpace.variables.containsKey(args[0]));
        }
    },
    eq {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // eq v1 v2
            String v1 = (new Value(args[0])).getWord();
            String v2 = (new Value(args[1])).getWord();

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

        public String calc(String[] args) {
            // eq v1 v2
            String v1 = (new Value(args[0])).getWord();
            String v2 = (new Value(args[1])).getWord();

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
    lt {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // eq v1 v2
            String v1 = (new Value(args[0])).getWord();
            String v2 = (new Value(args[1])).getWord();

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
    and {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // and v1 v2
            boolean b1 = Boolean.valueOf((new Value(args[0])).getWord());
            boolean b2 = Boolean.valueOf((new Value(args[1])).getWord());
            return String.valueOf(b1 && b2);
        }
    },
    or {
        public int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            // or v1 v2
            boolean b1 = Boolean.valueOf((new Value(args[0])).getWord());
            boolean b2 = Boolean.valueOf((new Value(args[1])).getWord());
            return String.valueOf(b1 || b2);
        }
    },
    run {

    };

    private static Double[] getTwoNum(String[] args) {
        Double[] d = new Double[2];
        for (int i = 0; i < 2; ++i) {
            if (args[i].startsWith("\"")) {
                // d[i] = NameSpace.variables.get(args[i].substring(1)).getNumber();
                d[i] = Double.parseDouble(args[i].substring(1));
            } else {
                d[i] = Double.parseDouble(args[i]);
            }
        }
        return d;
    }

    private int operandNum;

    public int getOpNum() {
        return operandNum;
    }

    public String calc(String[] args) {
        return "";
    }
}
