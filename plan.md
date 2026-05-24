# CJReminder 개발 계획

## 기술 스택 상세

### Backend
| 항목 | 기술 | 버전 | 용도 |
|---|---|---|---|
| Framework | Spring Boot | 4.0.6 | REST API 서버 |
| Language | Java | 25 | |
| ORM | Spring Data JPA + Hibernate | (Boot 관리) | 엔티티 매핑, Repository |
| Database | H2 | (Boot 관리) | 인메모리 DB (개발/데모) |
| Boilerplate | Lombok | (Boot 관리) | getter/setter/builder 생성 |
| Build | Gradle Kotlin DSL | 9.4.x | |
| API 문서 | SpringDoc OpenAPI | 최신 | Swagger UI 자동 생성 |

### Frontend
| 항목 | 기술 | 용도 |
|---|---|---|
| Framework | Next.js (latest) | React SSR/SPA |
| Language | TypeScript | 타입 안전성 |
| Styling | Tailwind CSS | 유틸리티 기반 스타일링 |
| HTTP Client | fetch (내장) | API 호출 |
| Icons | Lucide React | Apple SF Symbols 대체 |
| State | React useState/useContext | 클라이언트 상태 관리 |

### 프로젝트 구조
```
cjreminder/
├── src/                          # Spring Boot (Backend)
│   ├── main/java/cjay/ai/cjreminder/
│   │   ├── CjreminderApplication.java
│   │   ├── entity/
│   │   ├── repository/
│   │   ├── service/
│   │   ├── controller/
│   │   └── dto/
│   └── main/resources/
│       ├── application.properties
│       └── data.sql              # 초기 데이터
├── frontend/                     # Next.js (Frontend)
│   ├── app/
│   ├── components/
│   ├── lib/
│   └── types/
├── build.gradle.kts
├── spec.md
└── plan.md
```

---

## Phase 1: Backend 기본 API

> 목표: 리마인더와 리스트의 CRUD API를 완성하고 H2 콘솔로 데이터를 확인할 수 있는 상태

### 1-1. 프로젝트 설정
- `application.properties`에 H2 콘솔 활성화, JPA DDL 자동 생성 설정
- `spring-boot-starter-web` 의존성 추가 (현재 누락)
- CORS 설정 (프론트엔드 `localhost:3000` 허용)

### 1-2. Entity 생성
- `ReminderList` 엔티티 — id, name, color, createdAt, updatedAt
- `Reminder` 엔티티 — id, title, notes, dueDate, priority, flagged, completed, completedAt, list(ManyToOne), createdAt, updatedAt
- `Priority` enum — NONE, LOW, MEDIUM, HIGH
- `@PrePersist`, `@PreUpdate`로 타임스탬프 자동 관리

### 1-3. Repository
- `ReminderListRepository extends JpaRepository`
- `ReminderRepository extends JpaRepository` + 커스텀 쿼리 메서드
  - `findByListIdAndCompletedFalse(Long listId)`
  - `findByCompletedTrue()`
  - `findByFlaggedTrueAndCompletedFalse()`
  - `findByDueDateAndCompletedFalse(LocalDate date)`
  - `findByDueDateNotNullAndCompletedFalseOrderByDueDate()`
  - `findByTitleContainingOrNotesContaining(String title, String notes)`

### 1-4. DTO
- `ReminderListRequest` / `ReminderListResponse` (카운트 포함)
- `ReminderRequest` / `ReminderResponse`
- `SummaryResponse` (스마트 리스트별 카운트)

### 1-5. Service
- `ReminderListService` — CRUD + 카운트 조회
- `ReminderService` — CRUD + 완료 토글 + 플래그 토글 + 스마트 리스트 필터 + 검색

### 1-6. Controller
- `ReminderListController` — `/api/lists/**`
- `ReminderController` — `/api/lists/{listId}/reminders`, `/api/reminders/**`

### 1-7. 초기 데이터
- `data.sql`로 샘플 리스트 3개 + 리마인더 10개 삽입

### 1-8. 검증
- H2 콘솔 (`/h2-console`)에서 테이블 확인
- `curl` 또는 Swagger UI로 모든 API 엔드포인트 테스트
- `./gradlew test` 통과

---

## Phase 2: Frontend 기본 골격 + 사이드바

> 목표: Next.js 프로젝트를 생성하고, Apple Reminders 스타일의 2-column 레이아웃과 사이드바를 완성

### 2-1. Next.js 프로젝트 생성
- `npx create-next-app@latest frontend` (TypeScript, Tailwind CSS, App Router)
- Lucide React 아이콘 설치
- API proxy 또는 환경변수 설정 (`NEXT_PUBLIC_API_URL=http://localhost:8080/api`)

### 2-2. 타입 정의
- `types/index.ts` — Reminder, ReminderList, Priority, SummaryResponse 타입

### 2-3. API 클라이언트
- `lib/api.ts` — fetch 기반 API 호출 함수 (리스트 CRUD, 리마인더 CRUD, 스마트리스트, 검색, 요약)

### 2-4. 레이아웃
- 시스템 폰트 스택 적용
- 2-column 레이아웃 (사이드바 280px 고정 + 메인 flex-1)
- Apple 라이트 모드 색상 적용

### 2-5. 사이드바 컴포넌트
- `SearchBar` — 둥근 검색 입력 필드 (UI만, 기능은 Phase 4)
- `SmartListCards` — 2-column 그리드, 각 카드에 아이콘 + 라벨 + 카운트
  - API `/api/reminders/summary` 연동
- `ListItem` — 리스트 목록, 컬러 불릿 + 이름 + 카운트
  - API `/api/lists` 연동
