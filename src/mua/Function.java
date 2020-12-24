package mua;

public class Function implements OpInterface {
    private int oprandNum;

    public Value runList;

    public Value paraList;

    public NameSpace local;

    Function(int n, Value v1, Value v2) {
        local = new NameSpace();
        oprandNum = n;
        paraList = v1;
        for (Value keyV : paraList.listElement)
            local.localVariables.put(keyV.getElement(), new Value(""));
        runList = v2;
    }

    public int getOpNum() {
        return oprandNum;
    }

    public Value calc(Value[] args, NameSpace n) {
        String s = runList.getRunnableElement();
        s = s.substring(1);
        s = s.substring(0, s.length() - 1);
        Value[] a = new Value[1];
        a[0] = new Value(s);

        for (int i = 0; i < args.length; ++i) {
            local.localVariables.put(paraList.listElement.get(i).getElement(), args[args.length - i - 1]);
        }
        return Operation.run.calc(a, local);
    }

}
