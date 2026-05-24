# CJReminder 작업 목록

## Phase 1: Backend 기본 API

### 1-1. 프로젝트 설정
- [ ] `build.gradle.kts`에 `spring-boot-starter-web` 의존성 추가
- [ ] `build.gradle.kts`에 SpringDoc OpenAPI 의존성 추가
- [ ] `application.properties`에 H2 콘솔 활성화 (`spring.h2.console.enabled=true`)
- [ ] `application.properties`에 JPA DDL 자동 생성 설정 (`spring.jpa.hibernate.ddl-auto=create-drop`)
- [ ] `application.properties`에 H2 데이터소스 URL 설정
- [ ] CORS 설정 클래스 작성 (`WebConfig.java` — `localhost:3000` 허용)

### 1-2. Entity 생성
- [ ] `Priority` enum 생성 (NONE, LOW, MEDIUM, HIGH)
- [ ] `ReminderList` 엔티티 생성 (id, name, color, createdAt, updatedAt)
- [ ] `Reminder` 엔티티 생성 (id, title, notes, dueDate, priority, flagged, completed, completedAt, list, createdAt, updatedAt)
- [ ] `@PrePersist`, `@PreUpdate` 타임스탬프 자동 관리 적용
- [ ] `ReminderList` ↔ `Reminder` 관계 매핑 (OneToMany / ManyToOne, cascade)

### 1-3. Repository
- [ ] `ReminderListRepository` 생성
- [ ] `ReminderRepository` 생성 + 커스텀 쿼리 메서드:
  - [ ] `findByListIdAndCompletedFalse`
  - [ ] `findByCompletedTrue`
  - [ ] `findByFlaggedTrueAndCompletedFalse`
  - [ ] `findByDueDateAndCompletedFalse`
  - [ ] `findByDueDateNotNullAndCompletedFalseOrderByDueDate`
  - [ ] `findByTitleContainingIgnoreCaseOrNotesContainingIgnoreCase`

### 1-4. DTO
- [ ] `ReminderListRequest` 생성 (name, color)
- [ ] `ReminderListResponse` 생성 (id, name, color, reminderCount, createdAt, updatedAt)
- [ ] `ReminderRequest` 생성 (title, notes, dueDate, priority, flagged)
- [ ] `ReminderResponse` 생성 (전체 필드 + listId, listName, listColor)
- [ ] `SummaryResponse` 생성 (todayCount, scheduledCount, allCount, completedCount, flaggedCount)

### 1-5. Service
- [ ] `ReminderListService` — 전체 조회 (카운트 포함), 단건 조회, 생성, 수정, 삭제
- [ ] `ReminderService` — 리스트별 조회, 생성, 수정, 삭제
- [ ] `ReminderService` — 완료 토글, 플래그 토글
- [ ] `ReminderService` — 스마트 리스트 (today, scheduled, all, completed, flagged)
- [ ] `ReminderService` — 검색, 요약(summary)

### 1-6. Controller
- [ ] `ReminderListController` — GET/POST/PUT/DELETE `/api/lists`
- [ ] `ReminderController` — GET/POST `/api/lists/{listId}/reminders`
- [ ] `ReminderController` — PUT/DELETE/PATCH `/api/reminders/{id}`
- [ ] `ReminderController` — GET 스마트 리스트 (`/api/reminders/today|scheduled|all|completed|flagged`)
- [ ] `ReminderController` — GET 검색 (`/api/reminders/search`)
- [ ] `ReminderController` — GET 요약 (`/api/reminders/summary`)

### 1-7. 초기 데이터
- [ ] `data.sql` 작성 — 샘플 리스트 3개 + 리마인더 10개

### 1-8. 검증
- [ ] `./gradlew build` 성공
- [ ] H2 콘솔에서 테이블/데이터 확인
- [ ] curl 또는 Swagger UI로 전체 API 엔드포인트 테스트
- [ ] `./gradlew test` 통과

---

