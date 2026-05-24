"use client";

import { ReminderList, Selection } from "@/types";

interface ListItemProps {
  list: ReminderList;
  isSelected: boolean;
  onSelect: (selection: Selection) => void;
}

export default function ListItem({ list, isSelected, onSelect }: ListItemProps) {
  return (
    <button
      onClick={() => onSelect({ type: "list", id: list.id })}
      className={`w-full flex items-center gap-3 px-3 py-2 rounded-lg transition-colors duration-150 ${
        isSelected ? "bg-[#E5E5EA]" : "hover:bg-[#F2F2F7]"
      }`}
    >
      <div
        className="w-5 h-5 rounded-full flex-shrink-0"
        style={{ backgroundColor: list.color }}
      />
      <span className="flex-1 text-left text-sm text-[#1C1C1E]">
        {list.name}
      </span>
      <span className="text-sm text-[#8E8E93]">{list.reminderCount}</span>
    </button>
  );
}
