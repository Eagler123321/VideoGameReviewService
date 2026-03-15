# 🎮 Overcrit — Веб-сервис для рецензирования видеоигр

Платформа для публикации, просмотра и анализа пользовательских рецензий на видеоигры.

## 🚀 Технологии

| Компонент       | Технология               |
|-----------------|--------------------------|
| Backend         | Java 17, Spring Boot 3.5 |
| БД              | PostgreSQL + Liquibase   |
| Security        | Spring Security + JWT    |
| Тесты           | JUnit 5, Testcontainers  |
| Сборка          | Maven                    |
| Контейнеризация | Docker (в планах)        |

## 📦 Запуск локально

### Требования
- Java 17+
- PostgreSQL 15+
- Maven 3.8+

### Быстрый старт
```bash
# 1. Клонировать репозиторий
git clone https://github.com/Eagler123321/VideoGameReviewService.git
cd vercrit

# 2. Собрать и запустить приложение
./mvnw spring-boot:run