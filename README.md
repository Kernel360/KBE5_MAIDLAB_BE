# MaidLab - 생활 서비스 매칭 플랫폼

## 📄 프로젝트 소개

MaidLab은 바쁜 현대인의 일상을 지원하기 위해 청소, 베이비시터, 반려동물 케어 등의 생활 서비스를 제공하고, 고객(수요자)과 매니저(공급자)를 스마트하게 매칭하는 종합 생활 서비스 플랫폼입니다.

고객은 필요한 서비스를 손쉽게 예약하고, 매니저는 자신의 전문성을 바탕으로 안정적인 수익을 창출할 수 있도록 도와주는 양방향 플랫폼입니다.

## 🌐 배포 사이트

**배포 URL**: https://www.maidlab.site/ 

## 🛠️ 기술 스택

### Backend Core
- **Java 21** - 최신 LTS 버전
- **Spring Boot 3.4.5** - 웹 애플리케이션 프레임워크
- **Spring Security** - JWT 기반 인증 및 보안
- **Spring AOP** - 횡단 관심사 처리 (로깅, 예외처리)

### Data & Persistence
- **Spring Data JPA** - ORM 및 데이터 접근
- **QueryDSL** - 타입 안전한 동적 쿼리 작성
- **MySQL** - 데이터베이스
- **HikariCP** - 커넥션 풀링

### Authentication & Security
- **JWT (JSON Web Token)** - 토큰 기반 인증
- **Google OAuth 2.0** - 소셜 로그인

### Cloud & Storage
- **AWS S3** - 파일 저장소 (이미지, 문서)
- **AWS CloudFront** - CDN (콘텐츠 배포)

### API & Documentation
- **SpringDoc OpenAPI 3** - API 문서화 (Swagger)
- **Server-Sent Events (SSE)** - 실시간 알림

### DevOps & Infrastructure
- **GitHub Actions** - CI/CD 파이프라인
- **AWS EC2** - 배포 환경

### Build & Project Management
- **Gradle** - 빌드 도구
- **멀티모듈 프로젝트** - 모듈별 관심사 분리


## 🚀 주요 기능

### 👥 고객 (Consumer) 기능
- 일반/소셜 회원가입 및 로그인 (Google OAuth)
- 서비스 예약 신청 및 관리
- 매니저 검색 및 선호도 설정
- 예약 현황 조회 및 취소
- 리뷰 작성 및 평점 관리
- 포인트 충전 및 결제
- 실시간 알림 수신

### 🧹 매니저 (Manager) 기능
- 일반/소셜 회원가입 및 로그인 (Google OAuth)
- 프로필 등록 및 인증 서류 제출
- 서비스 가능 지역 및 시간 설정 (서울 지역별)
- 예약 요청 승인/거절
- 체크인/체크아웃 처리
- 정산 내역 조회 (주간 정산)
- 고객 리뷰 관리
- 실시간 알림 수신

### 👨‍💼 관리자 (Admin) 기능
- 고객/매니저 계정 관리
- 예약 및 매칭 현황 모니터링
- 정산 관리 및 승인
- 이벤트 관리
- 게시판 관리 및 답변
- 실시간 로그 모니터링 

## 📝 API 엔드포인트

### 주요 API 개요

| 카테고리 | 기본 경로 | 주요 기능 |
|---------|----------|----------|
| 인증 | `/api/auth` | 회원가입, 로그인, OAuth |
| 고객 | `/api/consumers` | 프로필, 선호도 관리 |
| 매니저 | `/api/manager` | 프로필, 스케줄 관리 |
| 예약 | `/api/reservations` | 예약 생성, 관리, 리뷰 |
| 매칭 | `/api/matching` | 매니저 매칭 |
| 포인트 | `/api/point` | 충전, 사용, 이력 |
| 알림 | `/api/notifications` | 실시간 알림 (SSE) |
| 게시판 | `/api/board` | 문의, 답변 |
| 파일 | `/api/files` | S3 업로드 |

**상세 API 문서**: 
- **배포**: https://api-maidlab.duckdns.org/

## 📂 프로젝트 구조

