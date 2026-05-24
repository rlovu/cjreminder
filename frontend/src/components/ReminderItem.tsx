"use client";

import { Reminder } from "@/types";
import { Flag, Trash2 } from "lucide-react";
import { useState } from "react";

interface ReminderItemProps {
  reminder: Reminder;
  listColor: string;
  onToggleComplete: (id: number) => void;
  onToggleFlag: (id: number) => void;
  onDelete: (id: number) => void;
  onClick: (reminder: Reminder) => void;
}

const priorityMarks: Record<string, string> = {
  LOW: "!",
  MEDIUM: "!!",
  HIGH: "!!!",
};

export default function ReminderItem({
  reminder,
  listColor,
  onToggleComplete,
  onToggleFlag,
  onDelete,
  onClick,
}: ReminderItemProps) {
  const [fading, setFading] = useState(false);

  const handleToggle = () => {
    if (!reminder.completed) {
      setFading(true);
      setTimeout(() => onToggleComplete(reminder.id), 500);
    } else {
      onToggleComplete(reminder.id);
    }
  };

  const isOverdue =
    reminder.dueDate &&
    !reminder.completed &&
    new Date(reminder.dueDate) < new Date(new Date().toDateString());

  return (
    <li
      className={`group flex items-start gap-3 py-2 border-b border-[#E5E5EA] dark:border-[#38383A] transition-opacity duration-300 ${
        fading ? "opacity-0" : "opacity-100"
      }`}
    >
      <button onClick={handleToggle} className="mt-0.5 flex-shrink-0">
        <div
          className={`w-5 h-5 rounded-full border-2 transition-all duration-300 flex items-center justify-center ${
            reminder.completed || fading ? "border-transparent" : ""
          }`}
          style={{
            borderColor: reminder.completed || fading ? "transparent" : listColor,
            backgroundColor: reminder.completed || fading ? listColor : "transparent",
          }}
        >
          {(reminder.completed || fading) && (
            <svg className="w-3 h-3 text-white" fill="none" viewBox="0 0 24 24" stroke="currentColor" strokeWidth={3}>
              <path strokeLinecap="round" strokeLinejoin="round" d="M5 13l4 4L19 7" />
            </svg>
          )}
        </div>
      </button>

      <div
        className="flex-1 min-w-0 cursor-pointer"
        onClick={() => onClick(reminder)}
      >
        <div className="flex items-center gap-1">
          {reminder.priority !== "NONE" && (
            <span className="text-xs font-bold" style={{ color: listColor }}>
              {priorityMarks[reminder.priority]}
            </span>
          )}
          <span
            className={`text-sm ${
              reminder.completed
                ? "line-through text-[#8E8E93]"
                : "text-[#1C1C1E] dark:text-white"
            }`}
          >
            {reminder.title}
          </span>
        </div>
        {reminder.notes && (
          <p className="text-xs text-[#8E8E93] truncate">{reminder.notes}</p>
        )}
        {reminder.dueDate && (
          <p className={`text-xs ${isOverdue ? "text-[#FF3B30]" : "text-[#8E8E93]"}`}>
            {new Date(reminder.dueDate).toLocaleDateString("ko-KR")}
          </p>
        )}
      </div>

      <div className="flex items-center gap-1 flex-shrink-0">
        {reminder.flagged && (
          <Flag className="w-4 h-4 text-[#FF9500] fill-[#FF9500]" />
        )}
        <button
          onClick={() => onToggleFlag(reminder.id)}
          className={`opacity-0 group-hover:opacity-100 transition-opacity ${
            reminder.flagged ? "hidden" : ""
          }`}
        >
          <Flag className="w-4 h-4 text-[#C7C7CC] hover:text-[#FF9500]" />
        </button>
        <button
          onClick={() => onDelete(reminder.id)}
          className="opacity-0 group-hover:opacity-100 transition-opacity"
        >
          <Trash2 className="w-4 h-4 text-[#C7C7CC] hover:text-[#FF3B30]" />
        </button>
      </div>
    </li>
  );
}
