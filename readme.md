## 그룹웨어 ##
본 샘플은 Spring Boot 2.7 (Spring Framework 5.3) + MyBatis 3 + MariaDB 기반으로 제작한 웹 기반 그룹웨어이다. `war`로 패키징하며 빌드는 Gradle을 사용한다.

웹 프로젝트 템플릿인 [Project9](https://github.com/gujc71/project9/)을 이용하여, 웹 개발을 쉽게 할 수 있다는 예제로 제작 중이다.

간단한 화면 설계는 [여기](https://docs.google.com/presentation/d/1QcCr2WwDNhcEbF3v9Kr_KQGe0ohOSXVOW_gneKO7VBw/edit#slide=id.p)서 확인할 수 있다.

### 주요 구현 기능 ###
- 일정: 월간 캘린더/일정
- 결재: 기안하기, 결재 받을 문서, 결재 할 문서
- 전자우편: 새메일, 받은 메일, 보낸 메일, IMAP 가져오기
- 게시판: 게시판 그룹 설정 가능 ([Project9](https://github.com/gujc71/project9/))

### 개발 환경 ###
    Programming Language - Java 1.8 (source/target), JDK 17 툴체인에서 빌드
    DB - MariaDB
    Framework - Spring Boot 2.7 (Spring Framework 5.3), MyBatis 3
    Build Tool - Gradle (Gradle Wrapper 포함)

### 설치 ###

#### Docker 사용 (권장) ####
1. `docker compose up -d` 실행 — `localhost:3306`에 MariaDB 컨테이너가 뜨고, 최초 기동 시 `tables.sql`/`tableData.sql`이 자동으로 로드된다.
2. `./gradlew bootRun` 실행 (Windows는 `gradlew.bat bootRun`)
3. http://localhost:8080/groupware9/ 로 접속
4. ID/PW: admin/admin, user1/user1, user2/user2 ...

#### 수동 설치 ####
1. MariaDB에 `groupware9` 데이터베이스를 생성하고, 저장소 루트의 `tables.sql`, `tableData.sql`을 실행하여 테이블과 데이터를 생성한다.
2. 로컬 접속 정보가 기본값(`root`/`gujc1004`)과 다르다면 `src/main/resources/config/application.yml`의 `db.*` 값을 수정한다.
3. `./gradlew bootRun`으로 실행하거나, `./gradlew bootWar`로 실행 가능한 WAR(`build/libs/project9-1.0.war`)를 빌드하여 배포한다.

### License ###
GPL v3
