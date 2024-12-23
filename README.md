![readme_프론트엔드_v3](https://github.com/user-attachments/assets/8e48b5ce-a9b6-49dd-8a27-6e3afe2b69d7)

# 📌 Quostomize-BE: 우리 커스터마이징
## 📝 프로젝트 소개

**우리 커스터마이징(QUOSTOMIZE)** 은 사용자가 매달 자신의 생활 패턴과 취향에 맞게 카드 혜택과 포인트 사용처를 직접 선택할 수 있는 서비스입니다.

**사용자가 직접 결정하는 맞춤형 혜택 제공**을 통해 기존 카드 서비스의 한계를 뛰어넘고, 변화하는 소비 트렌드에 유연하게 대응합니다.

### 👉🏻 [시연영상 바로가기](https://youtu.be/4sCnBonI3yI)
### 👉🏻 [사이트 바로가기](https://quostomizecard.site/home)
<br>


## 🚀 주요 설계 방향 
본 프로젝트는 **서비스 보안**과 **안정성 강화**를 주요 설계 방향으로 두고 개발되었습니다.

**1. 보안과 안정성 강화**
  - Next.js + Auth.js
    - 토큰을 브라우저 쿠키 대신 서버 세션에 저장하여 데이터 직접 노출 방지
  - Next.js API Route
    - 클라이언트-백엔드 간 직접 통신을 차단해 헤더 정보 및 API 주소 보호
  - JWT 보안 강화
    - 비밀번호는 단방향 암호화, 개인정보는 양방향 암호화 적용
    - Acess/Refresh Token 검증 및 Blacklist 로직 추가로 보안 수준 향상
  - AWS WAF 적용
    - AWS WAF 적용으로 SQL Injection, XSS 공격 예방

**2. 멱등성 적용**
  - Redis를 활용한 멱등키 관리로 카드 생성 요청 중복 처리 방지
  - 동일한 요청은 캐시된 응답 반환으로 효율성과 안정성 확보

**3. 대량 데이터 처리**
  - 배치 프로세스: 복권 응모 데이터를 매일 정해진 시간에 처리
    - Redaer: 1000명 데이터 읽어오기
    - Processor: 응모자 중 당첨자 선정
    - Writer: 당첨 결과 기록

**4. 코드 품질 관리**
  - SonarQube를 통한 정적 코드 분석으로 코드 품질 유지
  - DB Lock으로 동시성 문제 해결
  - 비동기 처리 강화로 안정적인 예외 처리 구현
  - JPA 활용 중 발생하는 N + 1 문제 예방

<br>

## 🔧 주요 기능
**1. 카드 혜택 - 혜택 선택의 자유**
![34](https://github.com/user-attachments/assets/169c99d9-17cd-4d44-b4b2-476fafd96a86)

   - 상위분류 혜택: 5가지 상위분류 선택 시 모든 가맹점에서 3% 적립
   - 맞춤형 혜택: 세부 가맹점 그룹 선택 시 최대 4% 적립
   - 유연한 변경: 30일마다 혜택 변경 가능

**2. 포인트 사용처 - 포인트 사용의 다양성**
![33](https://github.com/user-attachments/assets/232873f0-1275-4ef4-a2e6-3441331fd3a8)

   - 페이백: 카드 결제일에 포인트를 현금처럼 사용
   - 조각투자: 원하는 주식을 설정하고 포인트로 주식 매수
   - 일일복권: 매일 자정 추첨으로 최대 1만 포인트 지급

**3. 카드 생성 - 실제 카드 생성 프로세스와 멱등성을 적용한 생성 기능**
![커스터 마이징 서비스 (2)](https://github.com/user-attachments/assets/21f776ea-d0e6-4ff1-86a2-b4ab0807902b)

**4. 주식 기능**
![커스터 마이징 서비스 (3)](https://github.com/user-attachments/assets/cd6526f6-cc32-4161-a033-87d44d88365e)

   - RestClient를 활용한한국투자증권 OPENAPI와 연동
   - Access Token 발급 & 보유 주식 정보 기능
   - S3를 활용한 주식 이미지 다운로드

**5. 복권 기능**
![커스터 마이징 서비스 (4)](https://github.com/user-attachments/assets/5640c409-79a2-42f8-84c9-dc1ab16c1a29)

   - Spring Batch를 활용해 복권 기능을 활성화한 사용자 집계
   - 자정(00:00)에 1/1000 확률로 포인트 획득


<br>

---

![readme_관리자_v3](https://github.com/user-attachments/assets/288e1653-9493-4a24-ba00-1ce1fd54897b)
# 📌 Quostomize-admin: 관리자 페이지
## 📝 프로젝트 소개
우리 커스터마이징 서비스(QUOSTOMIZE)를 위한 관리자 페이지입니다.
<br>
정보 조회, 알림 메일 발송, 서비스 및 로그 관리, 실시간 모니터링 기능을 통해 서비스를 효율적으로 관리합니다.
### 👉🏻 [관리자 페이지 바로가기](https://quostomize-admin.vercel.app/)
<br>

## 🚀 주요 설계 방향 
본 프로젝트는 **서비스 안정성**과 **효율성 증대**를 주요 설계 방향으로 두고 개발되었습니다.
- **Authentication + Role 기반 Admin 여부 확인**: 로그인 시 사용자 인증 후, 관리자인지 확인하는 절차를 구현하여 권한을 제어합니다.
- **MiddleWare + API 내부 Role 체크**: 중간 미들웨어에서 API 접근 시 역할을 확인하여 이중 인증을 제공합니다.
- **만료 토큰 갱신 및 예외 처리**: 토큰 기반 인증을 적용하여 토큰 만료 시 갱신하고 예외 처리를 통해 안정적인 인증 프로세스를 유지합니다.
- **역할 기반 접근 제어**: **Admin**만 민감한 데이터에 접근할 수 있도록 하여 보안을 강화했습니다.
<br>

## 🔧 주요 기능
- **정보 조회**: 관리자가 다양한 정보(이용자, 카드, 가맹점 등)를 조회할 수 있습니다.
- **알림 메일 발송**: 관리자가 이메일을 통해 알림을 발송할 수 있는 기능입니다.
![커스터 마이징 서비스 (5)](https://github.com/user-attachments/assets/f9334aa2-84f1-4ad4-81fd-7540a9389de2)


- **서비스 관리**: 이용자, 카드, 가맹점 관리 기능을 제공합니다.
![커스터 마이징 서비스 (6)](https://github.com/user-attachments/assets/1d3c9ccf-d4eb-473a-8656-a91c3b1b8ced)


- **로그 관리**: MDC(Mapped Diagnostic Context) 필터를 적용하여 로그를 순차적으로 관리하고 분석할 수 있습니다.
![53](https://github.com/user-attachments/assets/95f2690a-ea8e-4560-8123-2c48c2ab60f8)

![54](https://github.com/user-attachments/assets/3473ca5d-82a8-46d9-971e-89c27271a158)


<br>

## 🌟 추가 구현사항 
- **모니터링**: Grafana를 통해 시스템 상태를 실시간으로 모니터링하고 문제를 즉시 대응할 수 있도록 합니다.
![커스터 마이징 서비스 (7)](https://github.com/user-attachments/assets/649bf739-7335-4c6d-a5a0-f8a64315e1d0)


<br>


## ⚙️ 기술 스택
![커스터 마이징 서비스](https://github.com/user-attachments/assets/e39930b0-e8e0-450d-a3cb-465df691d9ef)
<br>

## 🌐 백엔드 배포 파이프라인
백엔드는 Github Actions와 AWS를 활용하여 배포를 진행하였습니다. 자동화된 배포 과정을 통해 안정적인 서비스 운영을 지원합니다.
<br>

1. **커밋 푸시**  
   - 개발 중인 코드는 `dev` 브랜치에 푸시됩니다.  
   - `dev` 브랜치는 배포 및 QA를 위한 작업 브랜치입니다.  
2. **Pull Request 생성**  
   - 배포를 위해 `dev` 브랜치에서 `main` 브랜치로 Pull Request를 생성합니다.  
3. **Github Actions 실행**  
   - Pull Request 생성 시 **Github Actions**가 자동으로 트리거됩니다.  
   - 다음 작업이 순차적으로 진행됩니다:  
     - `yml` 파일 생성과 백엔드 코드 빌드  
     - **Docker 이미지 생성**  
4. **Docker 이미지 배포**  
   - 생성된 Docker 이미지는 AWS ECR (Elastic Container Registry)에 Push됩니다.  
5. **EC2 서버 배포**  
   - **EC2 인스턴스**가 ECR에서 최신 Docker 이미지를 Pull하여 업데이트를 진행합니다.  
   - 업데이트된 이미지를 통해 서비스가 배포됩니다.  
<br>  
위 과정을 통해 코드 푸시부터 배포까지의 작업이 자동화되어 빠르고 효율적으로 운영되고 있습니다.  

## 🖥️ 인프라 구조도
![image](https://github.com/user-attachments/assets/aeb76baa-ece2-40fd-8ed6-18205d223d69)

## 🖥️ ERD
![커스터 마이징 서비스 (1)](https://github.com/user-attachments/assets/a312fc9f-7c6f-47cd-b9e7-c6fff3264214)



## 🗂️ 주요 폴더 구조
**백엔드**
```
└── quostomizebe/
    ├── api/
    │   ├── admin/
    │   ├── adminResponse/
    │   ├── auth/
    │   ├── card/
    │   ├── cardBenefit/
    │   ├── cardApplicant/
    │   ├── health/
    │   ├── lotto/
    │   ├── member/
    │   ├── memberQuestion/
    │   ├── payment/
    │   ├── pointUsageMethod/
    │   ├── sms/
    │   └── stock/
    ├── common/
    │   ├── aspects/
    │   ├── auth/
    │   ├── config/
    │   ├── dto/
    │   ├── email/
    │   ├── entity/
    │   ├── error/
    │   ├── filter/
    │   ├── idempotency/
    │   ├── jwt/
    │   ├── s3/
    │   └── sms/
    ├── domain/
    │   ├── admin/
    │   ├── auth/
    │   ├── customizer/
    │   │   ├── adminResponse/
    │   │   ├── benefit/
    │   │   ├── card/
    │   │   ├── cardBenefit/
    │   │   ├── cardApplication/
    │   │   ├── customer/
    │   │   ├── lotto/
    │   │   ├── memberQuestion/
    │   │   ├── payment/
    │   │   ├── point/
    │   │   ├── pointUsageMethod/
    │   │   └── stock/
    │   └── log/
    └── QuostomizeBeApplication.java
```
<br>

## 📅 진행 일정 (39Days)
- **프로젝트 시작일**: 2024.11.11
- **프로젝트 종료일**: 2024.12.09
<br>

### ⌛ 상세 일정
- **메인 기능 API**: 2024.11.07 - 2024.11.15
- **배치 API**: 2024.11.01 - 2024.11.15
- **관리자 페이지 API**: 2024.12.02 - 2024.12.09
<br>


## ✍️ 컨벤션
**커밋 컨벤션**
- {Tag}/{작업 내용}
```
Feat/input : 비밀번호 숨김 처리
```
- 커밋 규칙
<table>
  <thead>
    <tr>
      <th>Tag Name</th>
      <th>Description</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>Feat</td>
      <td>새로운 기능을 추가</td>
    </tr>
    <tr>
      <td>Fix</td>
      <td>버그 수정</td>
    </tr>
    <tr>
      <td>Design</td>
      <td>CSS 등 사용자 UI 디자인 변경</td>
    </tr>
    <tr>
      <td>!BREAKING CHANGE</td>
      <td>커다란 API 변경의 경우</td>
    </tr>
    <tr>
      <td>!HOTFIX</td>
      <td>치명적인 버그 긴급 수정</td>
    </tr>
    <tr>
      <td>Style</td>
      <td>코드 포맷 변경, 세미콜론 누락 등</td>
    </tr>
    <tr>
      <td>Refactor</td>
      <td>프로덕션 코드 리팩토링</td>
    </tr>
    <tr>
      <td>Comment</td>
      <td>주석 추가 및 변경</td>
    </tr>
    <tr>
      <td>Docs</td>
      <td>문서 수정</td>
    </tr>
    <tr>
      <td>Test</td>
      <td>테스트 코드 추가 또는 수정</td>
    </tr>
    <tr>
      <td>Chore</td>
      <td>빌드 업무 수정 및 패키지 관리 업데이트</td>
    </tr>
    <tr>
      <td>Rename</td>
      <td>파일/폴더명 수정</td>
    </tr>
    <tr>
      <td>Remove</td>
      <td>파일/폴더 삭제</td>
    </tr>
  </tbody>
</table>
<br>

## 🧑‍🤝‍🧑 팀원 소개
<table>
  <tr>
    <td align="center">
      <a href="https://github.com/Kee0304">
        <img src="https://github.com/Kee0304.png" alt="기남석" width="150" height="150"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/newgamer11">
        <img src="https://github.com/newgamer11.png" alt="김영성" width="150" height="150"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kimh7537">
        <img src="https://github.com/kimh7537.png" alt="김현우" width="150" height="150"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/bangsk2">
        <img src="https://github.com/bangsk2.png" alt="방성경" width="150" height="150"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/seonmin5">
        <img src="https://github.com/seonmin5.png" alt="오선민" width="150" height="150"/>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/hcu55">
        <img src="https://github.com/hcu55.png" alt="홍찬의" width="150" height="150"/>
      </a>
    </td>
  </tr>
   <tr>
    <td align="center">
      <a href="https://github.com/Kee0304">
        <b>기남석</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/newgamer11">
        <b>김영성</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/kimh7537">
        <b>김현우</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/bangsk2">
        <b>방성경</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/seonmin5">
        <b>오선민</b>
      </a>
    </td>
    <td align="center">
      <a href="https://github.com/hcu55">
        <b>홍찬의</b>
      </a>
    </td>
  </tr>
  <tr>
   <td align="center">총괄 팀장<br/>Frontend 팀장<br/>FullStack 개발</td>
   <td align="center">FullStack 개발 팀원</td>
   <td align="center">Backend 팀장<br/>FullStack 개발</td>
   <td align="center">FullStack 개발 팀원</td>
   <td align="center">PM 팀장<br/>FullStack 개발 팀원</td>
   <td align="center">FullStack 개발 팀원</td>
 </tr>
  <tr>
    <td align="center">
      스프링 배치 복권 기능 구현 <br>
      내용 입력 <br>
      내용 입력
    </td>
    <td align="center">
      내용 입력 <br>
      내용 입력 <br>
      내용 입력
    </td>
    <td align="center">
      인증, 인가 구현 <br>
      CICD & 인프라 구축 <br>
      백엔드 프로젝트 세팅
    </td>
    <td align="center">
      내용 입력 <br>
      내용 입력 <br>
      내용 입력
    </td>
    <td align="center">
      내용 입력 <br>
      내용 입력 <br>
      내용 입력
    </td>
    <td align="center">
      MDC 로깅 구현 <br>
      내용 입력 <br>
      내용 입력
    </td>
  </tr>
</table>

<br>

## 👥 팀원 개인별 회고

### 기남석
- ~
  
### 김영성
- ~

### 김현우
- ~

### 방성경
- ~

### 오선민
- ~

### 홍찬의
- ~

<br>

## 🔗 관련 문서 링크
- [Quostomize-FE](https://github.com/woorifisa-projects-3rd/Quostomize-FE)
- [Quostomize-admin](https://github.com/woorifisa-projects-3rd/Quostomize-admin)
- [HeadlessUI](https://headlessui.com/)
