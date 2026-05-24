# CLAUDE.md — CJReminder 코딩 관례

## 테스트 규칙
- 기능 추가/수정 시 반드시 검증 테스트를 함께 작성한다.
- 도메인 엔티티 테스트는 JPA/Spring 컨텍스트 없이 순수 단위 테스트로 작성한다 (JUnit + AssertJ만 사용).

## 참고 문서
- spec.md: 기능 명세
- plan.md: 개발 계획
- tasks.md: 구현 태스크 체크리스트