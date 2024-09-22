package com.iplab.crudnoticeboard.config;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    // TODO #1 : 읽어보기
    // 이 클래스는 데이터베이스 연결 설정을 위한 클래스입니다.
    // DatabaseConfig.getConnection() 메서드를 통해 데이터베이스 연결을 얻을 수 있습니다.

    private static final Properties props = new Properties();

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(input);
        } catch (Exception e) {
            throw new RuntimeException("설정 파일을 로드할 수 없습니다.", e);
        }
    }

    // TODO #1-2 : 읽어보기
    // 여기에 바로 계정이름, 비밀번호 등을 넣으면 안됨.
    // 왜냐하면 이 파일은 소스코드에 하드코딩되어 있기 때문에 보안에 취약함.
    // 따라서 이 파일은 데이터베이스 연결 설정을 위한 파일이며, 실제 계정이름, 비밀번호 등은 별도의 파일이나 환경변수에 저장하는 것이 좋음.

    // 그 환경변수는 /src/main/resources/application.properties 파일에 저장되어 있음.
    // 저기 저장해두고 그걸 수정하면, 모든 소스파일에 있는 값들을 바꿀 수 있으므로, 코드 유지보수 효율성 증가.

    private static final String URL = props.getProperty("db.url");
    private static final String USER = props.getProperty("db.user");
    private static final String PASSWORD = props.getProperty("db.password");

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}
