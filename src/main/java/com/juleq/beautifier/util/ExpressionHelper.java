package com.juleq.beautifier.util;

import com.juleq.beautifier.Expression;

public class ExpressionHelper {

    /**
     * Concatenates two expressions (numerator and denominator) as an fraction matrix.
     *
     * @param left the numerator expression
     * @param operator the divider operator '/'
     * @param right the denominator expression
     * @return the expression as an matrix including divisor line and all spaces
     */
    public static Expression concatVertical(Expression left, Expression operator, Expression right) {
        return MatrixBuilder.newInstance()
                .withPivotIndex(left.getHeight())
                .withLeft(left)
                .withRight(right)
                .buildFractionMatrix(operator);
    }

    /**
     * Concatenates two expressions (left and right) as an single matrix. It is performed for '+','-' or '*' operators.
     * For the same pivot it is just a simple concatenation, for different pivots one from the expressions needs to be
     * shifted.
     *
     * @param left the left expression
     * @param right the right expression
     * @return the expression as an matrix including all spaces
     */
    public static Expression concatHorizontal(Expression left, Expression right) {
        if (left.getPivotIndex() == right.getPivotIndex()) {
            return concatSimple(left, right, left.getPivotIndex());
        } else {
            return concatWithShift(left, right);
        }
    }

    /**
     * Concatenates two expressions (left and right) and operator as an single matrix.
     *
     * @param left the left expression
     * @param operator the operator ('+' or '-' or '*')
     * @param right the right expression
     * @return the expression as an matrix including operator and all spaces
     */
    public static Expression concatHorizontal(Expression left, Expression operator, Expression right) {
        Expression result = concatHorizontal(left, operator);
        return concatHorizontal(result, right);
    }

    private static Expression concatSimple(Expression left, Expression right, int pivotIndex) {
        if (left.getHeight() == right.getHeight()) {
            return MatrixBuilder.newInstance()
                    .withPivotIndex(pivotIndex)
                    .withLeft(left)
                    .withRight(right)
                    .buildSimpleMatrix();
        } else {
            throw new IllegalStateException("Expressions with the same pivot index");
        }
    }

    /**
     * Concatenates two matrices. For different lengths one matrix needs to be firstly balanced and shifted. For the
     * same long expressions with different pivots the both matrices needs to be balanced.
     *
     * @param left the left expression
     * @param right the right expression
     * @return the concatenated expression
     */
    private static Expression concatWithShift(Expression left, Expression right) {
        if (left.getHeight() > right.getHeight()) {
            return MatrixBuilder.newInstance()
                    .withLeft(left)
                    .withRight(right)
                    .rightShift()
                    .withPivotIndex(left.getPivotIndex())
                    .buildSimpleMatrix();
        } else if (left.getHeight() < right.getHeight()) {
            return MatrixBuilder.newInstance()
                    .withLeft(left)
                    .withRight(right)
                    .leftShift()
                    .withPivotIndex(right.getPivotIndex())
                    .buildSimpleMatrix();
        } else {
            return MatrixBuilder.newInstance()
                    .withLeft(left)
                    .withRight(right)
                    .buildBalancedMatrix();
        }
    }
}