package com.juleq.beautifier;

import static com.juleq.beautifier.util.ExpressionHelper.concatHorizontal;
import static com.juleq.beautifier.util.ExpressionHelper.concatVertical;
import static java.lang.Math.max;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.repeat;

public class BeautifierVisitorImpl extends BeautifierBaseVisitor<Expression> {

    @Override
    public Expression visitSignExpr(BeautifierParser.SignExprContext ctx) {
        if (ctx.PLUS() != null) {
            return visit(ctx.expr());
        }
        return concatHorizontal(new Expression("-"), visit(ctx.expr()));
    }

    @Override
    public Expression visitParenthesisExpr(BeautifierParser.ParenthesisExprContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public Expression visitMultDivExpr(BeautifierParser.MultDivExprContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        String operator = ctx.operator.getText();

        switch (operator) {
            case "/":
                int fractionLength = max(left.getWidth(), right.getWidth()) + 2;
                String fraction = repeat('-', fractionLength);
                String divide = identOperator(right, fraction);
                return concatVertical(left, new Expression(divide), right);
            case "*":
                String multiply = identOperator(right, operator);
                return concatHorizontal(left, new Expression(multiply), right);
            default:
                throw new UnsupportedOperationException("Beautifier does not support operator " + operator);
        }
    }

    @Override
    public Expression visitPlusMinusExpr(BeautifierParser.PlusMinusExprContext ctx) {
        Expression left = visit(ctx.expr(0));
        Expression right = visit(ctx.expr(1));
        Expression operator = new Expression(identOperator(right, ctx.operator.getText()));
        return concatHorizontal(left, operator, right);
    }

    @Override
    public Expression visitNumericExpr(BeautifierParser.NumericExprContext ctx) {
        return new Expression(ctx.INT().getText());
    }

    private static String identOperator(Expression right, String operator) {
        return right.isFraction() ? SPACE + operator + SPACE : operator;
    }
}