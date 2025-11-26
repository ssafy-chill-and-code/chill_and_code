# chill_and_code
SSAFY 14기 1학기 관통프로젝트

## Commit Convention

프로젝트에서 사용하는 **Git Commit 규칙**입니다.
모든 커밋 메시지는 아래 규칙을 따라 작성합니다.

| 타입           | 설명                          | 예시                                         |
| ------------ | --------------------------- | ------------------------------------------ |
| **WIP**      | 아직 완료되지 않은 작업 단계에서 커밋할 때 사용 | `git commit -m "WIP: 로그인 API 개발 중"`        |
| **Feat**     | 새로운 기능을 추가할 때               | `git commit -m "Feat: 회원가입 기능 구현"`         |
| **Fix**      | 버그를 수정할 때                   | `git commit -m "Fix: 로그인 시 비밀번호 검증 오류 해결"` |
| **Refactor** | 기능 변화 없이 코드 구조 개선           | `git commit -m "Refactor: 불필요한 변수 정리"`     |
| **Chore**    | 설정 변경 등 비즈니스 로직과 무관한 작업     | `git commit -m "Chore: ESLint 설정 변경"`      |
| **Docs**     | 문서 관련 작업                    | `git commit -m "Docs: README에 프로젝트 설명 추가"` |


## Branch Convention

프로젝트에서는 기능 단위로 브랜치를 생성하고, 아래 규칙을 따라 이름을 작성합니다.

| 타입 | 설명 | 예시 |
| --- | --- | --- |
| **feat** | 기능 개발 | `feat/login-api` |
| **fix** | 버그 수정 | `fix/login-error` |
| **refactor** | 구조 개선 | `refactor/user-service` |
| **hotfix** | 긴급 수정 (운영/배포 단계) | `hotfix/token-refresh-bug` |
| **chore** | 설정 변경 | `chore/package-update` |
| **docs** | 문서 | `docs/api-spec` |
| **test** | 테스트 코드 | `test/user-service` |
