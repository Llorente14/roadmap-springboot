/* Naskah 4 klip onboarding. Waktu dalam detik, relatif terhadap awal scene. Jangan tampilkan kode solusi. */
"use strict";

const TREE = [
  { d: 0, n: "alterego-springboot-kit", dir: true },
  { d: 1, n: "docs", dir: true },
  { d: 1, n: "quest-board", dir: true },
  { d: 1, n: "src", dir: true },
  { d: 2, n: "main/java/com/alterego", dir: true },
  { d: 3, n: "fondasi", dir: true },
  { d: 4, n: "Hello.java" },
  { d: 4, n: "MatchMath.java" },
  { d: 3, n: "warmup", dir: true },
  { d: 2, n: "test/java/com/alterego", dir: true },
  { d: 3, n: "fondasi", dir: true },
  { d: 4, n: "F00_HelloTest.java" },
  { d: 4, n: "F01_MatchMathTest.java" },
  { d: 1, n: "mvnw.cmd" },
  { d: 1, n: "pom.xml" },
  { d: 1, n: "quest.cmd" },
  { d: 1, n: "README.md" }
];

const STUB = `package com.alterego.fondasi;

/** F00 — Merah ke hijau. */
public final class Hello {

    /** greet("Axel") -> "Halo, Axel!" */
    public static String greet(String name) {
        throw new UnsupportedOperationException("TODO F00");
    }

    /** sum(2, 3) -> 5 */
    public static int sum(int a, int b) {
        throw new UnsupportedOperationException("TODO F00");
    }
}`;
const STUB_OPEN = STUB.replace(/        throw new UnsupportedOperationException\("TODO F00"\);/g, "        // tulis kodemu di sini");

const TEST = `@DisplayName("F00 · Merah ke hijau")
class F00_HelloTest {

    @Test
    void greet() {
        assertThat(Hello.greet("Axel")).isEqualTo("Halo, Axel!");
        assertThat(Hello.greet("NINO")).isEqualTo("Halo, NINO!");
    }

    @Test
    void sum() {
        assertThat(Hello.sum(2, 3)).isEqualTo(5);
        assertThat(Hello.sum(-4, 4)).isZero();
    }
}`;

const REPO = "PS C:\\kode\\alterego-springboot-kit>";
const RUN_F00 = '.\\mvnw.cmd test "-Dtest=F00*"';
const JOURNAL = "## Yang saya pelajari\n- Test merah di awal itu normal.\n- ERROR = exception, FAILURE = hasil beda.\n\n## Yang bikin buntu\n- Baca stack trace dari baris paling atas dulu.";
const next = (big, title) => ({ type: "title", dur: 3, big, title, sub: "Klip berikutnya" });

