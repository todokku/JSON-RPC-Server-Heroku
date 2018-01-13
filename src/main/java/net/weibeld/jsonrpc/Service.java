package net.weibeld.jsonrpc;

public interface Service {

    /**
     * Add two integers.
     */
    public int add(int a, int b);

    /**
     * Subtract the second integer from the first integer.
     */
    public int subtract(int a, int b);

    /**
     * Multiply two integers.
     */
    public int multiply(int a, int b);

}
