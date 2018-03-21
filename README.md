# SpringEventHost Tutorial

http://localhost:8080/login

Applications.properties should be setup like below:
```
# ===============================
# = DATA SOURCE
# ===============================
spring.datasource.url = jdbc:mysql://localhost:3306/db_name
spring.datasource.username = USERNAME
spring.datasource.password = PASSWORD
spring.datasource.testWhileIdle = true
spring.datasource.validationQuery = SELECT 1

# ===============================
# = JPA / HIBERNATE
# ===============================
spring.jpa.show-sql = true
spring.jpa.hibernate.ddl-auto = update
spring.jpa.hibernate.naming-strategy = org.hibernate.cfg.ImprovedNamingStrategy
spring.jpa.properties.hibernate.dialect = org.hibernate.dialect.MySQL5Dialect

# ===============================
# = Thymeleaf configurations
# ===============================
spring.thymeleaf.mode=LEGACYHTML5
spring.thymeleaf.cache=false

# ==============================================================
# = Spring Security / Queries for AuthenticationManagerBuilder  
# ==============================================================
spring.queries.users-query=select email, password, active from user where email=?
spring.queries.roles-query=select u.email, r.role from user u inner join user_role ur on(u.id=ur.id) inner join role r on(ur.role_id=r.role_id) where u.email=?
```
