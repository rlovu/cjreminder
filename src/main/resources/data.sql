INSERT INTO reminder_list (name, color, created_at, updated_at) VALUES
('개인', '#007AFF', NOW(), NOW()),
('업무', '#FF3B30', NOW(), NOW()),
('쇼핑', '#34C759', NOW(), NOW());

INSERT INTO reminder (title, notes, due_date, priority, flagged, completed, completed_at, list_id, created_at, updated_at) VALUES
('운동하기', '헬스장 가기', CURRENT_DATE, 'HIGH', false, false, NULL, 1, NOW(), NOW()),
('책 읽기', '클린 코드 3장', DATEADD('DAY', 1, CURRENT_DATE), 'MEDIUM', false, false, NULL, 1, NOW(), NOW()),
('병원 예약', NULL, CURRENT_DATE, 'HIGH', true, false, NULL, 1, NOW(), NOW()),
('프로젝트 리뷰', '스프린트 회고 준비', CURRENT_DATE, 'HIGH', false, false, NULL, 2, NOW(), NOW()),
('회의록 정리', NULL, DATEADD('DAY', 2, CURRENT_DATE), 'LOW', false, false, NULL, 2, NOW(), NOW()),
('코드 리뷰', 'PR #42 리뷰', NULL, 'MEDIUM', true, false, NULL, 2, NOW(), NOW()),
('배포 준비', '스테이징 환경 점검', DATEADD('DAY', 3, CURRENT_DATE), 'NONE', false, false, NULL, 2, NOW(), NOW()),
('우유 사기', NULL, NULL, 'NONE', false, false, NULL, 3, NOW(), NOW()),
('세제 구매', '대용량으로', DATEADD('DAY', 1, CURRENT_DATE), 'LOW', false, false, NULL, 3, NOW(), NOW()),
('선물 포장', '생일 선물', CURRENT_DATE, 'MEDIUM', true, true, NOW(), 1, NOW(), NOW());
