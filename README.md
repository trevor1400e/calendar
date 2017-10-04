# SpringEventHost Tutorial

http://localhost:8080/login



Applications.properties should be setup like below:

spring.datasource.url = jdbc:mysql://localhost:3306/db_name
spring.datasource.username = USERNAME
spring.datasource.password = PASSWORD
spring.datasource.testWhileIdle = true
spring.datasource.validationQuery = SELECT 1

spring.jpa.show-sql = true
spring.jpa.hibernate.ddl-auto = update
spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

spring.thymeleaf.mode=LEGACYHTML5
spring.thymeleaf.cache=false

spring.queries.users-query=select email, password, active from user where email=?
spring.queries.roles-query=select u.email, r.role from user u inner join user_role ur on(u.user_id=ur.user_id) inner join role r on(ur.role_id=r.role_id) where u.email=?
