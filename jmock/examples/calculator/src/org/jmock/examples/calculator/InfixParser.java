/*  Copyright (c) 2000-2004 jMock.org
 */
package org.jmock.examples.calculator;

import java.io.IOException;


/**
 * A recursive descent parser for the following grammar:
 * <pre>
 *  expr = add_expr
 *  add_expr = mul_expr ("+" add_expr | "-" add_expr)*
 *  mul_expr = mul_expr ("*" pow_expr | "/" pow_expr)*
 *  pow_expr = val_expr ("^" pow_expr)*
 *  val_expr = number | varref | "(" expr ")"
 *  number ~= [0-9]+\.[0-9]+
 *  varref = identifier
 *  expr_list = expr ("," expr)*
 * </pre>
 */
public class InfixParser implements Parser
{
    private ExpressionFactory factory;

    public InfixParser() {
        this(new SimpleExpressionFactory());
    }

    public InfixParser( ExpressionFactory factory ) {
        this.factory = factory;
    }

    public Expression parse( String expressionString ) throws ParseException {
        Lexer lexer = new Lexer(expressionString);
        try {
            return parseExpression(lexer);
        }
        catch (IOException ex) {
            throw new ParseException("I/O error when parsing expression", ex);
        }
    }

    private Expression parseExpression( Lexer lexer )
            throws ParseException, IOException {
        return parseAddExpr(lexer);
    }

    private Expression parseAddExpr( Lexer lexer )
            throws ParseException, IOException {
        Expression left = parseMulExpr(lexer);

        for (; ;) {
            Token token = lexer.nextToken();

            switch (token.getType()) {
            case Token.ADD:
                left = factory.newAddition(left, parseAddExpr(lexer));
                break;
            case Token.SUBTRACT:
                left = factory.newSubtraction(left, parseAddExpr(lexer));
                break;
            default:
                lexer.pushBack(token);
                return left;
            }
        }
    }

    private Expression parseMulExpr( Lexer lexer )
            throws ParseException, IOException {
        Expression left = parsePowExpr(lexer);

        for (; ;) {
            Token token = lexer.nextToken();

            switch (token.getType()) {
            case Token.MULTIPLY:
                left = factory.newMultiplication(left, parseMulExpr(lexer));
                break;
            case Token.DIVIDE:
                left = factory.newDivision(left, parseMulExpr(lexer));
                break;
            default:
                lexer.pushBack(token);
                return left;
            }
        }
    }

    private Expression parsePowExpr( Lexer lexer )
            throws ParseException, IOException {
        Expression left = parseValExpr(lexer);

        for (; ;) {
            Token token = lexer.nextToken();

            switch (token.getType()) {
            case Token.POWER:
                left = factory.newPower(left, parsePowExpr(lexer));
                break;
            default:
                lexer.pushBack(token);
                return left;
            }
        }
    }

    private Expression parseValExpr( Lexer lexer )
            throws ParseException, IOException {
        Token token = lexer.nextToken();

        switch (token.getType()) {
        case Token.NUMBER:
            return factory.newLiteral(parseValue(token.getValue()));

        case Token.IDENTIFIER:
            return factory.newVariableReference(token.getValue());

        case Token.PAREN_OPEN:
            {
                Expression result = parseExpression(lexer);

                if (lexer.nextToken().getType() != Token.PAREN_CLOSE) {
                    throw new ParseException("closing parenthesis missing");
                }

                return result;
            }

        case Token.END:
            throw new ParseException("unexpected end of input");

        default:
            throw new ParseException("unexpected token \"" + token.getValue() + "\"");
        }
    }

    public double parseValue( String valueString )
            throws ParseException {
        try {
            return Double.parseDouble(valueString);
        }
        catch (NumberFormatException ex) {
            throw new ParseException("invalid number literal \"" + valueString + "\"");
        }
    }
}

