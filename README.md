# 1. 프로젝트 소개
스파르타코딩클럽 Java 심화 3기_ 배달앱 프로젝트 '삼식이' <br>
Spring Boot를 이용한 RESTful API 개발.<br>

## 프로젝트 목적
**배달 및 포장 음식 주문 관리 플랫폼 구현** <br>
### 주요 기능 : 
1. 회원가입
   * 회원 가입 및 로그인
2. 식당 검색
   * 메뉴 및 리뷰 조회
   * 평점 확인
3. 주문
   * 온라인 고객 주문 
   * 현장 주문(사장님이 생성)
4. 결제
   * 실제 결제 로직은 미구현 (결제 Data만 관리)
5. 식당 등록
   * 식당 신규 등록 (관리자)
   * 메뉴 추가 (식당 주인)
   * 메뉴 설명 등록, AI를 이용해 보조.


# 2. 팀원 소개

<table>
  <tr height="160px">
    <th align="center" width="140px">
      <a href="https://github.com/juhee99"><img height="130px" width="130px" src="https://avatars.githubusercontent.com/u/55836020?v=4"/></a>
    </th>
    <th align="center" width="140px">
      <a href="https://github.com/jyYoon96"><img height="130px" width="130px" src="https://avatars.githubusercontent.com/u/139435177?v=4"/>
    </th>
    <th align="center" width="140px">
      <a href="https://github.com/CE-TaeHoon"><img height="130px" width="130px" src="https://avatars.githubusercontent.com/u/128115726?v=4"/></a>
    </th>
    <th align="center" width="140px">
      <a href="https://github.com/singingsandhill"><img height="130px" width="130px" src="https://avatars.githubusercontent.com/u/64348312?v=4"/></a>
    </th>
  </tr>
  <tr>
    <td align="center" width="160px">
      <a href="https://github.com/juhee99"><strong>박주희</strong></a>
    </td>
    <td align="center" width="160px">
      <a href="https://github.com/jyYoon96"><strong>윤정영</strong></a>
    </td>
    <td align="center" width="160px">
      <a href="https://github.com/soyoung1832"><strong>이태훈</strong></a>
    </td>
    <td align="center" width="160px">
      <a href="https://github.com/singingsandhill"><strong>김지수</strong></a>
    </td>
  </tr>
  <tr>
    <td align="center" width="160px">
      카테고리, 가게 관리
    </td>
    <td align="center" width="160px">
      주문, 결제 관리
    </td>
    <td align="center" width="160px">
      상품 관리, AI 기능
    </td>
    <td align="center" width="160px">
      로그인, 사용자, 리뷰 관리
    </td>
  </tr>
  <tr>
    <td align="center" width="160px">
       BE, Git
    </td>
    <td align="center" width="160px">
       BE 개발
    </td>
    <td align="center" width="160px">
       BE 개발, AI 연동
    </td>
    <td align="center" width="160px">
       BE, DB
    </td>
  </tr>
</table>

# 3. 시스템 아키텍쳐
![시스템아키텍처 drawio](https://github.com/user-attachments/assets/a813e3a1-943a-407c-a55a-b9177fee83a9)

## 기술 스택

| **분류**            | **기술 스택**                                              | **설명** |
|--------------------|------------------------------------------------------|----------|
| **프레임워크**     | Spring Boot 3.3.8                                     | Spring 기반 애플리케이션 프레임워크 |
| **웹 개발**       | Spring Web (`spring-boot-starter-web`)               | RESTful API 및 웹 서비스 개발 |
| **보안**          | Spring Security (`spring-boot-starter-security`)     | 인증 및 인가 기능 |
| **데이터베이스**   |  PostgreSQL (`postgresql`) | 관계형 데이터베이스 지원 |
| **ORM**           | Spring Data JPA (`spring-boot-starter-data-jpa`)      | JPA 기반 ORM |
|                  | Hibernate                                             | JPA 기본 구현체 |
| **QueryDSL**      | `querydsl-jpa`, `querydsl-apt`                        | 타입 세이프한 동적 쿼리 작성 |
| **API 문서화**    | SpringDoc OpenAPI (`springdoc-openapi-starter-webmvc-ui`) | Swagger 기반 API 문서화 |
| **JWT 인증**      | `jjwt-api`, `jjwt-impl`, `jjwt-jackson`               | JWT 기반 인증 및 토큰 관리 |
| **유효성 검사**   | Spring Validation (`spring-boot-starter-validation`)  | 입력 데이터 검증 |
| **빌드 도구**     | Gradle (Groovy DSL)                                   | 의존성 및 빌드 관리 |
| **컴파일러 설정** | Java 17 (`sourceCompatibility = '17'`)                | Java 17 기반 컴파일 |
| **코드 생성**     | Lombok (`lombok`)                                     | Getter, Setter 자동 생성 |
| **테스트**        | JUnit 5 (`spring-boot-starter-test`, `junit-platform-launcher`) | 단위 및 통합 테스트 |

# 4. ERD
![ERD](https://github.com/user-attachments/assets/a91a4ecb-a5d6-4cc5-84f0-66116c95db6e)

# 5. 서비스 구성 및 실행 방법
## API 이용 가이드
* FE 연동 <br>
   * FE를 활용해 API를 이용해 이용, 로그인 이후에는 access_token을 header 담아서 요청.
* API 테스트 : http://run.blu2print.site:3332/swagger-ui/index.html
   * 권한별 로그인 정보 별도 요청

## BE 유지보수
```
[프로젝트 루트]
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── [계층형 구조]
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── [기타 설정 파일들]
```
* application.propertis : 공유 요청
   * 배포 DB 운영중 (supabase, postgreSQL)
<br>

* 현재 배포 상태 : AWS EC2 이용, 수동 배포
