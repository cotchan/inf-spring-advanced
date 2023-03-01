## inf-spring-advanced

## 목차

1. [Spring-tip](#spring-tip)
2. [동시성 문제](#동시성-문제)
3. [ThreadLocal](#threadlocal)
    - [ThreadLocal 사용법](#threadlocal-사용법)
    - [ThreadLocal 주의사항](#threadlocal-주의사항)

## Spring-tip

- 스프링에서 생성자가 1개만 있으면 자동으로 해당 생성자 위에 `@Autowired`가 붙게 됩니다

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

