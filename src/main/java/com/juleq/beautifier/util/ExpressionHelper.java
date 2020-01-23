package com.juleq.beautifier.util;

import com.juleq.beautifier.Expression;

public class ExpressionHelper {

    public static Expression concatVertical(Expression left, Expression operator, Expression right) {
        return MatrixBuilder.newInstance()
                .withIndex(left.getHeight())
                .withLeft(left)
                .withRight(right)
                .buildFractionMatrix(operator);
    }

    public static Expression concatHorizontal(Expression left, Expression right) {
        if (left.getPivotIndex() == right.getPivotIndex()) {
            return concatSimple(left, right, left.getPivotIndex());
        } else {
            return concatWithShift(left, right);
        }
    }

    public static Expression concatHorizontal(Expression left, Expression operator, Expression right) {
        Expression result = concatHorizontal(left, operator);
        return concatHorizontal(result, right);
    }

    private static Expression concatSimple(Expression left, Expression right, int pivotIndex) {
        if (left.getHeight() == right.getHeight()) {
            return MatrixBuilder.newInstance()
                    .withIndex(pivotIndex)
                    .withLeft(left)
                    .withRight(right)
                    .buildSimpleMatrix();
        } else {
            throw new IllegalStateException("Expressions with the same pivot index");
        }
    }

    private static Expression concatWithShift(Expression left, Expression right) {
        if (left.getHeight() > right.getHeight()) {
            return MatrixBuilder.newInstance()
                    .withLeft(left)
                    .withRight(right)
                    .rightShift()
                    .withIndex(left.getPivotIndex())
                    .buildSimpleMatrix();
        } else if (left.getHeight() < right.getHeight()) {
            return MatrixBuilder.newInstance()
                    .withLeft(left)
                    .withRight(right)
                    .leftShift()
                    .withIndex(right.getPivotIndex())
                    .buildSimpleMatrix();
        } else {
            return MatrixBuilder.newInstance()
                    .withLeft(left)
                    .withRight(right)
                    .buildBalancedMatrix();
        }
    }
}