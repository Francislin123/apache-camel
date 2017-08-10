# Feeds Admin Api

This application responsible for setting up a feed, through it you can register a new partner, feed or map their fields to a template.

# Technologies

  - Java 8
  - Gradle 4.0.1
  - Spring Boot 2.0.0M3
  - Liquibase 1.2.4
  - Hibernate 5.2.10
  - Oracle 11


##### You can also:


- Merge request in the master branch after being applied in production
- All stories need a branch from the homologation
- Every task needs a branch from the story
- The merge of the branchs of the tasks must be done in the equivalent
- A member who has developed the story will be responsible for the merge request in the homologation


### Clone project and Run application

```sh
$ git clone https://gitlab.wmxp.com.br/feeds/feeds-admin-api.git
```

By default in the development environment it uses h2 as a database

```sh
$ cd feeds-admin-api/
$ ./gradlew bootRun
```
