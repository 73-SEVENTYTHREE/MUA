package mua;

public enum Operation {
    make {
        private int operandNum = 2;

        public String calc(String[] args) {
            // make v1 v2
            String v1 = args[0];
            
            Value v2 = new Value(args[1]);
            // 先获取v2的字面量，然后再调用一遍getWord，解析出字符串值或者是变量内部的值
            // 然后再用Value把它包起来
            Value newV2 = new Value(v2.getWord());
            if (!NameSpace.variables.containsKey(v1.substring(1))) {
                NameSpace.variables.put(v1.substring(1), null);
            }
            NameSpace.variables.get(v1.substring(1)).setElement(newV2.getWord());
            return newV2.getWord();
        }
    },
    colon {

        private int operandNum = 1;
    },
    thing {
        private int operandNum = 1;
    },
    print {
        private int operandNum = 1;
    },
    read {
        private int operandNum = 1;
    },
    add {
        private int operandNum = 2;
    },
    sub {
        private int operandNum = 2;
    },
    mul {
        private int operandNum = 2;
    },
    div {
        private int operandNum = 2;
    },
    mod {
        private int operandNum = 2;
    };

    private int operandNum;

    public int getOpNum() {
        return operandNum;
    }

    public String calc(String[] args) {
        return "";
    }
}
