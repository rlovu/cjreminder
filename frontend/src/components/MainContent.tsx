"use client";

import { Reminder, Selection, SmartListType, ReminderList } from "@/types";
import ReminderItem from "./ReminderItem";
import InlineReminderInput from "./InlineReminderInput";

const smartListLabels: Record<SmartListType, string> = {
  today: "오늘",
  scheduled: "예정",
  all: "전체",
  completed: "완료됨",
  flagged: "플래그 지정됨",
};

const smartListColors: Record<SmartListType, string> = {
  today: "#007AFF",
  scheduled: "#FF3B30",
  all: "#1C1C1E",
  completed: "#8E8E93",
  flagged: "#FF9500",
};

interface MainContentProps {
  selection: Selection;
  lists: ReminderList[];
  reminders: Reminder[];
  onToggleComplete: (id: number) => void;
  onToggleFlag: (id: number) => void;
  onDelete: (id: number) => void;
  onSelect: (reminder: Reminder) => void;
  onCreateReminder: (title: string) => void;
  isSearching: boolean;
}

export default function MainContent({
  selection,
  lists,
  reminders,
  onToggleComplete,
  onToggleFlag,
  onDelete,
  onSelect,
  onCreateReminder,
  isSearching,
}: MainContentProps) {
  let title: string;
  let color: string;
  let showInput = false;

  if (isSearching) {
    title = "검색 결과";
    color = "#1C1C1E";
  } else if (selection.type === "smart") {
    const smartType = selection.id as SmartListType;
    title = smartListLabels[smartType];
    color = smartListColors[smartType];
  } else {
    const list = lists.find((l) => l.id === selection.id);
    title = list?.name || "";
    color = list?.color || "#1C1C1E";
    showInput = true;
  }

  return (
    <main className="flex-1 h-full bg-white flex flex-col overflow-hidden">
      <div className="px-6 pt-6 pb-2">
        <h1 className="text-2xl font-bold" style={{ color }}>
          {title}
        </h1>
      </div>

      <div className="flex-1 overflow-y-auto px-6">
        {reminders.length === 0 ? (
          <p className="text-[#8E8E93] text-sm mt-8 text-center">
            리마인더가 없습니다.
          </p>
        ) : (
          <ul>
            {reminders.map((reminder) => (
              <ReminderItem
                key={reminder.id}
                reminder={reminder}
                listColor={color}
                onToggleComplete={onToggleComplete}
                onToggleFlag={onToggleFlag}
                onDelete={onDelete}
                onClick={onSelect}
              />
            ))}
          </ul>
        )}
      </div>

      {showInput && (
        <InlineReminderInput color={color} onSubmit={onCreateReminder} />
      )}
    </main>
  );
}
