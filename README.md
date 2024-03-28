# 노인을 위한 세상은 있다.

<div style="text-align: right"><b>팀 에구구구</b></div>

[![since](https://github.com/jjj5306/CountryForOldMan/assets/74577707/d8bbcec9-77c0-4afd-90bf-a5321d1dec18)](https://github.com/jjj5306/CountryForOldMan)
[![dong](https://github.com/jjj5306/CountryForOldMan/assets/74577707/2cb1c1de-30dd-4799-ab90-4ec497559c45)](https://github.com/dwoo32)
[![hyun](https://github.com/jjj5306/CountryForOldMan/assets/74577707/afe7f4d7-25d3-422c-a5f8-017c5e69d6b5)](https://github.com/boxty123)
[![jo](https://github.com/jjj5306/CountryForOldMan/assets/74577707/a3e3bd12-19e5-4e1d-afd0-68f060f5ac90)
](https://github.com/jjj5306)
[![tae](https://github.com/jjj5306/CountryForOldMan/assets/74577707/10ca4088-e0d2-4d79-ab54-7e12e1233803)
](https://github.com/Taehw)

---

### [Docs](/docs/README.md)

### [커밋 메시지 규칙](#📜커밋-메시지-규칙)

<br><br><br>

# 📜커밋 메시지 규칙

## 커밋 메시지의 7가지 규칙

1. 제목과 본문을 빈 행으로 구분한다
2. 제목을 50글자 내로 제한
3. 제목 첫 글자는 대문자로 작성
4. 제목 끝에 마침표 넣지 않기
5. 제목은 명령문으로 사용하며 과거형을 사용하지 않는다
6. 본문의 각 행은 72글자 내로 제한
7. 어떻게 보다는 무엇과 왜를 설명한다

## 커밋 메시지 구조

- 헤더는 필수이며, 범위(scope), 본문(body), 바닥글(footer)은 선택사항이다.

  ```
  <type>(<scope>): <subject>          -- 헤더
  <BLANK LINE>
  <body>                              -- 본문
  <BLANK LINE>
  <footer>                            -- 바닥글
  ```

- <b>\<type></b>은 해당 커밋의 성격을 나타내며 아래 중 하나여야 한다.

  ```
  feat : 새로운 기능에 대한 커밋
  fix : 버그 수정에 대한 커밋
  build : 빌드 관련 파일 수정에 대한 커밋
  chore : 그 외 자잘한 수정에 대한 커밋
  ci : CI관련 설정 수정에 대한 커밋
  docs : 문서 수정에 대한 커밋
  style : 코드 스타일 혹은 포맷 등에 관한 커밋
  refactor :  코드 리팩토링에 대한 커밋
  test : 테스트 코드 수정에 대한 커밋
  ```

<br>

- <b>\<body></b>는 본문으로 헤더로 표현할 수 없는 상세한 내용을 적는다. 헤더로 표현이 가능하다면 생략 가능하다.

- <b>\<footer></b>는 바닥글로 어떤 이슈에서 왔는지 같은 참조 정보들을 추가하는 용도로 사용한다.<br>
  예를 들어 특정 이슈를 참조하려면 `close #1233` 과 같이 추가하면 된다.<br>
  close는 이슈를 참조하면서 main브랜치로 푸시될 때 이슈를 닫게 된다.

- <b>예시</b><br>
  <img src="https://github.com/jjj5306/CountryForOldMan/assets/74577707/20c6e9c7-dfce-4267-b4bc-bd8dd92979d9" width="50%" height="50%"/>

# Issue 규칙

Issue는 템플릿으로 만들어두어 실수를 방지하도록 한다.

- <b>제목</b>: [FE|BE] 이슈 제목

- 내용:

  ```
  ### 만들고자 하는 기능이 무엇인가요?
  ex) Todo 생성 기능

  ### 해당 기능을 구현하기 위해 할 일이 무엇인가요?
  1. [ ] Job1
  2. [ ] Job2
  3. [ ] Job3

  ### 예상 작업 시간
  ex) 3h
  ```

# 브랜치 생성 규칙

생성된 이슈를 기준으로 분기를 나누어 작업할거라 이슈 번호를 브랜치명으로 하기로 했다. 그리고 가능하면 merge 이후 브랜치는 삭제하는 것으로 협의.

- 브랜치명: **feat/#issue_number**
