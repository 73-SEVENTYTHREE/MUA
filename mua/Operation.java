package mua;

public enum Operation {
    make {
        private int operandNum = 2;
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
