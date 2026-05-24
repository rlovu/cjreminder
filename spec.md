# CJReminder PRD — Apple Reminders Web Clone

## 1. 프로젝트 개요

Apple Reminder App의 핵심 기능을 웹으로 구현한다.
사용자는 브라우저에서 리마인더를 생성/관리하고, 리스트별로 분류하며, 스마트 리스트로 빠르게 조회할 수 있다.

### 범위
- 단일 사용자 (인증 없음, MVP)
- 로컬 H2 DB 사용 (개발/데모 용도)
- REST API + SPA 구조

### 기술 스택

| 레이어 | 기술 |
|---|---|
| Backend | Spring Boot 4.0.6 / Java 25 / Spring Data JPA / H2 |
| Frontend | Next.js (latest) / TypeScript / Tailwind CSS |
| Build | Gradle Kotlin DSL (backend) / npm (frontend) |

---

## 2. 핵심 기능

### 2.1 리마인더 (Reminder)
- 리마인더 생성 (제목 필수, 메모/기한/우선순위 선택)
- 리마인더 수정/삭제
- 완료/미완료 토글
- 플래그 토글
- 우선순위 설정 (없음, 낮음, 보통, 높음)
- 기한(due date) 설정

### 2.2 리스트 (ReminderList)
- 리스트 생성 (이름, 색상)
- 리스트 수정/삭제
- 리스트별 리마인더 조회
- 리스트 삭제 시 소속 리마인더도 함께 삭제

### 2.3 스마트 리스트 (가상 필터)
- **오늘** — 기한이 오늘인 리마인더
- **예정** — 기한이 설정된 미완료 리마인더 (날짜순)
- **전체** — 모든 미완료 리마인더
- **완료됨** — 완료된 리마인더
- **플래그 지정됨** — 플래그가 켜진 미완료 리마인더

### 2.4 검색
- 제목/메모 키워드 검색

---

## 3. 데이터 모델

