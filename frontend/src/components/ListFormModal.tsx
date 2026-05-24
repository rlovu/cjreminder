"use client";

import { X } from "lucide-react";
import { useState, useEffect } from "react";

const COLORS = [
  "#FF3B30", "#FF9500", "#FFCC00", "#34C759", "#00C7BE", "#007AFF",
  "#5856D6", "#AF52DE", "#FF2D55", "#A2845E", "#8E8E93", "#1C1C1E",
];

interface ListFormModalProps {
  isOpen: boolean;
  initialName?: string;
  initialColor?: string;
  onSubmit: (name: string, color: string) => void;
  onClose: () => void;
}

export default function ListFormModal({
  isOpen,
  initialName = "",
  initialColor = "#007AFF",
  onSubmit,
  onClose,
}: ListFormModalProps) {
  const [name, setName] = useState(initialName);
  const [color, setColor] = useState(initialColor);

  useEffect(() => {
    setName(initialName);
    setColor(initialColor);
  }, [initialName, initialColor, isOpen]);

  if (!isOpen) return null;

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    const trimmed = name.trim();
    if (trimmed) {
      onSubmit(trimmed, color);
      onClose();
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center">
      <div className="absolute inset-0 bg-black/30 backdrop-blur-sm" onClick={onClose} />
      <div className="relative bg-white rounded-2xl shadow-xl w-[340px] p-6 animate-in fade-in zoom-in duration-200">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-lg font-semibold text-[#1C1C1E]">
            {initialName ? "목록 편집" : "새로운 목록"}
          </h2>
          <button onClick={onClose}>
            <X className="w-5 h-5 text-[#8E8E93]" />
          </button>
        </div>

        <form onSubmit={handleSubmit}>
          <input
            autoFocus
            type="text"
            placeholder="목록 이름"
            value={name}
            onChange={(e) => setName(e.target.value)}
            className="w-full mb-4 px-3 py-2 text-sm border border-[#E5E5EA] rounded-lg outline-none focus:ring-2 focus:ring-[#007AFF]"
          />

          <div className="grid grid-cols-6 gap-3 mb-6">
            {COLORS.map((c) => (
              <button
                key={c}
                type="button"
                onClick={() => setColor(c)}
                className={`w-8 h-8 rounded-full transition-transform ${
                  color === c ? "ring-2 ring-offset-2 ring-[#007AFF] scale-110" : ""
                }`}
                style={{ backgroundColor: c }}
              />
            ))}
          </div>

          <div className="flex gap-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-2 text-sm text-[#8E8E93] bg-[#F2F2F7] rounded-lg hover:bg-[#E5E5EA] transition-colors"
            >
              취소
            </button>
            <button
              type="submit"
              className="flex-1 py-2 text-sm text-white bg-[#007AFF] rounded-lg hover:bg-[#0056B3] transition-colors"
            >
              {initialName ? "저장" : "생성"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}
