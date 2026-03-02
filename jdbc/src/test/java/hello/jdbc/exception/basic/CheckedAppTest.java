package hello.jdbc.exception.basic;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.*;

/**
 * 쳬크 예외 문제점
 * 1. 복구 불가능한 예외 - 대부분의 예외는 복구 불가능
 * 2. 의존 관계에 대한 문제 - throws를 계속 던져야 되는 문제 발생, SQLException 의존해야 되는 문제(JDBC 의존)
 */

public class CheckedAppTest {

    @Test
    public void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(() -> controller.request())
                .isInstanceOf(Exception.class);
    }

    static class Controller {
        Service service = new  Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() throws SQLException, ConnectException {
            repository.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() throws ConnectException {
            throw new ConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() throws SQLException {
            throw new SQLException("ex");
        }
    }
}
