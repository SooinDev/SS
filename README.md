<div align="center">

# 📝 SS

### Simple & Smart Todo Management System

**터미널에서 만나는 가장 간편한 할 일 관리 도구**

[![Java](https://img.shields.io/badge/Java-1.8-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/Spring-5.3.31-6DB33F?style=for-the-badge&logo=spring&logoColor=white)](https://spring.io/)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.5.13-FF0000?style=for-the-badge)](https://mybatis.org/)
[![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)](https://mariadb.org/)

[특징](#-주요-특징) • [설치](#-빠른-시작) • [사용법](#-사용법) • [개발](#-개발-가이드)

</div>

---

## 🎯 프로젝트 소개

**SS(Simple System)** 는 Spring Framework와 MyBatis를 기반으로 구축된 CLI 환경의 Todo 관리 애플리케이션입니다.
복잡한 GUI 없이 터미널에서 빠르고 효율적으로 할 일을 관리할 수 있습니다.

### 💫 주요 특징

| 특징 | 설명 |
|:---:|:---|
| 🚀 **빠른 실행** | 터미널에서 즉시 실행 가능한 경량 애플리케이션 |
| 🎨 **컬러 인터페이스** | ANSI 색상을 활용한 직관적인 UI/UX |
| 🏗️ **엔터프라이즈 아키텍처** | Spring DI, MyBatis ORM을 활용한 계층형 구조 |
| 💾 **영속성 보장** | MariaDB 기반 안정적인 데이터 저장 |
| ⚡ **고성능** | HikariCP 커넥션 풀을 통한 최적화된 DB 연결 |
| 🧪 **테스트 가능** | 계층 분리로 단위 테스트 용이 |

---

## 🛠️ 기술 스택

### Core Technologies
```
Java 1.8          │ 안정적인 JVM 기반 실행 환경
Spring 5.3.31     │ DI/IoC 컨테이너 및 트랜잭션 관리
MyBatis 3.5.13    │ SQL Mapper 프레임워크
MariaDB           │ 관계형 데이터베이스
HikariCP 4.0.3    │ 고성능 JDBC 커넥션 풀
```

### Dependencies
- **Lombok** `1.18.30` - 보일러플레이트 코드 제거
- **SLF4J + Logback** `1.7.36` - 로깅 프레임워크
- **JUnit** `4.13.2` - 단위 테스트 프레임워크
- **Servlet API** `3.1.0` - 웹 애플리케이션 확장 대비

---

## 📦 프로젝트 구조

```
SS/
│
├── 📂 src/main/java/com/ss/
│   ├── 🎮 controller/
│   │   └── MainController.java      ← 애플리케이션 진입점 (CLI)
│   │
│   ├── 🔧 service/
│   │   ├── SsService.java           ← 비즈니스 로직 인터페이스
│   │   └── impl/
│   │       └── SsServiceImpl.java   ← 비즈니스 로직 구현체
│   │
│   ├── 🗂️ mapper/
│   │   └── TodoMapper.java          ← MyBatis SQL 매퍼
│   │
│   ├── 📊 vo/
│   │   └── TodoVO.java              ← Todo 데이터 객체
│   │
│   └── ⚙️ config/
│       └── AppConfig.java           ← Spring Bean 설정
│
├── 📄 pom.xml                        ← Maven 빌드 설정
└── 📖 README.md
```

### 아키텍처 패턴
```
┌─────────────────────────────────────────┐
│         MainController (CLI)            │  ← Presentation Layer
└───────────────┬─────────────────────────┘
                │
┌───────────────▼─────────────────────────┐
│          SsService Interface            │  ← Business Layer
│         (SsServiceImpl)                 │
└───────────────┬─────────────────────────┘
                │
┌───────────────▼─────────────────────────┐
│          TodoMapper (MyBatis)           │  ← Persistence Layer
└───────────────┬─────────────────────────┘
                │
┌───────────────▼─────────────────────────┐
│        MariaDB (HikariCP)               │  ← Database Layer
└─────────────────────────────────────────┘
```

---

## 🚀 빠른 시작

### 📋 사전 요구사항

```bash
☑️ Java JDK 1.8 이상
☑️ Apache Maven 3.x
☑️ MariaDB 10.x (또는 MySQL 5.7+)
```

### 📥 설치 및 실행

**1️⃣ 프로젝트 클론**
```bash
git clone https://github.com/SooinDev/SS.git
cd SS
```

**2️⃣ 데이터베이스 설정**
```sql
-- MariaDB / MySQL 접속 후 실행
CREATE DATABASE ss_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE ss_db;

CREATE TABLE todo (
    no BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(500) NOT NULL,
    reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_reg_date (reg_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

**3️⃣ DB 연결 설정**

`src/main/java/com/ss/config/AppConfig.java` 파일에서 DB 정보 수정:

```java
dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/ss_db");
dataSource.setUsername("your_username");    // 👈 수정 필요
dataSource.setPassword("your_password");    // 👈 수정 필요
```

**4️⃣ 빌드 및 실행**
```bash
# Maven 빌드
mvn clean package

# 애플리케이션 실행
mvn exec:java -Dexec.mainClass="com.ss.controller.MainController"
```

---

## 💡 사용법

### 🎮 명령어 레퍼런스

애플리케이션 실행 후 사용 가능한 명령어:

| 명령어 | 기능 | 사용 예시 |
|:------|:-----|:---------|
| `add [내용]` | 새로운 할 일 추가 | `add Spring Boot 학습하기` |
| `list` | 전체 할 일 목록 조회 | `list` |
| `delete [번호]` | 특정 번호의 할 일 삭제 | `delete 3` |
| `exit` | 프로그램 종료 | `exit` |

### 📸 실행 화면

```bash
$ mvn exec:java -Dexec.mainClass="com.ss.controller.MainController"

시스템 부팅 중... (DB 연결 확인)
===================================================
   ss (Ver 1.0) - Online
   명령어: add [내용], list, delete [번호], exit
===================================================

ss > add Spring Framework 공부하기
✅ 할 일이 등록되었습니다.

ss > add MyBatis 설정 완료하기
✅ 할 일이 등록되었습니다.

ss > list
--- To-Do List (2개) ---
[1] Spring Framework 공부하기 (12-23 14:30)
[2] MyBatis 설정 완료하기 (12-23 14:31)
--------------------------------

ss > delete 1
✅ 1번 할 일이 삭제되었습니다.

ss > list
--- To-Do List (1개) ---
[2] MyBatis 설정 완료하기 (12-23 14:31)
--------------------------------

ss > exit
시스템을 종료합니다. Bye! 👋
```

---

## 🔧 개발 가이드

### 핵심 개념

#### 1️⃣ Dependency Injection (의존성 주입)
Spring의 IoC 컨테이너가 객체 생성 및 의존성 관리를 담당합니다.

```java
@Configuration
public class AppConfig {
    @Bean
    public SsService ssService() {
        return new SsServiceImpl();
    }
}
```

#### 2️⃣ MyBatis SQL Mapping
인터페이스 기반 SQL 매핑으로 간결한 데이터 접근 계층 구현:

```java
@Mapper
public interface TodoMapper {
    void insertTodo(TodoVO todo);
    List<TodoVO> selectAllTodos();
    void deleteTodo(Long no);
}
```

#### 3️⃣ Transaction Management
Spring의 선언적 트랜잭션으로 데이터 무결성 보장:

```java
@Transactional
public void addTodo(String content) {
    // 트랜잭션 자동 관리
}
```

### 🧪 테스트 실행

```bash
# 전체 테스트 실행
mvn test

# 특정 테스트 실행
mvn test -Dtest=SsServiceTest
```

### 📦 빌드

```bash
# WAR 파일 생성
mvn clean package

# 생성 위치: target/ss.war
```

---

## 🎨 기술적 특징

### ✨ 성능 최적화
- **HikariCP**: 업계 최고 성능의 JDBC 커넥션 풀
- **MyBatis 캐싱**: SQL 실행 결과 캐싱으로 성능 향상
- **인덱스 최적화**: `reg_date` 컬럼 인덱스로 조회 성능 개선

### 🛡️ 코드 품질
- **계층 분리**: Presentation - Business - Persistence 계층 명확 분리
- **인터페이스 기반 설계**: 느슨한 결합으로 테스트 및 확장 용이
- **Lombok 활용**: Getter/Setter 자동 생성으로 가독성 향상

### 🎯 확장 가능성
- Servlet API 포함으로 **웹 애플리케이션 전환** 가능
- RESTful API 추가 가능
- 다양한 데이터베이스 전환 용이 (MySQL, PostgreSQL 등)

---

## 🗺️ 로드맵

- [ ] 할 일 완료 상태 토글 기능
- [ ] 우선순위 설정 기능
- [ ] 마감일 관리 기능
- [ ] RESTful API 버전 제공
- [ ] React 기반 웹 프론트엔드 연동
- [ ] Docker 컨테이너화

---

## 📄 라이선스

이 프로젝트는 개인 학습 및 포트폴리오 목적으로 제작되었습니다.

---

## 👨‍💻 개발자

<div align="center">

**Sooin**

[![GitHub](https://img.shields.io/badge/GitHub-SooinDev-181717?style=for-the-badge&logo=github)](https://github.com/SooinDev)
[![Email](https://img.shields.io/badge/Email-alwayswithsound@gmail.com-EA4335?style=for-the-badge&logo=gmail&logoColor=white)](mailto:alwayswithsound@gmail.com)

---

⭐ **이 프로젝트가 도움이 되셨다면 Star를 눌러주세요!**

*Made with ❤️ by Sooin*

</div>
