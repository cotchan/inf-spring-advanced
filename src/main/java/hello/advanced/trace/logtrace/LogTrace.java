package hello.advanced.trace.logtrace;

import hello.advanced.trace.TraceStatus;

/**
 * 로그 추적기를 위한 최소한의 기능인 begin(), end(), exception()만 구현
 */
public interface LogTrace {

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);
}
