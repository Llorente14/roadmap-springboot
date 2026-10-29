/*
 * Isi quest AlterEgo API. Dibaca oleh index.html.
 * Aturan isi: TIDAK ADA KODE SOLUSI. Hint 1 = konsep, hint 2 = dokumentasi, hint 3 = langkah dalam kalimat.
 */
const JDK = "https://docs.oracle.com/en/java/javase/17/docs/api/java.base/";
const BOOT = "https://docs.spring.io/spring-boot/reference/";
const MVC = "https://docs.spring.io/spring-framework/reference/web/webmvc/";
const DATA = "https://docs.spring.io/spring-data/mongodb/reference/";

window.QUEST_DATA = {
  levels: [
    { n: 0, name: "Warmup", tag: "Java yang dipakai Spring" },
    { n: 1, name: "Spring Basics", tag: "Controller, konfigurasi, profile" },
    { n: 2, name: "Mongo", tag: "Document dan repository" },
    { n: 3, name: "Seeder", tag: "JSON masuk database" },
    { n: 4, name: "REST Read", tag: "DTO, filter, pagination" },
    { n: 5, name: "Write", tag: "POST, PUT, DELETE, validasi" },
    { n: 6, name: "Errors", tag: "ProblemDetail" },
    { n: 7, name: "Stats", tag: "Agregasi dan audit data" },
    { n: 8, name: "Merch", tag: "Produk dan harga" },
    { n: 9, name: "Docs", tag: "OpenAPI" }
  ],

  quests: [
    // ───────────────────────── LEVEL 0 ─────────────────────────
    {
      id: "Q01", level: 0, xp: 50, minutes: 30,
      title: "Slug",
      mission: "Ubah teks bebas menjadi slug URL. Fungsi ini nanti dipakai untuk membuat id produk (Q25), jadi ini bukan latihan buangan.",
      spec: [
        "Huruf kecil semua; aksen dibuang (\"Café\" → \"cafe\").",
        "Setiap rangkaian karakter selain a–z dan 0–9 menjadi satu tanda minus.",
        "Tidak ada minus di awal atau akhir.",
        "null, teks kosong, atau teks yang hasilnya kosong (\"!!!\") → IllegalArgumentException."
      ],
      files: ["src/main/java/com/alterego/warmup/Slugs.java"],
      test: "Q01*",
      prereq: ["F00", "F01", "F02", "F03", "F04", "F09"],
      concepts: ["Regular expression", "java.text.Normalizer", "Guard clause / validasi argumen", "Maven Surefire & JUnit 5"],
      docs: [
        { label: "Pattern (sintaks regex Java)", url: JDK + "java/util/regex/Pattern.html" },
        { label: "Normalizer", url: JDK + "java/text/Normalizer.html" },
        { label: "String.replaceAll", url: JDK + "java/lang/String.html#replaceAll(java.lang.String,java.lang.String)" },
        { label: "JUnit 5: parameterized tests", url: "https://docs.junit.org/current/user-guide/#writing-tests-parameterized-tests" }
      ],
      done: ["12 test Q01 hijau", "Kamu bisa menjelaskan apa arti + dan ^ di regex"],
      hints: [
        "Pecah masalahnya jadi tiga tahap: buang aksen, ganti semua yang bukan huruf/angka, rapikan tepi. Setiap tahap adalah transformasi String → String.",
        "Untuk aksen, baca Normalizer bagian Form.NFD: huruf beraksen dipecah menjadi huruf dasar + tanda aksen. Tanda aksen termasuk kategori Unicode \"Mark\" (lihat bagian Unicode categories di dokumentasi Pattern).",
        "Validasi null di baris pertama. Normalisasi dengan NFD lalu hapus semua karakter kategori Mark. Ubah ke huruf kecil. Ganti setiap rangkaian non-alfanumerik dengan satu minus. Hapus minus di awal dan akhir. Kalau hasilnya kosong, lempar exception."
      ],
      prompt: "Jelaskan apa itu Unicode normalization (NFC vs NFD) dan bagaimana quantifier + bekerja di regex, dengan contoh yang bukan tentang slug. Jangan beri kode solusi.",
      reflect: "Kenapa slug lebih baik daripada memakai nama produk langsung di URL?"
    },
    {
      id: "Q02", level: 0, xp: 50, minutes: 45,
      title: "Roster dalam memori",
      mission: "Simpan pemain di memori dengan identitas game + id. Di data asli, \"aces\" muncul di MLBB dan PUBGM sebagai dua orang dengan peran berbeda. Masalah yang sama akan muncul lagi di MongoDB (Q11).",
      spec: [
        "add: kombinasi game+id ganda → IllegalStateException.",
        "find(game, id) → Optional; tidak ada → Optional kosong (bukan null).",
        "active(game) → hanya status \"active\", urut displayOrder naik.",
        "size() → jumlah pemain."
      ],
      files: ["src/main/java/com/alterego/warmup/RosterBook.java", "WarmupPlayer.java (record, sudah jadi)"],
      test: "Q02*",
      prereq: ["F05", "F06", "F07", "F08", "F09", "F10"],
      concepts: ["record", "Map dan kunci gabungan", "Optional", "Comparator"],
      docs: [
        { label: "Record classes", url: "https://docs.oracle.com/en/java/javase/17/language/records.html" },
        { label: "Optional", url: JDK + "java/util/Optional.html" },
        { label: "Map.putIfAbsent", url: JDK + "java/util/Map.html#putIfAbsent(K,V)" },
        { label: "Comparator.comparingInt", url: JDK + "java/util/Comparator.html#comparingInt(java.util.function.ToIntFunction)" }
      ],
      done: ["4 test Q02 hijau"],
      hints: [
        "Struktur data apa yang mencari berdasarkan kunci dengan cepat? Kuncinya harus menggabungkan dua nilai.",
        "Baca Map.putIfAbsent: nilai kembaliannya memberitahu apakah kunci sudah ada. Untuk Optional, cari method yang membungkus nilai yang mungkin null.",
        "Simpan pemain di Map dengan kunci gabungan game dan id. Saat menambah, cek apakah kunci sudah ada lalu lempar exception. find membungkus hasil get Map dengan Optional. active menyaring nilai Map berdasarkan game dan status, lalu mengurutkan berdasarkan displayOrder."
      ],
      prompt: "Kenapa Optional lebih baik daripada mengembalikan null? Kapan Optional TIDAK sebaiknya dipakai (misalnya sebagai field)? Jangan beri kode solusi.",
      reflect: "Apa yang terjadi di Q02 kalau kuncinya hanya id?"
    },
    {
      id: "Q03", level: 0, xp: 60, minutes: 45,
      title: "Lambda & Stream",
      mission: "Hitung statistik prestasi dengan Stream API, tanpa for/while. Ini latihan untuk Q23, yang menghitung angka yang sama dari database.",
      spec: [
        "countPlacement: placement persis sama (\"1-2\" bukan \"1\").",
        "totalPrize: jumlah prizeUsd, null dilewati, list kosong → 0.",
        "countByYear: Map tahun → jumlah.",
        "biggestPrize: Optional; abaikan null; list kosong → kosong."
      ],
      files: ["src/main/java/com/alterego/warmup/AchievementMath.java"],
      test: "Q03*",
      prereq: ["F11", "F12"],
      concepts: ["Lambda", "Stream: filter/map/mapToDouble/max", "Collectors.groupingBy + counting", "Method reference"],
      docs: [
        { label: "java.util.stream (package summary)", url: JDK + "java/util/stream/package-summary.html" },
        { label: "Collectors", url: JDK + "java/util/stream/Collectors.html" },
        { label: "Tutorial: Lambda expressions", url: "https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html" }
      ],
      done: ["4 test Q03 hijau", "Tidak ada for/while di AchievementMath"],
      hints: [
        "Setiap method adalah pipeline: sumber → saring → ubah → kumpulkan. Untuk menghitung per kelompok, ada collector khusus.",
        "Baca Collectors.groupingBy versi dengan downstream collector, dan Collectors.counting. Untuk angka double, lihat Stream.mapToDouble lalu sum.",
        "countPlacement: saring yang placement-nya sama, lalu hitung. totalPrize: ambil prizeUsd, buang null, ubah ke stream double, jumlahkan. countByYear: kelompokkan berdasarkan tahun dari startDate dengan collector penghitung. biggestPrize: buang yang hadiahnya null, lalu ambil maksimum dengan comparator berdasarkan hadiah."
      ],
      prompt: "Jelaskan perbedaan Stream.map dan Stream.flatMap, serta apa itu downstream collector di groupingBy, memakai contoh daftar belanja. Jangan beri kode untuk kasus saya.",
      reflect: "Kapan for-loop biasa justru lebih jelas daripada stream?"
    },
    {
      id: "Q04", level: 0, xp: 60, minutes: 60,
      title: "Validator: interface & generics",
      mission: "Buat validator pemain dan validator gabungan yang generik. Bean Validation di Q20 bekerja dengan ide yang sama. Di sini kamu membangunnya sendiri dulu.",
      spec: [
        "PlayerValidator: id slug, game mlbb/pubgm/cs2, nickname tidak kosong, displayOrder ≥ 1.",
        "Setiap pelanggaran = satu pesan berawalan \"namaField:\".",
        "CompositeValidator.of(...) menjalankan SEMUA validator dan menggabungkan error berurutan.",
        "CompositeValidator bekerja untuk tipe apa pun (Validator<String>, dll.), termasuk lambda."
      ],
      files: ["PlayerValidator.java", "CompositeValidator.java", "Validator.java (sudah jadi)"],
      test: "Q04*",
      prereq: ["F11", "F13"],
      concepts: ["Interface & polymorphism", "@FunctionalInterface", "Generics & type parameter", "Varargs"],
      docs: [
        { label: "Tutorial: Interfaces", url: "https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html" },
        { label: "Tutorial: Generics", url: "https://docs.oracle.com/javase/tutorial/java/generics/index.html" },
        { label: "Generic methods", url: "https://docs.oracle.com/javase/tutorial/java/generics/methods.html" },
        { label: "Set.of / List.copyOf", url: JDK + "java/util/Set.html#of(E...)" }
      ],
      done: ["5 test Q04 hijau"],
      hints: [
        "Composite pattern: objek yang terlihat seperti satu validator, padahal di dalamnya menyimpan banyak validator. Karena Validator adalah functional interface, lambda juga bisa menjadi Validator.",
        "Baca tutorial Generic Methods: of adalah static method yang punya type parameter sendiri. Varargs di dalam method diperlakukan sebagai array.",
        "PlayerValidator: siapkan list kosong, periksa setiap aturan satu per satu, tambahkan pesan untuk tiap aturan yang gagal, lalu kembalikan list. CompositeValidator: simpan validator dari of di sebuah field. Saat validate, jalankan setiap validator berurutan dan kumpulkan semua pesannya ke satu list."
      ],
      prompt: "Jelaskan composite design pattern dan kenapa generics membuat kode bisa dipakai ulang, dengan contoh di luar validasi. Jangan beri kode untuk kasus saya.",
      reflect: "Kenapa test bisa memberikan lambda ke CompositeValidator.of?"
    },
    {
      id: "Q05", level: 0, xp: 70, minutes: 60,
      title: "Harga Rupiah & exception",
      mission: "Parse harga dari data Shopee. Beberapa harga berupa rentang. Ada juga jebakan di InvalidPriceException: test menuntut jenis exception yang berbeda dari yang disiapkan.",
      spec: [
        "\"Rp585.000\" → 585000..585000 · \"Rp325.000 - Rp335.000\" → 325000..335000.",
        "Spasi di tepi diabaikan.",
        "Tidak valid (tanpa Rp, huruf, min > max, 3 bagian, kosong, null) → InvalidPriceException berisi input.",
        "InvalidPriceException harus unchecked."
      ],
      files: ["PriceParser.java", "InvalidPriceException.java", "PriceRange.java (sudah jadi)"],
      test: "Q05*",
      prereq: ["F03", "F09"],
      concepts: ["Checked vs unchecked exception", "Regex validasi format", "Long.parseLong", "Fail fast"],
      docs: [
        { label: "Tutorial: Unchecked exceptions — the controversy", url: "https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html" },
        { label: "RuntimeException", url: JDK + "java/lang/RuntimeException.html" },
        { label: "String.split", url: JDK + "java/lang/String.html#split(java.lang.String)" },
        { label: "Pattern (quantifier & grup)", url: JDK + "java/util/regex/Pattern.html" }
      ],
      done: ["14 test Q05 hijau", "Kamu bisa menjelaskan kenapa Spring hampir selalu memakai unchecked exception"],
      hints: [
        "Pisahkan dua pekerjaan: (1) memecah input menjadi satu atau dua bagian, (2) memvalidasi dan mengubah satu bagian \"RpX.XXX\" menjadi angka. Pekerjaan kedua cocok jadi method private.",
        "Baca halaman \"Unchecked Exceptions — The Controversy\". Lalu lihat superclass mana yang membuat exception tidak wajib dideklarasikan di throws. Untuk validasi format, regex dengan grup berulang untuk \".XXX\" sangat membantu.",
        "Ubah superclass InvalidPriceException. Di parse: tolak null, trim, pecah di tanda minus yang boleh dikelilingi spasi, tolak kalau lebih dari dua bagian. Untuk setiap bagian, cocokkan dengan pola Rp diikuti 1–3 digit lalu kelompok titik+3 digit, lalu buang Rp dan titik dan ubah ke long. Bandingkan min dan max."
      ],
      prompt: "Apa beda checked dan unchecked exception di Java, dan kenapa framework modern seperti Spring lebih suka unchecked? Beri contoh non-teknis. Jangan beri kode solusi.",
      reflect: "Apa yang kamu ubah di signature method ketika exception-nya masih checked, dan kenapa compiler memaksa itu?"
    },
    {
      id: "Q06", level: 0, xp: 70, minutes: 60,
      title: "Annotation & reflection",
      mission: "Bangun @NotBlank versi mini: baca anotasi saat program berjalan. Ada satu baris yang hilang di Required.java sehingga anotasinya tidak terlihat sama sekali. Temukan sendiri.",
      spec: [
        "@Required harus bisa dibaca saat runtime.",
        "missing(obj) → nama field ber-@Required yang null atau String kosong/spasi, urut deklarasi.",
        "Field tanpa @Required diabaikan; objek tanpa anotasi → list kosong."
      ],
      files: ["Required.java", "RequiredFieldChecker.java"],
      test: "Q06*",
      prereq: ["F05", "F09", "F13"],
      concepts: ["Retention policy", "Reflection: Class, Field", "setAccessible", "Pattern matching instanceof"],
      docs: [
        { label: "Tutorial: Annotations", url: "https://docs.oracle.com/javase/tutorial/java/annotations/index.html" },
        { label: "Retention / RetentionPolicy", url: JDK + "java/lang/annotation/RetentionPolicy.html" },
        { label: "Class.getDeclaredFields", url: JDK + "java/lang/Class.html#getDeclaredFields()" },
        { label: "Field", url: JDK + "java/lang/reflect/Field.html" }
      ],
      done: ["4 test Q06 hijau", "Kamu bisa menjelaskan bagaimana Spring menemukan @RestController"],
      hints: [
        "Anotasi punya \"umur\": ada yang hanya hidup di source code, ada yang di file .class, ada yang sampai runtime. Default-nya bukan runtime.",
        "Baca RetentionPolicy: tiga nilainya dan mana yang bisa dibaca lewat reflection. Lalu baca Field.isAnnotationPresent dan Field.get.",
        "Tambahkan meta-anotasi retensi runtime ke Required. Di missing: ambil semua field yang dideklarasikan class objek. Untuk setiap field yang punya Required, izinkan akses, baca nilainya, dan tambahkan nama field ke hasil kalau nilainya null atau String yang kosong setelah di-trim."
      ],
      prompt: "Jelaskan bagaimana framework seperti Spring atau JUnit memakai reflection untuk membaca anotasi, dan kenapa retention penting. Jangan beri kode untuk kasus saya.",
      reflect: "Kenapa @Test di JUnit harus memakai retention runtime?"
    },
    {
      id: "Q07", level: 0, xp: 80, minutes: 60,
      title: "Dependency Injection manual",
      mission: "Bangun Spring versi mini: container yang membuat objek, menyimpannya sebagai singleton, dan menyambungkan dependensinya. Setelah ini, @Component dan constructor injection tidak akan terasa seperti sihir.",
      spec: [
        "register(type, factory): simpan saja, factory BELUM dipanggil (lazy).",
        "get(type): panggil factory saat pertama, simpan, kembalikan instance yang sama setelahnya.",
        "Belum terdaftar → IllegalStateException yang menyebut nama class."
      ],
      files: ["MiniContainer.java"],
      test: "Q07*",
      prereq: ["F07", "F11", "F13"],
      concepts: ["Inversion of Control", "Singleton scope", "Lazy initialization", "Supplier<T>", "Class<T> sebagai kunci"],
      docs: [
        { label: "Spring: The IoC Container (intro)", url: "https://docs.spring.io/spring-framework/reference/core/beans/introduction.html" },
        { label: "Spring: Dependency Injection", url: "https://docs.spring.io/spring-framework/reference/core/beans/dependencies/factory-collaborators.html" },
        { label: "Supplier", url: JDK + "java/util/function/Supplier.html" },
        { label: "Class.cast", url: JDK + "java/lang/Class.html#cast(java.lang.Object)" }
      ],
      done: ["4 test Q07 hijau", "Kamu bisa menjelaskan IoC dalam satu kalimat"],
      hints: [
        "Kamu butuh dua tempat penyimpanan: satu untuk resep (factory), satu untuk objek yang sudah jadi. Kuncinya adalah tipe class.",
        "Baca pengantar IoC Container di dokumentasi Spring: perhatikan istilah \"bean definition\" (resep) dan \"singleton\" (objek jadi). Class.cast membantu mengembalikan tipe yang tepat tanpa cast mentah.",
        "register menyimpan factory di map resep. get: kalau map objek sudah punya tipe itu, kembalikan. Kalau tidak, ambil resepnya; kalau resepnya tidak ada, lempar exception dengan nama class. Kalau ada, panggil resep sekali, simpan hasilnya di map objek, lalu kembalikan."
      ],
      prompt: "Jelaskan Inversion of Control dan Dependency Injection dengan analogi restoran (dapur, pelayan, pemasok). Lalu apa bedanya singleton dan prototype scope? Jangan beri kode.",
      reflect: "Bagian mana dari MiniContainer yang dilakukan Spring saat aplikasi start?"
    },

    // ───────────────────────── LEVEL 1 ─────────────────────────
    {
      id: "Q08", level: 1, xp: 100, minutes: 45,
      title: "Controller pertama: /ping",
      mission: "Endpoint pertama kamu. Kecil, tapi melewati seluruh jalur: request HTTP → controller → JSON. Nama app harus dibaca dari konfigurasi, bukan ditulis mati di kode.",
      spec: [
        "GET /api/v1/ping → 200, JSON { \"status\": \"ok\", \"app\": \"alterego-api\" }.",
        "Nilai app = properti spring.application.name. Test kedua mengganti properti itu dan mengharapkan nilainya ikut berubah."
      ],
      files: ["src/main/java/com/alterego/api/... (bebas, mis. PingController.java)"],
      test: "Q08*",
      concepts: ["@RestController", "@GetMapping", "Serialisasi JSON otomatis", "@Value / Environment", "Component scanning"],
      docs: [
        { label: "Guide: Building a RESTful Web Service", url: "https://spring.io/guides/gs/rest-service/" },
        { label: "Annotated controllers", url: MVC + "mvc-controller.html" },
        { label: "Request mapping", url: MVC + "mvc-controller/ann-requestmapping.html" },
        { label: "@Value", url: "https://docs.spring.io/spring-framework/reference/core/beans/annotation-config/value-annotations.html" }
      ],
      done: ["2 test Q08 hijau", "App bisa dijalankan dan /api/v1/ping dibuka di browser (setelah Q10)"],
      hints: [
        "Spring memindai package di bawah class @SpringBootApplication dan mendaftarkan class beranotasi sebagai bean. Method controller yang mengembalikan objek otomatis diubah menjadi JSON.",
        "Ikuti guide \"Building a RESTful Web Service\" bagian Create a Resource Controller. Untuk membaca properti, baca halaman @Value. Map atau record sederhana bisa menjadi respons.",
        "Buat class controller di package com.alterego.api atau sub-package-nya. Inject nama app lewat constructor dengan anotasi yang membaca properti. Buat satu method GET di path /api/v1/ping yang mengembalikan objek berisi status dan app."
      ],
      prompt: "Bagaimana Spring Boot tahu class mana yang menjadi controller, dan bagaimana objek Java berubah menjadi JSON di respons? Jelaskan alurnya. Jangan beri kode.",
      reflect: "Kenapa test kedua penting? Apa yang salah kalau \"alterego-api\" ditulis langsung di kode?"
    },
    {
      id: "Q09", level: 1, xp: 100, minutes: 45,
      title: "@ConfigurationProperties",
      mission: "Kelompokkan konfigurasi seeder di satu class bertipe. Daripada @Value yang tersebar di mana-mana, semua properti alterego.seed.* diikat ke satu objek.",
      spec: [
        "Class com.alterego.api.seed.SeedProperties menjadi bean.",
        "Terikat ke prefix alterego.seed: enabled (boolean) dan location (String).",
        "Tambahkan juga ke application.yaml: enabled true, location classpath:seed/ (test memakai application-test.yaml)."
      ],
      files: ["src/main/java/com/alterego/api/seed/SeedProperties.java", "src/main/resources/application.yaml"],
      test: "Q09*",
      concepts: ["Type-safe configuration", "Relaxed binding", "Record sebagai properties", "@ConfigurationPropertiesScan / @EnableConfigurationProperties"],
      docs: [
        { label: "Type-safe Configuration Properties", url: BOOT + "features/external-config.html#features.external-config.typesafe-configuration-properties" },
        { label: "Enabling @ConfigurationProperties-annotated types", url: BOOT + "features/external-config.html#features.external-config.typesafe-configuration-properties.enabling-annotated-types" },
        { label: "YAML di Spring Boot", url: BOOT + "features/external-config.html#features.external-config.yaml" }
      ],
      done: ["1 test Q09 hijau"],
      hints: [
        "@ConfigurationProperties saja tidak cukup. Class itu juga harus didaftarkan sebagai bean. Ada beberapa cara mendaftarkannya.",
        "Baca bagian \"Enabling @ConfigurationProperties-annotated Types\": bandingkan anotasi scan di class aplikasi dengan anotasi enable yang menyebut class tertentu. Record didukung (constructor binding).",
        "Buat record atau class di package seed dengan dua komponen sesuai nama properti, beri anotasi properties dengan prefix yang tepat. Daftarkan lewat anotasi scan di class aplikasi utama. Tambahkan blok alterego.seed di application.yaml."
      ],
      prompt: "Apa kelebihan @ConfigurationProperties dibanding @Value, dan apa itu relaxed binding di Spring Boot? Jangan beri kode untuk kasus saya.",
      reflect: "Bagaimana test bisa memakai location yang berbeda dari aplikasi asli tanpa mengubah kode?"
    },
    {
      id: "Q10", level: 1, xp: 120, minutes: 60,
      title: "Profile dev + MongoDB Atlas",
      mission: "Siapkan database sungguhan di MongoDB Atlas (gratis), tanpa membocorkan password ke Git. Quest ini sebagian dikerjakan manual di browser.",
      spec: [
        "Daftar Atlas → buat cluster M0 (gratis) → buat database user → tambahkan IP address → salin connection string.",
        "Buat src/main/resources/application-dev.yaml yang membaca URI dari ${MONGODB_URI}.",
        "Tidak ada user:password di file konfigurasi mana pun.",
        "Tambahkan .env ke .gitignore.",
        "Uji: set env var MONGODB_URI, jalankan app dengan profil dev, buka /api/v1/ping."
      ],
      files: ["src/main/resources/application-dev.yaml", ".gitignore"],
      test: "Q10*",
      concepts: ["Spring profiles", "Externalized configuration", "Environment variables", "Secret hygiene"],
      docs: [
        { label: "MongoDB Atlas: Get Started", url: "https://www.mongodb.com/docs/atlas/getting-started/" },
        { label: "Spring Boot: Profiles", url: BOOT + "features/profiles.html" },
        { label: "Spring Boot: MongoDB", url: BOOT + "data/nosql.html#data.nosql.mongodb" },
        { label: "Property placeholders", url: BOOT + "features/external-config.html#features.external-config.files.property-placeholders" }
      ],
      done: ["3 test Q10 hijau", "App jalan dengan profil dev dan terhubung ke Atlas (lihat log saat start)"],
      hints: [
        "File application-NAMA.yaml hanya dibaca kalau profil NAMA aktif. Nilai ${...} diambil dari environment variable saat aplikasi berjalan.",
        "Baca halaman Spring Boot → Data → NoSQL → MongoDB untuk nama properti URI. Perhatikan nama properti di Spring Boot 4 berbeda dari tutorial lama Spring Boot 3. Baca juga Profiles bagian profile-specific files.",
        "Selesaikan langkah Atlas sampai mendapat connection string. Buat file yaml profil dev berisi properti URI Mongo dengan nilai placeholder env var. Tambahkan baris .env ke .gitignore. Di PowerShell, set env var, lalu jalankan spring-boot:run dengan profil dev."
      ],
      prompt: "Kenapa connection string database tidak boleh di-commit ke Git, dan apa saja cara umum menyimpan secret di aplikasi Spring? Jangan beri kode.",
      reflect: "Apa yang terjadi kalau app dijalankan TANPA profil dev? Coba, lalu catat error-nya."
    },

    // ───────────────────────── LEVEL 2 ─────────────────────────
    {
      id: "Q11", level: 2, xp: 120, minutes: 60,
      title: "Document Player + repository",
      mission: "Petakan collection players ke class Java, lalu buat repository dengan derived query. Test memasukkan dokumen mentah ke Mongo, dan class-mu harus bisa membacanya.",
      spec: [
        "Skema persis seperti BLUEPRINT §4 (collection \"players\", _id = \"game:playerId\").",
        "joinDate bertipe LocalDate.",
        "com.alterego.api.player.PlayerRepository dengan findByGameOrderByDisplayOrderAsc(game) dan findByGameAndStatusOrderByDisplayOrderAsc(game, status).",
        "findById(\"testgame:alpha\") bekerja."
      ],
      files: ["player/Player.java", "player/PlayerRepository.java"],
      test: "Q11*",
      concepts: ["@Document", "@Id", "MongoRepository", "Derived query methods", "Object mapping"],
      docs: [
        { label: "Spring Data MongoDB: Mapping", url: DATA + "mongodb/mapping/mapping.html" },
        { label: "Repositories: Query methods", url: DATA + "repositories/query-methods-details.html" },
        { label: "MongoDB repositories", url: DATA + "mongodb/repositories/repositories.html" },
        { label: "Supported keywords (MongoDB)", url: DATA + "mongodb/repositories/query-methods.html" }
      ],
      done: ["3 test Q11 hijau"],
      hints: [
        "Spring Data membuat implementasi repository dari nama method. Yang kamu tulis hanya interface. Field yang diberi @Id dipetakan ke _id.",
        "Baca Mapping: penamaan collection lewat @Document dan cara @Id bekerja. Lalu baca Query methods: susunan find…By…And…OrderBy…Asc.",
        "Buat class/record Player dengan field sesuai tabel skema, field id sebagai @Id, dan anotasi document yang menyebut nama collection. Buat interface yang meng-extend repository Mongo dengan tipe Player dan tipe id String, lalu tambahkan dua method sesuai nama di spesifikasi."
      ],
      prompt: "Bagaimana Spring Data bisa membuat query hanya dari nama method seperti findByNameOrderByAgeDesc? Jelaskan mekanismenya. Jangan beri kode untuk kasus saya.",
      reflect: "Kenapa _id dibuat gabungan game:playerId? (Lihat BLUEPRINT D1, lalu tulis dengan kalimatmu sendiri.)"
    },
    {
      id: "Q12", level: 2, xp: 120, minutes: 60,
      title: "Achievement: embed vs referensi",
      mission: "Petakan prestasi. prize adalah objek tertanam (embedded), sedangkan rosterIds adalah daftar referensi ke pemain. Dua cara pemodelan dalam satu dokumen.",
      spec: [
        "Collection \"achievements\", skema BLUEPRINT §4.",
        "prize = objek {amount, currency}, boleh null.",
        "com.alterego.api.achievement.AchievementRepository: findByGameOrderByStartDateDesc(game), findByGameAndRosterIdsContaining(game, playerId)."
      ],
      files: ["achievement/Achievement.java", "achievement/AchievementRepository.java"],
      test: "Q12*",
      concepts: ["Embedded document", "Array field query", "Containing keyword", "Embed vs reference"],
      docs: [
        { label: "Mapping: nested objects", url: DATA + "mongodb/mapping/mapping.html" },
        { label: "Query methods: keywords", url: DATA + "mongodb/repositories/query-methods.html" },
        { label: "MongoDB: Data modeling (embed vs reference)", url: "https://www.mongodb.com/docs/manual/data-modeling/concepts/embedding-vs-references/" }
      ],
      done: ["3 test Q12 hijau", "Jurnal: kapan embed, kapan referensi"],
      hints: [
        "Objek di dalam objek otomatis dipetakan ke sub-dokumen. Tidak perlu anotasi khusus. Untuk field array, ada keyword query yang artinya \"array ini memuat nilai X\".",
        "Baca tabel Supported keywords for query methods di dokumentasi MongoDB repositories dan cari keyword untuk koleksi. Baca juga artikel MongoDB tentang embedding vs references.",
        "Buat tipe kecil untuk prize (amount Double, currency String) dan pakai sebagai field di Achievement. rosterIds bertipe List<String>. Repository: dua method, satu mengurutkan startDate turun, satu memakai keyword untuk array berisi playerId."
      ],
      prompt: "Kapan sebaiknya memakai embedded document dan kapan referensi di MongoDB? Beri contoh dari domain toko buku. Jangan beri kode.",
      reflect: "Kalau seorang pemain ganti nickname, apakah data prestasi perlu diubah? Kenapa?"
    },

    // ───────────────────────── LEVEL 3 ─────────────────────────
    {
      id: "Q13", level: 3, xp: 150, minutes: 90,
      title: "Seeder: JSON → MongoDB",
      mission: "Saat aplikasi start, baca semua file di seed/ dan simpan ke Mongo. Data aslinya tidak bersih. Siapkan diri untuk menemukan kejutan: ID ganda, tanggal tidak lengkap, dan field yang ternyata array.",
      spec: [
        "Class com.alterego.api.seed.DataSeeder, berjalan saat start kalau alterego.seed.enabled = true.",
        "Baca <location>rosters/*.json (field players) dan <location>achievements/*.json (field achievements).",
        "Pemain: _id = game:id. Tanggal disimpan sebagai tanggal, bukan string.",
        "Tanggal \"yyyy-MM\" → tanggal 1 bulan itu (BLUEPRINT D3). startDate boleh null.",
        "Punya method publik seed() tanpa parameter (dipakai test Q14)."
      ],
      files: ["seed/DataSeeder.java"],
      test: "Q13*",
      concepts: ["CommandLineRunner / ApplicationRunner", "ResourcePatternResolver (classpath*:…/*.json)", "Jackson JsonNode / JsonMapper", "Mapping JSON → domain"],
      docs: [
        { label: "Spring Boot: Using ApplicationRunner or CommandLineRunner", url: BOOT + "features/spring-application.html#features.spring-application.command-line-runner" },
        { label: "Spring: Resources & wildcard patterns", url: "https://docs.spring.io/spring-framework/reference/core/resources.html" },
        { label: "Spring Boot: JSON (Jackson)", url: BOOT + "features/json.html" },
        { label: "Jackson 3 (databind)", url: "https://github.com/FasterXML/jackson-databind" }
      ],
      done: ["4 test Q13 hijau", "Jurnal: daftar data kotor yang kamu temukan"],
      hints: [
        "Pisahkan tiga tugas: menemukan file, membaca JSON menjadi pohon node, lalu mengubah node menjadi Player/Achievement. Di Spring Boot 4, Jackson versi 3 sudah tersedia (package tools.jackson).",
        "Baca Resources: PathMatchingResourcePatternResolver dan pola wildcard. Baca juga CommandLineRunner. Di Jackson 3, JsonNode.asString() menolak node array. Error itu akan muncul kalau sebuah field ternyata array.",
        "Inject SeedProperties dan dua repository lewat constructor. Di run, panggil seed() kalau enabled. Di seed: cari semua file roster dengan pola dari location, baca masing-masing, ubah setiap elemen players menjadi Player (tangani field null dan tanggal pendek), lalu simpan semuanya. Ulangi untuk achievements."
      ],
      prompt: "Apa beda CommandLineRunner dan ApplicationRunner di Spring Boot, dan kapan keduanya dijalankan dalam siklus hidup aplikasi? Jangan beri kode untuk kasus saya.",
      reflect: "Data kotor apa saja yang kamu temukan, dan bagaimana kamu memutuskan cara menanganinya?"
    },
    {
      id: "Q14", level: 3, xp: 120, minutes: 45,
      title: "Idempotent: upsert",
      mission: "Seeder boleh berjalan berkali-kali tanpa menggandakan data. Kalau data di DB berubah, seed mengembalikannya sesuai file (BLUEPRINT D5).",
      spec: [
        "seed() dua kali → jumlah dokumen tetap.",
        "Nickname di DB diubah → seed() → kembali sesuai file."
      ],
      files: ["seed/DataSeeder.java"],
      test: "Q14*",
      concepts: ["Idempotence", "Upsert", "save() dengan id yang sudah ada", "saveAll"],
      docs: [
        { label: "CrudRepository.save", url: "https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/repository/CrudRepository.html#save(S)" },
        { label: "MongoDB: Upsert behavior", url: "https://www.mongodb.com/docs/manual/reference/method/db.collection.updateOne/#upsert-behavior" }
      ],
      done: ["2 test Q14 hijau", "Q13 tetap hijau"],
      hints: [
        "Dengan _id yang deterministik (dihitung dari data, bukan dibuat acak), menyimpan dokumen dengan id yang sama berarti mengganti, bukan menambah.",
        "Baca perilaku save di Spring Data: apa yang terjadi kalau entitas punya id yang sudah ada di database? Bandingkan dengan insert.",
        "Kalau Q13 sudah memakai save/saveAll dengan id gabungan yang dihitung dari file, kemungkinan test ini langsung hijau. Kalau belum, pastikan setiap dokumen punya id dari data dan simpan dengan operasi yang mengganti, bukan menambah."
      ],
      prompt: "Apa arti idempotent dalam konteks API dan job data? Beri contoh operasi HTTP yang idempotent dan yang tidak. Jangan beri kode.",
      reflect: "Apa risiko D5 (seed menimpa perubahan admin)? Kapan aturan ini perlu diubah?"
    },
    {
      id: "Q15", level: 3, xp: 120, minutes: 45,
      title: "Gagal cepat, pesan jelas",
      mission: "Kalau file seed rusak, aplikasi harus menolak start, dan pesan error-nya menyebut file serta field yang salah. Test menyalakan aplikasi dengan seed-broken/.",
      spec: [
        "nickname kosong/null di roster → start gagal.",
        "Pesan error (atau cause-nya) memuat nama file \"broken.json\" dan kata \"nickname\".",
        "Jangan menyimpan sebagian data rusak."
      ],
      files: ["seed/DataSeeder.java (dan class validasi bila mau)"],
      test: "Q15*",
      concepts: ["Fail fast", "Exception chaining", "Validasi sebelum simpan", "Resource.getFilename"],
      docs: [
        { label: "Spring Boot: Startup failure", url: BOOT + "features/spring-application.html#features.spring-application.startup-failure" },
        { label: "Throwable (cause chain)", url: JDK + "java/lang/Throwable.html" },
        { label: "Resource.getFilename", url: "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/Resource.html#getFilename()" }
      ],
      done: ["1 test Q15 hijau", "Q13 dan Q14 tetap hijau"],
      hints: [
        "Exception yang dilempar dari runner saat startup membuat Spring menghentikan aplikasi. Yang perlu kamu pastikan adalah isi pesannya.",
        "Lihat Q04 dan Q06: kamu sudah pernah membangun validasi yang menyebut nama field. Resource punya method untuk mengambil nama file.",
        "Validasi semua pemain dari sebuah file sebelum menyimpan apa pun. Saat ada pelanggaran, lempar exception yang pesannya menyebut nama file, id pemain, dan nama field. Validasi dulu semua file, baru simpan, supaya data tidak tersimpan setengah."
      ],
      prompt: "Apa itu prinsip fail-fast, dan kenapa pesan error yang baik harus menyebut konteks (file, baris, nilai)? Jangan beri kode.",
      reflect: "Bagaimana rasanya pesan error buatanmu dibaca orang lain jam 2 pagi? Apakah sudah cukup jelas?"
    },

    // ───────────────────────── LEVEL 4 ─────────────────────────
    {
      id: "Q16", level: 4, xp: 150, minutes: 75,
      title: "GET /players + DTO",
      mission: "Endpoint daftar pemain dengan filter. Document Mongo tidak boleh bocor ke JSON: field id di API adalah playerId, bukan _id.",
      spec: [
        "GET /api/v1/players?game=&status= (keduanya opsional).",
        "Respons array PlayerDto (BLUEPRINT §6), urut displayOrder; tanpa game: urut game lalu displayOrder.",
        "joinDate \"yyyy-MM-dd\". Tidak ada _id / playerId di JSON.",
        "game di luar mlbb/pubgm/cs2 → 400 (BLUEPRINT D7)."
      ],
      files: ["player/PlayerController.java", "player/PlayerService.java", "player/PlayerDto.java", "common/..."],
      test: "Q16*",
      concepts: ["DTO & mapping", "@RequestParam opsional", "Service layer", "ResponseStatusException atau exception kustom"],
      docs: [
        { label: "@RequestParam", url: MVC + "mvc-controller/ann-methods/requestparam.html" },
        { label: "Spring Data: Sort", url: "https://docs.spring.io/spring-data/commons/reference/repositories/query-methods-details.html#repositories.special-parameters" },
        { label: "ResponseStatusException", url: "https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/server/ResponseStatusException.html" }
      ],
      done: ["4 test Q16 hijau", "Coba di app dev: /api/v1/players?game=pubgm"],
      hints: [
        "Controller tipis: terima parameter, panggil service, kembalikan DTO. Service memilih query repository sesuai kombinasi filter. DTO adalah record yang hanya berisi field untuk dunia luar.",
        "Baca @RequestParam bagian required. Untuk 400, cara paling cepat: exception yang membawa status HTTP (ResponseStatusException). Q22 nanti merapikannya.",
        "Buat record DTO dengan field sesuai kontrak dan method statis untuk mengubah Player menjadi DTO. Service: validasi game kalau dikirim, lalu pilih method repository (per game, per game+status, atau semua dengan sort). Controller memetakan GET di path daftar dan mengubah hasil service ke DTO."
      ],
      prompt: "Kenapa API sebaiknya tidak mengembalikan entity database secara langsung? Jelaskan risiko dan manfaat DTO. Jangan beri kode.",
      reflect: "Apa yang bocor ke klien kalau Player dikembalikan langsung?"
    },
    {
      id: "Q17", level: 4, xp: 120, minutes: 45,
      title: "GET /players/{game}/{id}",
      mission: "Detail satu pemain. Kasus \"aces\" kembali: id sama, game berbeda, orang berbeda.",
      spec: [
        "GET /api/v1/players/{game}/{id} → 200 PlayerDto.",
        "Tidak ada → 404. Game tidak dikenal → 400.",
        "cs2/whyphy → joinDate \"2021-05-01\"."
      ],
      files: ["player/PlayerController.java", "player/PlayerService.java"],
      test: "Q17*",
      concepts: ["@PathVariable", "Optional.orElseThrow", "404 vs 400"],
      docs: [
        { label: "URI patterns & @PathVariable", url: MVC + "mvc-controller/ann-requestmapping.html#mvc-ann-requestmapping-uri-templates" },
        { label: "Optional.orElseThrow", url: JDK + "java/util/Optional.html#orElseThrow(java.util.function.Supplier)" }
      ],
      done: ["5 test Q17 hijau", "Q16 tetap hijau"],
      hints: [
        "Repository sudah bisa findById dengan id gabungan, dan hasilnya Optional. Tinggal memutuskan apa yang terjadi kalau kosong.",
        "Baca URI patterns: dua variabel dalam satu path. Optional.orElseThrow menerima supplier exception.",
        "Tambahkan method GET dengan dua path variable. Di service, validasi game, susun id gabungan, cari lewat repository, dan lempar exception 404 kalau kosong. Ubah hasilnya ke DTO."
      ],
      prompt: "Kapan API harus mengembalikan 400, 404, dan 422? Beri contoh untuk masing-masing. Jangan beri kode.",
      reflect: "Kenapa game tidak dikenal = 400, tapi pemain tidak ada = 404?"
    },
    {
      id: "Q18", level: 4, xp: 180, minutes: 90,
      title: "Achievements + pagination",
      mission: "Daftar prestasi dengan filter dinamis dan pagination. Kombinasi filternya terlalu banyak untuk derived query, jadi ini saatnya memakai MongoTemplate + Criteria.",
      spec: [
        "GET /api/v1/achievements?game=&year=&placement=&page=0&size=10.",
        "Semua filter opsional dan boleh digabung. year = rentang tanggal tahun itu.",
        "Respons PageResponse {content, page, size, totalElements, totalPages}.",
        "Urut startDate turun, lalu id A-Z; tanpa tanggal di akhir. size default 10, maks 50."
      ],
      files: ["achievement/AchievementController.java", "achievement/AchievementService.java", "common/PageResponse.java"],
      test: "Q18*",
      concepts: ["MongoTemplate", "Query & Criteria dinamis", "Pageable / PageRequest", "Sort dengan dua kunci", "Rentang tanggal"],
      docs: [
        { label: "MongoTemplate: querying documents", url: DATA + "mongodb/template-query-operations.html" },
        { label: "Paging & sorting (special parameters)", url: "https://docs.spring.io/spring-data/commons/reference/repositories/query-methods-details.html#repositories.special-parameters" },
        { label: "PageRequest", url: "https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/PageRequest.html" }
      ],
      done: ["4 test Q18 hijau"],
      hints: [
        "Bangun query sedikit demi sedikit: mulai dari query kosong, tambahkan kriteria hanya untuk filter yang dikirim. Hitung total SEBELUM menerapkan halaman.",
        "Baca \"Querying Documents\" di MongoTemplate: Query, Criteria.where, gte/lt. Lalu PageRequest.of dengan Sort beberapa order. Jangan serialisasi Page dari Spring langsung. Buat record PageResponse sendiri.",
        "Batasi size ke rentang 1–50. Buat Query, tambahkan kriteria game/placement/year (year = mulai 1 Januari sampai sebelum 1 Januari tahun berikutnya). Hitung jumlah dengan count. Terapkan PageRequest dengan sort startDate turun lalu id naik. Jalankan find, ubah ke DTO, lalu hitung totalPages dari total dan size."
      ],
      prompt: "Jelaskan offset pagination vs cursor pagination, dan kenapa total harus dihitung terpisah. Jangan beri kode.",
      reflect: "Kenapa sort kedua (id) diperlukan supaya pagination stabil?"
    },
    {
      id: "Q19", level: 4, xp: 120, minutes: 45,
      title: "Prestasi seorang pemain",
      mission: "Endpoint yang mengikuti referensi: dari pemain ke prestasinya. Data asli belum punya rosterIds, jadi test memakai fixture (lihat BLUEPRINT §8).",
      spec: [
        "GET /api/v1/players/{game}/{id}/achievements → AchievementDto[].",
        "Pemain ada tapi tidak direferensikan → [] (bukan 404).",
        "Pemain tidak ada → 404."
      ],
      files: ["player/PlayerController.java", "achievement/AchievementService.java"],
      test: "Q19*",
      concepts: ["Query lewat referensi", "Ketergantungan antar fitur", "Koleksi kosong vs tidak ditemukan"],
      docs: [
        { label: "Query methods: collection keywords", url: DATA + "mongodb/repositories/query-methods.html" }
      ],
      done: ["3 test Q19 hijau"],
      hints: [
        "Dua langkah: pastikan pemainnya ada (pakai ulang logika Q17), lalu cari prestasi dengan method repository dari Q12.",
        "Kamu sudah punya semua bagiannya. Yang perlu diputuskan adalah service mana yang memanggil service mana.",
        "Tambahkan GET di path detail pemain + /achievements. Panggil logika detail pemain (yang sudah melempar 404). Kalau lolos, panggil repository prestasi dengan game dan id, lalu ubah ke DTO."
      ],
      prompt: "Kalau satu service membutuhkan service lain, bagaimana menjaga agar tidak terjadi ketergantungan melingkar (circular dependency)? Jangan beri kode.",
      reflect: "Kenapa daftar kosong lebih jujur daripada 404 di kasus ini?"
    },

    // ───────────────────────── LEVEL 5 ─────────────────────────
    {
      id: "Q20", level: 5, xp: 180, minutes: 90,
      title: "POST pemain + validasi",
      mission: "Endpoint menulis pertama. Input dari luar tidak bisa dipercaya: validasi dengan Bean Validation. Bandingkan dengan validator buatanmu di Q04.",
      spec: [
        "POST /api/v1/admin/players, body PlayerRequest (aturan di BLUEPRINT §6).",
        "Valid → 201 + header Location /api/v1/players/{game}/{id} + body PlayerDto.",
        "Tidak valid (termasuk joinDate bukan tanggal) → 400, tidak ada yang tersimpan.",
        "game+id sudah ada → 409, data lama tidak berubah."
      ],
      files: ["player/PlayerRequest.java", "player/PlayerController.java", "player/PlayerService.java"],
      test: "Q20*",
      concepts: ["@RequestBody", "@Valid + constraint (@NotBlank, @Size, @Pattern, @Min)", "ResponseEntity.created", "409 Conflict"],
      docs: [
        { label: "Spring Boot: Validation", url: BOOT + "io/validation.html" },
        { label: "Spring MVC: Validation", url: MVC + "mvc-controller/ann-validation.html" },
        { label: "Jakarta Bean Validation constraints", url: "https://jakarta.ee/specifications/bean-validation/3.0/apidocs/jakarta/validation/constraints/package-summary.html" },
        { label: "ResponseEntity", url: MVC + "mvc-controller/ann-methods/responseentity.html" }
      ],
      done: ["11 test Q20 hijau"],
      hints: [
        "Constraint ditaruh di field request, dan validasi dipicu oleh satu anotasi di parameter controller. Body yang tidak bisa dibaca (tanggal salah) gagal sebelum validasi, tapi tetap harus menghasilkan 400.",
        "Baca Spring MVC Validation. Cari constraint untuk: tidak kosong, panjang maksimum, regex, dan angka minimum. ResponseEntity punya builder untuk 201 dengan URI lokasi.",
        "Buat record request dengan constraint di tiap komponen. Controller: parameter body ber-@Valid. Service: cek dulu apakah id gabungan sudah ada (409). Kalau belum, ubah request menjadi Player, simpan, lalu kembalikan DTO. Controller membungkus hasilnya dengan respons created ke path detail."
      ],
      prompt: "Bagaimana Bean Validation bekerja di Spring MVC, dan apa beda error 400 karena JSON rusak dengan 400 karena validasi gagal? Jangan beri kode.",
      reflect: "Apa kesamaan dan perbedaan Bean Validation dengan PlayerValidator buatanmu di Q04?"
    },
    {
      id: "Q21", level: 5, xp: 150, minutes: 60,
      title: "PUT & DELETE",
      mission: "Lengkapi siklus hidup data. PUT di API ini hanya mengubah data, tidak membuat data baru. id dan game diambil dari path.",
      spec: [
        "PUT /api/v1/admin/players/{game}/{id} → 200 PlayerDto · 400 · 404.",
        "DELETE /api/v1/admin/players/{game}/{id} → 204 · 404.",
        "Setelah DELETE: GET → 404, DELETE lagi → 404."
      ],
      files: ["player/PlayerController.java", "player/PlayerService.java"],
      test: "Q21*",
      concepts: ["@PutMapping / @DeleteMapping", "204 No Content", "Semantik PUT", "Pakai ulang validasi"],
      docs: [
        { label: "Request mapping (shortcut variants)", url: MVC + "mvc-controller/ann-requestmapping.html" },
        { label: "RFC 9110: PUT", url: "https://www.rfc-editor.org/rfc/rfc9110#name-put" },
        { label: "RFC 9110: DELETE", url: "https://www.rfc-editor.org/rfc/rfc9110#name-delete" }
      ],
      done: ["4 test Q21 hijau", "Q20 tetap hijau"],
      hints: [
        "PUT = ambil yang lama (404 kalau tidak ada), ganti isinya, simpan. DELETE = pastikan ada, lalu hapus. Body PUT boleh memakai record request yang sama, tapi id/game dari body diabaikan.",
        "Baca RFC 9110 bagian PUT dan DELETE untuk kode status yang lazim. ResponseEntity punya builder untuk noContent.",
        "Service update: cari pemain (pakai ulang logika 404), buat objek baru dengan id dari path dan isi dari body, simpan, kembalikan DTO. Service delete: cari (404), hapus. Controller: dua mapping baru, DELETE mengembalikan 204 tanpa body."
      ],
      prompt: "Apa beda PUT dan PATCH, dan apakah PUT boleh membuat resource baru menurut HTTP? Jangan beri kode.",
      reflect: "Kenapa di record request, id dan game dibuat opsional supaya bisa dipakai untuk PUT?"
    },

    // ───────────────────────── LEVEL 6 ─────────────────────────
    {
      id: "Q22", level: 6, xp: 180, minutes: 75,
      title: "ProblemDetail di semua error",
      mission: "Seragamkan semua error menjadi application/problem+json (RFC 9457) lewat satu handler global. Klien cukup belajar satu format error.",
      spec: [
        "404, 400, 409 → content-type application/problem+json, ada status, title, detail.",
        "detail menyebut nilai yang bermasalah (id, game).",
        "400 validasi → properti tambahan errors: { field: pesan }."
      ],
      files: ["common/ApiExceptionHandler.java", "common/*Exception.java"],
      test: "Q22*",
      concepts: ["@RestControllerAdvice", "@ExceptionHandler", "ProblemDetail", "MethodArgumentNotValidException", "Exception kustom per makna"],
      docs: [
        { label: "Spring MVC: Error responses (ProblemDetail)", url: MVC + "mvc-ann-rest-exceptions.html" },
        { label: "@ExceptionHandler", url: MVC + "mvc-controller/ann-exceptionhandler.html" },
        { label: "Controller advice", url: MVC + "mvc-controller/ann-advice.html" },
        { label: "RFC 9457", url: "https://www.rfc-editor.org/rfc/rfc9457" }
      ],
      done: ["4 test Q22 hijau", "Q16–Q21 tetap hijau"],
      hints: [
        "Satu class advice menangkap exception dari semua controller. Buat exception sendiri untuk setiap makna (tidak ditemukan, input salah, konflik), lalu petakan masing-masing ke status.",
        "Baca \"Error Responses\": ProblemDetail.forStatusAndDetail dan setProperty untuk properti tambahan. Exception validasi menyimpan daftar FieldError di binding result-nya.",
        "Buat tiga exception unchecked di common. Ganti ResponseStatusException di service dengan exception itu. Buat class advice dengan satu handler per exception yang mengembalikan ProblemDetail. Tambahkan handler validasi yang menyusun map field ke pesan dari binding result, dan handler untuk body yang tidak bisa dibaca."
      ],
      prompt: "Apa itu RFC 9457 Problem Details, dan kenapa format error yang konsisten penting untuk klien API? Jangan beri kode.",
      reflect: "Sebelum dan sesudah Q22: berapa banyak if/try-catch yang hilang dari controller-mu?"
    },

    // ───────────────────────── LEVEL 7 ─────────────────────────
    {
      id: "Q23", level: 7, xp: 180, minutes: 75,
      title: "Statistik per game",
      mission: "Hitung statistik dari data prestasi, seperti di Q03, tapi sekarang dari database. Pilih sendiri: stream di Java, atau aggregation pipeline Mongo.",
      spec: [
        "GET /api/v1/stats/{game} → {game, tournaments, titles, runnerUps, totalPrizeUSD, byYear}.",
        "titles = placement persis \"1\", runnerUps = persis \"2\".",
        "totalPrizeUSD: hanya currency USD, amount null dilewati.",
        "Tanpa startDate: dihitung di tournaments, tidak di byYear. Game tidak dikenal → 400."
      ],
      files: ["stats/StatsController.java", "stats/StatsService.java", "stats/StatsDto.java"],
      test: "Q23*",
      concepts: ["Agregasi di aplikasi vs di database", "Aggregation framework (opsional)", "Map sebagai JSON object"],
      docs: [
        { label: "Spring Data MongoDB: Aggregation framework", url: DATA + "mongodb/aggregation-framework.html" },
        { label: "MongoDB: $group", url: "https://www.mongodb.com/docs/manual/reference/operator/aggregation/group/" }
      ],
      done: ["4 test Q23 hijau", "Jurnal: kenapa memilih stream atau aggregation"],
      hints: [
        "Data per game sedikit (puluhan dokumen). Menghitung di Java dengan stream sama sahnya dengan aggregation. Yang penting kamu tahu trade-off-nya.",
        "Buka lagi kode Q03 (AchievementMath). Kalau ingin mencoba aggregation, baca bagian Aggregation framework: match → group.",
        "Service: validasi game, ambil semua prestasi game itu, lalu hitung jumlah, title, runner-up, total hadiah USD, dan pengelompokan per tahun (lewati yang tanpa tanggal; gunakan map terurut). Kembalikan record DTO."
      ],
      prompt: "Kapan sebaiknya agregasi dilakukan di database dan kapan di aplikasi? Sebutkan faktor pertimbangannya. Jangan beri kode.",
      reflect: "Kalau datanya 10 juta dokumen, apa yang akan kamu ubah?"
    },
    {
      id: "Q24", level: 7, xp: 200, minutes: 75,
      title: "Audit summary",
      mission: "File achievement punya summary yang ditulis tangan. Bandingkan dengan hasil hitung. Spoiler: data asli PUBGM dan CS2 TIDAK konsisten. API-mu akan menemukannya.",
      spec: [
        "Seeder juga menyimpan summary per game (tempat dan bentuknya bebas).",
        "GET /api/v1/stats/{game}/check → {game, consistent, fields[3]} (BLUEPRINT §6).",
        "fields urut: titles, runnerUps, totalPrizeUSD. Status: UNKNOWN (declared null) / MATCH (selisih < 0.01) / MISMATCH.",
        "Setiap MISMATCH ditulis ke log sebagai WARN."
      ],
      files: ["seed/DataSeeder.java", "stats/..."],
      test: "Q24*",
      concepts: ["Data quality check", "Null sebagai 'tidak diketahui'", "Perbandingan floating point", "Logging (SLF4J)"],
      docs: [
        { label: "Spring Boot: Logging", url: BOOT + "features/logging.html" },
        { label: "MongoTemplate / collection mentah", url: DATA + "mongodb/template-api.html" },
        { label: "Floating point: kenapa tidak pakai ==", url: "https://docs.oracle.com/cd/E19957-01/806-3568/ncg_goldberg.html" }
      ],
      done: ["3 test Q24 hijau", "Jurnal: field mana yang tidak cocok di data asli, dan apa dugaanmu penyebabnya"],
      hints: [
        "Ada dua bagian: menyimpan angka yang dideklarasikan (saat seed), lalu membandingkannya dengan hasil Q23 (saat request). Pakai ulang StatsService.",
        "Untuk menyimpan summary, kamu bisa membuat document baru atau memakai MongoTemplate langsung. Bandingkan double dengan toleransi, bukan ==. Baca bagian Logging untuk logger.",
        "Saat seed, simpan objek summary per game kalau ada. Di service: hitung stats, ambil summary game itu, lalu untuk tiga field susun hasil berisi nama, nilai deklarasi (null kalau tidak ada atau bukan angka), nilai hitung, dan status. consistent bernilai true kalau tidak ada MISMATCH. Tulis log warn untuk setiap MISMATCH."
      ],
      prompt: "Bagaimana cara merancang pengecekan kualitas data yang membedakan 'tidak diketahui' dan 'salah'? Jangan beri kode.",
      reflect: "Data siapa yang benar: summary atau daftar prestasi? Bagaimana memastikannya tanpa mengarang?"
    },

    // ───────────────────────── LEVEL 8 ─────────────────────────
    {
      id: "Q25", level: 8, xp: 200, minutes: 90,
      title: "Merch: produk & harga",
      mission: "Seed 85 produk Shopee, lalu sediakan katalog. Slugs (Q01) dan PriceParser (Q05) akhirnya dipakai sungguhan. Waspada: gambar berupa array.",
      spec: [
        "Seed products.json → collection products (_id = slug dari nama).",
        "priceMin/priceMax dari harga; imageUrl = gambar pertama.",
        "GET /api/v1/products?maxPrice=&sort=name|price_asc|price_desc; sort lain → 400.",
        "GET /api/v1/products/{slug} → 200 / 404."
      ],
      files: ["product/*", "seed/DataSeeder.java"],
      test: "Q25*",
      concepts: ["Pakai ulang kode domain", "Enum untuk pilihan sort", "Sort dinamis", "Data array → nilai tunggal"],
      docs: [
        { label: "Spring Data: Sort", url: "https://docs.spring.io/spring-data/commons/docs/current/api/org/springframework/data/domain/Sort.html" },
        { label: "Spring MVC: type conversion untuk enum @RequestParam", url: MVC + "mvc-controller/ann-methods/typeconversion.html" }
      ],
      done: ["5 test Q25 hijau", "Seluruh suite Q08–Q24 tetap hijau"],
      hints: [
        "Bagian seed hampir sama dengan Q13. Bagian API mirip Q16. Hal barunya ada pada pemetaan pilihan sort ke Sort Spring Data dan penanganan nilai sort yang tidak valid.",
        "Kelas warmup bisa langsung dipakai dari package api, karena masih satu proyek. Baca Sort.by dan Sort.Order untuk kombinasi naik/turun.",
        "Saat seed, untuk setiap produk: buat slug dari nama, parse harga menjadi min dan max, ambil elemen pertama gambar, lalu simpan. Controller daftar: terjemahkan sort ke Sort (lempar 400 kalau tidak dikenal), ambil semua dengan sort itu, saring berdasarkan maxPrice, lalu ubah ke DTO. Detail: cari berdasarkan slug, 404 kalau tidak ada."
      ],
      prompt: "Bagaimana sebaiknya memvalidasi parameter pilihan (seperti sort) di REST API: string, enum, atau whitelist? Bandingkan. Jangan beri kode.",
      reflect: "Apa yang terjadi kalau dua produk punya nama yang menghasilkan slug sama? Rancang solusinya di jurnal."
    },

    // ───────────────────────── LEVEL 9 ─────────────────────────
    {
      id: "Q26", level: 9, xp: 150, minutes: 45,
      title: "OpenAPI + Swagger UI",
      mission: "Tambahkan dependency sendiri di pom.xml, lalu API-mu terdokumentasi otomatis. Ini quest terakhir MVP.",
      spec: [
        "Tambahkan springdoc-openapi (starter webmvc-ui) versi yang kompatibel dengan Spring Boot 4.",
        "/v3/api-docs memuat path utama; /swagger-ui/index.html bisa dibuka.",
        "Opsional: beri judul dan deskripsi API, plus contoh di DTO."
      ],
      files: ["pom.xml", "(opsional) config OpenAPI"],
      test: "Q26*",
      concepts: ["Maven dependency & kompatibilitas versi", "OpenAPI 3", "Dokumentasi yang dihasilkan dari kode"],
      docs: [
        { label: "springdoc-openapi", url: "https://springdoc.org/" },
        { label: "Maven Central: springdoc-openapi-starter-webmvc-ui", url: "https://central.sonatype.com/artifact/org.springdoc/springdoc-openapi-starter-webmvc-ui" },
        { label: "OpenAPI Specification", url: "https://spec.openapis.org/oas/latest.html" }
      ],
      done: ["2 test Q26 hijau", "./mvnw test seluruhnya HIJAU", "Commit: v1.0 MVP"],
      hints: [
        "Satu dependency di pom.xml sudah cukup untuk dua endpoint itu. Yang sulit adalah memilih versi yang benar.",
        "Baca halaman springdoc: tabel kompatibilitas versi springdoc dengan Spring Boot. Spring Boot 4 butuh springdoc generasi yang lebih baru dari 2.x.",
        "Cari versi terbaru springdoc-openapi-starter-webmvc-ui yang mendukung Spring Boot 4 di Maven Central, tambahkan di blok dependencies pom.xml, reload Maven di IDE, jalankan test, lalu buka Swagger UI di app dev."
      ],
      prompt: "Apa itu OpenAPI dan bagaimana dokumentasi API bisa dihasilkan dari kode? Apa kelebihan dan kekurangannya dibanding menulis dokumentasi manual? Jangan beri kode.",
      reflect: "Lihat lagi BLUEPRINT §6. Apa yang berbeda dari rencana awal dibanding hasil akhir, dan kenapa?"
    }
  ],

  badges: [
    { id: "first-blood", name: "First Blood", desc: "Quest pertama selesai" },
    { id: "warmed-up", name: "Warmed Up", desc: "Level 0 tuntas" },
    { id: "spring-loaded", name: "Spring Loaded", desc: "Level 1 tuntas" },
    { id: "data-detective", name: "Data Detective", desc: "Seeder (Level 3) tuntas: data kotor dijinakkan" },
    { id: "clean-sweep", name: "Clean Sweep", desc: "Satu level tuntas tanpa membuka hint" },
    { id: "no-spoilers", name: "No Spoilers", desc: "5 quest berturut-turut tanpa hint" },
    { id: "scribe", name: "Scribe", desc: "10 jurnal, masing-masing minimal 300 karakter" },
    { id: "streak-3", name: "Tiga Hari", desc: "Streak 3 hari" },
    { id: "streak-7", name: "Seminggu", desc: "Streak 7 hari" },
    { id: "auditor", name: "Auditor", desc: "Q24 selesai: kamu menemukan data yang tidak konsisten" },
    { id: "mvp", name: "MVP", desc: "Semua quest selesai, termasuk Fondasi" }
  ]
};
