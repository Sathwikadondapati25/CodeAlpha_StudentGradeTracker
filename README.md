````md
# 🎓 GradeBook PRO EDITION

A professional Java Swing desktop dashboard to manage student grades with Login/Logout, CRUD, Analytics, and Permanent Storage (file-based).

---

## Features

### Login + Logout System
- Admin login page
- Username/password validation
- Logout from menu

### Student Management (CRUD)
- Add student
- Edit selected student
- Delete selected student
- Dynamic search by name
- Student profile popup (double-click row)

### Grade System
- Auto grade calculation (A, B, C, F)
- Pass/Fail status
- Average / Highest / Lowest

### Dashboard Analytics
- Total students card
- Pass percentage card
- Grade distribution bars
- Performance statistics cards

### Permanent Storage (File Handling)
- Auto-load on startup
- Auto-save on every change
- Stores data in `data/students.csv`

### Export
- Export CSV
- Export PDF (simple 1-page report)

### Dark Mode
- Toggle from menu: `View → Toggle Dark Mode`

---

## Tech Stack

- Java
- Java Swing
- JFreeChart
- ArrayList
- OOP Concepts
- File Handling

---

## Folder Structure (GitHub Upload)

```text
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
│  └─ GradeTrackerGUI.java
├─ scripts/
│  └─ run_gui.bat
├─ Main.java
└─ README.md
````

---

## How to Run (Windows / VS Code)

1. Install Java JDK 17+
2. Open terminal in project folder
3. Run:

```bat
scripts\run_gui.bat
```

---

## Admin Login

Admin credentials are securely stored in:

* `data/admin.properties`

On first launch, the application allows the administrator to create custom login credentials.

---

## Data Storage

Student records are stored in:

* `data/students.csv`

The app auto-loads this file at startup and auto-saves after changes.

---

## Export

Use menu:

* `File → Export CSV...`
* `File → Export PDF...`

---

## Beginner Explanation (Frontend + Backend in Java)

* Frontend (UI): Swing screens (`LoginFrame`, `DashboardFrame`, `GradeBookDashboardPanel`)
* Backend (Logic): `StudentService` (ArrayList + analytics)
* Storage Layer: `FileStore` (CSV read/write + admin credentials file)
* Utilities: `Validator`, `CsvUtil`, `PdfExporter`, `ThemeManager`

---

## Sample Report Output (inside the app)

```text
========================================
          GRADEBOOK SUMMARY REPORT
========================================

Total Students : 3
Average Marks  : 70.33
Pass Percentage: 66.67%
Highest Marks  : sath(100)
Lowest Marks   : nitin(35)

--- All Students ---
1) sath           100 Grade: A
2) shiv            76 Grade: B
3) nitin           35 Grade: F
```

---

## Notes

* Marks are restricted to 0–100 using a spinner.
* This is a 100% Java desktop app (no web frontend/backend).

---

## Screenshots

### Login Screen

![Login Screen](screenshots/login-screen.png)

### Main Dashboard

![Main Dashboard](screenshots/main-dashboard.png)

### Export Menu

![Export Menu](screenshots/export-menu.png)

### Student Profile

![Student Profile](screenshots/student-profile.png)

### Search Feature

![Search Feature](screenshots/live-search.png)

### Edit Student

![Edit Student](screenshots/edit-student.png)

### Delete Confirmation

![Delete Confirmation](screenshots/delete-confirmation.png)

### Logout Popup

![Logout Popup](screenshots/logout-confirmation.png)

### Invalid Login Popup

![Invalid Login Popup](screenshots/invalid-login.png)

---

## Future Improvements

* MySQL database integration
* Advanced PDF report generation
* Attendance management
* Student profile images
* Leaderboard system
* Semester-wise analytics
* Role-based authentication

---

## Project Purpose

This project was developed as a professional Java Swing desktop application for:

* Internship showcase
* GitHub portfolio
* Resume projects
* Mini/Major project demonstration
* Learning Java GUI + OOP concepts

---

## Developer Information

**Sathwikadondapati25**
B.Tech CSE Student
Java & Software Development Enthusiast

```
```
