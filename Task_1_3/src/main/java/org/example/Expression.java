package org.example;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Stack;

public abstract class Expression {
    public void print() {
        System.out.println(this.toString());
    }

    @Override
    public abstract String toString();

    public abstract Expression derivative(String variable);

    protected abstract int eval(Map<String, Integer> variables);

    public int eval(String assignments) {
        Map<String, Integer> vars = new HashMap<>();
        if (assignments != null && !assignments.trim().isEmpty()) {
            String[] pairs = assignments.split(";");
            for (String pair : pairs) {
                String[] parts = pair.split("=");
                if (parts.length == 2) {
                    vars.put(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                }
            }
        }
        return this.eval(vars);
    }

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int hashCode();

    public abstract Expression simplify();

    public static Expression parseAdvanced(String str) {
        Stack<Expression> values = new Stack<>();
        Stack<Character> ops = new Stack<>();
        int i = 0;
        while (i < str.length()) {
            char c = str.charAt(i);
            if (c == ' ') {
                i++;
                continue;
            }
            if (Character.isDigit(c)) {
                int val = 0;
                while (i < str.length() && Character.isDigit(str.charAt(i))) {
                    val = val * 10 + (str.charAt(i) - '0');
                    i++;
                }
                values.push(new Number(val));
            } else if (Character.isLetter(c)) {
                StringBuilder sb = new StringBuilder();
                while (i < str.length() && Character.isLetter(str.charAt(i))) {
                    sb.append(str.charAt(i));
                    i++;
                }
                values.push(new Variable(sb.toString()));
            } else if (c == '(') {
                ops.push(c);
                i++;
            } else if (c == ')') {
                while (!ops.isEmpty() && ops.peek() != '(') {
                    applyTopOperator(values, ops.pop());
                }
                ops.pop();
                i++;
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                while (!ops.isEmpty() && getPriority(ops.peek()) >= getPriority(c)) {
                    applyTopOperator(values, ops.pop());
                }
                ops.push(c);
                i++;
            } else {
                i++;
            }
        }
        while (!ops.isEmpty()) {
            applyTopOperator(values, ops.pop());
        }
        return values.pop();
    }

    private static int getPriority(char op) {
        if (op == '(') return 0;
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return -1;
    }
    private static void applyTopOperator(Stack<Expression> values, char op) {
        Expression right = values.pop();
        Expression left = values.pop();
        switch (op) {
            case '+': values.push(new Add(left, right)); break;
            case '-': values.push(new Sub(left, right)); break;
            case '*': values.push(new Mul(left, right)); break;
            case '/': values.push(new Div(left, right)); break;
        }
    }
}
