package net.weibeld.jsonrpc;

/**
 * Implementation of the Service interface with the remote methods provided by
 * the JSON-RPC server to the JSON-RPC client.
 */
public class ServiceImpl implements Service {

    @Override
    public int add(int a, int b) {
        return a + b;
    }

    @Override
    public int subtract(int a, int b) {
        return a - b;
    }

    @Override
    public int multiply(int a, int b) {
        return a * b;
    }

}
