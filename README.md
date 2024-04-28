# 🎞️ ticker-bell
> 각종 공연 예매 및 알림을 받아 볼 수 있는 서비스

<br><br><br><br>

## 🎯프로젝트 목표
아래와 같은 내용들을 학습하기 위해 진행한 학습 목적의 협업 프로젝트입니다.
- Spring Batch를 활용한 일괄 처리
- 통합테스트, 단위테스트를 작성하고 Jacoco를 활용하여 커버리지 측정
- Docker, Github Action을 활용해 CI/CD 구축
- Nginx의 Blue/Green 무중단 배포
- Spring Security, Oauth2.0 을 활용한 인증, 인가 구현
- 불필요한 어노테이션을 제거해 최적화
- 데이터베이스 및 아키텍처 설계
- 백엔드 팀원과 컨벤션을 지키며 협업하기
<br><br><br><br>

## 📌 주요 기능
### 회원
* Spring Security, JWT, Oauth2.0을 활용한 소셜 로그인
* Access Token, Refresh Token을 활용한 자동 로그인
* SMS 문자 인증
* 비회원, 예매자, 등록자 구분

### 공연
* 공연 등록, 수정
* naver map api를 활용하여 네비게이션 기능 제공
* 남은 좌석, 선택 좌석 선택
* AWS S3 이미지 업로드

### 예매
* 비회원 예매 / 회원 예매 분리
* 회원 예매일 경우 할인 적용
* 예매 내역 조회, 취소, 환불

### 알림
* 예매 내역 하루 전 SMS로 예매 정보 일괄 전송
* 서비스 내 실시간 알림(SSE)를 통해 공연 시작 전 알림
* 각종 결제 관련 알림

<br><br><br><br>

## 🗂 디렉토리 구조
```markdown
jikgong
|-- 📂domain
    └-- 📂alarm
    └-- 📂casting
    └-- 📂common
    └-- 📂emitter
    └-- 📂event
    └-- 📂host
    └-- 📂image
    └-- 📂map
    └-- 📂member
    └-- 📂selectedSeat
    └-- 📂sms
    └-- 📂specialseat
    └-- 📂tag
    └-- 📂ticketing
    └-- 📂utils
|-- 📂global
    |-- 📂batch
    |-- 📂config
    |-- 📂dto
    |-- 📂exception
    |-- 📂graphql
    |-- 📂security
```
<br><br><br><br>

## 🛠️ Architecture
![image](https://github.com/dgjinsu/tickerBell-back/assets/97269799/8aa03e39-be80-431f-b243-60647b052036)


<br><br><br><br>

## 🛠️ ERD
![image](https://github.com/dgjinsu/tickerBell-back/assets/97269799/aa91e9ae-fa24-48e2-927d-abb07e687573)


## 팀원
backend: [김진수](https://github.com/dgjinsu), [최준혁](https://github.com/cjunhyeok)
<br>
frontend: [이경주](https://github.com/jiimy), [이인국](https://github.com/inggu96)
