package mua;

public interface OpInterface {
    public int getOpNum();

    public Value calc(Value[] args, NameSpace n);
}
