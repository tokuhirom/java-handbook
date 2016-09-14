package com.example.mybatis;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.*;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class MybatisTest {
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Blog {
        Long id;
        String title;
    }

    interface BlogMapper {
        @Insert("INSERT INTO blog.blog (title) VALUES (#{title})")
        @Options(useGeneratedKeys = true)
        int insert(Blog blog);

        @Update("UPDATE blog SET title=#{blog.title} WHERE blog_id=#{id} ORDER BY blog_id DESC")
        long update(@Param("id") long id, @Param("blog") Blog blog);

        @Delete("DELETE blog WHERE id=#{id}")
        long delete(long id);

        @Select("SELECT COUNT(*) FROM blog")
        long count();

        @Select("SELECT * FROM blog ORDER BY ${order}")
        List<Blog> findAll(@Param("order") String order);

        @Select("SELECT * FROM blog WHERE blog_id=#{blog_id}")
        Blog findById(@Param("blog_id") long blogId);
    }

    @Test
    public void test() throws SQLException {
        // DataSource 取得
        JdbcDataSource jdbcDataSource = buildDataSource();

        // テーブル定義
        String schema = "CREATE TABLE blog (id BIGINT NOT NULL auto_increment PRIMARY KEY, title VARCHAR(255))";

        Environment environment = new Environment(MybatisTest.class.getSimpleName(), new JdbcTransactionFactory(), jdbcDataSource);
        Configuration configuration = new Configuration(environment);
        configuration.setLocalCacheScope(LocalCacheScope.STATEMENT);
        configuration.addMapper(BlogMapper.class);
        SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
        SqlSessionFactory sqlSessionFactory = sqlSessionFactoryBuilder.build(configuration);
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            Connection connection = sqlSession.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(schema);
            preparedStatement.executeUpdate();

            // マッパーを取得
            BlogMapper mapper = sqlSession.getMapper(BlogMapper.class);

            // INSERT したら auto_increment な ID の値が返ってくる
            Blog blog1 = new Blog(null, "My tech blog");
            mapper.insert(blog1);
            log.info("blog1: {}", blog1);

            Blog blog2 = new Blog(null, "My lfe blog");
            mapper.insert(blog2);
            log.info("blog2: {}", blog2);

            // COUNT する
            {
                long count = mapper.count();
                log.info("count: {}", count);
            }

            // 全行とってみる
            {
                List<Blog> all = mapper.findAll("id");
                log.info("all: {}", all);
            }

            // 一行上書きする
            long update = mapper.update(blog2.getId(), new Blog(null, "My life blog"));
            log.info("Updated rows: {}", update);

            // 全行とってみる
            List<Blog> all2 = mapper.findAll("id DESC");
            log.info("all: {}", all2);

            // 1行 DELETE する。返り値は affected rows。
            long affectedRows = mapper.delete(blog1.getId());
            log.info("affectedRows: {}", affectedRows);

            // COUNT する
            {
                long count = mapper.count();
                log.info("count: {}", count);
            }

            // 全行取得してみる
            {
                List<Blog> all = mapper.findAll("id");
                log.info("all: {}", all);
            }
        }
    }

    private JdbcDataSource buildDataSource() {
        JdbcDataSource jdbcDataSource = new JdbcDataSource();
        jdbcDataSource.setURL("jdbc:h2:mem:test;TRACE_LEVEL_SYSTEM_OUT=1");
        return jdbcDataSource;
    }
}
