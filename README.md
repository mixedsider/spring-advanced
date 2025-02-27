# SPRING ADVANCED

## 개발자
    Blog : https://strnetwork.tistory.com/
    github : https://github.com/mixedsider

## 진행하면서 적은 포스트
    https://strnetwork.tistory.com/60
    https://strnetwork.tistory.com/59
    https://strnetwork.tistory.com/58


## 프로젝트 개요
    https://github.com/f-api/spring-advanced
해당 프로젝트는 위의 프로젝트를 fork 하여 밑에서 서술할 레벨별 개선 및 테스트 코드를 추가를 하는 프로젝트입니다.


## 기능 설명
해당 프로젝트는 단순한 일정 공유 프로젝트입니다.

이 프로젝트는 회원 가입 및 로그인을 해야 사용을 하실 수 있습니다.

대표작 일정을 만들고 관리할 매니저를 추가를 할 수 있으며, 밑에 댓글을 추가를 하여 동참을 할 수 있습니다.

## 레벨별 개선한 부분 ( fork 전 브랜치 기준 )
- Lv.1
  - WeatherClient 30 번째 줄 개선
  - JwtFilter 64 번째 줄 개선
  - DTO 에서 모든 속성이 final 이 붙었다면 @RequiredArgsConstructor 로 변경 후 생성자 변경
  - AuthService User 생성 부분 Builder패턴으로 변경, 33번째 줄 개선
  - CommentService getComments 메소드 부분 for문 -> stream 문 가독성 개선
  - ManagerService getManagers 메소드 부분 for문 -> stream 문 가독성 개선
  - UserChangePasswordRequest @Valid 어노테이션추가로 UserService 28번째 줄 개선
  - 기타 코드 개선
- Lv.2
  - TodoRepository @EntityGraph 로 변경
- Lv.3
  - passwordEncoderTest 테스트 메소드 수정
  - ManagerServiceTest manager_목록_조회_시_Todo가_없다면_IRE_에러를_던진다 메소드 수정
  - ManagerServiceTest todo의_user가_null인_경우_예외가_발생한다 메소드에 맞게 mangerService 로직 수정
  - CommentServiceTest comment_등록_중_할일을_찾지_못해_에러가_발생한다 메소드 수정
- Lv.4
  - AdminInterceptor 추가
- Lv.5
  - manager or comments Controller URI 수정
  - managerController @DeleteMapping 매개변수 수정
  - managerService mixedsider/spring-advanced main 브랜치 기준 101번줄 수정
- Lv.6
  - ![스크린샷 2025-02-27 10-36-09](https://github.com/user-attachments/assets/1897c6d8-dbea-436b-b874-d4ed372f3891)
  - 테스트 폴더 전체 검사시 나오는 수치


## 모든 사용자 API 문서
|  API 명  | HTTP Method | EndPoint | 요청 방식                     | 응답 코드 | 응답 데이터 |
|---------|------------|-----------|-----------------------------|---------|----------|
|  회원가입  | POST  | /auth/signup  | RequestBody : SignupRequest |200|SignupResponse|
|  로그인  |  POST  | /auth/signin  | RequestBody : SigninRequest  |200|SigninResponse|

## 일반 사용자 API 문서 ( 회원가입 유저 || JWT가 있는 유저 ) 
|  API 명  | HTTP Method | EndPoint | 요청 방식                     | 응답 코드 | 응답 데이터 |
|---------|------------|-----------|-----------------------------|---------|----------|
|  일정등록  |  POST  | /todos  |  RequestBody : TodoSaveRequest | 200 | TodoSaveResponse |
|  일정목록확인  |  GET  |  /todos  | Nullable RequestParam : page, size | 200  | Page<TodoResponse>  |
|  일정단독확인  |  GET  |  /todos/{todoId}  |  PathVariable : todoId  | 200, 400  |  TodoResponse  |
|  비밀번호변경  |  PUT  |  /users/{userId}  |  RequestBody : UserChangePasswordRequest  | 200, 400  |  -  |
|  사용자 조회  |  GET  |  /users/{userId}  |  PathVariable : userId  |  200, 400  |  UserResponse  |
|  댓글 등록  |  POST  |  /comments  |  PathVariable : todoId, RequestBody : CommentSaveRequest  |  200, 400  |  CommentSaveResponse  |
|  댓글 가져오기  | GET  |  /comments  |  PathVariable : todoId  |  200  |  List<CommentResponse>  |
|  매니저 지정하기  |  POST  |  /managers  |  PathVariable : todoId, RequestBody : ManagerSaveRequest  |  200, 400  |  ManagerSaveResponse  |
|  매니저 목록 가져오기  |  GET  |  /managers  |  PathVariable : todoId  |  200  |  List<ManagerResponse>  |
|  매니저 지정 풀기  |  DELETE  |  /managers  |  PathVariable : todoId, managerId  |  200 , 400 |  -  |

## 어드민 API 문서 ( 회원가입 유저 && UserRole.ADMIN  )
|  API 명  | HTTP Method | EndPoint | 요청 방식                     | 응답 코드 | 응답 데이터 |
|---------|------------|-----------|-----------------------------|---------|----------|
| 권한 변경  |  PATCH  |  /admin/users/{userId}  |  PathVariable : userId, RequestBody : UserRoleChangeRequest  |  200, 400  |  -  |
| 댓글 삭제  |  DELETE  |  /admin/comments/{commentId}  |  PathVariable : commentId  |  200  |  -  |
