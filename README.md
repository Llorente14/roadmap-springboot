# AlterEgo API — Spring Boot Lock-In Training

Kit belajar Spring Boot. Kamu membangun **backend REST untuk data asli Alter Ego Esports** (roster, prestasi, statistik, merchandise) lewat 26 quest berurutan, dengan test yang masih merah sebagai wasit. Sebelum itu ada 14 pelajaran **Fondasi** (F00–F13) untuk Java dari nol.

**Tidak ada kode solusi di repo ini.** Semua implementasi kamu tulis sendiri. Pelajaran Fondasi hanya memberi contoh konsep dengan data lain.

## Mulai di sini

1. Buka **`quest-board/index.html`** di browser dan letakkan di samping IDE.
2. Baca **`docs/GUIDE.md`**: alur kerja, cara menulis kode, cara validasi, protokol debugging, aturan memakai AI.
3. Baca **`docs/BLUEPRINT.md`**: arsitektur, model data, kontrak API, dan keputusan desain.
4. Jalankan pelajaran pertama:

   ```powershell
   .\mvnw.cmd test "-Dtest=F00*"
   ```

   Merah = normal. Buat jadi hijau.

## Isi repo

| Path | Isi | Boleh diubah? |
|---|---|---|
| `src/main/java/com/alterego/fondasi/` | Latihan Fondasi (`TODO Fnn`) | **Ya**: ini tugasmu |
| `src/main/java/com/alterego/warmup/` | Stub Level 0 (`TODO Qnn`) | **Ya**: ini tugasmu |
| `src/main/java/com/alterego/api/` | Aplikasi Spring (masih kosong) | **Ya**: ini tugasmu |
| `src/main/resources/seed/` | Data asli AlterEgo (JSON) | Tidak perlu |
| `src/test/**` | Test semua quest + helper | **Tidak** |
| `docs/` | BLUEPRINT, GUIDE | Boleh ditambah catatan |
| `quest-board/` | Papan quest + jurnal | Tidak perlu |

## Stack

Java 17 · Spring Boot 4.1 · Maven wrapper · MongoDB (Atlas untuk dev, embedded untuk test) · Bean Validation · springdoc (Q26)

## Status

`.\mvnw.cmd test` → semua quest hijau = MVP selesai.
