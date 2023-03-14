## proxy

> 다양한 상황에서 프록시 사용법을 이해하기 위해 다음과 같은 기준으로 기본 예제 프로젝트를 만들어보자. 

예제는 크게 3가지 상황으로 만든다.

- v1 - 인터페이스와 구현 클래스 - 스프링 빈으로 수동 등록 
- v2 - 인터페이스 없는 구체 클래스 - 스프링 빈으로 수동 등록 
- v3 - 컴포넌트 스캔으로 스프링 빈 자동 등록


```java
@Import(AppV1Config.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app") //주의
public class ProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}

}
```

- 클래스를 스프링 빈으로 등록한다.
- 여기서는 `AppV1Config.class`를 스프링 빈으로 등록한다.
- 일반적으로 `@Configuration` 같은 설정 파일을 등록할 때 사용하지만, 스프링 빈을 등록할 때도 사용할 수 있다.