"use client";

import { Plus } from "lucide-react";
import { useState } from "react";

interface InlineReminderInputProps {
  color: string;
  onSubmit: (title: string) => void;
}

export default function InlineReminderInput({ color, onSubmit }: InlineReminderInputProps) {
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState("");

  const handleSubmit = () => {
    const trimmed = title.trim();
    if (trimmed) {
      onSubmit(trimmed);
      setTitle("");
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent) => {
    if (e.key === "Enter") {
      e.preventDefault();
      handleSubmit();
    } else if (e.key === "Escape") {
      setTitle("");
      setIsEditing(false);
    }
  };

  if (!isEditing) {
    return (
      <div className="px-6 py-3 border-t border-[#E5E5EA]">
        <button
          onClick={() => setIsEditing(true)}
          className="flex items-center gap-1 text-sm font-medium transition-colors"
          style={{ color }}
        >
          <Plus className="w-4 h-4" />
          <span>새로운 미리 알림</span>
        </button>
      </div>
    );
  }

  return (
    <div className="px-6 py-3 border-t border-[#E5E5EA] flex items-center gap-3">
      <div
        className="w-5 h-5 rounded-full border-2 flex-shrink-0"
        style={{ borderColor: color }}
      />
      <input
        autoFocus
        type="text"
        placeholder="제목"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        onKeyDown={handleKeyDown}
        onBlur={() => {
          handleSubmit();
          setIsEditing(false);
        }}
        className="flex-1 text-sm text-[#1C1C1E] outline-none placeholder-[#C7C7CC]"
      />
    </div>
  );
}
