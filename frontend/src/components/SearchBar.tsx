"use client";

import { Search } from "lucide-react";

interface SearchBarProps {
  value: string;
  onChange: (value: string) => void;
}

export default function SearchBar({ value, onChange }: SearchBarProps) {
  return (
    <div className="relative px-3 pt-3 pb-2">
      <Search className="absolute left-6 top-1/2 -translate-y-1/2 mt-[2px] w-4 h-4 text-[#8E8E93]" />
      <input
        type="text"
        placeholder="검색"
        value={value}
        onChange={(e) => onChange(e.target.value)}
        className="w-full pl-9 pr-3 py-[6px] rounded-lg bg-[#E5E5EA] text-sm text-[#1C1C1E] placeholder-[#8E8E93] outline-none focus:ring-2 focus:ring-[#007AFF]"
      />
    </div>
  );
}