## Phase 2: Frontend 기본 골격 + 사이드바

### 2-1. Next.js 프로젝트 생성
- [ ] `npx create-next-app@latest frontend` (TypeScript, Tailwind CSS, App Router)
- [ ] Lucide React 설치 (`npm install lucide-react`)
- [ ] `.env.local`에 `NEXT_PUBLIC_API_URL=http://localhost:8080/api` 설정

### 2-2. 타입 정의
- [ ] `types/index.ts` — ReminderList, Reminder, Priority, SummaryResponse 타입 정의

### 2-3. API 클라이언트
- [ ] `lib/api.ts` — 리스트 CRUD 함수
- [ ] `lib/api.ts` — 리마인더 CRUD 함수
- [ ] `lib/api.ts` — 스마트 리스트 조회, 검색, 요약 함수

### 2-4. 레이아웃
- [ ] `app/layout.tsx` — 시스템 폰트 스택, 전역 스타일
- [ ] `app/page.tsx` — 2-column 레이아웃 (사이드바 280px + 메인 flex-1)

### 2-5. 사이드바 컴포넌트
- [ ] `SearchBar.tsx` — 둥근 검색 입력 필드 (UI만)
- [ ] `SmartListCards.tsx` — 2-column 그리드 카드 (아이콘 + 라벨 + 카운트), summary API 연동
- [ ] `ListItem.tsx` — 컬러 불릿 + 이름 + 카운트
- [ ] `Sidebar.tsx` — SearchBar + SmartListCards + 리스트 목록 조합
- [ ] 선택 상태 관리 (useState로 현재 선택된 리스트/스마트리스트 추적)

### 2-6. 메인 영역
- [ ] `MainContent.tsx` — 선택된 리스트 헤더 (이름 + 카운트)
- [ ] 빈 상태 placeholder 표시

### 2-7. 검증
- [ ] `npm run dev` 실행 확인
- [ ] 사이드바에 스마트 리스트 카드 표시 확인 (API 연동)
- [ ] 사이드바에 사용자 리스트 표시 확인 (API 연동)
- [ ] 리스트/스마트리스트 클릭 시 선택 상태 변경 확인

---

## Phase 3: 리마인더 목록 표시 + CRUD

### 3-1. ReminderItem 컴포넌트
- [ ] 원형 체크박스 (미완료: 빈 원, 완료: 채워진 원)
- [ ] 제목 + 메모 미리보기 (1줄, 회색 작은 폰트)
- [ ] 기한 표시 (지난 날짜 빨간색)
- [ ] 우선순위 표시 (`!` / `!!` / `!!!`)
- [ ] 플래그 아이콘 (주황색 깃발)
- [ ] 완료 체크 애니메이션 (fill + strikethrough + fade-out)

### 3-2. MainContent 리마인더 목록
- [ ] 사용자 리스트 선택 시 `GET /api/lists/{listId}/reminders` 호출
- [ ] 스마트 리스트 선택 시 해당 API 호출 (today, scheduled, all, completed, flagged)
- [ ] 리마인더 목록 렌더링

### 3-3. InlineReminderInput
- [ ] "+ 새로운 미리 알림" 버튼 (리스트 색상 텍스트)
- [ ] 클릭 시 인라인 입력 행 삽입
- [ ] Enter 키로 저장 (`POST /api/lists/{listId}/reminders`)
- [ ] 저장 후 목록 즉시 갱신

### 3-4. 완료 토글
- [ ] 체크박스 클릭 → `PATCH /api/reminders/{id}/toggle`
- [ ] 애니메이션 완료 후 목록에서 제거 (미완료 목록인 경우)
- [ ] 사이드바 카운트 갱신

### 3-5. 삭제
- [ ] 리마인더 호버 시 삭제 버튼 표시
- [ ] `DELETE /api/reminders/{id}` 호출
- [ ] 목록 + 사이드바 카운트 갱신

