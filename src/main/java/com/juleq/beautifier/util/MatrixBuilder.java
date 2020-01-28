package com.juleq.beautifier.util;

import com.juleq.beautifier.Expression;

import java.util.stream.IntStream;

import static java.lang.Math.abs;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.repeat;

public class MatrixBuilder {

    private Expression left;
    private Expression right;
    private int pivotIndex;

    private MatrixBuilder() {}

    public static MatrixBuilder newInstance() {
        return new MatrixBuilder();
    }

    public MatrixBuilder withPivotIndex(int pivotIndex) {
        this.pivotIndex = pivotIndex;
        return this;
    }

    public MatrixBuilder withLeft(Expression left) {
        this.left = left;
        return this;
    }

    public MatrixBuilder withRight(Expression right) {
        this.right = right;
        return this;
    }

    public MatrixBuilder rightShift() {
        right = shift(right, left);
        return this;
    }

    public MatrixBuilder leftShift() {
        left = shift(left, right);
        return this;
    }

    public Expression buildSimpleMatrix() {
        Expression result = new Expression(pivotIndex);
        left.getMatrix().forEach((k, v) -> result.addToMatrix(k, v.concat(right.getMatrix().get(k))));
        return result;
    }

    /**
     * Balances two expressions with the same length according to their pivots. Some spaces will be filled to balanced
     * part to the top or to the bottom.
     *
     * @return the balanced expression
     */
    public Expression buildBalancedMatrix() {
        int delta = left.getPivotIndex() - right.getPivotIndex();

        if (delta > 0) {
            balanceFromBottom(delta, right);
            left = balanceFromTop(delta, left);
        }
        if (delta < 0) {
            left = balanceFromTop(abs(delta), left);
            balanceFromBottom(abs(delta), right);
        }
        return buildSimpleMatrix();
    }

    /**
     * Creates fraction expression as an matrix.
     *
     * @param operator the divider operator
     * @return the expression representing fraction as an matrix
     */
    public Expression buildFractionMatrix(Expression operator) {
        int fractionWidth = operator.getWidth();
        int fractionHeight = left.getHeight() + right.getHeight() + 1;
        Expression result = new Expression(left.getHeight());
        result.setFraction(true);
        alignFractionPart(0, left.getHeight(), fractionWidth, result, left, right);
        result.addToMatrix(left.getHeight(), operator.getExpresion(0));
        alignFractionPart(left.getHeight() + 1, fractionHeight, fractionWidth, result, right, left);
        return result;
    }

    private void balanceFromBottom(int delta, Expression operand) {
        IntStream.range(operand.getHeight(), operand.getHeight() + delta)
                .forEach(i -> operand.addToMatrix(operand.getHeight(), repeat(SPACE, operand.getWidth())));
    }

    private Expression balanceFromTop(int delta, Expression operand) {
        Expression result = new Expression(operand.getPivotIndex() + delta);
        IntStream.range(0, delta).forEach(i -> result.addToMatrix(i, repeat(SPACE, operand.getWidth())));
        IntStream.range(result.getHeight(), operand.getHeight() + delta).
                forEach(i -> result.addToMatrix(i, operand.getExpresion(i - delta)));
        return result;
    }

    /**
     * Aligns numerator and denominator according to their width.
     */
    private void alignFractionPart(int from, int to, int fractionWidth, Expression result, Expression left, Expression right) {
        int exprIndex = 0;
        for (int i = from; i < to; i++, exprIndex++) {
            if (left.getWidth() > right.getWidth()) {
                // add just one space before and after for longer expression
                result.addToMatrix(i, SPACE + left.getExpresion(exprIndex) + SPACE);
            } else {
                // count spaces for shorter expression, more spaces are after expression for odd width
                int leftAlign = (fractionWidth - left.getWidth()) / 2 ;
                int rightAlign = (fractionWidth - left.getWidth()) / 2 + (fractionWidth - left.getWidth()) % 2;
                result.addToMatrix(i, repeat(SPACE, leftAlign) + left.getExpresion(exprIndex) + repeat(SPACE, rightAlign));
            }
        }
    }

    /**
     * Firstly, it is need to expand right or left operand to the matrix with the same length. Then shift this matrix according to
     * pivot difference, so the both expression will be on the same pivot line with the same length and will be prepared
     * for simple concatenation.
     *
     * @param shifted the expression to expand and shift
     * @param stable the expression without any change
     * @return the expanded and shifted expression
     */
    private Expression shift(Expression shifted, Expression stable) {
        Expression expanded = new Expression();
        int delta = abs(shifted.getPivotIndex() - stable.getPivotIndex());

        // firstly just copy shifted expression to matrix with the same height as master expression
        IntStream.iterate(0, i -> i < stable.getHeight(), i -> i + 1)
                .forEach(i -> expanded.addToMatrix(i, shifted.getMatrix().getOrDefault(i, repeat(SPACE, shifted.getWidth()))));

        // then shift each row according to the difference of pivot indexes
        Expression result = new Expression(stable.getPivotIndex(), expanded.getHeight(), expanded.getWidth());
        IntStream.iterate(0, i -> i < expanded.getHeight(), i -> i + 1)
                .forEach(i -> result.shiftToMatrix((i + delta) % expanded.getHeight(), expanded.getMatrix().get(i)));

        return result;
    }
}