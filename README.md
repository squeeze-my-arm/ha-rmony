![header](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=header&text=하-ㄹ모니%20(Har-mony)&fontSize=40)

------------

## 8좀주멀러조 - 하-ㄹ모니
🤝 Spring_6기 A반 8조 프로젝트 협업 도구 하-ㄹ모니(Har-mony) 🤝

------------

### ⚙️ 개발 환경
- `Java JDK 17.0.7`
- `MySQL Server 8.0`
- `JPA`

### 🔧 사용한 Tool
<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Java-007396?&style=flat&logo=Java&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Spring-6DB33F?&style=flat&logo=spring&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" style="margin-right: 10px;"/>
  <img src="https://img.shields.io/badge/ApachetTomcat-F8DC75?style=flat&logo=apachetomcat&logoColor=white"style="margin-right: 10px;"/>
  <img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Github-181717?style=flat&logo=github&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Intellijidea-000000?style=flat&logo=intellijidea&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white" style="margin-right: 10px;">
</div> 
<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white" style="margin-right: 10px;"/>
	<img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white" style="margin-right: 10px;" />
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=white" />
</div>

------------

### 🚩 프로젝트 소개
#### 하-ㄹ모니 (Ha-rmony)
> "프로젝트 협업 도구"
>  - 조화롭게 협업하라는 의미
>  - 팀원 모두가 조화롭게 협업할 수 있도록 도와주는 협업 툴
>  - 칸반 보드 형태로 프로젝트 관리와 작업 흐름 관리  
  
------------

### 📍 하-ㄹ모니 기능 소개

#### 1) 사용자 관리
- 로그인 / 회원가입
- 사용자 정보 수정 및 삭제
    
#### 2) 보드 관리
- 보드 생성
	- 보드를 생성할 수 있습니다.
- 보드 수정
	- 보드의 이름, 보드의 색상, 보드의 설명을 수정할 수 있습니다.
- 보드 삭제
	- 생성한 사용자만 보드를 삭제할 수 있습니다.
- 보드 초대
	- 특정 사용자들을 보드에 초대해 협업할 수 있습니다.
     
#### 3) 컬럼 관리
- 컬럼 생성
	- 보드 내부에 컬럼을 생성할 수 있습니다.
- 컬럼 수정
	- 컬럼 이름을 수정할 수 있습니다.
- 컬럼 삭제
	- 보드의 참여자라면 컬럼을 삭제할 수 있습니다.
- 컬럼 순서 이동
	- 컬러의 순서를 자유롭게 이동할 수 있습니다.
 
#### 4) 카드 관리
- 카드 생성
	- 컬럼 내부에 카드를 생성할 수 있습니다
- 카드 수정
	- 카드의 이름, 카드의 설명, 카드의 색상을 수정할 수 있습니다.
    - 카드에 작업자를 할당하거나, 작업자를 변경할 수 있습니다.
- 카드 삭제
	- 카드를 삭제할 수 있습니다.
- 카드 이동
	- 같은 컬럼 내에서 카드의 순서를 변경할 수 있습니다.
    - 카드를 다른 컬럼으로도 이동할 수 있습니다.

#### 5) 카드 상세
- 댓글 달기
	- 협업하는 사람들끼리 카드에 대해 이야기를 나눌 수 있습니다.
- 날짜 지정
	- 카드에 마감일을 설정하고 관리할 수 있습니다.

------------

### 📜 API 명세서

<details>
<summary>회원 API</summary>
<p align="center"><img src="https://velog.velcdn.com/images/azuressu/post/3696ec77-91fd-43bf-b3ef-d8d30d0ba079/image.png" width="700"/></p>
</details>

<details>
<summary>보드 API</summary>
<p align="center"><img src="https://velog.velcdn.com/images/azuressu/post/e36f1c45-8ff6-4581-9ffe-7073e42ce5d3/image.png" width="700"/></p>
</details>

<details>
<summary>컬럼 API</summary>
<p align="center"><img src="https://velog.velcdn.com/images/azuressu/post/854564d9-92fd-4100-96cb-19dd8142269d/image.png" width="700"/></p>
</details>

