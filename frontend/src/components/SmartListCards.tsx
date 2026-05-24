"use client";

import {
  Calendar,
  CalendarClock,
  Inbox,
  CheckCircle2,
  Flag,
} from "lucide-react";
import { SummaryResponse, SmartListType, Selection } from "@/types";

const smartLists: {
  type: SmartListType;
  label: string;
  color: string;
  icon: React.ElementType;
}[] = [
  { type: "today", label: "오늘", color: "#007AFF", icon: Calendar },
  { type: "scheduled", label: "예정", color: "#FF3B30", icon: CalendarClock },
  { type: "all", label: "전체", color: "#1C1C1E", icon: Inbox },
  { type: "completed", label: "완료됨", color: "#8E8E93", icon: CheckCircle2 },
  { type: "flagged", label: "플래그", color: "#FF9500", icon: Flag },
];

interface SmartListCardsProps {
  summary: SummaryResponse | null;
  selection: Selection;
  onSelect: (selection: Selection) => void;
}

export default function SmartListCards({
  summary,
  selection,
  onSelect,
}: SmartListCardsProps) {
  const getCount = (type: SmartListType): number => {
    if (!summary) return 0;
    const map: Record<SmartListType, number> = {
      today: summary.todayCount,
      scheduled: summary.scheduledCount,
      all: summary.allCount,
      completed: summary.completedCount,
      flagged: summary.flaggedCount,
    };
    return map[type];
  };

  return (
    <div className="grid grid-cols-2 gap-2 px-3 pb-2">
      {smartLists.map(({ type, label, color, icon: Icon }) => {
        const isSelected =
          selection.type === "smart" && selection.id === type;
        return (
          <button
            key={type}
            onClick={() => onSelect({ type: "smart", id: type })}
            className={`flex flex-col items-start p-3 rounded-xl transition-colors duration-150 ${
              isSelected ? "bg-[#D1D1D6] dark:bg-[#3A3A3C]" : "bg-white dark:bg-[#2C2C2E] hover:bg-[#F2F2F7] dark:hover:bg-[#3A3A3C]"
            }`}
          >
            <div className="flex items-center justify-between w-full mb-1">
              <div
                className="w-7 h-7 rounded-full flex items-center justify-center"
                style={{ backgroundColor: color }}
              >
                <Icon className="w-4 h-4 text-white" />
              </div>
              <span className="text-xl font-bold text-[#1C1C1E] dark:text-white">
                {getCount(type)}
              </span>
            </div>
            <span className="text-xs font-medium text-[#8E8E93]">{label}</span>
          </button>
        );
      })}
    </div>
  );
}
