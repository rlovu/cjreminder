import {
  Reminder,
  ReminderList,
  SmartListType,
  SummaryResponse,
  Priority,
} from "@/types";

const API = process.env.NEXT_PUBLIC_API_URL || "http://localhost:8080/api";

async function request<T>(path: string, options?: RequestInit): Promise<T> {
  const res = await fetch(`${API}${path}`, {
    headers: { "Content-Type": "application/json" },
    ...options,
  });
  if (res.status === 204) return undefined as T;
  if (!res.ok) throw new Error(`API error: ${res.status}`);
  return res.json();
}

export const listApi = {
  findAll: () => request<ReminderList[]>("/lists"),
  findById: (id: number) => request<ReminderList>(`/lists/${id}`),
  create: (name: string, color: string) =>
    request<ReminderList>("/lists", {
      method: "POST",
      body: JSON.stringify({ name, color }),
    }),
  update: (id: number, name: string, color: string) =>
    request<ReminderList>(`/lists/${id}`, {
      method: "PUT",
      body: JSON.stringify({ name, color }),
    }),
  delete: (id: number) =>
    request<void>(`/lists/${id}`, { method: "DELETE" }),
};

export const reminderApi = {
  findByListId: (listId: number) =>
    request<Reminder[]>(`/lists/${listId}/reminders`),
  create: (listId: number, title: string) =>
    request<Reminder>(`/lists/${listId}/reminders`, {
      method: "POST",
      body: JSON.stringify({ title }),
    }),
  update: (
    id: number,
    data: {
      title: string;
      notes?: string | null;
      dueDate?: string | null;
      priority?: Priority;
      flagged?: boolean;
      listId?: number;
    }
  ) =>
    request<Reminder>(`/reminders/${id}`, {
      method: "PUT",
      body: JSON.stringify(data),
    }),
  delete: (id: number) =>
    request<void>(`/reminders/${id}`, { method: "DELETE" }),
  toggleComplete: (id: number) =>
    request<Reminder>(`/reminders/${id}/toggle`, { method: "PATCH" }),
  toggleFlag: (id: number) =>
    request<Reminder>(`/reminders/${id}/flag`, { method: "PATCH" }),
  findSmart: (type: SmartListType) =>
    request<Reminder[]>(`/reminders/${type}`),
  search: (q: string) =>
    request<Reminder[]>(`/reminders/search?q=${encodeURIComponent(q)}`),
  summary: () => request<SummaryResponse>("/reminders/summary"),
};
