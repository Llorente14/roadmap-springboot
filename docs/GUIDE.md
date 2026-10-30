# GUIDE — Cara memakai kit ini

Kit ini mengikuti tiga langkah metode Manware:

| Langkah | Di kit ini |
|---|---|
| **Blueprint** — rencanakan sebelum menulis kode | `docs/BLUEPRINT.md`: arsitektur, model data, kontrak API, keputusan desain |
| **Division** — pecah tujuan besar jadi tugas kecil | 14 pelajaran Fondasi + 26 quest di quest board, masing-masing 20–90 menit |
| **Systems** — cara sistematis untuk debugging | Test sebagai wasit, loop kerja tetap, protokol debugging di bawah |

Aturan utamanya: **tidak ada kode solusi di mana pun.** Bahkan hint level 3 hanya berupa langkah dalam kalimat biasa. Satu pengecualian: pelajaran Fondasi memuat contoh kode konsep, tapi selalu dengan data lain (buah, siswa, toko), bukan jawaban latihannya.

---

## 0. Persiapan (sekali saja)

1. Buka folder `SpringbootApp` di IDE. Disarankan IntelliJ IDEA Community, karena dukungan Maven dan Spring-nya paling mulus.
2. Buka `quest-board/index.html` di browser (klik dua kali file-nya). Letakkan jendelanya di samping IDE.
3. Jalankan test sekali untuk memastikan semuanya terpasang:

   ```powershell
   .\mvnw.cmd test "-Dtest=F00*"
   ```

   Hasil yang benar: **BUILD FAILURE** dengan pesan `UnsupportedOperationException: TODO F00`. Merah di awal itu normal. Itu titik start.

4. `git init`, lalu commit pertama (lihat bagian 7).

> Catatan: test Spring pertama (Q08) mengunduh MongoDB embedded (beberapa ratus MB, ±3 menit) sekali saja. Di laptop ini sudah terunduh saat kit disiapkan, lalu disimpan di cache. Run berikutnya hanya butuh beberapa detik.

---

## 0½. Mulai dari Fondasi (F00–F13)

Belum lancar Java? Mulai dari level **Fondasi** di quest board. Setiap pelajaran punya dua tab:

- **Pelajaran**: konsep, contoh kode (data buah/siswa/toko), kesalahan umum, dan bagian *Coba di jshell*.
- **Misi**: latihan bertest di `src/main/java/com/alterego/fondasi/`, alurnya sama dengan quest biasa.

```powershell
jshell                                 # konsol Java interaktif; keluar dengan /exit
.\mvnw.cmd test "-Dtest=F03*"          # satu pelajaran
.\mvnw.cmd test "-Dtest=F*"            # semua Fondasi
```

Quest Q01–Q07 **terkunci** sampai pelajaran Fondasi yang dipakainya selesai. Daftarnya terlihat di quest board ("Fondasi yang dipakai").

Beberapa latihan meminta kamu **membuat file sendiri** (misalnya `MatchResult.java` di F05, `ScoreFormatException.java` di F09). Test-nya mencari class itu berdasarkan nama, jadi pesan gagalnya akan memberi tahu apa yang belum ada.

> **Satu file rusak memblokir semua test.** Maven mengompilasi *seluruh* `src/main` sebelum menjalankan test apa pun. Kalau ada kode setengah jadi yang belum bisa dikompilasi (pesan `COMPILATION ERROR` / `cannot find symbol`), test lain pun tidak akan jalan. Simpan pekerjaan setengah jadi dalam keadaan bisa dikompilasi: kembalikan baris `throw new UnsupportedOperationException("TODO ...")`, atau jadikan bagian yang belum jadi sebagai komentar.

---

## 1. Loop kerja satu quest

```
┌─► 1. BACA quest di board: misi, spesifikasi, kriteria selesai
│   2. BACA test-nya: src/test/java/.../Qnn_*Test.java
│      Test adalah spesifikasi yang bisa dijalankan. Baca nama test dan assertion-nya.
│   3. BACA dokumentasi resmi (link di quest). Belum menulis kode.
│   4. RENCANAKAN di jurnal: 3–5 langkah dalam kalimatmu sendiri.
│   5. TULIS kode: langkah terkecil yang membuat satu test hijau.
│   6. JALANKAN: .\mvnw.cmd test "-Dtest=Qnn*"
│   7. Merah? Ikuti protokol debugging (bagian 4). Hijau? Lanjut ke test berikutnya.
│   8. RAPIKAN kode setelah semua hijau (nama, duplikasi). Jalankan test lagi.
│   9. JURNAL: apa yang dipelajari, error yang muncul, cara memperbaikinya.
│  10. Tandai SELESAI di board (Ctrl+Enter), lalu commit.
└── quest berikutnya
```

Jangan lompat dari langkah 2 ke langkah 5. Langkah 3 dan 4 adalah inti latihan ini.

