package hello.advanced.trace.threadlocal;

import hello.advanced.trace.threadlocal.code.FieldService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class FieldServiceTest {

    private FieldService fieldService = new FieldService();

    /**
     * 동시성 문제 발생 X
     *      [Test worker] INFO hello.advanced.trace.threadlocal.FieldServiceTest - main start
     *      [thread-A] INFO hello.advanced.trace.threadlocal.code.FieldService - 저장 name=userA -> nameStore=null
     *      [thread-A] INFO hello.advanced.trace.threadlocal.code.FieldService - 조회 nameStore=userA
     *      [thread-B] INFO hello.advanced.trace.threadlocal.code.FieldService - 저장 name=userB -> nameStore=userA
     *      [thread-B] INFO hello.advanced.trace.threadlocal.code.FieldService - 조회 nameStore=userB
     *      [Test worker] INFO hello.advanced.trace.threadlocal.FieldServiceTest - main exit
     *
     * 동시성 문제 발생 O
     *      [Test worker] INFO hello.advanced.trace.threadlocal.FieldServiceTest - main start
     *      [thread-A] INFO hello.advanced.trace.threadlocal.code.FieldService - 저장 name=userA -> nameStore=null
     *      [thread-B] INFO hello.advanced.trace.threadlocal.code.FieldService - 저장 name=userB -> nameStore=userA
     *      [thread-A] INFO hello.advanced.trace.threadlocal.code.FieldService - 조회 nameStore=userB
     *      [thread-B] INFO hello.advanced.trace.threadlocal.code.FieldService - 조회 nameStore=userB
     *      [Test worker] INFO hello.advanced.trace.threadlocal.FieldServiceTest - main exit
     */
    @Test
    void doService() {
        log.info("main start");

        //스레드 2개 만들기
        Runnable userA = () -> {
            fieldService.logic("userA");
        };

        Runnable userB = () -> {
            fieldService.logic("userB");
        };

        Thread threadA = new Thread(userA);
        threadA.setName("thread-A");
        Thread threadB = new Thread(userB);
        threadB.setName("thread-B");

        threadA.start();
//        sleep(2000); // 동시성 문제 발생 X
        sleep(100);  // 동시성 문제 발생 O
        threadB.start();

        sleep(3000); //메인 쓰레드 종료 대기
        log.info("main exit");
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