<details>
<summary>카드 API</summary>
<p align="center"><img src="https://velog.velcdn.com/images/azuressu/post/692e1598-9a27-4234-9ce0-8c9c5a214a3b/image.png" width="700"/></p>
</details>


### 📃 ERD 테이블
<details>
<summary>ERD 관계도</summary>
<p align="center"><img src="https://github.com/squeeze-my-arm/ha-rmony/assets/74248726/54cbe61d-52f6-4ac6-b2df-d1991e67cfe5" width="800"/></p>
</details>

### 📂 패키지 구성

<details>
<summary>패키지</summary>
<pre>
<code>
├── aop
│   ├── BoardUserCheck.java
│   ├── UserCheck.java
│   └── UserCheckAop.java
├── board
│   ├── Board.java
│   ├── BoardController.java
│   ├── BoardRepository.java
│   ├── BoardRequestDto.java
│   ├── BoardResponseDto.java
│   └── BoardService.java
├── boardColumn
│   ├── BoardColumn.java
│   ├── BoardColumnController.java
│   ├── BoardColumnRepository.java
│   ├── BoardColumnRequestDto.java
│   ├── BoardColumnResponseDto.java
│   ├── BoardColumnService.java
│   └── BoardColumnServiceImpl.java
├── boardUser
│   ├── BoardUser.java
│   ├── BoardUserController.java
│   ├── BoardUserEnum.java
│   ├── BoardUserRepository.java
│   ├── BoardUserReponseDto.java
│   └── BoardUserService.java
├── card
│   ├── Card.java
│   ├── CardController.java
│   ├── CardInColumnReponseDto.java
│   ├── CardOrderRequestDto.java
│   ├── CardRepository.java
│   ├── CardRequestDto.java
│   ├── CardRequestUserDto.java
│   ├── CardResponseDto.java
│   └── CardService.java
├── cardUser
│   ├── CardUser.java
│   ├── CardUserRepository.java
│   └── CardUserReponseDto.java
├── comment
│   ├── Comment.java
│   ├── CommentController.java
│   ├── CommentRepository.java
│   ├── CommentRequestDto.java
│   ├── CommentResponseDto.java
│   └── CommentService.java
├── common
│   ├── ApiReponseDto.java
│   ├── PasswordConfig.java
│   ├── RestTemplateConfig.java
│   ├── Timestamped.java
│   ├── ViewController.java
│   └── WebSecurityConfig.java
├── email
│   ├── EmailSender.java
│   ├── EmailSenderController.java
│   └── EmailSenderService.java
├── security
│   ├── JwtAuthorizationFilter.java
│   ├── JwtUtil.java
│   ├── UserDetailImpl.java
│   └── UserDetailServiceImpl.java
├── social
│   ├── GoogleService.java
│   ├── GoogleUserInfo.java
│   └── SocialController.java
└── user
     ├── LoginRequestDto.java
     ├── SignupRequestDto.java
     ├── User.java
     ├── UserController.java
     ├── UserRepository.java
     ├── UserResponseDto.java
     ├── UserService.java
     └── UserUpdateRequestDto.java
</code>
</pre>
</details>

------------

### 👩‍💻 팀원 역할
- 김나형: 보드 관리
- 심형철: 카드 관리
- 이상인: 컬럼 관리
- 이수연: 카드 관리

### 📆 진행 일정
<details>
<summary>일정 정리</summary>
  <p>- 8/07 : 프로젝트 내용 정리, S.A 작성 및 제출, 역할 분담</p>
  <p>- 8/08 : 1차 확인</p>
  <p>- 8/09 : 완료된 부분까지 코드 취합하며 테스트</p>
  <p>- 8/10 : 2차 확인 </p>
  <p>- 8/11 : 3차 확인</p>
  <p>- 8/12 : 코드 취합, 발표 준비</p>
  <p>- 8/13 : 최종 확인 및 정리</p>
</details>


------------

![Footer](https://capsule-render.vercel.app/api?type=waving&color=auto&height=200&section=footer)
