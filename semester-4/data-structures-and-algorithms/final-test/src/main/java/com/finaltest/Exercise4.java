package com.finaltest;

// Julian Sellanes (301494667)

public class Exercise4 {
    public interface Stack<E> {
        int size();
        boolean isEmpty();
        void push(E e);
        E top();
        E pop();
    }

    public static class LinkedStack<E> implements Stack<E> {

        private static class Node<E> {
            E element;
            Node<E> next;
            Node(E e, Node<E> n) { element = e; next = n; }
        }

        private Node<E> head = null;
        private int size = 0;

        public int size()        { return size; }
        public boolean isEmpty() { return size == 0; }

        public void push(E e) {
            head = new Node<>(e, head);
            size++;
        }

        public E top() {
            return isEmpty() ? null : head.element;
        }

        public E pop() {
            if (isEmpty()) return null;
            E answer = head.element;
            head = head.next;
            size--;
            return answer;
        }
    }

    public static boolean isBalanced(String expr) {
        if (expr == null) return true;
        Stack<Character> stack = new LinkedStack<>();
        for (int i = 0; i < expr.length(); i++) {
            char c = expr.charAt(i);
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) return false;
                char open = stack.pop();
                if (!matches(open, c)) return false;
            }
        }
        return stack.isEmpty();
    }

    private static boolean matches(char open, char close) {
        return (open == '(' && close == ')')
            || (open == '[' && close == ']')
            || (open == '{' && close == '}');
    }

    public static void main(String[] args) {
        System.out.println("Exercise 4:");
        System.out.println();

        String[] expressions = {
            "((a + b) * [c - {d / e}])", 
            "(a + b]",    
            "((a + b)",             
            "a + b) * (c",            
            "{[()]}{}[]",                  
            "",                          
            "no delimiters at all"
        };

        for (String expr : expressions) {
            System.out.printf("isBalanced(\"%s\") = %s%n", expr, isBalanced(expr));
        }
    }
}