const CLIPS = [
  {
    n: 1, name: "Setup proyek", boardState: null,
    scenes: [
      { type: "title", dur: 3.5, big: "01", title: "Setup proyek", sub: "Java 17 · repo milikmu · IDE · quest board" },
      {
        type: "term", dur: 9,
        lines: [
          { u: .6, cmd: "java -version" },
          { u: 1.9, out: 'openjdk version "17.0.12" 2024-07-16' },
          { u: 2, out: "OpenJDK Runtime Environment Temurin-17.0.12+7 (build 17.0.12+7)" },
          { u: 2.1, out: "OpenJDK 64-Bit Server VM Temurin-17.0.12+7 (build 17.0.12+7, mixed mode, sharing)" }
        ],
        marks: [[2.6, 9, 1, '"17.0.12"']],
        caps: [[0, "Kit ini butuh **Java 17**. Cek dulu di terminal."], [4.5, "Belum ada? Install **Temurin 17** dari `adoptium.net`, lalu buka terminal baru."]]
      },
      {
        type: "gh", dur: 14, k: { use: 2.5, create: 5, type: 6.3, typeDur: 2.4, submit: 11 },
        cursor: [[2.5, "#ghUse", "click"], [4.4, "#ghNew", "click"], [6, "#ghName", "click"], [11, "#ghSubmit", "click"]],
        caps: [[0, "Buka repo kit di GitHub, lalu klik **Use this template**."], [5, "Beri nama repo. Public atau Private, terserah kamu."], [10.4, "Klik **Create repository**. Repo ini milikmu: tempat semua solusimu."]]
      },
      {
        type: "term", dur: 11,
        lines: [
          { u: .5, cmd: "git clone https://github.com/kamu/alterego-springboot-kit.git", cps: 36 },
          { u: 2.6, out: "Cloning into 'alterego-springboot-kit'..." },
          { u: 3.1, out: "Receiving objects: 100% (412/412), done." },
          { u: 3.8, cmd: "cd alterego-springboot-kit" },
          { u: 5.6, ps: REPO, cmd: ".\\mvnw.cmd -v" },
          { u: 7.1, out: "Apache Maven 3.9.11" },
          { u: 7.2, out: "Java version: 17.0.12, vendor: Eclipse Adoptium" }
        ],
        marks: [[8, 11, 5, "Apache Maven 3.9.11"]],
        caps: [[0, "Clone repo milikmu ke laptop."], [5.4, "Maven sudah ikut di repo lewat `mvnw`. Tidak perlu install."]]
      },
      {
        type: "ide", dur: 11, tree: TREE,
        treeOn: [[3.5, 6, 5], [3.5, 6, 10], [6.5, 11, 2], [6.5, 11, 15]],
        tags: [[3.5, 5, "kode kamu"], [3.5, 10, "test · jangan diubah", true], [6.5, 2, "board"], [6.5, 15, "test + commit"]],
        caps: [[0, "Buka foldernya di **IntelliJ IDEA** atau **VS Code** (Extension Pack for Java)."], [3.5, "`src/main` = kodemu. `src/test` = test, jangan diubah."], [6.5, "`quest-board` = papan quest. `quest.cmd` = jalankan test lalu commit."]]
      },
      {
        type: "board", dur: 8,
        cursor: [[3, "@.q-row[data-q='F00']", "click"]],
        hl: [[3.4, 8, "@.q-row[data-q='F00']", "Mulai di sini"]],
        caps: [[0, "Buka `quest-board/index.html` di browser."], [3.4, "Mulai dari **F00**. Quest lain terbuka setelah prasyaratnya selesai."]]
      },
      next("02", "File yang dikerjakan")
    ]
  },
  {
    n: 2, name: "File yang dikerjakan", boardState: null,
    scenes: [
      { type: "title", dur: 3.5, big: "02", title: "File yang dikerjakan", sub: "Pelajaran · misi · file stub · file test" },
      {
        type: "board", dur: 13,
        scroll: [[0, 0], [2, 0], [8.8, 1500], [9.6, 1500], [10.6, 0]],
        ops: [[11.4, "click", "#paneMission"]],
        cursor: [[11.4, "@#paneMission", "click"]],
        caps: [[0, "Setiap quest dimulai dari tab **Pelajaran**."], [3.5, "Isinya konsep, contoh kode, kesalahan umum, dan latihan di jshell."], [10, "Selesai membaca? Buka tab **Misi**."]]
      },
      {
        type: "board", dur: 14,
        scroll: [[0, 0], [1, [".spec", 170]], [9, [".spec", 170]], [9.8, [".files", 380]]],
        hl: [[1.3, 5, "@.spec", "Spesifikasi"], [5, 9.3, "@.cmd", "Uji"], [10, 14, "@.files", "File"]],
        caps: [[0, "Tab **Misi** berisi apa yang harus kamu buat."], [1.3, "**Spesifikasi** = perilaku yang dicek oleh test."], [5, "**Uji** = perintah test. Salin, lalu jalankan di terminal."], [10, "**File** = tempat kamu menulis kode."]]
      },
      {
        type: "ide", dur: 13, tree: TREE,
        treeOn: [[0, 13, 6]], tab: [[0, "Hello.java", false]], code: [[0, STUB], [8.5, STUB_OPEN]],
        lit: [[2.5, 13, [8, 13]]],
        caps: [[0, "Buka file dari bagian File: `Hello.java`."], [2.5, "Isinya masih `throw new UnsupportedOperationException`: tanda belum dikerjakan."], [8.5, "Hapus baris `throw`, lalu tulis kodemu sendiri di situ."]]
      },
      {
        type: "ide", dur: 12, tree: TREE,
        treeOn: [[0, 12, 11]], tab: [[0, "F00_HelloTest.java", true]], code: [[0, TEST]],
        lit: [[2.5, 7.3, [6, 7, 12, 13]]],
        caps: [[0, "Baca test-nya dulu: `F00_HelloTest.java`."], [2.5, "Setiap `assertThat` = contoh input dan hasil yang diharapkan."], [7.3, "Jangan ubah file test. Yang kamu ubah hanya kode di `src/main`."]]
      },
      next("03", "Run test & baca hasil")
    ]
  },
  {
    n: 3, name: "Run test & baca hasil",
    scenes: [
      { type: "title", dur: 3.5, big: "03", title: "Run test & baca hasil", sub: "Merah · ERROR vs FAILURE · hijau" },
      {
        type: "term", dur: 19, ps: REPO,
        lines: [
          { u: .5, cmd: RUN_F00 },
          { u: 2.2, out: "[INFO] Running F00 · Merah ke hijau", c: "info" },
          { u: 2.7, out: "[ERROR] greet menyapa dengan nama  <<< ERROR!", c: "err" },
          { u: 2.8, out: "java.lang.UnsupportedOperationException: TODO F00", c: "err" },
          { u: 2.9, out: "        at com.alterego.fondasi.Hello.greet(Hello.java:8)", c: "dim" },
          { u: 3.0, out: "[ERROR] sum menjumlahkan dua angka  <<< ERROR!", c: "err" },
          { u: 3.1, out: "java.lang.UnsupportedOperationException: TODO F00", c: "err" },
          { u: 3.2, out: "        at com.alterego.fondasi.Hello.sum(Hello.java:13)", c: "dim" },
          { u: 3.5, out: "[ERROR] Tests run: 2, Failures: 0, Errors: 2, Skipped: 0", c: "err" },
          { u: 3.7, out: "[INFO] BUILD FAILURE", c: "err" }
        ],
        marks: [[8, 13.5, 2, "ERROR!"], [8, 13.5, 5, "ERROR!"], [13.5, 19, 4, "Hello.java:8"]],
        caps: [[0, "Jalankan test quest dari folder repo."], [4, "Merah di awal itu normal: method-nya belum kamu tulis."], [8, "**ERROR** = kode melempar exception. Di sini: baris `throw` masih ada."], [13.5, "`Hello.java:8` = file dan baris yang perlu kamu buka."]]
      },
      {
        type: "term", dur: 13, ps: REPO,
        lines: [
          { u: .5, cmd: RUN_F00 },
          { u: 2.2, out: "[INFO] Running F00 · Merah ke hijau", c: "info" },
          { u: 2.7, out: "[ERROR] sum menjumlahkan dua angka  <<< FAILURE!", c: "err" },
          { u: 2.8, out: "org.opentest4j.AssertionFailedError:", c: "err" },
          { u: 2.9, out: "expected: 5" },
          { u: 3.0, out: " but was: 0" },
          { u: 3.1, out: "        at com.alterego.fondasi.F00_HelloTest.sum(F00_HelloTest.java:12)", c: "dim" },
          { u: 3.4, out: "[ERROR] Tests run: 2, Failures: 1, Errors: 0, Skipped: 0", c: "err" },
          { u: 3.6, out: "[INFO] BUILD FAILURE", c: "err" }
        ],
        marks: [[4.5, 8.5, 2, "FAILURE!"], [8.5, 13, 4, "expected: 5"], [8.5, 13, 5, "but was: 0"]],
        caps: [[0, "Sudah menulis kode? Jalankan lagi."], [4.5, "**FAILURE** = kode jalan, tapi hasilnya beda dari harapan."], [8.5, "Bandingkan `expected` dengan `but was`. Perbaiki, lalu ulangi."]]
      },
      {
        type: "term", dur: 8, ps: REPO,
        lines: [
          { u: .5, cmd: RUN_F00 },
          { u: 2.2, out: "[INFO] Running F00 · Merah ke hijau", c: "info" },
          { u: 2.6, out: "[INFO] Tests run: 2, Failures: 0, Errors: 0, Skipped: 0", c: "ok" },
          { u: 2.8, out: "[INFO] BUILD SUCCESS", c: "ok" }
        ],
        marks: [[3.4, 8, 3, "BUILD SUCCESS"]],
        caps: [[0, "Ulangi sampai semuanya lolos."], [3.4, "Hijau semua: **BUILD SUCCESS**. Quest siap diselesaikan."]]
      },
      {
        type: "term", dur: 16, ps: REPO,
        lines: [
          { u: .5, cmd: '.\\mvnw.cmd test "-Dtest=F01*"' },
          { u: 2.2, out: "[ERROR] COMPILATION ERROR :", c: "err" },
          { u: 2.3, out: "[ERROR] /src/main/java/com/alterego/warmup/RosterBook.java:[24,9] cannot find symbol", c: "err" },
          { u: 2.5, out: "[INFO] BUILD FAILURE", c: "err" },
          { u: 10, cmd: "chcp 65001" },
          { u: 11, out: "Active code page: 65001" }
        ],
        marks: [[5.5, 10, 2, "RosterBook.java:[24,9]"]],
        caps: [[0, "Satu file gagal dikompilasi = **semua** test berhenti, termasuk quest lain."], [5.5, "Baca `COMPILATION ERROR` paling atas: file dan baris yang rusak."], [10, "Tanda · tampil sebagai karakter aneh? Jalankan `chcp 65001` dulu."]]
      },
      next("04", "Selesai & simpan")
    ]
  },
  {
    n: 4, name: "Selesai & simpan", boardState: null,
    scenes: [
      { type: "title", dur: 3.5, big: "04", title: "Selesai & simpan", sub: "Commit otomatis · jurnal · push · ekspor" },
      {
        type: "term", dur: 12, ps: REPO,
        lines: [
          { u: .5, cmd: ".\\quest F00" },
          { u: 1.6, out: "== F00 - Merah ke hijau ==", c: "info" },
          { u: 3.6, out: "F00 hijau.", c: "ok" },
          { u: 4, out: "9b415a6 solve(F00): Merah ke hijau" },
          { u: 4.1, out: " src/main/java/com/alterego/fondasi/Hello.java | 8 +++++---", c: "dim" }
        ],
        marks: [[4.5, 8.5, 3, "solve(F00): Merah ke hijau"]],
        caps: [[0, "Test hijau? Jalankan `.\\quest F00`."], [4.5, "Test diulang. Kalau hijau, kodemu otomatis di-commit."], [8.5, "Masih merah? Tidak ada commit. Perbaiki dulu, lalu ulangi."]]
      },
      {
        type: "board", dur: 24,
        ops: [[0, "click", "#paneMission"], [2.6, "click", "[data-check='0']"], [3.8, "click", "[data-check='1']"], [18, "click", "#btnDone"]],
        type_: [[9.3, 14.8, "#jArea", JOURNAL]],
        scroll: [[0, 0], [1.6, [".checks", 260]], [6.5, [".checks", 260]], [7.6, ["#journalBlk", 110]], [15.4, ["#journalBlk", 110]], [16.4, ["#finish", 470]], [19, ["#finish", 470]], [20, 0]],
        cursor: [[2.6, "@[data-check='0']", "click"], [3.8, "@[data-check='1']", "click"], [9, "@#jArea", "click"], [18, "@#btnDone", "click"]],
        hl: [[16.6, 18, "@#btnDone", "Selesaikan quest"], [20.3, 24, "@.q-head", "Lulus"]],
        caps: [[0, "Kembali ke board, tab **Misi**."], [1.6, "Centang kriteria yang benar-benar sudah kamu penuhi."], [7.6, "Tulis jurnal minimal 120 karakter: apa yang dipelajari, apa yang bikin buntu."], [16.4, "Klik **Selesaikan quest**: XP masuk, stempel LULUS muncul."]]
      },
      {
        type: "term", dur: 9, ps: REPO,
        lines: [
          { u: .5, cmd: "git push" },
          { u: 1.4, out: "Enumerating objects: 11, done.", c: "dim" },
          { u: 1.6, out: "To https://github.com/kamu/alterego-springboot-kit.git", c: "dim" },
          { u: 1.8, out: "   d6e7883..9b415a6  main -> main" }
        ],
        marks: [[2.4, 9, 3, "main -> main"]],
        caps: [[0, "Push supaya solusimu aman di GitHub."], [4.5, "Atau sekaligus: `.\\quest F00 -Push`."]]
      },
      {
        type: "board", dur: 9,
        ops: [[1, "click", "#btnData"]],
        cursor: [[1, "@#btnData", "click"], [3.6, "@#btnExport"]],
        hl: [[1.5, 9, "@#popData", "Ekspor / impor"]],
        caps: [[0, "Progres board hanya tersimpan di browser ini."], [3.6, "Klik **Ekspor JSON** secara rutin sebagai cadangan. Impor di browser lain."]]
      },
      { type: "title", dur: 3.5, big: "GO", title: "Mulai dari F00", sub: "Pelajaran → test merah → kode → hijau → .\\quest" }
    ]
  }
];
