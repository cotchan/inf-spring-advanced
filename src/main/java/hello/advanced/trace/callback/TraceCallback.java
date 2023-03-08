package hello.advanced.trace.callback;

/**
 * 콜백을 전달하는 interface
 */
public interface TraceCallback<T> {
    T call();
}