- 선택 상태 관리 (어떤 리스트/스마트리스트가 선택됐는지)

### 2-6. 메인 영역 빈 상태
- 선택된 리스트 이름 + 카운트 헤더 표시
- 리마인더 목록은 빈 상태 placeholder

### 2-7. 검증
- `npm run dev`로 개발 서버 실행
- 사이드바에 스마트 리스트 카드와 사용자 리스트가 API에서 로드되어 표시되는지 확인
- 리스트 클릭 시 선택 상태가 변경되는지 확인

---

## Phase 3: 리마인더 목록 표시 + CRUD

> 목표: 리마인더를 목록으로 표시하고 생성/완료/삭제가 동작하는 상태

### 3-1. ReminderItem 컴포넌트
- 원형 체크박스 + 제목 + 메모 미리보기 + 플래그 아이콘
- 기한 표시 (지난 날짜는 빨간색)
- 우선순위 표시 (`!`, `!!`, `!!!`)
- 완료 체크 시 fill 애니메이션 + strikethrough + fade-out

### 3-2. MainContent 컴포넌트
- 선택된 리스트/스마트리스트에 따라 API 호출하여 리마인더 목록 표시
- 리스트 선택: `GET /api/lists/{listId}/reminders`
- 스마트 리스트 선택: `GET /api/reminders/today|scheduled|all|completed|flagged`

### 3-3. InlineReminderInput
- 하단 "+ 새로운 미리 알림" 버튼
- 클릭 시 인라인 입력 행 삽입, Enter로 저장
- `POST /api/lists/{listId}/reminders` 호출

### 3-4. 완료 토글
- 체크박스 클릭 → `PATCH /api/reminders/{id}/toggle`
- 애니메이션 후 목록에서 제거 (미완료 목록인 경우)

### 3-5. 삭제
- 리마인더 항목에 삭제 버튼 (호버 시 표시)
- `DELETE /api/reminders/{id}`

### 3-6. 검증
- 리스트 선택 → 리마인더 목록 로드 확인
- 새 리마인더 인라인 생성 → 목록에 즉시 반영 확인
- 완료 토글 → 애니메이션 + 목록 갱신 확인
- 스마트 리스트 (오늘, 예정 등) 필터링 정상 동작 확인

---

## Phase 4: 상세 편집 + 리스트 관리 + 검색

> 목표: 리마인더 상세 편집, 리스트 생성/수정/삭제, 검색 기능 완성

### 4-1. ReminderDetail 패널
- 리마인더 클릭 시 오른쪽에 상세 패널 슬라이드-인
- 편집 필드: 제목, 메모 (textarea), 기한 (date picker), 우선순위 (드롭다운), 플래그 (토글 스위치)
- 리스트 이동 (드롭다운 선택)
- 삭제 버튼 (빨간색)
- `PUT /api/reminders/{id}` 호출 (변경 시 자동 저장 또는 포커스 아웃 시 저장)

### 4-2. ListFormModal
- "+ 목록 추가" 버튼 클릭 → 모달 표시
- 리스트 이름 입력 + 12색 원형 팔레트에서 색상 선택
- 리스트 수정: 리스트 항목 우클릭/더보기 메뉴 → 편집 모달
- 리스트 삭제: 확인 다이얼로그 → `DELETE /api/lists/{id}`
- 배경 블러, scale + opacity 전환 애니메이션

### 4-3. 검색 기능
- SearchBar에 입력 → 디바운스 (300ms) → `GET /api/reminders/search?q={keyword}`
- 검색 결과를 메인 영역에 표시
- 검색 해제 시 이전 리스트로 복귀

### 4-4. 플래그 토글
- 리마인더 항목 또는 상세 패널에서 플래그 토글
- `PATCH /api/reminders/{id}/flag`
- 깃발 아이콘 bounce 애니메이션

### 4-5. 검증
- 리마인더 클릭 → 상세 패널 열림, 편집 후 저장 확인
- 리스트 생성 → 사이드바에 즉시 반영 확인
- 리스트 삭제 → 소속 리마인더 함께 삭제 확인
- 검색어 입력 → 결과 필터링 확인
- 전체 CRUD 플로우 E2E 확인

---

## Phase 5: 다크 모드 + 반응형 + 폴리싱

> 목표: 프로덕션 수준의 UI 완성도. Apple Reminders와 시각적으로 유사한 최종 상태

### 5-1. 다크 모드
- `prefers-color-scheme` 미디어 쿼리 기반 자동 전환
- Tailwind `dark:` variant로 모든 컴포넌트에 다크 모드 색상 적용
- spec.md의 다크 모드 색상표 준수

### 5-2. 반응형 디자인
- `>=1024px`: 2-column (사이드바 항상 표시)
- `768px~1023px`: 사이드바 접기/펼치기 (햄버거 버튼)
- `<768px`: 사이드바 오버레이, 메인 전체 너비

### 5-3. 애니메이션 마무리
- 리스트 전환 fade (0.2s)
- 모달 scale + opacity (0.2s)
- 사이드바 호버 배경색 전환 (0.15s ease)
- 리마인더 삭제 slide-out

### 5-4. 에러 핸들링
- API 실패 시 토스트 메시지 표시
- 빈 상태 UI (리마인더 없을 때 placeholder)
- 네트워크 에러 시 재시도 안내

### 5-5. 검증
- 다크 모드 토글/시스템 설정 변경 시 모든 컴포넌트 색상 확인
- 다양한 화면 크기에서 레이아웃 확인 (Chrome DevTools)
- Lighthouse 접근성 점수 확인
- 전체 기능 회귀 테스트
