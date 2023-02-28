package hello.advanced.app.v0;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceV0 {

    /**
     * 스프링에서 생성자가 1개만 있으면 자동으로 해당 생성자 위에 Autowired가 붙게 됩니다
     */

    private final OrderRepositoryV0 orderRepository;

    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }
}

