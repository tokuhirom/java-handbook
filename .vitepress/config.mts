import { defineConfig } from 'vitepress'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  base: '/kotlin-js-samples/',
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
        text: 'Introduction',
        items: [
          { text: 'IntelliJ について', link: '/00-intellij' },
          { text: 'JUnit 5', link: '/01-junit5' },
        ]
      },
      {
        text: 'Javaの基本',
        items: [
          { text: 'Ant', link: '/ant' },
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
        text: 'ビルドツール',
        items: [
          { text: 'Gradle', link: '/gradle' },
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
      {
        text: 'テストツール',
        items: [
          { text: 'JUnit', link: '/testing/junit' },
          { text: 'Mockito', link: '/testing/mockito' },
          { text: 'MockWebServer', link: '/testing/mockwebserver' },
        ]
      },
      {
        text: 'ツール',
        items: [
          { text: 'VisualVM', link: '/tools/visualvm' },
        ]
      }
    ],

    socialLinks: [
      { icon: 'github', link: 'https://github.com/tokuhirom/kotlin-js-samples/' }
    ]
  }
})