### Membaca test

Contoh potongan `Q02_RosterBookTest`:

```java
@Test
@DisplayName("find yang tidak ketemu mengembalikan Optional kosong")
void findMissing() {
    assertThat(book.find("cs2", "nino")).isEmpty();
}
```

Dari sini terbaca: method `find(game, id)` harus mengembalikan `Optional` yang kosong, bukan `null`, bukan exception. Setiap `@DisplayName` adalah satu aturan.

Test Spring (Q08+) memanggil kodemu lewat HTTP (`mvc.perform(get(...))`) atau lewat helper `Contract`. Kalau class yang dibutuhkan belum ada, pesannya akan memberitahu, misalnya:
`Class PlayerRepository belum ada. Buat di package com.alterego.api.player.`

### Perintah yang sering dipakai

```powershell
.\mvnw.cmd test "-Dtest=Q05*"                 # satu quest
.\mvnw.cmd test "-Dtest=Q05*#rejects*"        # satu method test
.\mvnw.cmd test "-Dtest=Q0*"                  # Level 0 sekaligus
.\mvnw.cmd test                               # semua (merah sampai semua quest selesai: itu progress bar)
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"   # jalankan app sungguhan (setelah Q10)
```

Di IntelliJ: klik ikon ▶ hijau di samping nama class test. Hasilnya lebih enak dibaca.

---

## 2. Cara menulis kode (aturan rumah)

1. **Langkah kecil.** Buat satu test hijau, jalankan, baru lanjut. Jangan menulis 100 baris lalu menjalankan test.
2. **Ikuti kontrak, bukan tebakan.** Nama class/method/field yang disebut di BLUEPRINT dan test itu wajib. Sisanya bebas.
3. **Satu tanggung jawab per class.** Controller menerima HTTP, Service memutuskan, Repository menyimpan.
4. **Constructor injection.** Dependensi masuk lewat constructor, field `final`. Kamu akan paham alasannya setelah Q07.
5. **DTO di batas sistem.** Document Mongo tidak pernah keluar sebagai JSON. Request masuk lewat class request sendiri.
6. **Gagal dengan jelas.** Exception membawa pesan yang menyebut *apa* dan *nilai apa* yang salah.
7. **Nama dalam bahasa Inggris, komentar boleh Indonesia.** Tulis komentar hanya untuk "mengapa", bukan "apa".
8. **Jangan mengubah file kit.** Yang termasuk file kit: semua di `src/test/**`, `application-test.yaml`, `seed-test/`, `seed-broken/`. Kalau test terasa salah, catat di jurnal dan tanyakan. Jangan "membetulkan" test.

---

## 3. Validasi: kapan sebuah quest benar-benar selesai

Quest selesai kalau **ketiganya** terpenuhi:

- [ ] `.\mvnw.cmd test "-Dtest=Qnn*"` → `BUILD SUCCESS`, tanpa mengubah file test.
- [ ] Test quest sebelumnya tetap hijau. Jalankan level yang sedang dikerjakan, misalnya `"-Dtest=Q1*"`.
- [ ] Jurnal terisi. Kamu bisa menjelaskan *mengapa* kodenya bekerja, bukan hanya *bahwa* kodenya bekerja.

Validasi tambahan untuk quest API (Q16+): jalankan app dengan profil `dev` dan coba endpoint-nya sendiri. Bisa lewat browser, `curl`, atau Swagger UI setelah Q26. Test membuktikan kontrak. Mencoba sendiri membangun intuisi.

```powershell
curl.exe -s "http://localhost:8080/api/v1/players?game=mlbb"
curl.exe -s -X POST "http://localhost:8080/api/v1/admin/players" -H "Content-Type: application/json" -d '{\"id\":\"coba\",\"game\":\"mlbb\",\"nickname\":\"COBA\",\"type\":\"player\",\"status\":\"active\",\"displayOrder\":50}'
```

---

## 4. Protokol debugging (Systems)

Kalau test merah, jangan menebak dan jangan mengubah kode secara acak. Ikuti urutan ini:

1. **Baca pesan pertamanya sampai habis.** Di output Maven, cari `[ERROR]` dan baris `expected:` / `but was:`.
   - `UnsupportedOperationException: TODO` → kamu belum mengganti stub.
   - `COMPILATION ERROR` / `cannot find symbol` → ada file di `src/main` yang belum bisa dikompilasi, mungkin bukan file quest yang sedang dikerjakan. Baca path dan nomor barisnya.
   - `Failed to load ApplicationContext` → aplikasinya **gagal start**, bukan test-nya yang salah. Scroll ke bawah ke `Caused by:` yang **terakhir**. Itu akar masalahnya.
   - `Status expected:<200> but was:<404>` → endpoint tidak ditemukan. Periksa path dan anotasi mapping.
   - `No qualifying bean` / `belum menjadi bean Spring` → class-nya tidak dikelola Spring.
