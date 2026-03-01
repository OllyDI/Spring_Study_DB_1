package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

/**
 * 기본적으로 언체크 예외 사용
 * 체크 예외는 비즈니스 로직상 의도적으로 던지는 예외에만 사용 -> 잘 안씀
 *  - 예외를 반드시 잡아서 처리해야 하는 경우 체크 예외 사용
 */


@Slf4j
public class UnCheckedTest {

    @Test
    public void unchecked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    public void unchecked_throw() {
        Service service = new Service();
        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyUnCheckedException.class);
    }

    /**
     * RuntimeException 을 상속받은 예외는 언체크 예외가 됨
     */
    static class MyUnCheckedException extends RuntimeException {
        public MyUnCheckedException(String message) {
            super(message);
        }
    }

    /**
     * UnChecked 예외는 예외를 잡거나, 던지지 않아도 됨
     * 예외를 잡지 않으면 자동으로 밖으로 던짐
     */
    static class Service {
        Repository repository = new  Repository();

        /**
         * 필요한 경우 예외를 잡아서 처리
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUnCheckedException e) {
                // 예외 처리 로직
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }

        /**
         * 예외를 잡지 않아도 됨. 자연스럽게 상위로 넘어감
         * 체크 예외와 다르게 throws 예외 선언을 하지 않아도 됨
         */
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            throw new MyUnCheckedException("ex");
        }
    }
}