```
KBE5_MAIDLAB_BE/
├── app/                    # 메인 애플리케이션 모듈
│   ├── src/main/
│   │   ├── java/kernel/maidlab/
│   │   │   └── MaidlabBeApplication.java
│   │   └── resources/
│   │       ├── application.yaml
│   │       ├── application-local.yaml
│   │       ├── application-prod.yaml
│   │       └── data.sql
│   └── build.gradle
├── admin/                  # 관리자 모듈
│   ├── src/main/java/kernel/maidlab/admin/
│   │   ├── auth/          # 관리자 인증
│   │   ├── board/         # 게시판 관리
│   │   ├── consumer/      # 고객 관리
│   │   ├── manager/       # 매니저 관리
│   │   ├── matching/      # 매칭 관리
│   │   ├── reservation/   # 예약 관리
│   │   ├── event/         # 이벤트 관리
│   │   ├── logs/          # 로그 모니터링
│   │   └── websocket/     # 웹소켓 설정
│   └── build.gradle
├── api/                    # API 컨트롤러 모듈
│   ├── src/main/java/kernel/maidlab/
│   │   ├── auth/          # 인증 API
│   │   ├── aws/           # S3 API
│   │   ├── board/         # 게시판 API
│   │   ├── consumer/      # 고객 API
│   │   ├── manager/       # 매니저 API
│   │   ├── matching/      # 매칭 API
│   │   ├── notification/  # 알림 API
│   │   ├── point/         # 포인트 API
│   │   └── reservation/   # 예약 API
│   └── build.gradle
├── domain/                 # 도메인 로직 모듈
│   ├── src/main/java/kernel/maidlab/domain/
│   │   ├── auth/          # 인증 도메인
│   │   ├── board/         # 게시판 도메인
│   │   ├── consumer/      # 고객 도메인
│   │   ├── manager/       # 매니저 도메인
│   │   ├── matching/      # 매칭 도메인
│   │   ├── notification/  # 알림 도메인
│   │   ├── point/         # 포인트 도메인
│   │   └── reservation/   # 예약 도메인
│   └── build.gradle
├── core/                   # 공통 기능 및 횡단 관심사
│   ├── src/main/java/kernel/maidlab/core/
│   │   ├── aop/           # AOP 설정
│   │   ├── aws/           # AWS 설정
│   │   ├── exception/     # 예외 처리
│   │   ├── security/      # 보안 설정
│   │   └── websocket/     # 웹소켓 설정
│   └── build.gradle
├── common/                 # 공통 유틸리티 모듈
│   ├── src/main/java/kernel/maidlab/common/
│   │   ├── config/        # 설정 클래스
│   │   ├── dto/           # 공통 DTO
│   │   ├── entity/        # 기본 엔티티
│   │   ├── enums/         # 열거형
│   │   └── util/          # 유틸리티
│   └── build.gradle
├── build.gradle            # 루트 빌드 스크립트
├── settings.gradle         # 멀티모듈 설정
└── README.md
```

### 모듈별 역할 및 의존성

#### 모듈 설계 원칙
- **모듈 간 의존성 최소화**: 각 모듈은 명확한 책임을 가지며 필요한 모듈만 의존
- **계층형 아키텍처**: 상위 모듈이 하위 모듈을 의존하는 단방향 구조
- **공통 기능 분리**: 횡단 관심사는 core 모듈에서 관리

#### 모듈별 역할

| 모듈 | 역할 | 의존성 | 주요 기능 |
|------|------|--------|----------|
| **app** | 메인 애플리케이션 | admin, api, domain, core, common | 전체 모듈 통합 실행 |
| **admin** | 관리자 시스템 | core, common | 관리자 대시보드, 계정 관리 |
| **api** | REST API 계층 | domain, core, common | 컨트롤러, API 엔드포인트 |
| **domain** | 비즈니스 로직 | core, common | 도메인 엔티티, 서비스 로직 |
| **core** | 공통 핵심 기능 | common | 보안, AOP, 예외처리, AWS |
| **common** | 공통 유틸리티 | 없음 | 설정, DTO, 열거형, 유틸리티 |

## 🧪 API 테스트

### Swagger UI를 통한 API 테스트
- **배포**: https://api-maidlab.duckdns.org/swagger-ui/index.html
- 애플리케이션 실행 후 Swagger UI에서 각 API 엔드포인트를 테스트할 수 있습니다.


## 💡 향후 개선사항

### 기능 개선
- [ ] 고급 매칭 알고리즘 개발
- [ ] admin 대시보드 도식화
- [ ] 모바일 앱 푸시 알림 연동

### 성능 개선
- [ ] 데이터베이스 쿼리 최적화
- [ ] Redis 캐싱 전략 도입 
- [ ] API 응답 시간 최적화
- [ ] 대용량 트래픽 처리 개선
- [ ] 매칭 로직 이벤트 드리븐 아키텍처 도입
  - 메시지 큐 (RabbitMQ/Apache Kafka) 사용
  - 비동기 매칭 처리로 성능 최적화
  - 이벤트 소싱 패턴 적용

### 시스템 개선
- [ ] 배치 시스템 도입
  - Spring Batch 활용 대용량 데이터 처리
  - 정산 자동화 배치 작업
  - 통계 데이터 생성 자동화
- [ ] 데이터베이스 최적화
  - 월별 테이블 파티셔닝 (예약, 정산 데이터)
  - 통계 전용 테이블 구현
  - 인덱스 최적화 및 쿼리 성능 개선

### 보안 강화
- [ ] 민감 정보 암호화 강화
- [ ] 보안 감사 로그 추가
- [ ] API Rate Limiting 구현

## 👥 개발팀

**MAIDLAB Backend Team**  
Kernel360 5기

### 팀원 구성
- **팀장**: 이소은
- **팀원**: 김진성, 조승현, 변재호

---

**문의사항이나 버그 리포트는 [GitHub Issues](https://github.com/Kernel360/KBE5_MAIDLAB_BE/issues)를 통해 제보해 주세요.**