2. **Ulangi masalahnya sekecil mungkin.** Jalankan satu method test saja (`-Dtest=Qnn*#namaMethod`).
3. **Tulis hipotesis di jurnal sebelum mengubah kode.** Contoh: "Saya kira 404 karena path di controller `/players` bukan `/api/v1/players`."
4. **Uji satu hipotesis, ubah satu hal.** Jalankan lagi. Kalau salah, kembalikan perubahan itu.
5. **Lihat data sungguhan.** Tambahkan log sementara, atau buka MongoDB lewat Compass/Atlas UI untuk melihat isi dokumen.
6. **Masih buntu setelah 25 menit?** Buka hint berikutnya (XP berkurang, tidak apa-apa), atau tanya AI dengan prompt aman (bagian 5).
7. **Catat di jurnal:** gejala → penyebab → perbaikan. Ini bagian yang paling berharga dari jurnal.

Error yang pasti akan kamu temui di kit ini (karena data aslinya memang begitu): ID ganda, tanggal tidak lengkap, field array yang dikira string, dan summary yang tidak cocok. Semuanya disengaja dan didokumentasikan di BLUEPRINT bagian 5.

---

## 5. Aturan memakai AI

Boleh: meminta **penjelasan konsep**, **arti pesan error**, atau **perbandingan pendekatan**.
Tidak boleh: menempelkan test atau soal lalu meminta kodenya.

Template prompt aman (setiap quest punya versinya sendiri di board):

```
Saya sedang belajar Spring Boot dan TIDAK mau diberi kode solusi.
Jelaskan konsep <X> dengan bahasa sederhana dan contoh yang TIDAK berkaitan dengan kasus saya.
Lalu beri 2 pertanyaan untuk menguji pemahaman saya.
```

```
Saya mendapat error ini: <tempel pesan error>.
Jangan perbaiki kode saya. Jelaskan arti error ini dan 3 kemungkinan penyebab umumnya,
supaya saya bisa memeriksanya sendiri.
```

Kalau AI tetap memberi kode: jangan disalin. Tutup, tulis ulang dari pemahamanmu sendiri, lalu catat di jurnal.

---

## 6. Quest board

- **J / K**: quest berikutnya / sebelumnya · **Ctrl+Enter**: tandai selesai · **Ctrl+E**: preview Markdown · **Ctrl+.**: mode fokus · **Esc**: keluar · **?**: daftar shortcut
- Quest dengan gembok belum bisa dibuka; panelnya menunjukkan pelajaran Fondasi yang harus diselesaikan dulu. J/K melewati quest terkunci.
- XP penuh kalau tanpa hint. Hint 1 −10%, hint 2 −20%, hint 3 −30% (berlaku kumulatif).
- Jurnal tersimpan otomatis di browser (localStorage). Klik **Ekspor** secara berkala dan simpan file JSON-nya, misalnya di `docs/progress/`, supaya progres tidak hilang kalau cache browser dibersihkan.

---

## 7. Git

```powershell
git init
git add .
git commit -m "chore: learning kit scaffold"
```

Setelah sebuah quest hijau, simpan solusinya dengan runner:

```powershell
.\quest F04              # jalankan test F04; kalau hijau → commit "solve(F04): <judul>"
.\quest F04 F05          # beberapa quest, satu commit per quest
.\quest Q12 -Push        # commit lalu push
```

Runner hanya meng-commit file milik quest itu (F00–Q07), atau semua perubahan di `src/main` + `pom.xml` (Q08–Q26). Kalau test masih merah, tidak ada commit.
Jangan pernah commit file `.env` atau connection string. Q10 memeriksanya.

---

## 8. Peta quest

| Level | Quest | Tema |
|---|---|---|
| F · Fondasi | F00–F13 | Java dari nol: tipe data, if/loop, String, method, class & field, List, Map/Set, Optional, exception, sorting, lambda, stream, generics |
| 0 · Warmup | Q01–Q07 | Java yang dipakai Spring: record, Optional, Stream, generics, exception, annotation, DI manual |
| 1 · Spring Basics | Q08–Q10 | Controller pertama, konfigurasi, profile + Atlas |
| 2 · Mongo | Q11–Q12 | Document, repository, derived query, embed vs referensi |
| 3 · Seeder | Q13–Q15 | Impor JSON, upsert idempotent, gagal-cepat |
| 4 · REST Read | Q16–Q19 | DTO, filter, path variable, pagination, query lewat referensi |
| 5 · Write | Q20–Q21 | POST/PUT/DELETE, Bean Validation, 201/409 |
| 6 · Errors | Q22 | ProblemDetail dan exception handler global |
| 7 · Stats | Q23–Q24 | Agregasi dan audit data |
| 8 · Merch | Q25 | Produk, parsing harga, sorting |
| 9 · Docs | Q26 | OpenAPI + Swagger UI |
