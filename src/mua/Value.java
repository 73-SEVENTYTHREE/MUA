package mua;

import java.util.ArrayList;

public class Value {
    public static enum Type {
        number, word, list, bool, function;
    }

    private String element;
    public ArrayList<Value> listElement;
    public Type type;
    public Function func;

    Value(String e) {
        element = e;
        listElement = new ArrayList<>();
        type = Type.word;
    }

    public String getWord() {
        if (element.startsWith("\"")) {
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
        if (type != Type.list && type != Type.function)
            return element;
        else {
            return getListElement(true);
        }
    }

    private String getListElement(boolean isOut) {
        if (type != Type.list && type != Type.function)
            return element;
        else {
            String retStr = "[";
            if (listElement.isEmpty()) {
                return "";
            }

            for (Value v : listElement) {
                retStr = retStr + v.getListElement(false) + " ";
            }
            retStr = retStr.substring(0, retStr.length() - 1);
            retStr = retStr + "]";
            if (isOut) {
                retStr = retStr.substring(1);
                retStr = retStr.substring(0, retStr.length() - 1);
            }
            return retStr;
        }
    }

    public void setElement(String e) {
        element = e;
    }

    public void addListVal(Value v) {
        listElement.add(v);
    }

    public String[] toStringArray() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < listElement.size(); ++i) {
            list.add(listElement.get(i).getElement());
        }
        return list.toArray(new String[0]);
    }

    public String getRunnableElement() {
        if (type != Type.list && type != Type.function)
            return element;
        else {
            String retStr = "[";
            if (listElement.isEmpty()) {
                return "[]";
            }

            for (Value v : listElement) {
                retStr = retStr + v.getRunnableElement() + " ";
            }
            retStr = retStr.substring(0, retStr.length() - 1);
            retStr = retStr + "]";
            return retStr;
        }
    }
}
