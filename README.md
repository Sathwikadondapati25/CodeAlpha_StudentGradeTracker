## GradeBook PRO EDITION (Java Swing Dashboard)

A professional **Java Swing** desktop dashboard to manage student grades with **Login/Logout**, **CRUD**, **Analytics**, and **Permanent Storage** (file-based).

### Features
- **Login + Logout System**
  - Admin login page
  - Username/password validation
  - Logout from menu
- **Student Management (CRUD)**
  - Add student
  - Edit selected student
  - Delete selected student
  - Dynamic search by name
  - Student profile popup (double-click row)
- **Grade System**
  - Auto grade calculation (A, B, C, F)
  - Pass/Fail status
  - Average / Highest / Lowest
- **Dashboard Analytics**
  - Total students card
  - Pass percentage card
  - Grade distribution bars
  - Performance statistics cards
- **Permanent Storage (File Handling)**
  - Auto-load on startup
  - Auto-save on every change
  - Stores data in `data/students.csv`
- **Export**
  - Export CSV
  - Export PDF (simple 1-page report)
- **Dark Mode**
  - Toggle from menu: `View → Toggle Dark Mode`

### Tech Stack
- Java (Swing)
- `ArrayList`
- OOP (Classes, Objects, Methods)

### Folder Structure (GitHub Upload)
```
CodeAlpha_StudentGradeTracker/
├─ data/
│  ├─ admin.properties
│  └─ students.csv
├─ src/
│  ├─ Student.java
│  ├─ StudentService.java
│  ├─ LoginFrame.java
│  ├─ DashboardFrame.java
│  ├─ GradeBookDashboardPanel.java
│  ├─ StudentEditDialog.java
│  ├─ StudentProfileDialog.java
│  ├─ FileStore.java
│  ├─ CsvUtil.java
│  ├─ PdfExporter.java
│  ├─ ThemeManager.java
│  ├─ Validator.java
│  └─ GradeTrackerGUI.java   (legacy entry, calls Main)
├─ scripts/
│  └─ run_gui.bat
├─ Main.java
└─ README.md
```

### How to Run (Windows / VS Code)
1. Install **Java JDK 17+**
2. Open terminal in project folder
3. Run:
```bat
scripts\run_gui.bat
```

### Admin Login
Credentials are stored in:
- `data/admin.properties`

Default credentials:
- **Username**: `admin`
- **Password**: `admin123`

### Data Storage
Student records are stored in:
- `data/students.csv`

The app auto-loads this file at startup and auto-saves after changes.

### Export
Use menu:
- `File → Export CSV...`
- `File → Export PDF...`

### Beginner Explanation (Frontend + Backend in Java)
- **Frontend (UI)**: Swing screens (`LoginFrame`, `DashboardFrame`, `GradeBookDashboardPanel`)
- **Backend (Logic)**: `StudentService` (ArrayList + analytics)
- **Storage Layer**: `FileStore` (CSV read/write + admin credentials file)
- **Utilities**: `Validator`, `CsvUtil`, `PdfExporter`, `ThemeManager`

### Sample Report Output (inside the app)
```
========================================
          GRADEBOOK SUMMARY REPORT
========================================

Total Students : 3
Average Marks  : 78.33
Pass Percentage: 66.67%
Highest Marks  : Asha (92)
Lowest Marks   : Ravi (60)

--- All Students ---
 1) Asha                      92  Grade: A
 2) Ravi                      60  Grade: C
 3) Neha                      83  Grade: B
```

### Notes
- Marks are restricted to **0–100** using a spinner.
- This is a **100% Java desktop app** (no web frontend/backend).
