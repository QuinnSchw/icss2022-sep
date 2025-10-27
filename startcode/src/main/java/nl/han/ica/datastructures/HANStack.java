package nl.han.ica.datastructures;

import nl.han.ica.icss.ast.ASTNode;

import java.util.ArrayList;
import java.util.List;

public class HANStack <T> implements IHANStack<T>{

    List<T> HANLijst = new ArrayList<>();
    @Override
    public void push(T value) {
        HANLijst.add(value);
    }

    @Override
    public T pop() {
        T value = HANLijst.get(HANLijst.size() - 1);
        HANLijst.remove(HANLijst.size() - 1);
        return value;
    }

    @Override
    public T peek() {
        return HANLijst.get(HANLijst.size() - 1);
    }
}
