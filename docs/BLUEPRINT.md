# BLUEPRINT — AlterEgo API

> Langkah 1 dari metode Manware: **Blueprint**. Rencanakan arsitektur, data, dan logika inti sebelum menulis kode.
> Dokumen ini adalah kontrak. Test di `src/test` memeriksa isi dokumen ini. Kalau dokumen dan test berbeda, **test yang benar**. Laporkan perbedaannya supaya dokumen bisa diperbaiki.

---

## 1. Apa yang dibangun

Backend REST untuk data **Alter Ego Esports**: roster pemain, prestasi turnamen, statistik, dan produk merchandise.
Datanya data asli dari situs AlterEgo (`D:\Axel\Coding\AlterEgo\alterego_web\data`). Data itu disalin ke `src/main/resources/seed/`.

Hasil akhir MVP: API yang bisa dipanggil situs Astro AlterEgo, didokumentasikan otomatis di Swagger UI.

**Bukan MVP (fase 2):** login/auth, upload file impor, deploy, frontend, converter dokumen.

## 2. Stack

| Bagian | Pilihan | Alasan |
|---|---|---|
| Bahasa | Java 17 | Sudah terpasang, minimum Spring Boot 4 |
| Framework | Spring Boot 4.1.x | Versi stabil terbaru |
| Build | Maven wrapper (`mvnw`) | Tidak perlu memasang Maven |
| Database | MongoDB (Atlas untuk dev, embedded untuk test) | Data sumbernya JSON, cocok dengan model dokumen |
| Validasi | Jakarta Bean Validation | Standar Spring |
| Dokumentasi API | springdoc-openapi (dipasang di Q26) | Swagger UI otomatis |

## 3. Arsitektur

```
HTTP request
   │
   ▼
Controller      ← menerima request, validasi input, mengubah DTO <-> domain
   │
   ▼
Service         ← logika bisnis (aturan, perhitungan, keputusan)
   │
   ▼
Repository      ← akses MongoDB (Spring Data)
   │
   ▼
MongoDB
```

Aturan lapisan:
- Controller **tidak** memanggil Repository langsung untuk logika yang lebih dari sekadar ambil data. Logika masuk Service.
- Document (class yang disimpan ke Mongo) **tidak pernah** dikirim langsung sebagai respons. Selalu lewat DTO.
- Satu package per fitur, bukan per lapisan:

```
com.alterego.api
├── AlteregoApiApplication.java
├── common/        ← exception, error handler, validasi game
├── seed/          ← SeedProperties, DataSeeder
├── player/        ← Player, PlayerRepository, PlayerService, PlayerController, DTO
├── achievement/   ← Achievement, AchievementRepository, ...
├── stats/         ← StatsService, StatsController
└── product/       ← Product, ProductRepository, ...
com.alterego.warmup  ← Level 0 (Java murni, boleh dihapus setelah selesai)
```

Nama class yang **wajib** (karena diperiksa test): `seed.SeedProperties`, `seed.DataSeeder`, `player.PlayerRepository`, `achievement.AchievementRepository`. Nama class lain bebas.

## 4. Model data (MongoDB)

### `players`

| Field | Tipe | Catatan |
|---|---|---|
| `_id` | String | **Gabungan `game:playerId`**, mis. `mlbb:nino`. Lihat keputusan D1 |
| `playerId` | String | Slug asli dari data, mis. `nino` |
| `game` | String | `mlbb` / `pubgm` / `cs2` |
| `nickname` | String | Wajib, tidak boleh kosong |
| `fullName` | String? | |
| `role` | String? | Boleh null (pemain PUBGM tidak punya role) |
| `type` | String | `player` / `staff` |
| `status` | String | `active` / `inactive` |
| `division` | String? | |
| `joinDate` | Date? | Disimpan sebagai tanggal, **bukan string**. Lihat D3 |
| `displayOrder` | int | Urutan tampil |
| `profileImg` | String? | URL |

Field lain di JSON (`previousTeams`, `gameSpecific`, `sources`, dst.) **tidak** disimpan di MVP.

### `achievements`

| Field | Tipe | Catatan |
|---|---|---|
| `_id` | String | `id` dari JSON, sudah unik |
| `game`, `division`, `tournament`, `tier` | String | |
| `startDate`, `endDate` | Date? | `startDate` bisa null di data asli. Lihat D4 |
| `placement` | String | `"1"`, `"2"`, `"5-8"`, `"1-2"`, … tetap string |
| `placementLabel` | String | |
| `prize` | Objek tertanam `{ amount: Double?, currency: String }` atau null | **Embedded** |
| `rosterIds` | String[] | **Referensi** ke `playerId` di game yang sama |
| `highlight` | boolean | |

