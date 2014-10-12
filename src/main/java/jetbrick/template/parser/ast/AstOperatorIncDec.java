/**
 * Copyright 2013-2014 Guoqiang Chen, Shanghai, China. All rights reserved.
 *
 *   Author: Guoqiang Chen
 *    Email: subchen@gmail.com
 *   WebURL: https://github.com/subchen
 *
 * Licensed under the Apache License, Version 2.0 (the License);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an AS IS BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template.parser.ast;

import jetbrick.template.Errors;
import jetbrick.template.runtime.InterpretContext;
import jetbrick.template.runtime.InterpretException;

/**
 *  <h2>Unary Operator<h2>
 *  <p>
 *  {@link http://docs.oracle.com/javase/specs/jls/se7/html/jls-5.html#jls-5.6.1}<br/>
 *  <ul>
 *    <li>Each dimension expression in an array creation expression</li>
 *    <li>The index expression in an array access expression </li>
 *    <li>The operand of a unary plus operator + </li>
 *    <li>The operand of a unary minus operator - </li>
 *    <li>The operand of a bitwise complement operator ~ </li>
 *    <li>Each operand, separately, of a shift operator &gt;&gt;, &gt;&gt;&gt;, or &lt;&lt; </li>
 *  </ul>
 *  </p>
 */
public final class AstOperatorIncDec extends AstExpression {
    private final int operator;
    private final AstExpression expression;

    public AstOperatorIncDec(int operator, AstExpression expression, Position position) {
        super(position);
        this.operator = operator;
        this.expression = expression;
    }

    @Override
    public Object execute(InterpretContext ctx) throws InterpretException {
        if (!(expression instanceof ResetableValueExpression)) {
            throw new InterpretException(Errors.OP_UNARY_UNDEFINED, Tokens.NAMES[operator], "expression").set(position);
        }

        Object o = expression.execute(ctx);
        if (o == null) {
            throw new InterpretException(Errors.OBJECT_IS_NULL).set(expression.getPosition());
        }

        Object value;

        try {
            switch (operator) {
            case Tokens.INC_BEFORE:
                o = value = ALU.inc(o);
                break;
            case Tokens.DEC_BEFORE:
                o = value = ALU.dec(o);
                break;
            case Tokens.INC_AFTER:
                value = ALU.inc(o);
                break;
            case Tokens.DEC_AFTER:
                value = ALU.dec(o);
                break;
            default:
                throw new UnsupportedOperationException();
            }

        } catch (ArithmeticException e) {
            throw new InterpretException(e).set(position);
        }

        ((ResetableValueExpression) expression).setValue(ctx.getValueStack(), value);

        return o;
    }
}
