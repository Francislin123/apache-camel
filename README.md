# Feeds Admin Api

This application responsible for setting up a feed, through it you can register a new partner, feed or map their fields to a template.

For more details access [Admin e API Interna Feeds](https://confluence.wmxp.com.br/display/MI/2.+Admin+e+API+interna+Feeds).

## Technologies

  - Java 8
  - Gradle 4.0.1
  - Spring Boot 1.5.6.RELEASE
  - Liquibase 1.2.4
  - Hibernate 5.2.12
  - Oracle 11
  - SpringFox 2.7.0
  - Apache Camel 2.19.2
  - Elastic Search 2.4.6
  - Feign 9.3.1
  - Keycloak 3.3.0.CR2


##### You can also:

- Merge request in the master branch after being applied in production
- All stories need a branch from the homologation
- Every task needs a branch from the story
- The merge of the branchs of the tasks must be done in the equivalent
- A member who has developed the story will be responsible for the merge request in the homologation


#### Clone project and Run application

```sh
$ git clone https://gitlab.wmxp.com.br/feeds/feeds-admin-api.git
```

By default in the development environment it uses h2 as a database

```sh
$ cd feeds-admin-api/
$ ./gradlew bootRun
```
