"use client";

import { ReminderList, Selection, SummaryResponse } from "@/types";
import SearchBar from "./SearchBar";
import SmartListCards from "./SmartListCards";
import ListItem from "./ListItem";
import { Plus } from "lucide-react";

interface SidebarProps {
  lists: ReminderList[];
  summary: SummaryResponse | null;
  selection: Selection;
  searchQuery: string;
  onSelect: (selection: Selection) => void;
  onSearchChange: (query: string) => void;
  onAddList: () => void;
}

export default function Sidebar({
  lists,
  summary,
  selection,
  searchQuery,
  onSelect,
  onSearchChange,
  onAddList,
}: SidebarProps) {
  return (
    <aside className="w-[280px] h-full bg-[#F2F2F7] flex flex-col border-r border-[#E5E5EA] overflow-y-auto">
      <SearchBar value={searchQuery} onChange={onSearchChange} />
      <SmartListCards summary={summary} selection={selection} onSelect={onSelect} />

      <div className="px-3 pt-3 pb-1">
        <h2 className="text-xs font-semibold text-[#8E8E93] uppercase tracking-wide">
          나의 목록
        </h2>
      </div>

      <div className="flex-1 px-1 pb-2">
        {lists.map((list) => (
          <ListItem
            key={list.id}
            list={list}
            isSelected={selection.type === "list" && selection.id === list.id}
            onSelect={onSelect}
          />
        ))}
      </div>

      <div className="px-3 pb-3">
        <button
          onClick={onAddList}
          className="flex items-center gap-1 text-sm text-[#007AFF] hover:text-[#0056B3] transition-colors"
        >
          <Plus className="w-4 h-4" />
          <span>목록 추가</span>
        </button>
      </div>
    </aside>
  );
}
