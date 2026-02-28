package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.*;

@Slf4j
public class CheckedTest {

    @Test
    public void checked_chatch() {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    public void checked_throw() {
        Service service = new Service();

        assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }


    /**
     * Exception 을 상속받은 예외는 체크 예외가 됨
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }


    /**
     * Checked 예외는 예외를 잡아서 처리하거나, 던지거나 둘 중 하나를 필수로 선택해야 함
     */
    static class Service {
        Repository respository = new Repository();

        /**
         * 예외를 잡아서 처리하는 코드
         */
        public void callCatch() {
            try {
                respository.call();
            } catch (MyCheckedException e) {
                // 예외 처리 로직
                log.info("예외 처리, message={}", e.getMessage(), e);
            }
        }


        /**
         * 체크 예외를 밖으로 던지는 코드
         * 체크 예외는 얘외를 잡지 않고 밖으로 던지려면 throws 예외를 메서드에 필수로 선언
         * @throws MyCheckedException
         */
        public void callThrow() throws MyCheckedException {
            respository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("ex");
        }
    }
}
