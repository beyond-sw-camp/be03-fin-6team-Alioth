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

<details>
  <summary> Alioth CI / CD 계획 </summary>

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

</details>

---

<details>
  <summary> Alioth CI / CD </summary>

## Front-End 배포
1. Git Actions -> node.js 설치 및 배포에 필요한 환경세팅
2. npm install, build
3. aws 연결 후 S3에 배포
4. S3 -> CloudFront 연결
5. CloudFront -> Route 53 연결
6. Route 53 -> AWS Certificate Manager 를 이용해 SSL 인증

## Back-End 배포
1. Git Actions -> JDK 21 설치 및 배포에 필요한 환경세팅
2. gradlew 실행 권한주기
3. EKS 위한 kubectl 설치
4. aws 연결 후 EKS 클러스터 연결
5. ECR 연결
6. Git Actions Secrets 파일 연결
7. Dockerfile 사용해 이미지 빌드 및 ECR push
8. EKS Deployment 생성 및 실행
9. EKS Service 생성 및 실행
10. EKS Pod Auto Scaling 위한 HPA 생성 및 실행
11. ALB-Ingress-Controller 다운 및 실행
12. EKS ALB-Ingress(ALB) 생성 및 실행
13. ALB -> Route 53 연결
14. Route 53 -> AWS Certificate Manager 를 이용해 SSL 인증


## 배포 구조
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-deploy.png)

## kubectl Commend 
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-kubectl.png)

## 배포 중요 내용
1. EKS vpc 에 2~4개의 node 생성 ( 최소 2개, 최대 4개 EC2 Auto Scaling)
2. 노드에 pod 생성 및 한개 pod 에 spring boot 프로젝트 서비스 2개 실행 (server, statistics)
3. HPA (최소 2개, 최대 4개 pod resources 70% 이상이면 Pod Auto Scaling)
4. ALB-Ingress 를 사용해 IP로 pod 연결
5. pod 포트번호에 따라 다른 서비스 실행 (server, statistics)
6. pod 수가 증가하면 node 수가 증가할 수 있음
7. pod 수가 증가하면 ALB-Ingress-Controller 에서 인식해서 ALB 자동으로 pod 연결
8. 모든 pod 는 readinessProbe, livenessProbe를 사용하여 무중단 배포를 지향함
9. ALB-Ingress 또한 pod에 연결하기전에 healthcheck 를 하고 연결하여 끊김없는 서비스 연결을 지향함

## Git Actions

#### Front-End
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-front-actions.png)

#### Back-End
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-back-actions.png)

## 배포 자원

#### Front-End S3
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-S3.png)

#### Front-End CloudFront
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-CloudFront.png)

#### Front-End CloudFront Deploy
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-CloudFront-deploy.png)

------

#### Back-End ECR
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-ECR.png)

#### Back-End EKS
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-EKS.png)

#### Back-End EKS POD
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-pod.png)

#### Back-End EKS HPA (Horizontal Pod Autoscaling)
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-HPA.png)

#### Back-End EKS Deployment
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-deployment.png)

#### Back-End EKS Service
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-service.png)

#### Back-End Ingress
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-ingress.png)

#### Back-End alb
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-alb.png)

#### Back-End ALB-Ingress
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-ingress-alb.png)

#### Back-End Image File S3
![](https://github.com/beyond-sw-camp/be03-fin-6team-Alioth/blob/main/Docs/img/alioth-img-S3.png)

</details>