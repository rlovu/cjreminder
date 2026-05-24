"use client";

import { useState, useEffect, useCallback } from "react";
import {
  Reminder,
  ReminderList,
  Selection,
  SmartListType,
  SummaryResponse,
} from "@/types";
import { listApi, reminderApi } from "@/lib/api";
import Sidebar from "@/components/Sidebar";
import MainContent from "@/components/MainContent";
import ReminderDetail from "@/components/ReminderDetail";
import ListFormModal from "@/components/ListFormModal";

export default function Home() {
  const [lists, setLists] = useState<ReminderList[]>([]);
  const [summary, setSummary] = useState<SummaryResponse | null>(null);
  const [reminders, setReminders] = useState<Reminder[]>([]);
  const [selection, setSelection] = useState<Selection>({
    type: "smart",
    id: "all",
  });
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedReminder, setSelectedReminder] = useState<Reminder | null>(null);
  const [showListModal, setShowListModal] = useState(false);

  const loadLists = useCallback(async () => {
    const data = await listApi.findAll();
    setLists(data);
  }, []);

  const loadSummary = useCallback(async () => {
    const data = await reminderApi.summary();
    setSummary(data);
  }, []);

  const loadReminders = useCallback(async () => {
    if (searchQuery) return;
    if (selection.type === "smart") {
      const data = await reminderApi.findSmart(selection.id as SmartListType);
      setReminders(data);
    } else {
      const data = await reminderApi.findByListId(selection.id as number);
      setReminders(data);
    }
  }, [selection, searchQuery]);

  const refreshAll = useCallback(async () => {
    await Promise.all([loadLists(), loadSummary(), loadReminders()]);
  }, [loadLists, loadSummary, loadReminders]);

  useEffect(() => {
    loadLists();
    loadSummary();
  }, [loadLists, loadSummary]);

  useEffect(() => {
    loadReminders();
  }, [loadReminders]);

  useEffect(() => {
    if (!searchQuery) {
      loadReminders();
      return;
    }
    const timer = setTimeout(async () => {
      const data = await reminderApi.search(searchQuery);
      setReminders(data);
    }, 300);
    return () => clearTimeout(timer);
  }, [searchQuery, loadReminders]);

  const handleSelect = (sel: Selection) => {
    setSelection(sel);
    setSearchQuery("");
    setSelectedReminder(null);
  };

  const handleToggleComplete = async (id: number) => {
    await reminderApi.toggleComplete(id);
    await refreshAll();
    if (selectedReminder?.id === id) setSelectedReminder(null);
  };

  const handleToggleFlag = async (id: number) => {
    const updated = await reminderApi.toggleFlag(id);
    setReminders((prev) =>
      prev.map((r) => (r.id === updated.id ? updated : r))
    );
    if (selectedReminder?.id === id) setSelectedReminder(updated);
    await loadSummary();
  };

  const handleDelete = async (id: number) => {
    await reminderApi.delete(id);
    if (selectedReminder?.id === id) setSelectedReminder(null);
    await refreshAll();
  };

  const handleCreateReminder = async (title: string) => {
    if (selection.type !== "list") return;
    await reminderApi.create(selection.id as number, title);
    await refreshAll();
  };

  const handleSaveReminder = async (
    id: number,
    data: {
      title: string;
      notes?: string | null;
      dueDate?: string | null;
      priority?: string;
      flagged?: boolean;
      listId?: number;
    }
  ) => {
    const updated = await reminderApi.update(id, {
      ...data,
      priority: data.priority as any,
    });
    setReminders((prev) =>
      prev.map((r) => (r.id === updated.id ? updated : r))
    );
    setSelectedReminder(updated);
    await Promise.all([loadLists(), loadSummary()]);
  };

  const handleCreateList = async (name: string, color: string) => {
    await listApi.create(name, color);
    await loadLists();
  };

  return (
    <div className="h-full flex">
      <Sidebar
        lists={lists}
        summary={summary}
        selection={selection}
        searchQuery={searchQuery}
        onSelect={handleSelect}
        onSearchChange={setSearchQuery}
        onAddList={() => setShowListModal(true)}
      />

      <MainContent
        selection={selection}
        lists={lists}
        reminders={reminders}
        onToggleComplete={handleToggleComplete}
        onToggleFlag={handleToggleFlag}
        onDelete={handleDelete}
        onSelect={setSelectedReminder}
        onCreateReminder={handleCreateReminder}
        isSearching={!!searchQuery}
      />

      {selectedReminder && (
        <ReminderDetail
          reminder={selectedReminder}
          lists={lists}
          onSave={handleSaveReminder}
          onDelete={handleDelete}
          onClose={() => setSelectedReminder(null)}
        />
      )}

      <ListFormModal
        isOpen={showListModal}
        onSubmit={handleCreateList}
        onClose={() => setShowListModal(false)}
      />
    </div>
  );
}
