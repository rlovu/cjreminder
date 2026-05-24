# CLAUDE.md — CJReminder 코딩 관례

## 패키지 구조
```
cjay.ai.cjreminder
├── domain/                    # 엔티티, enum (JPA 어노테이션 포함)
├── repository/                # Spring Data JPA Repository
├── service/
│   ├── ports/inp/             # Service 인터페이스 (입력 포트)
│   └── Default*.java          # Service 구현체
├── controller/                # REST API Controller
└── dto/                       # 요청/응답 DTO
```

## 도메인 엔티티 규칙
- `@PrePersist` / `@PreUpdate` 사용 금지. 타임스탬프(createdAt, updatedAt)는 생성/수정 메서드에서 직접 설정한다.
- 생성은 정적 팩토리 메서드(`create()`)를 사용한다. Builder와 생성자는 `private`/`protected`로 제한한다.
- 상태 변경은 도메인 메서드(`update()`, `toggleComplete()` 등)를 통해서만 한다.
- 패키지명은 `entity`가 아닌 `domain`을 사용한다.

## Service 계층 규칙
- 인터페이스와 구현을 분리한다.
- 인터페이스는 `service.ports.inp` 패키지에 정의한다 (`cjay.ai.cjreminder.service.ports.inp`).
- 구현 클래스는 `service` 패키지에 두고, 이름 앞에 `Default`를 붙인다 (예: `DefaultReminderListService`).

## 테스트 규칙
- 기능 추가/수정 시 반드시 검증 테스트를 함께 작성한다.
- 도메인 엔티티 테스트는 JPA/Spring 컨텍스트 없이 순수 단위 테스트로 작성한다 (JUnit + AssertJ만 사용).
- Service 테스트는 `@SpringBootTest` + `@Transactional` 통합 테스트로 작성한다. Mock(Mockito) 사용 금지.

## 참고 문서
- spec.md: 기능 명세
- plan.md: 개발 계획
- task.md: 구현 태스크 체크리스트
