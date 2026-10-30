/* Mesin render deterministik: renderAt(t) menggambar frame pada detik t. Dipanggil berurutan oleh render.mjs. */
"use strict";
const $ = s => document.querySelector(s);
const params = new URLSearchParams(location.search);
const CLIP = CLIPS[(+params.get("clip") || 1) - 1];
const board = $("#board");

let acc = 0;
for (const s of CLIP.scenes) { s.t0 = acc; acc += s.dur; }
const TOTAL = acc;

const CAPS = [];
for (const s of CLIP.scenes) (s.caps || []).forEach(([u, text], i, a) =>
  CAPS.push({ start: s.t0 + u, end: s.t0 + (a[i + 1] ? a[i + 1][0] : s.dur), text }));

const esc = v => String(v).replace(/[&<>"]/g, c => ({ "&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;" }[c]));
const rich = v => esc(v).replace(/`([^`]+)`/g, "<code>$1</code>").replace(/\*\*([^*]+)\*\*/g, "<b>$1</b>");
const plain = v => v.replace(/`([^`]+)`/g, "$1").replace(/\*\*([^*]+)\*\*/g, "$1");
const clamp = (v, a = 0, b = 1) => Math.max(a, Math.min(b, v));
const ease = x => x < .5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2;

// ── target → rect di koordinat stage (elemen yang sudah hilang memakai posisi terakhirnya) ──
const lastRect = {};
function rectOf(target) {
  if (!target) return null;
  if (typeof target === "object") return { left: target.x, top: target.y, width: target.w || 0, height: target.h || 0 };
  let r = null;
  if (target[0] === "@") {
    const el = board.contentDocument && board.contentDocument.querySelector(target.slice(1));
    if (el) { const f = board.getBoundingClientRect(), e = el.getBoundingClientRect(); r = { left: f.left + e.left, top: f.top + e.top, width: e.width, height: e.height }; }
  } else {
    const el = document.querySelector(target);
    if (el) { const e = el.getBoundingClientRect(); r = { left: e.left, top: e.top, width: e.width, height: e.height }; }
  }
  if (r && r.width) lastRect[target] = r;
  return r && r.width ? r : lastRect[target] || null;
}
const center = r => r && { x: r.left + Math.min(r.width / 2, 60), y: r.top + r.height / 2 };

// ── syntax highlight Java sederhana ──
function java(line) {
  const out = []; let m;
  const re = /(\/\/.*$|\/\*\*?.*?(\*\/|$)|^\s*\*.*$)|("(?:[^"\\]|\\.)*")|(@\w+)|\b(package|import|public|private|final|class|static|return|throw|new|void|int|String|true|false|null)\b|\b([A-Z]\w*)\b/g;
  let i = 0;
  while ((m = re.exec(line))) {
    out.push(esc(line.slice(i, m.index)));
    const cls = m[1] ? "c" : m[3] ? "s" : m[4] ? "a" : m[5] ? "k" : "n";
    out.push(`<span class="${cls}">${esc(m[0])}</span>`);
    i = m.index + m[0].length;
  }
  out.push(esc(line.slice(i)));
  return out.join("");
}

// ── scene builders ──
const win = (title, body, cls = "") => `<div class="win ${cls}"><div class="win-bar"><div class="dots"><i></i><i></i><i></i></div><span>${esc(title)}</span></div><div class="win-body">${body}</div></div>`;

const B = {
  title: {
    enter: s => `<div class="title" id="tt"><div class="big">${esc(s.big)}</div><h1>${esc(s.title)}</h1>${s.sub ? `<p>${esc(s.sub)}</p>` : ""}<div class="rule" id="ttRule"></div></div>`,
    update(s, u) {
      const a = clamp(u / .45) * clamp((s.dur - u) / .35);
      $("#tt").style.opacity = a;
      $("#tt").style.transform = `translateY(${(1 - ease(clamp(u / .6))) * 30}px)`;
      $("#ttRule").style.width = `${ease(clamp((u - .2) / 1.1)) * 1100}px`;
    }
  },
  term: {
    enter: s => win(s.title || "Windows PowerShell", `<div class="term" id="term"></div>`),
    update(s, u) {
      const html = [];
      s.lines.forEach((ln, i) => {
        if (u < ln.u) return;
        if (ln.clear) { html.length = 0; return; }
        let h;
        if (ln.cmd !== undefined) {
          const n = Math.floor((u - ln.u) * (ln.cps || 24));
          const typing = n < ln.cmd.length;
          h = `<span class="ps">${esc(ln.ps || s.ps || "PS C:\\kode>")} </span><span class="cmd">${esc(ln.cmd.slice(0, n))}</span>${typing ? '<span class="caret"></span>' : ""}`;
        } else h = `<span class="${ln.c || ""}">${esc(ln.out)}</span>`;
        for (const [u0, u1, idx, sub] of s.marks || [])
          if (idx === i && u >= u0 && u < u1) h = h.replace(esc(sub), `<mark>${esc(sub)}</mark>`);
        html.push(h);
      });
      // prompt kosong saat terminal menunggu perintah berikutnya
      const last = s.lines.filter(l => !l.clear && u >= l.u).pop();
      const typing = last && last.cmd !== undefined && Math.floor((u - last.u) * (last.cps || 24)) < last.cmd.length;
      const busy = last && last.cmd !== undefined && !typing && s.lines.some(l => l.out !== undefined && l.u > last.u && l.u - last.u < 4 && l.u > u);
      if (!typing && !busy) {
        const nxt = s.lines.find(l => l.cmd !== undefined && l.u > u);
        const prev = s.lines.filter(l => l.cmd !== undefined && l.u <= u).pop();
        const ps = (nxt || prev || {}).ps || s.ps || "PS C:\\kode>";
        html.push(`<span class="ps">${esc(ps)} </span>${Math.floor(u * 2) % 2 ? "" : '<span class="caret"></span>'}`);
      }
      $("#term").innerHTML = html.map(h => `<div>${h}</div>`).join("");
    }
  },
  ide: {
    enter: s => win(s.title || "IntelliJ IDEA · alterego-springboot-kit", `<div class="ide"><div class="tree" id="tree"></div><div class="ed"><div class="tabs" id="tabs"></div><div class="code" id="code"></div></div></div>`),
    update(s, u) {
      const at = list => (list || []).filter(x => u >= x[0]).pop();
      const on = new Set((s.treeOn || []).filter(([a, b]) => u >= a && u < b).map(x => x[2]));
      const tags = {}; for (const [a, i, txt, gray] of s.tags || []) if (u >= a) tags[i] = [txt, gray];
      $("#tree").innerHTML = s.tree.map((it, i) => `<div class="it ${it.dir ? "dir" : ""} ${on.has(i) ? "on" : ""}" id="tr${i}" style="padding-left:${24 + it.d * 26}px">${it.dir ? "▾ " : "  "}${esc(it.n)}${tags[i] ? `<span class="tag ${tags[i][1] ? "gray" : ""}">${esc(tags[i][0])}</span>` : ""}</div>`).join("");
      const tab = at(s.tab) || [0, "", false];
      $("#tabs").innerHTML = tab[1] ? `<div class="t on">${esc(tab[1])}${tab[2] ? '<span class="ro">test · jangan diubah</span>' : ""}</div>` : "";
      const code = (at(s.code) || [0, ""])[1];
      const lit = new Set((s.lit || []).filter(([a, b]) => u >= a && u < b).flatMap(x => x[2]));
      $("#code").innerHTML = code ? code.split("\n").map((l, i) => `<div class="ln ${lit.has(i + 1) ? "on" : ""}" id="ln${i + 1}"><span class="no">${i + 1}</span><span>${java(l) || " "}</span></div>`).join("") : "";
    }
  },
  gh: {
    enter: s => win("github.com", `<div class="gh" id="gh"></div>`, "gh-win"),
    update(s, u) {
      const g = $("#gh"), k = s.k;
      if (u < k.create) {
        g.innerHTML = `<h2><span class="own">Llorente14</span> / <b>alterego-springboot-kit</b><span class="pill">Public template</span></h2>
          <div class="row"><span class="btn green" id="ghUse">Use this template ▾</span><span class="btn">Code ▾</span></div>
          <div class="files"><div>Llorente14 · kit Spring Boot: stub kosong + test</div><div>docs/</div><div>quest-board/</div><div>src/</div><div>pom.xml</div><div>README.md</div></div>
          ${u >= k.use ? `<div class="menu" id="ghMenu" style="left:60px;top:190px"><div class="${u >= k.create - .5 ? "hot" : ""}" id="ghNew">Create a new repository</div><div>Open in a codespace</div></div>` : ""}`;
      } else {
        const name = "alterego-springboot-kit".slice(0, Math.floor(clamp((u - k.type) / k.typeDur) * 23));
        const done = u >= k.submit + .4;
        g.innerHTML = `<h2>Create a new repository</h2>
          <p class="note">Template: Llorente14/alterego-springboot-kit</p>
          <label>Owner / Repository name</label>
          <div class="row" style="margin:0"><span class="inp" style="min-width:220px">kamu</span><span style="font-size:30px;color:#9198a1">/</span><span class="inp ${u >= k.type - .3 && !done ? "focus" : ""}" id="ghName">${esc(name)}${u >= k.type - .3 && u < k.type + k.typeDur + .2 ? '<span class="caret" style="height:24px;width:11px"></span>' : ""}</span></div>
          <label>Visibility</label>
          <div class="radio"><span class="on">Public</span><span>Private</span></div>
          <p class="note">Repo ini milikmu sendiri. Semua solusi dan commit-mu tersimpan di sini.</p>
          <div class="row"><span class="btn green" id="ghSubmit">${done ? "Generating repository…" : "Create repository"}</span></div>`;
      }
    }
  },
  board: {
    enter() { return ""; },
    update() {}
  }
};

// ── state per scene ──
let cur = null, opIdx = 0;
function enter(s) {
  cur = s; opIdx = 0;
  $("#scene").innerHTML = B[s.type].enter(s);
  $("#browser").hidden = s.type !== "board";
  if (s.type === "board") $("#addr").textContent = s.addr || "file:///C:/kode/alterego-springboot-kit/quest-board/index.html";
}

function runOps(s, u) {
  const ops = s.ops || [];
  while (opIdx < ops.length && u >= ops[opIdx][0]) {
    const [, kind, arg] = ops[opIdx++];
    const d = board.contentDocument;
    if (kind === "click") { const el = d.querySelector(arg); if (el) el.click(); }
    else if (kind === "js") arg(d, board.contentWindow);
  }
  for (const [u0, u1, sel, text] of s.type_ || []) {
    if (u < u0) continue;
    const el = board.contentDocument.querySelector(sel); if (!el) continue;
    const v = text.slice(0, Math.floor(clamp((u - u0) / (u1 - u0)) * text.length));
    if (el.value !== v) { el.value = v; el.dispatchEvent(new Event("input", { bubbles: true })); }
  }
  const sc = s.scroll;
  if (sc && sc.length) {
    const val = tg => {
      if (typeof tg === "number") return tg;
      const el = board.contentDocument.querySelector(tg[0]);
      return el ? el.getBoundingClientRect().top + board.contentWindow.scrollY - (tg[1] || 90) : 0;
    };
    let y;
    if (u <= sc[0][0]) y = val(sc[0][1]);
    else if (u >= sc[sc.length - 1][0]) y = val(sc[sc.length - 1][1]);
    else { const i = sc.findIndex(k => k[0] > u); const a = sc[i - 1], b = sc[i]; y = val(a[1]) + (val(b[1]) - val(a[1])) * ease((u - a[0]) / (b[0] - a[0])); }
    board.contentWindow.scrollTo(0, Math.max(0, y));
  }
}

function drawCursor(s, u) {
  const ks = s.cursor || [], c = $("#cursor"), ring = $("#ring");
  ring.style.opacity = 0;
  if (!ks.length) { c.hidden = true; return; }
  c.hidden = false;
  const pts = ks.map(k => center(rectOf(k[1])) || { x: 960, y: 540 });
  let p;
  const first = { x: pts[0].x + 160, y: pts[0].y + 120 };
  const move = (from, to, a, b) => { const m = Math.min(.8, b - a); const x = ease(clamp((u - (b - m)) / m)); return { x: from.x + (to.x - from.x) * x, y: from.y + (to.y - from.y) * x }; };
  if (u < ks[0][0]) p = move(first, pts[0], 0, ks[0][0]);
  else {
    const i = ks.findIndex(k => k[0] > u);
    if (i === -1) p = pts[pts.length - 1];
    else p = move(pts[i - 1], pts[i], ks[i - 1][0], ks[i][0]);
  }
  c.style.transform = `translate(${p.x - 6}px, ${p.y - 3}px)`;
  for (let i = 0; i < ks.length; i++) {
    const d = u - ks[i][0];
    if (ks[i][2] === "click" && d >= 0 && d < .45) {
      ring.style.left = pts[i].x + "px"; ring.style.top = pts[i].y + "px";
      ring.style.opacity = 1 - d / .45; ring.style.transform = `scale(${.4 + d * 1.8})`;
    }
  }
}

function drawHl(s, u) {
  const out = [];
  for (const [u0, u1, tg, label] of s.hl || []) {
    if (u < u0 || u >= u1) continue;
    const r = rectOf(tg); if (!r) continue;
    const a = clamp((u - u0) / .25) * clamp((u1 - u) / .2);
    out.push(`<div class="hl" style="opacity:${a};left:${r.left - 130}px;top:${r.top - 126}px;width:${r.width + 20}px;height:${r.height + 20}px">${label ? `<span class="lbl">${esc(label)}</span>` : ""}</div>`);
  }
  $("#hls").innerHTML = out.join("");
}

function renderAt(t) {
  t = clamp(t, 0, TOTAL - 1e-6);
  const s = CLIP.scenes.find(x => t >= x.t0 && t < x.t0 + x.dur);
  if (s !== cur) enter(s);
  const u = t - s.t0;
  if (s.type === "board") runOps(s, u);
  B[s.type].update(s, u);
  $("#progFill").style.width = `${(t / TOTAL) * 100}%`;
  const cap = CAPS.find(c => t >= c.start && t < c.end);
  $("#cap").style.opacity = cap ? clamp((t - cap.start) / .25) : 0;
  if (cap && $("#capText").dataset.k !== cap.text) { $("#capText").innerHTML = rich(cap.text); $("#capText").dataset.k = cap.text; }
  drawHl(s, u);
  drawCursor(s, u);
}

async function prepare() {
  $("#clipNo").textContent = `${String(CLIP.n).padStart(2, "0")} / 04`;
  $("#clipName").textContent = CLIP.name;
  await document.fonts.ready;
  if (CLIP.boardState !== undefined) {
    if (CLIP.boardState) localStorage.setItem("aeqb.v1", JSON.stringify(CLIP.boardState));
    else localStorage.removeItem("aeqb.v1");
    await new Promise(res => { board.onload = res; board.src = "../../quest-board/index.html"; });
    const st = board.contentDocument.createElement("style");
    st.textContent = "html{scrollbar-width:none}::-webkit-scrollbar{display:none}*{transition-duration:0s!important;scroll-behavior:auto!important}";
    board.contentDocument.head.appendChild(st);
    await board.contentDocument.fonts.ready;
  }
  renderAt(0);
  return { total: TOTAL, caps: CAPS.map(c => ({ ...c, text: plain(c.text) })) };
}
window.prepare = prepare;
window.renderAt = renderAt;
