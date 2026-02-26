/*
 * ITsquad Sync – APK Download Server
 * ------------------------------------
 * Express server that:
 *   1. Serves a branded download page at /
 *   2. Serves the APK from /public/*.apk
 *   3. Logs every request to logs/access.log  (append)
 *   4. Logs errors   to logs/error.log   (append)
 *   5. Listens on HTTP (port 3000 by default, configurable via PORT env var)
 *
 * For production: put this behind nginx/caddy with a real TLS cert.
 */

const express = require('express');
const morgan  = require('morgan');
const path    = require('path');
const fs      = require('fs');

const app  = express();
const PORT = process.env.PORT || 8080;

// ── Ensure logs dir exists ──────────────────────────────────────────────
const logsDir = path.join(__dirname, 'logs');
if (!fs.existsSync(logsDir)) fs.mkdirSync(logsDir, { recursive: true });

// ── Access log (Apache-combined format, appended) ───────────────────────
const accessLogStream = fs.createWriteStream(
  path.join(logsDir, 'access.log'),
  { flags: 'a' }
);
app.use(morgan('combined', { stream: accessLogStream }));
// Also log to console for easy debugging
app.use(morgan('dev'));

// ── Static files (HTML, CSS, APK) ──────────────────────────────────────
app.use(express.static(path.join(__dirname, 'public')));

// ── Health check endpoint (useful for testing) ─────────────────────────
app.get('/health', (_req, res) => {
  res.json({ status: 'ok', uptime: process.uptime() });
});

// ── APK download counter endpoint ──────────────────────────────────────
let downloadCount = 0;
app.get('/api/download-count', (_req, res) => {
  res.json({ downloads: downloadCount });
});

// ── Track APK downloads ────────────────────────────────────────────────
app.get('/download', (req, res) => {
  const apkDir = path.join(__dirname, 'public');
  // Find first .apk in public/
  const apks = fs.readdirSync(apkDir).filter(f => f.endsWith('.apk'));
  if (apks.length === 0) {
    return res.status(404).send('No APK file found. Place your .apk in the public/ folder.');
  }
  downloadCount++;

  const apkPath = path.join(apkDir, apks[0]);
  const logLine = `[${new Date().toISOString()}] DOWNLOAD #${downloadCount} – ${apks[0]} from ${req.ip}\n`;
  fs.appendFileSync(path.join(logsDir, 'access.log'), logLine);

  res.download(apkPath, apks[0]);
});

// ── Error log ──────────────────────────────────────────────────────────
const errorLogStream = fs.createWriteStream(
  path.join(logsDir, 'error.log'),
  { flags: 'a' }
);
app.use((err, _req, res, _next) => {
  const msg = `[${new Date().toISOString()}] ${err.stack || err}\n`;
  errorLogStream.write(msg);
  console.error(msg);
  res.status(500).send('Internal server error');
});

// ── Start ──────────────────────────────────────────────────────────────
const server = app.listen(PORT, '0.0.0.0', () => {
  console.log(`\n🟢  ITsquad Download Server running at  http://localhost:${PORT}\n`);
  console.log(`   Static files : ${path.join(__dirname, 'public')}`);
  console.log(`   Logs         : ${logsDir}`);
  console.log(`   Place your .apk in public/ and visit /download\n`);
});
server.on('error', (err) => {
  console.error(`❌  Server failed to start: ${err.message}`);
  process.exit(1);
});