### `products`

| Field | Tipe | Catatan |
|---|---|---|
| `_id` | String | Slug dari `nama` (aturan Q01) |
| `name` | String | |
| `priceMin`, `priceMax` | long | Rupiah, hasil parsing `harga` (aturan Q05) |
| `description` | String | |
| `imageUrl` | String? | **Elemen pertama** dari array `gambar` |

### Ringkasan yang tertulis di file (`summary`)

File achievement punya objek `summary` (`titles`, `runnerUps`, `totalPrizeUSD`, …). Simpan apa adanya per game, karena Q24 membandingkannya dengan hasil hitung. Tempat dan cara penyimpanannya bebas kamu tentukan.

## 5. Keputusan desain (dan alasannya)

**D1. `_id` pemain = `game:playerId`.** Data asli punya `aces` di MLBB (Head of Esports) dan `aces` di PUBGM (Manager). Kalau `_id = playerId`, salah satunya tertimpa. Alternatifnya `_id` ObjectId + unique compound index (`game`, `playerId`). Pilihan itu lebih "benar" untuk skala besar, tapi upsert-nya lebih rumit. Untuk MVP dipilih kunci gabungan.

**D2. `prize` embedded, `rosterIds` referensi.** Hadiah hanya punya arti di dalam satu prestasi dan selalu dibaca bersama, jadi di-embed. Pemain adalah entitas sendiri yang berubah sendiri, jadi direferensikan lewat id. Ini aturan umum pemodelan dokumen: *embed* kalau dimiliki dan dibaca bersama, *referensi* kalau berdiri sendiri.

**D3. Tanggal tidak lengkap.** `cs2/whyphy` punya `joinDate: "2021-05"`. Aturannya: format `yyyy-MM` disimpan sebagai tanggal 1 bulan itu (`2021-05-01`). Ini kompromi yang disadari. Presisinya hilang, tapi tipenya konsisten.

**D4. Prestasi tanpa `startDate`.** `pubgm/pmpl-indonesia-fall-2022` tidak punya tanggal. Prestasi ini tetap disimpan dan dihitung di `tournaments`, tapi tidak masuk `byYear`, tidak ikut filter `year`, dan diurutkan paling akhir.

**D5. Seed = sumber kebenaran untuk id yang ada di file.** Seeder melakukan *upsert*. Kalau data pemain hasil seed diubah lewat API, perubahan itu ditimpa lagi saat aplikasi restart. Pemain yang dibuat lewat API (id yang tidak ada di file) tidak disentuh.

**D6. Seed rusak = gagal start.** Lebih baik aplikasi menolak menyala daripada diam-diam menyimpan data rusak. Pesan error wajib menyebut file dan field.

**D7. Game valid hanya `mlbb`, `pubgm`, `cs2`.** Nilai lain di parameter atau path menghasilkan **400**, bukan 404. Alasannya, permintaan seperti itu salah bentuk, bukan "data tidak ditemukan".

## 6. Kontrak API (`/api/v1`)

Semua respons JSON. Tanggal dikirim sebagai `"yyyy-MM-dd"`. Semua error memakai `application/problem+json` (Q22).

### Publik

| Method | Path | Query | Respons |
|---|---|---|---|
| GET | `/ping` | | `{ "status": "ok", "app": "<spring.application.name>" }` |
| GET | `/players` | `game?`, `status?` | `PlayerDto[]`, urut `displayOrder` (tanpa `game`: urut game lalu displayOrder) |
| GET | `/players/{game}/{id}` | | `PlayerDto` / 404 |
| GET | `/players/{game}/{id}/achievements` | | `AchievementDto[]` (bisa kosong) / 404 kalau pemain tidak ada |
| GET | `/achievements` | `game?`, `year?`, `placement?`, `page=0`, `size=10` (maks 50) | `PageResponse<AchievementDto>` |
| GET | `/stats/{game}` | | `StatsDto` |
| GET | `/stats/{game}/check` | | `SummaryCheckDto` |
| GET | `/products` | `maxPrice?`, `sort=name\|price_asc\|price_desc` | `ProductDto[]` |
| GET | `/products/{slug}` | | `ProductDto` / 404 |

### Admin (belum dikunci di MVP)

| Method | Path | Body | Respons |
|---|---|---|---|
| POST | `/admin/players` | `PlayerRequest` | 201 + header `Location: /api/v1/players/{game}/{id}` + `PlayerDto` · 400 · 409 |
| PUT | `/admin/players/{game}/{id}` | `PlayerRequest` (id & game diambil dari path) | 200 `PlayerDto` · 400 · 404 |
| DELETE | `/admin/players/{game}/{id}` | | 204 · 404 |

