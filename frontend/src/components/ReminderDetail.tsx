"use client";

import { Reminder, Priority, ReminderList } from "@/types";
import { X, Trash2 } from "lucide-react";
import { useState, useEffect } from "react";

interface ReminderDetailProps {
  reminder: Reminder;
  lists: ReminderList[];
  onSave: (
    id: number,
    data: {
      title: string;
      notes?: string | null;
      dueDate?: string | null;
      priority?: Priority;
      flagged?: boolean;
      listId?: number;
    }
  ) => void;
  onDelete: (id: number) => void;
  onClose: () => void;
}

const priorityLabels: Record<Priority, string> = {
  NONE: "없음",
  LOW: "낮음",
  MEDIUM: "보통",
  HIGH: "높음",
};

export default function ReminderDetail({
  reminder,
  lists,
  onSave,
  onDelete,
  onClose,
}: ReminderDetailProps) {
  const [title, setTitle] = useState(reminder.title);
  const [notes, setNotes] = useState(reminder.notes || "");
  const [dueDate, setDueDate] = useState(reminder.dueDate || "");
  const [priority, setPriority] = useState<Priority>(reminder.priority);
  const [flagged, setFlagged] = useState(reminder.flagged);
  const [listId, setListId] = useState(reminder.listId);

  useEffect(() => {
    setTitle(reminder.title);
    setNotes(reminder.notes || "");
    setDueDate(reminder.dueDate || "");
    setPriority(reminder.priority);
    setFlagged(reminder.flagged);
    setListId(reminder.listId);
  }, [reminder]);

  const handleSave = () => {
    onSave(reminder.id, {
      title,
      notes: notes || null,
      dueDate: dueDate || null,
      priority,
      flagged,
      listId,
    });
  };

  return (
    <div className="w-[320px] h-full bg-white dark:bg-[#1C1C1E] border-l border-[#E5E5EA] dark:border-[#38383A] flex flex-col overflow-y-auto">
      <div className="flex items-center justify-between px-4 py-3 border-b border-[#E5E5EA] dark:border-[#38383A]">
        <h2 className="text-sm font-semibold text-[#1C1C1E] dark:text-white">상세</h2>
        <button onClick={onClose}>
          <X className="w-5 h-5 text-[#8E8E93] hover:text-[#1C1C1E]" />
        </button>
      </div>

      <div className="flex-1 p-4 space-y-4">
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          onBlur={handleSave}
          className="w-full text-sm font-medium text-[#1C1C1E] border border-[#E5E5EA] dark:border-[#38383A] rounded-lg px-3 py-2 outline-none focus:ring-2 focus:ring-[#007AFF] dark:bg-[#2C2C2E] dark:text-white"
        />

        <textarea
          placeholder="메모"
          value={notes}
          onChange={(e) => setNotes(e.target.value)}
          onBlur={handleSave}
          rows={3}
          className="w-full text-sm text-[#1C1C1E] border border-[#E5E5EA] dark:border-[#38383A] rounded-lg px-3 py-2 outline-none focus:ring-2 focus:ring-[#007AFF] dark:bg-[#2C2C2E] dark:text-white resize-none"
        />

        <div>
          <label className="block text-xs text-[#8E8E93] mb-1">기한</label>
          <input
            type="date"
            value={dueDate}
            onChange={(e) => {
              setDueDate(e.target.value);
              setTimeout(handleSave, 0);
            }}
            className="w-full text-sm text-[#1C1C1E] border border-[#E5E5EA] dark:border-[#38383A] rounded-lg px-3 py-2 outline-none focus:ring-2 focus:ring-[#007AFF] dark:bg-[#2C2C2E] dark:text-white"
          />
        </div>

        <div>
          <label className="block text-xs text-[#8E8E93] mb-1">우선순위</label>
          <select
            value={priority}
            onChange={(e) => {
              setPriority(e.target.value as Priority);
              setTimeout(handleSave, 0);
            }}
            className="w-full text-sm text-[#1C1C1E] border border-[#E5E5EA] dark:border-[#38383A] rounded-lg px-3 py-2 outline-none focus:ring-2 focus:ring-[#007AFF] dark:bg-[#2C2C2E] dark:text-white"
          >
            {(Object.keys(priorityLabels) as Priority[]).map((p) => (
              <option key={p} value={p}>
                {priorityLabels[p]}
              </option>
            ))}
          </select>
        </div>

        <div className="flex items-center justify-between">
          <label className="text-sm text-[#1C1C1E] dark:text-white">플래그</label>
          <button
            onClick={() => {
              setFlagged(!flagged);
              setTimeout(handleSave, 0);
            }}
            className={`w-10 h-6 rounded-full transition-colors ${
              flagged ? "bg-[#FF9500]" : "bg-[#E5E5EA]"
            }`}
          >
            <div
              className={`w-5 h-5 bg-white rounded-full shadow transition-transform ${
                flagged ? "translate-x-[18px]" : "translate-x-[2px]"
              }`}
            />
          </button>
        </div>

        <div>
          <label className="block text-xs text-[#8E8E93] mb-1">목록</label>
          <select
            value={listId}
            onChange={(e) => {
              setListId(Number(e.target.value));
              setTimeout(handleSave, 0);
            }}
            className="w-full text-sm text-[#1C1C1E] border border-[#E5E5EA] dark:border-[#38383A] rounded-lg px-3 py-2 outline-none focus:ring-2 focus:ring-[#007AFF] dark:bg-[#2C2C2E] dark:text-white"
          >
            {lists.map((l) => (
              <option key={l.id} value={l.id}>
                {l.name}
              </option>
            ))}
          </select>
        </div>
      </div>

      <div className="p-4 border-t border-[#E5E5EA] dark:border-[#38383A]">
        <button
          onClick={() => onDelete(reminder.id)}
          className="flex items-center gap-1 text-sm text-[#FF3B30] hover:text-[#CC2D25] transition-colors"
        >
          <Trash2 className="w-4 h-4" />
          <span>삭제</span>
        </button>
      </div>
    </div>
  );
}
