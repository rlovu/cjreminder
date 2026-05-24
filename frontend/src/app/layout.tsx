import type { Metadata } from "next";
import "./globals.css";

export const metadata: Metadata = {
  title: "CJReminder",
  description: "Apple Reminders Web Clone",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="ko" className="h-full antialiased dark:bg-black">
      <body
        className="h-full"
        style={{ fontFamily: "-apple-system, BlinkMacSystemFont, 'Inter', system-ui, sans-serif" }}
      >
        {children}
      </body>
    </html>
  );
}
