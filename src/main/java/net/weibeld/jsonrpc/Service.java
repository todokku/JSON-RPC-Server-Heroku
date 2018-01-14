package net.weibeld.jsonrpc;

/**
 * Service interface declaring the remote methods provided by the JSON-RPC
 * server to the JSON-RPC client.
 */
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