### 3-6. 검증
- [ ] 리스트 선택 → 리마인더 목록 정상 로드
- [ ] 인라인 생성 → 목록 즉시 반영
- [ ] 완료 토글 → 애니메이션 + 목록 갱신
- [ ] 스마트 리스트 필터링 정상 동작
- [ ] 삭제 → 목록 + 카운트 갱신

---

## Phase 4: 상세 편집 + 리스트 관리 + 검색

### 4-1. ReminderDetail 패널
- [ ] 리마인더 클릭 시 상세 패널 슬라이드-인
- [ ] 제목 편집 (input)
- [ ] 메모 편집 (textarea)
- [ ] 기한 설정 (date picker)
- [ ] 우선순위 선택 (드롭다운: 없음/낮음/보통/높음)
- [ ] 플래그 토글 스위치
- [ ] 리스트 이동 (드롭다운으로 다른 리스트 선택)
- [ ] 삭제 버튼 (빨간색)
- [ ] `PUT /api/reminders/{id}` 호출 (포커스 아웃 시 자동 저장)

### 4-2. ListFormModal
- [ ] "+ 목록 추가" 버튼 → 생성 모달 표시
- [ ] 리스트 이름 입력 필드
- [ ] 12색 원형 팔레트 색상 선택
- [ ] 모달 배경 블러 + scale/opacity 애니메이션
- [ ] 리스트 수정 (더보기 메뉴 → 편집 모달)
- [ ] 리스트 삭제 (확인 다이얼로그 → `DELETE /api/lists/{id}`)

### 4-3. 검색 기능
- [ ] SearchBar 입력 → 디바운스 300ms
- [ ] `GET /api/reminders/search?q={keyword}` 호출
- [ ] 검색 결과를 메인 영역에 표시
- [ ] 검색 해제 시 이전 리스트로 복귀

### 4-4. 플래그 토글
- [ ] 리마인더 항목에서 플래그 토글
- [ ] 상세 패널에서 플래그 토글
- [ ] `PATCH /api/reminders/{id}/flag` 호출
- [ ] 깃발 아이콘 bounce 애니메이션

### 4-5. 검증
- [ ] 리마인더 상세 편집 후 저장 확인
- [ ] 리스트 생성 → 사이드바 즉시 반영
- [ ] 리스트 삭제 → 소속 리마인더 함께 삭제
- [ ] 검색어 입력 → 결과 필터링
- [ ] 전체 CRUD 플로우 E2E 확인

---

## Phase 5: 다크 모드 + 반응형 + 폴리싱

### 5-1. 다크 모드
- [ ] Tailwind `darkMode: 'media'` 설정
- [ ] 사이드바 다크 모드 색상 적용
- [ ] 메인 영역 다크 모드 색상 적용
- [ ] 카드/모달/입력 필드 다크 모드 색상 적용
- [ ] spec.md 색상표 준수 확인

### 5-2. 반응형 디자인
- [ ] `>=1024px`: 사이드바 항상 표시
- [ ] `768px~1023px`: 햄버거 버튼으로 사이드바 접기/펼치기
- [ ] `<768px`: 사이드바 오버레이 + 메인 전체 너비

### 5-3. 애니메이션 마무리
- [ ] 리스트 전환 fade (0.2s)
- [ ] 모달 scale + opacity (0.2s)
- [ ] 사이드바 호버 배경색 전환 (0.15s ease)
- [ ] 리마인더 삭제 slide-out

### 5-4. 에러 핸들링
- [ ] API 실패 시 토스트 메시지 표시
- [ ] 빈 상태 UI (리마인더 없을 때 placeholder)
- [ ] 네트워크 에러 시 재시도 안내

### 5-5. 검증
- [ ] 다크 모드 전환 시 전체 컴포넌트 색상 확인
- [ ] 다양한 화면 크기 레이아웃 확인 (Chrome DevTools)
- [ ] Lighthouse 접근성 점수 확인
- [ ] 전체 기능 회귀 테스트
