package mua;

public enum Operation {
    make {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            //make args[1] args[0]
            //make v1 v2
            //毫无疑问，v1一定会以引号开头
            String v1 = args[1].substring(1);
            String v2 = args[0];
            //如果不存在v1的字面量的变量就新建一个
            if (!NameSpace.variables.containsKey(v1)) {
                NameSpace.variables.put(v1, new Value(""));
            }

            //如果v2不以引号开头，那么他一定是某个变量的名字，直接赋值就行了（相当于指针）
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
            //:v
            return NameSpace.variables.get(args[0]).getElement();
        }
    },
    thing {
        private int operandNum = 1;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            //thing v
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

        public String calc(String[] args){
            return args[0];
        }
    },
    add {
        private int operandNum = 2;

        public int getOpNum() {
            return operandNum;
        }

        public String calc(String[] args) {
            //add n1 n2
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
            //add n1 n2
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
            //add n1 n2
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
            //add n1 n2
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
            //add n1 n2
            Double[] d = getTwoNum(args);
            return String.valueOf(d[1].intValue() % d[0].intValue());
        }
    };

    private static Double[] getTwoNum(String[] args) {
        Double[] d = new Double[2];
        for (int i = 0; i < 2; ++i) {
            if (args[i].startsWith("\"")) {
                //d[i] = NameSpace.variables.get(args[i].substring(1)).getNumber();
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
