package com.company;

/**
 * Created by tianqiliu on 2017-08-07.
 */

/**
 * a stack that supports push, pop, top, and retrieving the minimum element in constant time.
 * assumption: the user will not overly pop the stack
 */
public class MinStack {
    private int[] stack;
    private int min;
    private int len;
    /** initialize your data structure here. */
    public MinStack() {
        stack = new int[4];
        min = Integer.MAX_VALUE;
        len = 0;
    }

    public void push(int x) {
        if (len == stack.length) {
            int[] temp = new int[stack.length << 2]; //expand by factor of 4
            System.arraycopy(stack, 0, temp, 0, len);
            stack = temp;
        }
        // save the old min
        if (x <= min) {
            stack[len++] = min;
            min = x;
            if (len == stack.length) {
                int[] temp = new int[stack.length << 1];
                System.arraycopy(stack, 0, temp, 0, len);
                stack = temp;
            }
        }
        stack[len++] = x;
    }

    public void pop() {
        --len;
        //undo the saving
        if (stack[len] == min) {
            --len;
            min = stack[len];
        }
    }

    public int top() {
        return stack[len - 1];
    }

    public int getMin() {
        return min;
    }
}
