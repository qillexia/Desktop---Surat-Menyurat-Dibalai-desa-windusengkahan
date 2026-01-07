# Implementation Plan - Sistem Digitalisasi Pelayanan Surat Menyurat Desa Windusengkahan (Java Desktop)

## 1. Project Overview
Project ini bertujuan untuk mendigitalisasi proses pelayanan surat menyurat di Balai Desa Windusengkahan menggunakan aplikasi Desktop berbasis Java. Sistem akan menghubungkan Data Warga, Staff Pelayanan, Kepala Desa, dan Admin.

## 2. Architecture & Workflow
- **Platform**: Desktop Application.
- **Language**: Java IO / Java Swing (GUI).
- **Users**:
  - **Admin**: Manajemen User.
  - **Staff**: Input Data, Cetak Surat.
  - **Kepala Desa**: Approval Surat.
- **Data Storage**: **MySQL Database** (via JDBC). Kita akan menggunakan database bernama `db_windusengkahan`.

## 3. Technology Stack
- **Language**: Java (JDK).
- **GUI Framework**: Java Swing (dengan custom styling agar tampilan modern/premium).
- **Design Pattern**: MVC (Model-View-Controller) sederhana.

## 4. Key Features to Implement
### Phase 1: Foundation & UI
- [ ] Setup Java Project structure.
- [ ] Create `MainFrame` (Window Utama).
- [ ] Implement Custom UI Components (Rounded Buttons, Modern Panels) untuk estetika.
- [ ] Desain Layout Login yang menarik.

### Phase 2: Role-Based Views
- [ ] **Login System**: Hardcoded users untuk simulasi (Admin, Staff, Kades).
- [ ] **Dashboard Admin**:
    - Manage User accounts.
- **Dashboard Staff**:
    - Form Input Data Warga.
    - List Permohonan Surat.
- **Dashboard Kepala Desa**:
    - Tabel Approval (Acc/Reject).

### Phase 3: Logic & Printing
- [ ] Logika persetujuan surat.
- [ ] Fitur "Cetak" (Export to PDF atau Simple Printer Job).

## 5. Directory Structure
```
d:/SEMESTER 3/PEMROGRAMAN DESKTOP/PROJECT/
├── src/
│   ├── main/
│   │   ├── Main.java
│   │   ├── model/ (Data Classes: User, Surat, Warga)
│   │   ├── view/  (GUI Frames & Panels: LoginView, DashboardKades, etc)
│   │   ├── controller/ (Logic)
│   │   └── util/ (Helpers: styles, constants)
├── bin/ (Compiled classes)
└── README.md
```
