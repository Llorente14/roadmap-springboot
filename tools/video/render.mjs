// Render klip onboarding: frame per frame lewat Chrome headless → ffmpeg (MP4 H.264 + WebM VP9 + poster WebP + VTT).
// Pakai:  node render.mjs            (semua klip)
//         node render.mjs 2          (klip 2 saja)
//         node render.mjs 2 --stills 5,12.5,30   (PNG cek cepat ke out/stills)
import { chromium } from "playwright-core";
import { spawn } from "node:child_process";
import http from "node:http";
import fs from "node:fs";
import path from "node:path";
import { fileURLToPath } from "node:url";

const here = path.dirname(fileURLToPath(import.meta.url));
const root = path.resolve(here, "../..");
const outDir = path.join(root, "media", "onboarding");
const FPS = 30;
const CHROME = process.env.CHROME || "C:/Program Files/Google/Chrome/Application/chrome.exe";
const NAMES = ["01-setup-proyek", "02-file-yang-dikerjakan", "03-run-test", "04-selesai-simpan"];

const args = process.argv.slice(2);
const stillsArg = args.includes("--stills") ? args[args.indexOf("--stills") + 1] : null;
const clips = args.filter(a => /^[1-4]$/.test(a)).map(Number);
const todo = clips.length ? clips : [1, 2, 3, 4];

const TYPES = { ".html": "text/html", ".js": "text/javascript", ".css": "text/css", ".json": "application/json", ".svg": "image/svg+xml" };
const server = http.createServer((req, res) => {
  const p = path.join(root, decodeURIComponent(new URL(req.url, "http://x").pathname));
  if (!p.startsWith(root) || !fs.existsSync(p) || fs.statSync(p).isDirectory()) { res.writeHead(404); return res.end(); }
  res.writeHead(200, { "Content-Type": TYPES[path.extname(p)] || "application/octet-stream" });
  fs.createReadStream(p).pipe(res);
});
await new Promise(r => server.listen(0, "127.0.0.1", r));
const base = `http://127.0.0.1:${server.address().port}`;

const run = (cmd, a, input) => new Promise((res, rej) => {
  const p = spawn(cmd, a, { stdio: [input ? "pipe" : "ignore", "ignore", "pipe"] });
  let err = ""; p.stderr.on("data", d => { err += d; });
  p.on("close", c => c === 0 ? res() : rej(new Error(`${cmd} gagal (${c}):\n${err.slice(-2000)}`)));
  if (input) input(p.stdin);
});
const ts = s => { const ms = Math.round(s * 1000); const h = Math.floor(ms / 3600000), m = Math.floor(ms / 60000) % 60, sec = Math.floor(ms / 1000) % 60; return `${String(h).padStart(2, "0")}:${String(m).padStart(2, "0")}:${String(sec).padStart(2, "0")}.${String(ms % 1000).padStart(3, "0")}`; };

const browser = await chromium.launch({ executablePath: CHROME, args: ["--force-device-scale-factor=1", "--hide-scrollbars"] });
const page = await browser.newPage({ viewport: { width: 1920, height: 1080 }, deviceScaleFactor: 1 });
page.on("pageerror", e => console.error("pageerror:", e.message));
fs.mkdirSync(outDir, { recursive: true });

for (const n of todo) {
  await page.goto(`${base}/tools/video/stage.html?clip=${n}`);
  const { total, caps } = await page.evaluate(() => window.prepare());
  const name = NAMES[n - 1];

  if (stillsArg) {
    const dir = path.join(here, "out", "stills"); fs.mkdirSync(dir, { recursive: true });
    for (const t of stillsArg.split(",").map(Number)) {
      await page.evaluate(t => window.renderAt(t), t);
      await page.screenshot({ path: path.join(dir, `c${n}-${t}.png`) });
    }
    console.log(`klip ${n}: ${total.toFixed(1)} s, stills → ${dir}`);
    continue;
  }

  const frames = Math.ceil(total * FPS);
  const mp4 = path.join(outDir, `${name}.mp4`);
  const t0 = Date.now();
  await run("ffmpeg", ["-y", "-loglevel", "error", "-f", "image2pipe", "-framerate", String(FPS), "-c:v", "mjpeg", "-i", "-",
    "-c:v", "libx264", "-preset", "slow", "-crf", "24", "-tune", "animation", "-pix_fmt", "yuv420p", "-movflags", "+faststart", mp4], async stdin => {
    for (let f = 0; f < frames; f++) {
      await page.evaluate(t => window.renderAt(t), f / FPS);
      const buf = await page.screenshot({ type: "jpeg", quality: 92 });
      if (!stdin.write(buf)) await new Promise(r => stdin.once("drain", r));
      if (f % 300 === 0) console.log(`klip ${n}: frame ${f}/${frames}`);
    }
    stdin.end();
  });
  await run("ffmpeg", ["-y", "-loglevel", "error", "-i", mp4, "-c:v", "libvpx-vp9", "-crf", "40", "-b:v", "0", "-row-mt", "1", "-deadline", "good", "-cpu-used", "2", path.join(outDir, `${name}.webm`)]);
  await run("ffmpeg", ["-y", "-loglevel", "error", "-ss", "2", "-i", mp4, "-frames:v", "1", "-vf", "scale=1280:-2", "-c:v", "libwebp", "-quality", "78", path.join(outDir, `${name}.webp`)]);
  fs.writeFileSync(path.join(outDir, `${name}.vtt`), "WEBVTT\n\n" + caps.map((c, i) => `${i + 1}\n${ts(c.start)} --> ${ts(c.end)}\n${c.text}\n`).join("\n"));
  const kb = f => Math.round(fs.statSync(path.join(outDir, f)).size / 1024);
  console.log(`klip ${n} selesai dalam ${Math.round((Date.now() - t0) / 1000)} s: mp4 ${kb(name + ".mp4")} KB, webm ${kb(name + ".webm")} KB`);
}

await browser.close();
server.close();
