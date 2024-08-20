import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  base: '/java-handbook/',
  title: "Kotlin JS Samples",
  description: "Sample code for Kotlin/JS",
  ignoreDeadLinks: 'localhostLinks',
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
    ],

    sidebar: [
      {
        text: 'はじめに',
        items: [
          { text: 'Java の最初に', link: '/intro/' },
        ]
      },
      {
        text: 'Javaの基本',
        items: [
          { text: 'コレクション', items: [
              { text: 'List', link: '/collection/list' },
              { text: 'Map', link: '/collection/map' },
              { text: 'Set', link: '/collection/set' },
            ]
          }
        ]
      },
      {
        text: 'データベース関連',
        items: [
          { text: 'H2', link: '/database/h2' },
          { text: 'MyBatis', link: '/database/mybatis' },
          { text: 'MySQL Connector/J', link: '/database/mysql' },
        ]
      },
      {
        text: 'Javaライブラリ',
        items: [
          { text: 'Guava', link: '/libraries/guava' },
          { text: 'Jackson', link: '/libraries/jackson' },
          { text: 'JMH', link: '/libraries/jmh' },
          { text: 'Lombok', link: '/libraries/lombok' },
          { text: 'Retrofit', link: '/libraries/retrofit' },
          { text: 'SLF4J', link: '/libraries/slf4j' },
        ]
      },
      {
        text: 'Maven Central',
        link: '/maven-central'
      },
      {
        text: 'メタプログラミング',
        items: [
          { text: 'リフレクション', link: '/metaprogramming/reflection' },
        ]
      },
      {
        text: 'テストツール',
        items: [
          { text: 'JUnit', link: '/testing/junit' },
          { text: 'JUnit 5', link: '/testing/junit5/' },
          { text: 'Mockito', link: '/testing/mockito' },
          { text: 'MockWebServer', link: '/testing/mockwebserver' },
        ]
      },
      {
        text: 'IDE',
        items: [
          { text: 'IntelliJ について', link: '/ide/00-intellij' },
        ]
      },
      {
        text: 'ビルドツール',
        items: [
          { text: 'Ant', link: '/ant' },
          { text: 'Gradle', link: '/gradle' },
        ]
      },
      {
        text: 'ツール',
        items: [
          { text: 'VisualVM', link: '/tools/visualvm' },
        ]
      },
      {
        text: 'Spring関連',
        items: [
          { text: '設定ファイルの記述方法', link: '/spring/config' },
          { text: 'Freemarker', link: '/spring/freemarker' },
          { text: 'HikariCP', link: '/spring/hikaricp' },
          { text: 'i18n', link: '/spring/i18n' },
          { text: 'MyBatis', link: '/spring/mybatis' },
          { text: 'WebJars', link: '/spring/webjars' },
        ]
      },
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/tokuhirom/java-handbook/' }
    ]
  }
})
