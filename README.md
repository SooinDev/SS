# 📝 SS (Simple System)

> CLI 기반 Todo 관리 시스템

[![Java](https://img.shields.io/badge/Java-1.8-007396?style=flat&logo=java)](https://www.oracle.com/java/)
[![Spring](https://img.shields.io/badge/Spring-5.3.31-6DB33F?style=flat&logo=spring)](https://spring.io/)
[![MyBatis](https://img.shields.io/badge/MyBatis-3.5.13-red?style=flat)](https://mybatis.org/)
[![Maven](https://img.shields.io/badge/Maven-Project-C71A36?style=flat&logo=apache-maven)](https://maven.apache.org/)

## ✨ 프로젝트 소개

SS는 Spring Framework와 MyBatis를 활용한 경량 CLI Todo 관리 시스템입니다.
터미널에서 간편하게 할 일을 추가, 조회, 삭제할 수 있습니다.

### 주요 특징

- 🎯 직관적인 CLI 인터페이스
- 🔄 Spring Framework 기반 의존성 주입
- 💾 MyBatis를 통한 데이터 영속성 관리
- 🗄️ MariaDB + HikariCP 연결 풀 최적화
- 🎨 컬러풀한 터미널 출력

## 🛠️ 기술 스택

### Backend
- **Java**: 1.8
- **Spring Framework**: 5.3.31
  - Spring Context
  - Spring JDBC
  - Spring TX
  - Spring WebMVC
- **MyBatis**: 3.5.13
- **Database**: MariaDB 3.3.3
- **Connection Pool**: HikariCP 4.0.3

### Utilities
- **Lombok**: 1.18.30
- **SLF4J + Logback**: 1.7.36
- **JUnit**: 4.13.2

## 📁 프로젝트 구조

```
SS/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── ss/
│                   ├── config/
│                   │   └── AppConfig.java       # Spring 설정
│                   ├── controller/
│                   │   └── MainController.java  # CLI 진입점
│                   ├── service/
│                   │   ├── SsService.java       # 서비스 인터페이스
│                   │   └── impl/
│                   │       └── SsServiceImpl.java
│                   ├── mapper/
│                   │   └── TodoMapper.java      # MyBatis Mapper
│                   └── vo/
│                       └── TodoVO.java          # Value Object
├── pom.xml
└── README.md
```

## 🚀 시작하기

### 사전 요구사항

- Java 1.8 이상
- Maven 3.x
- MariaDB 설치 및 실행

### 설치 방법

1. **저장소 클론**
   ```bash
   git clone <repository-url>
   cd SS
   ```

2. **데이터베이스 설정**
   ```sql
   -- 데이터베이스 생성
   CREATE DATABASE ss_db;

   -- 테이블 생성
   USE ss_db;
   CREATE TABLE todo (
       no BIGINT AUTO_INCREMENT PRIMARY KEY,
       content VARCHAR(500) NOT NULL,
       reg_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
   );
   ```

3. **빌드**
   ```bash
   mvn clean compile
   ```

4. **실행**
   ```bash
   mvn exec:java -Dexec.mainClass="com.ss.controller.MainController"
   ```

## 💡 사용법

### 명령어

| 명령어 | 설명 | 예시 |
|--------|------|------|
| `add [내용]` | 새로운 할 일 추가 | `add 프로젝트 문서 작성` |
| `list` | 전체 할 일 목록 조회 | `list` |
| `delete [번호]` | 특정 할 일 삭제 | `delete 1` |
| `exit` | 프로그램 종료 | `exit` |

### 실행 예시

```bash
시스템 부팅 중... (DB 연결 확인)
===================================================
   ss (Ver 1.0) - Online
   명령어: add [내용], list, delete [번호], exit
===================================================
ss > add 코드 리뷰하기
할 일이 등록되었습니다.

ss > list
--- To-Do List (1개) ---
[1] 코드 리뷰하기 (12-20 15:30)
--------------------------------

ss > delete 1
1번 할 일이 삭제되었습니다.

ss > exit
시스템을 종료합니다. Bye!
```

## 🔧 설정

데이터베이스 연결 정보는 `AppConfig.java`에서 수정할 수 있습니다:

```java
// HikariCP DataSource 설정
dataSource.setJdbcUrl("jdbc:mariadb://localhost:3306/ss_db");
dataSource.setUsername("your_username");
dataSource.setPassword("your_password");
```

## 📝 개발 노트

### 아키텍처 패턴
- **Layer Architecture**: Controller - Service - Mapper - VO
- **Dependency Injection**: Spring Framework의 IoC 컨테이너 활용
- **Transaction Management**: Spring의 선언적 트랜잭션 관리

### 특징
- Annotation 기반 Spring 설정 (`@Configuration`, `@Bean`)
- MyBatis Mapper 인터페이스를 통한 SQL 매핑
- HikariCP를 통한 효율적인 커넥션 풀 관리

## 📄 라이선스

이 프로젝트는 개인 학습 및 포트폴리오 목적으로 제작되었습니다.

## 👤 개발자

**Your Name**
- GitHub: [@SooinDev](https://github.com/SooinDev)
- Email: alwayswithsound@gmail.com

---

⭐ 이 프로젝트가 도움이 되었다면 Star를 눌러주세요!
