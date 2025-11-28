|기능|METHOD|ENDPOINT (URL)|
| - | - | - |
|<p></p><p>회원가입</p><p></p>|POST|/api/users|
|<p></p><p>로그인</p>|POST|<p>/api/auth/login</p><p></p>|
|<p></p><p>로그아웃</p><p></p>|POST|/api/auth/logout|
|<p></p><p>프로필 조회</p><p></p>|GET|/api/users/{userId}|
|프로필 수정|PATCH|/api/users/{userId}|
|일정 생성|POST|/api/schedules|
|일정 조회|GET|/api/schedules?month=YYYY-MM|
|일정 수정|PATCH|/api/schedules/{scheduleId}|
|일정 삭제|DELETE|/api/schedules/{scheduleId}|
|게시글 작성|POST|/api/posts|
|게시글 목록 조회|GET|/api/posts?sort=latest&region=xx|
|게시글 상세 조회|GET|/api/posts/{postId}|
|게시글 수정|PATCH|/api/posts/{postId}|
|게시글 삭제|DELETE|/api/posts/{postId}|
|댓글 작성|POST|/api/posts/{postId}/comments|
|댓글 조회|GET|/api/posts/{postId}/comments|
|댓글 수정|PATCH|/api/comments/{commentId}|
|댓글 삭제|DELETE|/api/comments/{commentId}|
|워케이션 일정 자동 생성|POST|/api/workation/schedules|
|추천 기간 분석|GET|/api/recommend/period|
|일정 제약도(태깅 결과) 읽기|GET|/api/schedules/constraints|
|연속 가능기간 탐색|GET|/api/recommend/period/continuous|
|추천 장소 제공|GET|/api/recommend/places?region=xx&style=xx|
|추천 이유 제공|GET|/api/recommend/reasons?periodId=xx&placeId=xx|
|외부 장소 API 연동|GET|/api/external/places?query=xx|
|AI 모델 점수화(내부 호출)|POST|/api/ai/score|



|데이터|
| - |
|<p>Request:</p><p>- email(String)</p><p>- password(String)</p><p>- nickname(String)</p><p>- region(String)</p><p></p><p>Response:</p><p>- userId(Long)</p><p>- success(Boolean)</p><p>- message(String)</p><p>- error(String)</p>|
|<p>Request:</p><p>- email(String)</p><p>- password(String)</p><p></p><p>Response:</p><p>- accessToken(String)</p><p>- refreshToken(String)</p><p>- userInfo(Object)</p>|
|<p></p><p>Request:</p><p>- accessToken(String)</p><p></p><p>Response:</p><p>- success(Boolean)</p><p></p>|
|<p>Response:</p><p>- email</p><p>- nickname</p><p>- region</p><p>- workationStyle</p>|
|<p>Request:</p><p>- nickname(optional)</p><p>- region(optional)</p><p>- workationStyle(optional)</p><p></p><p>Response:</p><p>- success(Boolean)</p>|
|<p>- title(String)</p><p>- startDate(String)</p><p>- endDate(String)</p><p>- type(String)</p><p>- memo(optional)</p><p></p><p>자동 태깅 결과:</p><p>- constraintTag(String: CRITICAL/FLEXIBLE/REMOTE\_OK/...)</p><p></p><p>Response:</p><p>- scheduleId(Long)</p><p>- savedTag(String)</p>|
|<p>Response:</p><p>- schedules: List<Object></p><p>- scheduleId</p><p>- title</p><p>- type</p><p>- startDate</p><p>- endDate</p><p>- constraintTag</p>|
|<p>Request:</p><p>- title(optional)</p><p>- startDate(optional)</p><p>- endDate(optional)</p><p>- type(optional)</p><p></p><p>수정 후 자동 재태깅 수행</p>|
|<p>Response:</p><p>- success(Boolean)</p>|
|<p>Request:</p><p>- category(String)</p><p>- title(String)</p><p>- content(String)</p><p>- region(optional)</p><p></p><p>Response:</p><p>- postId(Long)</p>|
|<p>Response:</p><p>- posts: List<Object></p>|
|<p>Response:</p><p>- title</p><p>- content</p><p>- nickname</p><p>- createdAt</p>|
|<p>Request:</p><p>- title(optional)</p><p>- content(optional)</p>|
|<p>Response:</p><p>- success(Boolean)</p>|
|<p>Request:</p><p>- content(String)</p><p></p><p>Response:</p><p>- commentId(Long)</p>|
|<p>Response:</p><p>- comments: List<Object></p>|
|<p>Request:</p><p>- content(String)</p>|
|<p>Response:</p><p>- success(Boolean)</p>|
|<p>Request:</p><p>- basedOn(String: userInput | recommendation)</p><p>- period(start/end)</p><p>- placeId(optional)</p><p></p><p>Response:</p><p>- scheduleId(Long)</p>|
|<p>Response:</p><p>- periods: List<Object></p><p>- startDate</p><p>- endDate</p><p>- score</p><p>- message String</p>|
|<p>Response:</p><p>- scheduleId</p><p>- constraintTag</p>|
|<p>Response:</p><p>- availableSlots: List<Object></p>|
|<p>Response:</p><p>- places: List<Object></p><p>- placeId</p><p>- name</p><p>- score</p><p>- imageUrl</p><p>- tags</p>|
|<p>Response:</p><p>- periodReason</p><p>- placeReason</p><p>- summary(User-friendly text)</p>|
|<p>Response:</p><p>- apiResults(List<Object>)</p>|
|<p>Request:</p><p>- schedules</p><p>- places</p><p>Response:</p><p>- scoreResult(Object)</p>|



|ROLE|담당자|IsMVP|FE  구현|
| - | - | - | - |
|Anyone||No||
|Anyone||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|Anyone||No||
|Anyone||No||
|User||No||
|User||No||
|User||No||
|Anyone||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|User||No||
|BE\_SYSTEM||No||
|BE\_SYSTEM||No||

