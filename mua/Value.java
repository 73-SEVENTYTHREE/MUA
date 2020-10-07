package mua;

public class Value {
    private static enum Type {
        number, word, list, bool;
    }

    private String element;
    private Type type;

    Value(String e) {
        element = e;
        type = Type.word;
    }

    public String getWord() {
        if (element.matches("^\"")) {
            return NameSpace.variables.get(element.substring(1)).getElement();
        } else {
            return element;
        }
    }

    public double getNumber() {
        return Double.parseDouble(getWord());
    }

    public boolean getBool() {
        return getWord().equals("true");
    }

    public String getElement() {
        return element;
    }
}