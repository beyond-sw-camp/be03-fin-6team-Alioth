# be03-fin-6team-Alioth

---

# 보험회사 영업 관리 시스템 : ALIOTH

#### 팀장: ⛏️ 조경남  ⛏️
#### 과장: 🗡️ 박성준  🗡️
#### 사장: 🛸 조훈  🛸   
#### 회장: 🐤 한희준 🐤

---

### [프로젝트 개요 PDF](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/ALIOTH_2024.03.15_%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%EA%B0%9C%EC%9A%94.pdf)

---

### [요구사항 정의서 PDF](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/ALIOTH_2024.03.22_요구사항정의서.pdf)

---

### [프로젝트 WBS PDF](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/ALIOTH_2024.03.20_WBS.pdf)

---

### Alioth DB ERD
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/AliothERD.png)

---

### Alioth Page Flow Chart
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/AliothFlowChart.png)

---

### Alioth Wire Frame
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/화면구성도.png)

### [화면 설계도 PDF](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/ALIOTH_2024.03.22_화면설계도.pdf)


---

### Alioth API

### [API 명세서 PDF](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/ALIOTH_2024.04.05_API명세서.pdf)

#### 알림 기능 : FCM (완료)
#### 엑셀 다운로드 기능 : Apache POI (구현 완료 후 테스트 중)
#### 대용량 업데이트 : batch (멘토링 후 구조 변경)

---


### Alioth TEST

단위 테스트 폴더

![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/TEST0405/테스트1.png)

![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/TEST0405/테스트2.png)

모두 통과하였고 그중 2가지

![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/TEST0405/계약테스트.png)

![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/TEST0405/전체달성테스트.png)

---

### [Alioth UI/UX TEST PDF](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/ALIOTH_2024.04.24_UI_UX.pdf) 


---


### [Alioth CI / CD]

## 목표 및 범위:
- 메인 서비스, 통계서비스 다중 서비스로 구성하고
  쿠버네티스를 활용하여 배포해 대규모 데이터 및 트래픽 대응을 목표로 함.

<br>

## 환경 및 도구:
- Github Action, Docker, ECR, Kubernetes

<br>

## 빌드 및 배포:
  - 빌드 스크립트: Gradle -> jar
  - 자동화 스크립트: jar -> Action.yml 통해 배포

<br>

## 배포 전략:
- Bule-Green 배포 전략

<br>

### 단계:

1. **GitHub 저장소 체크아웃**

2. **jar 만들기**

3. **AWS 자격 증명 구성**

4. **클러스터 정보 업데이트**

5. **Amazon ECR (이미지 저장소) 로그인**

6. **Docker 이미지 빌드 및 푸시**

7. **Kubernetes에 서비스 적용 및 배포 재시작**