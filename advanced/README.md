## inf-spring-advanced

## 목차

1. [Spring-tip](#spring-tip)
2. [좋은 설계란?](#좋은-설계란)
3. [동시성 문제](#동시성-문제)
3. [ThreadLocal](#threadlocal)
    - [ThreadLocal 사용법](#threadlocal-사용법)
    - [ThreadLocal 주의사항](#threadlocal-주의사항)
4. [템플릿 메서드 패턴](#템플릿-메서드-패턴)
    - [핵심 기능](#핵심-기능)
    - [부가 기능](#부가-기능)
    - [템플릿 메서드 패턴 구조 그림](#템플릿-메서드-패턴-구조-그림)
    - [템플릿 메서드 패턴 - 정의](#템플릿-메서드-패턴---정의)
    - [템플릿 메서드 패턴 - 단점](#템플릿-메서드-패턴---단점)
5. [전략 패턴](#전략-패턴)
    - [전략 패턴 - 핵심](#전략-패턴---핵심)
    - [전략 패턴 - 템플릿 메서드 패턴과 차이점](#전략-패턴---템플릿-메서드-패턴과-차이점)
    - [전략 패턴 - 선 조립 후 실행방식](#전략-패턴---선-조립-후-실행방식)
    - [전략 패턴 - 파라미터 전달 방식](#전략-패턴---파라미터-전달-방식)
    - [전략 패턴 - 정리](#전략-패턴---정리)
6. [템플릿 콜백 패턴](#템플릿-콜백-패턴)
    - [콜백이란](#콜백이란)
    - [스프링의 템플릿 콜백 패턴](#스프링의-템플릿-콜백-패턴)
    - [여전히 남아있는 한계점](#여전히-남아있는-한계점)

## Spring-tip

- 스프링에서 생성자가 1개만 있으면 자동으로 해당 생성자 위에 `@Autowired`가 붙게 됩니다
- 제네릭에서 반환 타입이 필요한데, 반환할 내용이 없으면 `Void` 타입을 사용하고 `null`을 반환하면 된다.

## 좋은 설계란?

- 진정한 좋은 설계는 바로 `변경`이 일어날 때 자연스럽게 드러난다.

### 단일 책임 원칙(SRP)

- `V4`는 단순히 템플릿 메서드 패턴을 적용해서 소스코드 몇 줄을 줄인 것이 전부가 아니다.
- 로그를 남기는 부분에 단일 책임 원칙(SRP)을 지킨 것이다.
- 변경 지점을 하나로 모아서 변경에 쉽게 대처할 수 있는 구조를 만든 것이다.

## 동시성 문제

- 동시성 문제는 지역 변수에서는 발생하지 않는다.
- 지역 변수는 쓰레드마다 각각 다른 메모리 영역이 할당된다.
- 동시성 문제가 발생하는 곳은 `같은 인스턴스의 필드(주로 싱글톤에서` 자주 발생), 또는 `static 같은 공용 필드`에 접근할 때 발생한다.
- `동시성 문제는 값을 읽기만 하면 발생하지 않는다.` 어디선가 값을 변경하기 때문에 발생한다.

## ThreadLocal

- 쓰레드 로컬은 해당 쓰레드만 접근할 수 있는 특별한 저장소를 말한다.
- 쓰레드 로컬을 사용하면 각 쓰레드마다 별도의 내부 저장소를 제공한다.
  - 따라서 같은 인스턴스의 쓰레드 로컬 필드에 접근해도 문제 없다.
- 자바는 언어차원에서 쓰레드 로컬을 지원하기 위한 `java.lang.ThreadLocal` 클래스를 제공한다. 

<img width="700" alt="스크린샷 2023-03-02 오전 1 26 52" src="https://user-images.githubusercontent.com/75410527/222202215-28f9e1e1-97b4-4c06-885a-c2a9bc35d6a2.png">

<img width="700" alt="스크린샷 2023-03-02 오전 1 27 05" src="https://user-images.githubusercontent.com/75410527/222202243-02199786-ec23-45e6-babe-87fd725b25ea.png">

<img width="700" alt="스크린샷 2023-03-02 오전 1 27 16" src="https://user-images.githubusercontent.com/75410527/222202252-65883ff2-db52-44ab-be95-7179116053d3.png">

### ThreadLocal 사용법

- 값 저장: `ThreadLocal.set(xxx)` 
- 값 조회: `ThreadLocal.get()`
- 값 제거: `ThreadLocal.remove()`

### ThreadLocal 주의사항

- 쓰레드 로컬의 값을 사용 후 제거하지 않고 그냥 두면 WAS(톰캣)처럼 쓰레드 풀을 사용하는 경우에 심각한 문제가 발생할 수 있다.
- 이런 문제를 예방하려면 사용자의 요청이 끝날 때 쓰레드 로컬의 값을 `ThreadLocal.remove()`를 통해서 꼭 제거해야 한다.
  - 즉, 해당 쓰레드가 쓰레드 로컬을 모두 사용하고 나면 `ThreadLocal.remove()`를 호출해서 쓰레드 로컬에 저장된 값을 제거해주어야 한다.
  - 쓰레드 로컬을 사용할 때는 이 부분을 꼭 기억하자.

#### 사용자A 저장 요청

<img width="700" alt="스크린샷 2023-03-02 오전 1 19 53" src="https://user-images.githubusercontent.com/75410527/222202039-7e161a4a-1226-4038-825f-cb5afd5ca9a8.png">

#### 사용자A 저장 요청 종료

<img width="700" alt="스크린샷 2023-03-02 오전 1 20 28" src="https://user-images.githubusercontent.com/75410527/222202093-5f5b4269-a331-4d6d-a1e8-b48667c61cb2.png">

#### 사용자B 조회 요청

<img width="700" alt="스크린샷 2023-03-02 오전 1 20 41" src="https://user-images.githubusercontent.com/75410527/222202108-39e58fed-756b-4b95-bb4a-2adcae8abeea.png">


## 템플릿 메서드 패턴

### 핵심 기능

- 핵심 기능은 해당 객체가 제공하는 고유의 기능

### 부가 기능

- 핵심 기능을 보조하기 위해 제공되는 기능
  - 예를 들어서 `로그 추적 로직`, `트랜잭션 기능` 등
- 이러한 부가 기능은 단독으로 사용되지는 않고, 핵심 기능과 함께 사용된다.

```java
TraceStatus status = null;

try {
    status = trace.begin("message"); 
    //핵심 기능 호출
    trace.end(status);
} catch (Exception e) {
    trace.exception(status, e);
    throw e; 
}
```

### 템플릿 메서드 패턴 구조 그림

<img width="700" alt="스크린샷 2023-03-02 오후 7 49 12" src="https://user-images.githubusercontent.com/75410527/222415073-954c5c5a-e218-4bc8-9245-4baaa6891061.png">

- 위에 `추상 템플릿`이라는 게 있음
- 템플릿은 기준이 되는 거대한 틀로, 이 템플릿에 `변하지 않는 로직들을 다 몰아놓음`
- 그리고 일부 변하는 부분을 별도로 호출해서 해결함
  - 예) `call()`이라는 메서드(`변하는 부분`)를 하나 만들고, 이 변하는 부분을 `자식 클래스에서 만들어서 오버라이딩해서 구성`하는 것

```java
@Slf4j
public abstract class AbstractTemplate {

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        call(); //상속
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }

    protected abstract void call();
}
```



### 템플릿 메서드 패턴 - 정의

- 작업에서 알고리즘의 골격을 정의하고 일부 단계를 하위 클래스로 연기합니다.
- 템플릿 메서드를 사용하면 하위 클래스가 알고리즘의 구조를 변경하지 않고도 알고리즘의 특정 단계를 재정의 할 수 있습니다.
- 부모 클래스에 알고리즘의 골격인 템플릿을 정의하고, 일부 변경되는 로직은 자식 클래스에 정의하는 것이다.

### 템플릿 메서드 패턴 - 단점

#### 하지만

- `템플릿 메서드 패턴은 상속을 사용한다.` 따라서 상속에서 오는 단점들을 그대로 안고간다.
- 특히 자식 클래스가 부모 클래스와 컴파일 시점에 강하게 결합되는 문제가 있다. (의존관계 문제)
- 자식 클래스 입장에서는 부모 클래스의 기능을 전혀 사용하지 않는다.
  - 근데 부모 클래스의 기능을 다ㅏㅏㅏㅏㅏ 상속받아서 거대 클래스가 되어버림
  - 부모 클래스의 기능을 사용한다면 상속이 타당하지만 사용하지 않으므로 더 큰 문제
- 그럼에도 불구하고 템플릿 메서드 패턴을 위해 자식 클래스는 부모 클래스를 상속받고 있다. (`부모클래스에 의존`)
  - 코드 상에 명확히 `extends AbstractTemplate` 의존에 걸려버림 

#### 템플릿 메서드 패턴과 비슷한 역할을 하면서 상속의 단점을 제거할 수 있는 디자인 패턴이 바로 전략 패턴(Strategy Pattern)

## 전략 패턴

- 전략 패턴은 변하지 않는 부분을 `Context`라는 곳에 두고, 변하는 부분을 `Strategy`라는 인터페이스를 만들고 해당 인터페이스를 구현하도록 해서 문제를 해결한다.
- 상속이 아니라 `위임으로 문제를 해결`한다.
- 전략 패턴에서 `Context`는 `변하지 않는 템플릿 역할`을 하고, `Strategy`는 변하는 알고리즘 역할을 한다.

<img width="700" alt="스크린샷 2023-03-05 오후 6 19 39" src="https://user-images.githubusercontent.com/75410527/222952168-0f8b07f8-31a6-4db8-873d-bc7574646122.png">

### 전략 패턴 - 핵심

- 전략 패턴의 핵심은 `Context`는 `Strategy` 인터페이스에만 의존한다는 점이다.
- 덕분에 `Strategy`의 구현체를 변경하거나 새로 만들어도 `Context` 코드에는 영향을 주지 않는다.
- 이게 바로 `스프링 의존관계 주입에서 사용하는 방식`인 `전략 패턴`이다.

> Strategy.java

```java
public interface Strategy {
    void call();
}
```

> StrategyLogic1.java

```java
@Slf4j
public class StrategyLogic1 implements Strategy {
    @Override
    public void call() {
        log.info("비즈니스 로직1 실행");
    }
}
```

> StrategyLogic2.java

```java
@Slf4j
public class StrategyLogic2 implements Strategy {
    @Override
    public void call() {
        log.info("비즈니스 로직2 실행");
    }
}
```

> ContextV1.java

```java
/**
 * 필드에 전략을 보관하는 방식
 *
 * ContextV1은 변하지 않는 로직을 가지고 있는 템플릿 역할을 하는 코드
 * 전략 패턴에서는 이것을 컨텍스트(문맥)이라 한다.
 */
@Slf4j
public class ContextV1 {

    private Strategy strategy;

    public ContextV1(Strategy strategy) {
        this.strategy = strategy;
    }

    public void execute() {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call();    //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}
```

> ContextV2.java

```java
/**
 * 전략을 파라미터로 전달받는 방식
 *
 * ContextV2는 변하지 않는 로직을 가지고 있는 템플릿 역할을 하는 코드
 * 전략 패턴에서는 이것을 컨텍스트(문맥)이라 한다.
 */
@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call();    //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}
```

### 전략 패턴 - 템플릿 메서드 패턴과 차이점

- 템플릿 메서드 패턴과 가지는 큰 차이점은, Strategy의 구현체가 `상속이 아닌 위임에 영향을 받는다`는 점 
  - Strategy의 구현체가 템플릿 메서드 패턴에서는 상속을 받으니까 부모 클래스의 코드가 바뀌면 얘가 영향을 받았음
  - 그러나 전략 패턴에서는 Strategy의 구현체가 `Strategy`라는 굉장히 단순한 인터페이스에만 영향을 받음 (`인터페이스에만 의존`)
- 그래서 Context 코드가 바뀌든 말든 구현체는 영향을 전혀 받지 않음 

### 전략 패턴 - 선 조립 후 실행방식

- 여기서 이야기하고 싶은 부분은 `Context`의 내부 필드에 `Strategy`를 두고 사용하는 부분이다.
- 이 방식은 `Context`와 `Strategy`를 실행전에 원하는 모양으로 조립해두고, 그 다음에 `Context`를 실행하는 선 조립, 후 실행 방식에서 매우 유용하다.
- `Context`와 `Strategy`를 한 번 조립하고 나면 이후로는 `Context`를 실행하기만 하면 된다.
- 이 방식의 단점은 `Context`와 `Strategy`를 조립한 이후에는 전략을 변경하기가 번거롭다는 점이다.

<img width="700" alt="스크린샷 2023-03-05 오후 7 09 42" src="https://user-images.githubusercontent.com/75410527/222954201-807a551f-f676-437c-b25d-2ab241a66fe0.png">


### 전략 패턴 - 파라미터 전달 방식

- `Context`와 `Strategy`를 '선 조립 후 실행'하는 방식이 아니라 `Context`를 실행할 때마다 전략을 인수로 전달한다.
- 클라이언트는 `Context`를 실행하는 시점에 원하는 `Strategy`를 전달할 수 있다. 따라서 이전 방식과 비교해서 원하는 전략을 더욱 유연하게 변경할 수 있다.

```java
@Slf4j
public class ContextV2Test {
    /**
     * 전략 패턴 사용
     */
    @Test
    void strategyV1() {
        ContextV2 context = new ContextV2();
        context.execute(new StrategyLogic1());
        context.execute(new StrategyLogic2());
    }
}
```

<img width="700" alt="스크린샷 2023-03-05 오후 6 59 03" src="https://user-images.githubusercontent.com/75410527/222954138-97739a11-da03-4056-97c2-5b6b774689a7.png">


### 전략 패턴 - 정리

- 변하지 않는 부분을 `Context`에 두고 변하는 부분을 `Strategy`를 구현해서 만든다.

<img width="700" alt="스크린샷 2023-03-05 오후 6 59 49" src="https://user-images.githubusercontent.com/75410527/222954129-007180d9-cf6a-4342-878c-2508b95c860c.png">

## 템플릿 콜백 패턴

- `ContextV2`는 변하지 않는 템플릿 역할을 한다. 그리고 변하는 부분은 파라미터로 넘어온 `Strategy`의 코드를 실행해서 처리한다.
- 이렇게 `다른 코드의 인수로서 넘겨주는 실행 가능한 코드`를 콜백(`callback`)이라 한다.

```java
/**
 * 전략을 파라미터로 전달받는 방식
 *
 * ContextV2는 변하지 않는 로직을 가지고 있는 템플릿 역할을 하는 코드
 * 전략 패턴에서는 이것을 컨텍스트(문맥)이라 한다.
 */
@Slf4j
public class ContextV2 {

    public void execute(Strategy strategy) {
        long startTime = System.currentTimeMillis();
        //비즈니스 로직 실행
        strategy.call();    //위임
        //비즈니스 로직 종료
        long endTime = System.currentTimeMillis();
        long resultTime = endTime - startTime;
        log.info("resultTime = {}", resultTime);
    }
}
```

```java
public class ContextV2Test {
    /**
     * 익명 내부 클래스 사용방식 + 람다
     */
    @Test
    void strategyV3() {
        ContextV2 context = new ContextV2();
        //execute에 내가 실행할 코드 조각을 넘긴다
        context.execute(() -> log.info("비즈니스 로직1 실행"));
        context.execute(() -> log.info("비즈니스 로직2 실행"));
    }
}
```

### 콜백이란

- 콜백이란, 다른 코드의 인수로서 넘겨주는 실행 가능한 코드를 말한다.
- 콜백을 넘겨받는 코드(`ContextV2`)는 이 콜백을 필요에 따라서 즉시 실행할 수도 있고, 아니면 나중에 실행할 수도 있다.
- 쉽게 이야기해서 콜백은 코드가 호출(`call`)은 되는데 코드를 넘겨준 곳의 뒤(`back`)에서 실행된다는 뜻이다. 
  - ContextV2 예제에서 콜백은 `Strategy`이다.
  - 여기에서는 클라이언트에서 직접 `Strategy`를 실행하는 것이 아니라, 클라이언트가 `ContextV2.execute(...)`를 실행할 때 `Strategy`를 넘겨주고, `ContextV2` 뒤에서 `Strategy`가 실행된다.

### 스프링의 템플릿 콜백 패턴

- 스프링에서는 `ContextV2`와 같은 방식의 전략 패턴을 템플릿 콜백 패턴이라 한다.
  - 전략 패턴에서 `Context`가 템플릿 역할을 하고, `Strategy` 부분이 콜백으로 넘어온다 생각하면 된다.
  - 템플릿 콜백 패턴은 GOF 패턴은 아니고, 스프링 내부에서 이런 방식을 자주 사용하기 때문에, `스프링 안에서만 이렇게 부른다.`
  - 전략 패턴에서 템플릿과 콜백 부분이 강조된 패턴이라 생각하면 된다.
  - 스프링에서는 `JdbcTemplate`, `RestTemplate`, `TransactionTemplate`, `RedisTemplate`처럼 다양한 템플릿 콜백 패턴이 사용된다.
  - 스프링에서 이름에 `XxxTemplate`이 있다면 템플릿 콜백 패턴으로 만들어져 있다 생각하면 된다.

<img width="700" alt="스크린샷 2023-03-08 오후 8 24 29" src="https://user-images.githubusercontent.com/75410527/223708294-dc9f52a6-5d81-4a29-a8fb-b79b434f273b.png">

### 여전히 남아있는 한계점

- `지금까지 위에서 설명한 방식의 한계`는 아무리 최적화를 해도 `결국 로그 추적기를 적용하기 위해서 원본 코드를 수정해야 한다는 점`이다.
- 클래스가 수백개이면 수백개를 더 힘들게 수정하는가 조금 덜 힘들게 수정하는가의 차이가 있을 뿐, 본질적으로 코드를 다 수정해야 하는 것은 마찬가지이다.
- 개발자의 게으름에 대한 욕심은 끝이 없다. 수 많은 개발자가 이 문제에 대해서 집요하게 고민해왔고, 여러가지 방향으로 해결책을 만들어왔다.
- 지금부터 원본 코드를 손대지 않고 로그 추적기를 적용할 수 있는 방법에 대해 알아보자. 그러기 위해서 프록시 개념을 먼저 이해해야 한다.
