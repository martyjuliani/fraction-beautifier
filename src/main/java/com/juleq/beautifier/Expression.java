package com.juleq.beautifier;

import java.util.Map;
import java.util.TreeMap;

public class Expression {

    private Map<Integer, String> matrix = new TreeMap<>();
    private int height;
    private int width;
    private int pivotIndex;
    private boolean isFraction;

    public Expression() {}

    public Expression(String expression) {
       this(expression, 0);
    }

    public Expression(int pivotIndex) {
        this.pivotIndex = pivotIndex;
    }

    public Expression(String expression, int pivotIndex) {
        this.pivotIndex = pivotIndex;
        matrix.put(0, expression);
        width = expression.length();
        height++;
    }

    public Expression(int pivotIndex, int height, int width) {
        this.pivotIndex = pivotIndex;
        this.height = height;
        this.width = width;
    }

    public Map<Integer, String> getMatrix() {
        return matrix;
    }

    public String getExpresion(int key) {
        return matrix.get(key);
    }

    public void addToMatrix(Integer key, String value) {
        matrix.put(key, value);
        height++;
        width = value.length();
    }

    public void shiftToMatrix(Integer key, String value) {
        matrix.put(key, value);
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getPivotIndex() {
        return pivotIndex;
    }

    public boolean isFraction() {
        return isFraction;
    }

    public void setFraction(boolean fraction) {
        isFraction = fraction;
    }
}