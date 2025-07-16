# MaidLab - 생활 서비스 매칭 플랫폼

## 📄 프로젝트 소개

MaidLab은 바쁜 현대인의 일상을 지원하기 위해 청소, 베이비시터, 반려동물 케어 등의 생활 서비스를 제공하고, 고객(수요자)과 매니저(공급자)를 스마트하게 매칭하는 종합 생활 서비스 플랫폼입니다.

고객은 필요한 서비스를 손쉽게 예약하고, 매니저는 자신의 전문성을 바탕으로 안정적인 수익을 창출할 수 있도록 도와주는 양방향 플랫폼입니다.

## 🌐 배포 사이트

**프로덕션 URL**: https://www.maidlab.site/

## 🔗 GitHub Repository

https://github.com/Kernel360/KBE5_MAIDLAB_BE

## 🛠️ 기술 스택

### Backend
- **Java 21** - 최신 LTS 버전
- **Spring Boot 3.4.5** - 웹 애플리케이션 프레임워크
- **Spring Security** - JWT 기반 인증 및 보안
- **Spring AOP** - 횡단 관심사 처리
- **Spring Data JPA** - ORM 및 데이터 접근
- **QueryDSL** - 타입 안전한 쿼리 작성

### Database & Storage
- **MySQL** - 메인 데이터베이스
- **Redis** - 캐시 및 세션 스토리지
- **AWS S3** - 파일 저장소
- **AWS CloudFront** - CDN

### Architecture
- **멀티모듈 아키텍처** - 모듈별 관심사 분리
- **RESTful API** - HTTP 기반 API 설계

## ⚙️ 설치 및 실행 방법

### 사전 요구사항
- Java 21
- MySQL 8.0+
- Redis
- Git

### 1. 프로젝트 클론
```bash
git clone https://github.com/Kernel360/KBE5_MAIDLAB_BE.git
cd KBE5_MAIDLAB_BE
```

### 2. 데이터베이스 설정
```sql
-- MySQL 데이터베이스 생성
CREATE DATABASE maidlab CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### 3. 환경 변수 설정
```bash
# 로컬 개발 환경 변수
export DB_LOCAL_USERNAME=your_username
export DB_LOCAL_PASSWORD=your_password
export DB_LOCAL_DATABASE=maidlab
export DB_LOCAL_PORT=3306

export AWS_S3_ACCESSKEY=your_aws_access_key
export AWS_S3_SECRETKEY=your_aws_secret_key

export JWT_SECRET_KEY=your-super-secret-jwt-key-256-bits-minimum
export JWT_HEADER=Authorization
export JWT_PREFIX="Bearer "
export JWT_EXPIRATION_ACCESS=3600000
export JWT_EXPIRATION_REFRESH=604800000

export GOOGLE_CLIENT_ID=your_google_oauth_client_id
export GOOGLE_CLIENT_SECRET=your_google_oauth_client_secret
export GOOGLE_REDIRECT_URL=http://localhost:8080/api/auth/oauth/google
```

### 4. 애플리케이션 실행
```bash
# 프로젝트 빌드
./gradlew build

# 애플리케이션 실행
./gradlew :app:bootRun
```

### 5. API 문서 확인
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html

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
- 프로필 등록 및 인증 서류 제출
- 서비스 가능 지역 및 시간 설정
- 예약 요청 승인/거절
- 체크인/체크아웃 처리
- 정산 내역 조회
- 고객 리뷰 관리
- 실시간 알림 수신

### 👨‍💼 관리자 (Admin) 기능
- 대시보드 및 통계 조회
- 고객/매니저 계정 관리
- 예약 및 매칭 현황 모니터링
- 정산 관리 및 승인
- 이벤트 및 공지사항 관리
- 게시판 관리 및 답변
- 실시간 로그 모니터링

## 📝 API 엔드포인트

### 주요 API 개요

| 카테고리 | 기본 경로 | 주요 기능 | 인증 |
|---------|----------|----------|------|
| 인증 | `/api/auth` | 회원가입, 로그인, OAuth | JWT |
| 고객 | `/api/consumers` | 프로필, 선호도 관리 | CONSUMER |
| 매니저 | `/api/manager` | 프로필, 스케줄 관리 | MANAGER |
| 예약 | `/api/reservations` | 예약 생성, 관리, 리뷰 | JWT |
| 매칭 | `/api/matching` | 매니저 매칭 | JWT |
| 포인트 | `/api/point` | 충전, 사용, 이력 | JWT |
| 알림 | `/api/notifications` | 실시간 알림 (SSE) | JWT |
| 게시판 | `/api/board` | 문의, 답변 | JWT |
| 파일 | `/api/files` | S3 업로드 | JWT |

**상세 API 문서**: [Swagger UI](http://localhost:8080/swagger-ui/index.html)

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
├── core/                   # 공통 기능 모듈
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

### 모듈별 역할
- **app**: 메인 애플리케이션, 모든 모듈을 조합하여 실행
- **admin**: 관리자 전용 기능 및 API
- **api**: REST API 컨트롤러 계층
- **domain**: 비즈니스 로직 및 도메인 엔티티
- **core**: 공통 기능 (보안, AOP, 예외처리 등)
- **common**: 공통 유틸리티 및 설정

## 🧪 테스트 방법

### 단위 테스트 실행
```bash
# 전체 테스트 실행
./gradlew test

# 특정 모듈 테스트 실행
./gradlew :domain:test
./gradlew :api:test
```

**참고**: 현재 `app` 모듈의 테스트는 비활성화되어 있습니다.

### API 테스트
- **Swagger UI**: http://localhost:8080/swagger-ui/index.html
- 애플리케이션 실행 후 Swagger UI에서 각 API 엔드포인트를 테스트할 수 있습니다.

### 테스트 데이터
- `app/src/main/resources/data.sql` 파일에 초기 데이터가 정의되어 있습니다.

## 💡 향후 개선사항

### 기능 개선
- [ ] 실시간 채팅 기능 추가
- [ ] 매니저 위치 기반 추천 시스템
- [ ] 고급 매칭 알고리즘 개발
- [ ] 모바일 앱 푸시 알림 연동
- [ ] 결제 시스템 다양화

### 성능 개선
- [ ] 데이터베이스 쿼리 최적화
- [ ] 캐싱 전략 개선
- [ ] API 응답 시간 최적화
- [ ] 대용량 트래픽 처리 개선

### 보안 강화
- [ ] API Rate Limiting 구현
- [ ] 민감 정보 암호화 강화
- [ ] CORS 정책 세밀화
- [ ] 보안 감사 로그 추가

## 👥 개발팀

**MaidLab Backend Team**  
Kernel360 5기 - KBE5 팀

---

**문의사항이나 버그 리포트는 [GitHub Issues](https://github.com/Kernel360/KBE5_MAIDLAB_BE/issues)를 통해 제보해 주세요.**
