package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * 쳬크 예외 문제점
 * 1. 복구 불가능한 예외 - 대부분의 예외는 복구 불가능
 * 2. 의존 관계에 대한 문제 - throws를 계속 던져야 되는 문제 발생, SQLException 의존해야 되는 문제(JDBC 의존)
 */

@Slf4j
public class UnCheckedAppTest {

    @Test
    public void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    @Test
    public void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
            log.info("ex", e);
        }
    }

    static class Controller {
        Service service = new  Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);  // SQLException을 RuntimeException 으로 바꿔서 던짐
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        // 이 생성자를 통해서 이 전에 발생한 예외를 포함 시켜서 확인 가능
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
