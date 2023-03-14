package hello.advanced.trace;

/**
 * 로그의 상태정보를 나타내는 Class
 */
public class TraceStatus {

    private TraceId traceId;
    private Long startTimeMs;   // 로그 시작시간
    /**
     * 로그를 시작하면 끝이 있어야 함
     * 시작시 사용한 메시지, 이후 로그 종료시에도 이 메시지를 사용해서 출력한다.
     */
    private String message;

    public TraceStatus(TraceId traceId, Long startTimeMs, String message) {
        this.traceId = traceId;
        this.startTimeMs = startTimeMs;
        this.message = message;
    }

    public TraceId getTraceId() {
        return traceId;
    }

    public Long getStartTimeMs() {
        return startTimeMs;
    }

    public String getMessage() {
        return message;
    }
}