### ReminderList
| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long (PK, auto) | |
| name | String (not null) | 리스트 이름 |
| color | String | HEX 색상 코드 (예: #FF6B6B) |
| createdAt | LocalDateTime | 생성일시 |
| updatedAt | LocalDateTime | 수정일시 |

### Reminder
| 필드 | 타입 | 설명 |
|---|---|---|
| id | Long (PK, auto) | |
| title | String (not null) | 제목 |
| notes | String (nullable) | 메모 |
| dueDate | LocalDate (nullable) | 기한 |
| priority | Enum (NONE, LOW, MEDIUM, HIGH) | 우선순위 |
| flagged | boolean | 플래그 여부 |
| completed | boolean | 완료 여부 |
| completedAt | LocalDateTime (nullable) | 완료일시 |
| list | ManyToOne → ReminderList | 소속 리스트 |
| createdAt | LocalDateTime | 생성일시 |
| updatedAt | LocalDateTime | 수정일시 |

---

## 4. REST API

Base path: `/api`

### 리스트

| Method | Path | 설명 |
|---|---|---|
| GET | /lists | 전체 리스트 조회 (각 리스트의 미완료 카운트 포함) |
| POST | /lists | 리스트 생성 |
| PUT | /lists/{id} | 리스트 수정 |
| DELETE | /lists/{id} | 리스트 삭제 (소속 리마인더 cascade) |

### 리마인더

| Method | Path | 설명 |
|---|---|---|
| GET | /lists/{listId}/reminders | 리스트별 리마인더 조회 |
| POST | /lists/{listId}/reminders | 리마인더 생성 |
| PUT | /reminders/{id} | 리마인더 수정 |
| DELETE | /reminders/{id} | 리마인더 삭제 |
| PATCH | /reminders/{id}/toggle | 완료 토글 |
| PATCH | /reminders/{id}/flag | 플래그 토글 |

### 스마트 리스트 (조회 전용)

| Method | Path | 설명 |
|---|---|---|
| GET | /reminders/today | 오늘 기한 리마인더 |
| GET | /reminders/scheduled | 기한이 있는 미완료 리마인더 |
| GET | /reminders/all | 전체 미완료 리마인더 |
| GET | /reminders/completed | 완료된 리마인더 |
| GET | /reminders/flagged | 플래그 리마인더 |

### 검색

| Method | Path | 설명 |
|---|---|---|
| GET | /reminders/search?q={keyword} | 제목/메모 검색 |

### 요약 (사이드바 카운트용)

| Method | Path | 설명 |
|---|---|---|
| GET | /reminders/summary | 스마트 리스트별 카운트 반환 |

---

## 5. UI/UX 디자인 (Apple Reminders 준수)

### 디자인 원칙
Apple Reminders macOS/iPadOS 앱의 룩앤필을 최대한 재현한다.
- **SF Pro 느낌의 서체**: `Inter` 또는 시스템 폰트 스택 (`-apple-system, BlinkMacSystemFont`)
- **색상 팔레트**: Apple 시스템 컬러 기반 (systemBlue `#007AFF`, systemRed `#FF3B30`, systemOrange `#FF9500`, systemYellow `#FFCC00`, systemGreen `#34C759`, systemPurple `#AF52DE`)
- **라이트 모드 기본**, 다크 모드 지원
- **부드러운 모서리**: `border-radius` 10~14px (카드, 버튼, 모달)
- **미니멀 그림자**: `box-shadow` 얕은 그림자로 깊이감 표현
- **전환 애니메이션**: 리스트 전환, 완료 체크 시 부드러운 transition

### 전체 레이아웃
```
┌─────────────────────────────────────────────────────────┐
│  ┌─ Sidebar (280px) ──┐  ┌─ Main Content ────────────┐  │
│  │ 🔍 검색 바          │  │                           │  │
│  │                     │  │  리스트 제목 + 카운트      │  │
│  │ ┌─────┐  ┌─────┐   │  │  ─────────────────────     │  │
│  │ │오늘 │  │예정  │   │  │  ○ 리마인더 항목 1        │  │
│  │ │  3  │  │ 12  │   │  │    메모 텍스트...         │  │
│  │ └─────┘  └─────┘   │  │  ○ 리마인더 항목 2        │  │
│  │ ┌─────┐  ┌─────┐   │  │  ● 리마인더 항목 3 (완료) │  │
│  │ │전체 │  │완료  │   │  │  ○ 리마인더 항목 4   🚩   │  │
│  │ │ 24  │  │  8  │   │  │                           │  │
│  │ └─────┘  └─────┘   │  │                           │  │
│  │ ┌─────┐            │  │                           │  │
│  │ │플래그│            │  │                           │  │
│  │ │  2  │            │  │                           │  │
│  │ └─────┘            │  │                           │  │
│  │                     │  │                           │  │
│  │ 나의 목록           │  │                           │  │
│  │ ● 개인              │  │                           │  │
│  │ ● 업무              │  │                           │  │
│  │ ● 쇼핑              │  │                           │  │
│  │                     │  │                           │  │
│  │ + 목록 추가         │  │  + 새로운 미리 알림       │  │
│  └─────────────────────┘  └───────────────────────────┘  │
└─────────────────────────────────────────────────────────┘
```

### 사이드바 상세

**검색 바**
- 사이드바 최상단, 둥근 모서리의 회색 배경 입력 필드
- 돋보기 아이콘 + placeholder "검색"
- Apple 스타일 `background: #F2F2F7` (라이트), `#1C1C1E` (다크)

**스마트 리스트 카드 (2x3 그리드)**
- 각 카드: 둥근 모서리 직사각형, 아이콘 + 라벨 + 카운트
- 아이콘은 원형 배경 안에 표시
- 카드별 색상:
  - 오늘: `#007AFF` (파랑), 캘린더 아이콘
  - 예정: `#FF3B30` (빨강), 달력+시계 아이콘
  - 전체: `#1C1C1E` (검정), 서류함 아이콘
  - 완료됨: `#8E8E93` (회색), 체크 아이콘
  - 플래그 지정됨: `#FF9500` (주황), 깃발 아이콘
- 선택 시 카드 배경 하이라이트

**나의 목록**
- "나의 목록" 섹션 헤더
- 각 리스트: 컬러 원형 불릿 + 이름 + 미완료 카운트 (오른쪽)
- 선택된 리스트: 배경 하이라이트 (`#E5E5EA` 라이트 / `#3A3A3C` 다크)
- 하단 "+ 목록 추가" 버튼

### 메인 컨텐츠 영역 상세

**헤더**
- 리스트 이름을 리스트 색상으로 표시, 굵은 큰 텍스트 (24~28px bold)
- 미완료 카운트 표시

**리마인더 항목 (ReminderItem)**
- 왼쪽: 빈 원형 체크박스 (미완료) / 채워진 원형 (완료, 리스트 색상)
- 체크박스 클릭 시: 원이 채워지는 애니메이션 → 0.5초 후 완료 목록으로 이동
- 가운데: 제목 (1줄) + 메모 미리보기 (1줄, 회색, 작은 폰트)
- 기한 표시: 제목 아래에 작은 텍스트로 날짜 표시 (지난 기한은 빨간색)
- 오른쪽: 플래그 아이콘 (주황 `#FF9500`) — 플래그 설정 시 표시
- 우선순위 표시: 제목 앞에 `!` (낮음), `!!` (보통), `!!!` (높음) — 리스트 색상
- 구분선: 항목 사이 얇은 라인 (`0.5px`, `#E5E5EA`)
- 호버 시: 배경 미세 하이라이트

**새 리마인더 추가**
- 하단 "+ 새로운 미리 알림" 버튼 (리스트 색상 텍스트)
- 클릭 시: 목록 최하단에 인라인 편집 행 삽입 (제목 입력 → Enter로 저장)
- Apple 스타일 인라인 생성 (모달 없이 직접 입력)

**리마인더 상세 편집**
- 리마인더 클릭 시: 오른쪽에 상세 패널 슬라이드-인 또는 인라인 확장
- 편집 필드: 제목, 메모, 기한 (date picker), 우선순위 (드롭다운), 플래그 (토글)
- 리스트 이동 (드롭다운으로 다른 리스트 선택)
- 삭제 버튼 (빨간색)

### 리스트 생성/수정 모달

- 모달 중앙 표시, 배경 블러 처리
- 입력 필드: 리스트 이름
- 색상 선택: Apple 기본 12색 원형 팔레트
  - `#FF3B30`, `#FF9500`, `#FFCC00`, `#34C759`, `#00C7BE`, `#007AFF`, `#5856D6`, `#AF52DE`, `#FF2D55`, `#A2845E`, `#8E8E93`, `#1C1C1E`
- 아이콘 선택 (선택사항, MVP에서는 생략 가능)

### 인터랙션 & 애니메이션

| 인터랙션 | 동작 |
|---|---|
| 완료 체크 | 원형 체크박스 fill 애니메이션 (0.3s) → 텍스트 strikethrough + fade → 0.5s 후 목록에서 제거 |
| 리스트 전환 | 메인 컨텐츠 fade 전환 (0.2s) |
| 사이드바 호버 | 항목 배경색 변경 (0.15s ease) |
| 리마인더 삭제 | 왼쪽 스와이프 또는 delete키 → slide-out 애니메이션 |
| 모달 열기/닫기 | scale + opacity 전환 (0.2s) |
| 플래그 토글 | 깃발 아이콘 bounce 효과 |

### 반응형
- **1024px 이상**: 2-column (사이드바 + 메인)
- **768px~1023px**: 사이드바 접기/펼치기 (햄버거 메뉴)
- **768px 미만**: 사이드바 오버레이 + 메인 전체 너비

### 다크 모드

| 요소 | 라이트 | 다크 |
|---|---|---|
| 배경 (사이드바) | `#F2F2F7` | `#1C1C1E` |
| 배경 (메인) | `#FFFFFF` | `#000000` |
| 카드 배경 | `#FFFFFF` | `#2C2C2E` |
| 텍스트 (기본) | `#1C1C1E` | `#FFFFFF` |
| 텍스트 (보조) | `#8E8E93` | `#8E8E93` |
| 구분선 | `#E5E5EA` | `#38383A` |
| 선택 하이라이트 | `#E5E5EA` | `#3A3A3C` |

---

## 6. 프론트엔드 구조

### 주요 페이지/컴포넌트
```
frontend/
├── app/
│   ├── layout.tsx            # 루트 레이아웃 (폰트, 다크모드 provider)
│   └── page.tsx              # 메인 SPA 페이지
├── components/
│   ├── Sidebar.tsx           # 사이드바 전체 (검색 + 스마트리스트 + 사용자리스트)
│   ├── SmartListCards.tsx    # 스마트 리스트 2x3 그리드 카드
│   ├── ListItem.tsx          # 사이드바의 개별 리스트 항목
│   ├── ListFormModal.tsx     # 리스트 생성/수정 모달 (색상 팔레트 포함)
│   ├── MainContent.tsx       # 메인 영역 (헤더 + 리마인더 목록)
│   ├── ReminderItem.tsx      # 개별 리마인더 행 (체크박스 + 제목 + 메모 + 플래그)
│   ├── ReminderDetail.tsx    # 리마인더 상세 편집 패널
│   ├── InlineReminderInput.tsx # 인라인 새 리마인더 입력
│   └── SearchBar.tsx         # 검색 입력 필드
├── lib/
│   └── api.ts                # API 호출 함수
└── types/
    └── index.ts              # TypeScript 타입 정의
```

---

## 6. 마일스톤

### M1: Backend API (기반)
- Entity, Repository, Service, Controller 구현
- H2 설정 및 초기 데이터
- API 테스트

### M2: Frontend 기본 구조
- Next.js 프로젝트 생성
- 레이아웃 및 사이드바
- API 연동

### M3: 리마인더 CRUD
- 리마인더 생성/수정/삭제/완료 토글 UI
- 스마트 리스트 필터링

### M4: 마무리
- 검색 기능
- 반응형 디자인
- 에러 핸들링
