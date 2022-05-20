![waving](https://capsule-render.vercel.app/api?type=waving&height=200&text=🎉아삼이삼_A106팀🎉&fontAlign=50&fontAlignY=40&color=gradient)


# 서비스 - Min'dLet

## 팀 소개

| 구분 | 이름   | 개발 파트  |
| ---- | ------ | :--------: |
| 팀장 | 안영진 |   백엔드   |
| 팀원 | 김경석 |   백엔드   |
| 팀원 | 이선민 | 프론트엔드 |
| 팀원 | 이윤기 | 백엔드 |
| 팀원 | 임건호 | 프론트엔드 |
| 팀원 | 진민규 | 프론트엔드 |

## 서비스 기획의도

 롤링페이퍼를 통하여 일상에 지친 소비자들에게 잠시 동화적 순간을 선물하여 힘이 되어 주는 것이 우리 웹사이트의 목표입니다. 평소 마음에 담아왔던 말을 정성스레 적어 민들레 씨앗으로 “후” 불어낸 뒤 다른 사람에게 위로를 받기도, 조언을 받기도 하는 것입니다.

---
## Git Convention

- Commit Convention

```
git commit -m "#jira-id Feat: 로그인 API 구현"
git commit -m "#jira-id Fix: 봉사 신청 버그 수정"
git commit -m "#jira-id Style: 메인 페이지 메뉴 스타일 변경"
git commit -m "#jira-id Chore: 라이브러리 추가"
git commit -m "#jira-id Docs: 리드미 작성"
```

- Branch Convention

```
eg. stack/type/domain/#Jira이슈번호

stack : fe, be
type : feature, refactor, bug-fix, docs, style, config
domain : global, member, dandelion, garden

> 브랜치 종류
master : 서비스 출시
develop : backend, frontend 브랜치를 이 브랜치에 병합
be : 백엔드 소스코드를 이 브랜치에 병합. 젠킨스 적용
fe : 프론트엔드 소스코드를 이 브랜치에 병합. 젠킨스 적용
be/feature/member/#adwadawdwadwad : 백엔드 작업 브랜치
fe/feature/member/#adwadawdwadwad : 프론트엔드 작업 브랜치
```

## Jira Convention

```
[BE: 세팅] 개발환경 세팅
[BE: 세팅] 서버 세팅
[BE: 설계] 데이타베이스 설계
[BE: 개발] OOO API 기능 추가
[BE: 개발] OOO API 버그 수정
[BE: 개발] OOO API 리팩토링
[FE: 개발] 회원가입 컴포넌트 개발 
[FE: 디자인] 민들레씨 날라가는
[FE: 학습] 리액트 네이티브 학습
이런식으로 앞단에는 카테고리
뒤에는 자유양식 이긴하나 누가 봐도 이해 할 수 있게 작성
```

## 서비스 주요기능

### 롤링페이퍼 시작을 위한 씨앗 날리기

로그인 후 랜딩페이지에서 아래에서 위로 스와이프 하면 나오는 화면. 이를 통해 롤링페이퍼를 시작할 수 있고, 시작된 롤링페이퍼는 다른 이에게 랜덤으로 확인되어 이어 써지게 됩니다. 전송하려면 특색있게도 마이크를 이용하여 “후” 바람을 불어야 합니다.

![씨앗 날리기.gif](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/e0324139-fada-44f0-a0db-6f2b4570b962/씨앗_날리기.gif)

### 다른 이의 롤링페이퍼에 이어 작성하기 위한 랜덤 씨앗 확인

롤링페이퍼를 완성하기 위하여 랜덤으로 다른 이의 씨앗에 접근하여 이어 쓰는 기능입니다.

![씨앗 확인.gif](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/c0e2aa55-91fc-46d5-9499-48b1ecc3ac80/씨앗_확인.gif)

### 꽃밭 확인

자신에게 돌아온 롤링페이퍼를 확인할 수 있습니다. 총 5개까지 씨앗을 날릴 수 있기에 5개의 팻말이 있고, 앨범에 보관할 수도 있습니다.

![꽃밭.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/b937af4d-238a-4c79-8c63-742b4d9909d1/꽃밭.png)

### 꽃밭 확인

간직하고 싶은 꽃잎을 확인할 수 있습니다.

![보관함.png](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/3e07df8c-a07d-439f-90ac-a45335f9ba4b/보관함.png)

## 와이어 프레임

![Untitled](https://s3-us-west-2.amazonaws.com/secure.notion-static.com/8286f486-0434-4a9f-ba7f-063d59f57199/Untitled.png)

## Site Map

<hr>

