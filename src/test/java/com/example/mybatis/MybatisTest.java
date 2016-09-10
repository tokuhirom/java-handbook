package com.example.mybatis;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Slf4j
public class MybatisTest {
    @Value
    public static class Blog {
        Long id;
        String title;
    }

    interface BlogMapper {
        @Insert("INSERT INTO blog (title) VALUES (#{title})")
        int insert(Blog blog);

        @Update("UPDATE blog SET title=#{title} WHERE id=#{id}")
        void update(Blog blog);
    }

    @Test
    public void test() throws SQLException {
        // DataSource 取得
        JdbcDataSource jdbcDataSource = buildDataSource();

        // テーブル定義
        String schema = "CREATE TABLE blog (id INTEGER NOT NULL auto_increment PRIMARY KEY, title VARCHAR(255))";

        Environment environment = new Environment(MybatisTest.class.getSimpleName(), new JdbcTransactionFactory(), jdbcDataSource);
        Configuration configuration = new Configuration(environment);
        configuration.addMapper(BlogMapper.class);
        DefaultSqlSessionFactory defaultSqlSessionFactory = new DefaultSqlSessionFactory(configuration);
        try (SqlSession sqlSession = defaultSqlSessionFactory.openSession()) {
            Connection connection = sqlSession.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(schema);
            preparedStatement.executeUpdate();

            // マッパーを取得
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);
            // auto_increment な ID の値が返ってくる
            int id = mapper.insert(new Blog(null, "Hello"));
            log.info("id: {}", id);
        }
    }

    private JdbcDataSource buildDataSource() {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=3");
        return jdbcDataSource;
    }
}
