package fr.epita.services;

public class Exception extends java.lang.Exception {
    private Object obj;

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }
}