### Bentuk data

```jsonc
// PlayerDto
{ "id": "nino", "game": "mlbb", "nickname": "NINO", "fullName": "...", "role": "EXP Laner",
  "type": "player", "status": "active", "division": "Alter Ego", "joinDate": "2024-07-30",
  "displayOrder": 1, "profileImg": "https://..." }
// Catatan: TIDAK ada "_id" atau "playerId" di respons.

// PlayerRequest (validasi)
// id: slug [a-z0-9-]+ · game: mlbb|pubgm|cs2 · nickname: wajib, maks 30
// type: player|staff · status: active|inactive · displayOrder: >= 1 · joinDate: yyyy-MM-dd (opsional)
// fullName, role, division, profileImg: opsional

// AchievementDto
{ "id": "m7-world-championship", "game": "mlbb", "tournament": "M7 World Championship", "tier": "S-Tier",
  "startDate": "2026-01-25", "placement": "2", "placementLabel": "Runner-up",
  "prize": { "amount": 150000, "currency": "USD" } }

// PageResponse<T>
{ "content": [ ... ], "page": 0, "size": 10, "totalElements": 21, "totalPages": 3 }
// Urutan: startDate terbaru dulu; tanggal sama -> id A-Z; tanpa tanggal paling akhir.

// StatsDto (angka asli dari seed/ per 2026-10-29)
{ "game": "mlbb", "tournaments": 20, "titles": 6, "runnerUps": 5, "totalPrizeUSD": 496514.8,
  "byYear": { "2019": 1, "2020": 2, "2021": 3, "2022": 2, "2023": 3, "2024": 3, "2025": 4, "2026": 2 } }
// titles = placement persis "1"; runnerUps = persis "2" ("1-2" bukan keduanya)
// totalPrizeUSD = jumlah prize.amount dengan currency USD (null dilewati)

// SummaryCheckDto (hasil asli: summary PUBGM di file TIDAK cocok dengan daftar prestasinya)
{ "game": "pubgm", "consistent": false, "fields": [
    { "name": "titles",        "declared": null,   "computed": 4,      "status": "UNKNOWN"  },
    { "name": "runnerUps",     "declared": null,   "computed": 3,      "status": "UNKNOWN"  },
    { "name": "totalPrizeUSD", "declared": 674089, "computed": 592650, "status": "MISMATCH" } ] }
// urutan fields tetap: titles, runnerUps, totalPrizeUSD
// declared null -> UNKNOWN; selisih < 0.01 -> MATCH; lainnya -> MISMATCH
// consistent = tidak ada MISMATCH. Setiap MISMATCH juga ditulis ke log sebagai WARN.

// ProductDto
{ "slug": "alter-ego-esports-homura-26-27-official-jersey-home",
  "name": "Alter Ego Esports Homura 26/27 Official Jersey - Home",
  "priceMin": 585000, "priceMax": 585000, "imageUrl": "https://..." }
// sort=name: nama A-Z · price_asc: priceMin naik · price_desc: priceMax turun · lainnya: 400
// maxPrice: priceMin <= maxPrice

// Error (ProblemDetail, RFC 9457)
{ "type": "about:blank", "title": "Not Found", "status": 404, "detail": "Pemain cs2/nino tidak ditemukan",
  "instance": "/api/v1/players/cs2/nino" }
// 400 validasi menambah: "errors": { "nickname": "tidak boleh kosong", "game": "..." }
```

## 7. Konfigurasi

| File | Isi |
|---|---|
| `application.yaml` | Nama app, `alterego.seed.enabled: true`, `alterego.seed.location: classpath:seed/` |
| `application-dev.yaml` | Koneksi MongoDB Atlas dari env var `${MONGODB_URI}` |
| `src/test/resources/application-test.yaml` | **Milik kit, jangan diubah.** Embedded Mongo, seed dari `seed-test/` |

Menjalankan aplikasi dengan Atlas (PowerShell):

```powershell
$env:MONGODB_URI = "mongodb+srv://USER:PASS@cluster.xxxxx.mongodb.net/alterego"
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

## 8. Data test

`src/test/resources/seed-test/` adalah salinan seed asli, ditambah **satu fixture**: `achievements/fixture-roster-link.json`. Fixture ini membuat satu prestasi palsu yang mereferensikan `nino`, karena di data asli semua `rosterIds` masih kosong. Fixture ini tidak boleh disalin ke `src/main`.

`src/test/resources/seed-broken/` berisi data yang sengaja dirusak untuk Q15.
