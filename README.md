Мини банковское приложение

Технологии:
- Java 21
- Spring Boot 3.x
- Spring Data JPA
- Spring Security
- Spring Web (Spring MVC)
- PostgreSQL 
- Java JWT
- Spring HATEOAS
- Maven


API Эндпоинты
Аутентификация:
1. POST /auth/registration - регистрирует нового пользователя в системе. При регистрации проверяются правильность введённых данных,
а также совпадение паролей. После успешной регистрации генерируется JWT токен для аутентификации пользователя
2. POST /auth/login - аутентифицирует пользователя в системе. При успешной аутентификации возвращается JWT токен, который может быть использован для дальнейших запросов.

Пользователи:
1. GET /user/{username} - получение информации о пользователе по его логину

Аккаунт:
1. POST /account/create - создание нового банковского счета для текущего авторизованного пользователя
2. GET /account/{accountNumber}/transactions - получение списка транзакций по счету с пагинацией. По умолчанию возвращаются последние 20 транзакций, отсортированные по времени.
3. POST /account/{accountNumber}/deposit - пополнение счета на указанную сумму
4. POST /account/{accountNumber}/withdraw - снятие средств с указанного счета
5. POST /account/{accountNumber}/transfer - перевод  средств между счетами. Перевод выполняется между счетом, указанным в URL, и счетом, указанным в теле запроса.

Swagger - http://localhost:8080/swagger-ui/index.html
