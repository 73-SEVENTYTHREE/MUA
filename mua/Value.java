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
        if (element.matches("^\".*")) {
            if (NameSpace.variables.containsKey(element.substring(1))) {
                return NameSpace.variables.get(element.substring(1)).getElement();
            } else
                return element.substring(1);
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

    public void setElement(String e) {
        element = e;
    }
}
