/*
 * test.js – Automated smoke tests for the download server.
 * Run:  npm test   (or:  node test.js)
 *
 * Starts the server on a random port, runs checks, then shuts down.
 */

const http = require('http');
const path = require('path');
const fs   = require('fs');

// ── Helpers ─────────────────────────────────────────────────────────────
function get(url) {
  return new Promise((resolve, reject) => {
    http.get(url, res => {
      let body = '';
      res.on('data', c => (body += c));
      res.on('end', () => resolve({ status: res.statusCode, headers: res.headers, body }));
    }).on('error', reject);
  });
}

let passed = 0;
let failed = 0;
function assert(label, condition) {
  if (condition) {
    console.log(`  ✅  ${label}`);
    passed++;
  } else {
    console.error(`  ❌  ${label}`);
    failed++;
  }
}

// ── Main ────────────────────────────────────────────────────────────────
(async () => {
  // Pick a random port so we don't collide
  process.env.PORT = '0';

  // Create a tiny dummy APK so /download has something to serve
  const publicDir = path.join(__dirname, 'public');
  const dummyApk  = path.join(publicDir, 'test-dummy.apk');
  const dummyData = 'PK_DUMMY_APK_FOR_TESTING';
  fs.writeFileSync(dummyApk, dummyData);

  // Require server (it calls app.listen internally).
  // We need to monkey-patch express to capture the actual port.
  const express = require('express');
  const origListen = express.application.listen;

  let serverInstance;
  let actualPort;

  express.application.listen = function (...args) {
    // Replace port 0 → let OS pick, grab the result
    args[0] = 0;
    serverInstance = origListen.apply(this, args);
    actualPort = serverInstance.address().port;
    return serverInstance;
  };

  // Now load the server module (which calls listen)
  require('./server');

  // Wait a beat for the listen callback
  await new Promise(r => setTimeout(r, 500));

  if (!actualPort) {
    // Fallback: re-read from the server instance
    actualPort = serverInstance?.address()?.port;
  }

  const base = `http://localhost:${actualPort}`;
  console.log(`\n🧪  Running tests against ${base}\n`);

  // ── Test 1: homepage loads ────────────────────────────────────────────
  try {
    const res = await get(`${base}/`);
    assert('GET / returns 200', res.status === 200);
    assert('GET / contains page title', res.body.includes('ITsquad Sync'));
    assert('GET / contains download button', res.body.includes('/download'));
  } catch (e) {
    assert(`GET / reachable (${e.message})`, false);
  }

  // ── Test 2: health endpoint ──────────────────────────────────────────
  try {
    const res = await get(`${base}/health`);
    assert('GET /health returns 200', res.status === 200);
    const json = JSON.parse(res.body);
    assert('GET /health has status ok', json.status === 'ok');
    assert('GET /health has uptime', typeof json.uptime === 'number');
  } catch (e) {
    assert(`GET /health works (${e.message})`, false);
  }

  // ── Test 3: APK download ─────────────────────────────────────────────
  try {
    const res = await get(`${base}/download`);
    assert('GET /download returns 200', res.status === 200);
    assert('GET /download content-disposition is attachment', 
      (res.headers['content-disposition'] || '').includes('attachment'));
    assert('GET /download body has APK content', res.body.includes('PK_DUMMY'));
  } catch (e) {
    assert(`GET /download works (${e.message})`, false);
  }

  // ── Test 4: download counter ─────────────────────────────────────────
  try {
    const res = await get(`${base}/api/download-count`);
    assert('GET /api/download-count returns 200', res.status === 200);
    const json = JSON.parse(res.body);
    assert('Download counter incremented', json.downloads >= 1);
  } catch (e) {
    assert(`GET /api/download-count works (${e.message})`, false);
  }

  // ── Test 5: static CSS loads ─────────────────────────────────────────
  try {
    const res = await get(`${base}/style.css`);
    assert('GET /style.css returns 200', res.status === 200);
    assert('GET /style.css has CSS content', res.body.includes('--navy'));
  } catch (e) {
    assert(`GET /style.css works (${e.message})`, false);
  }

  // ── Test 6: access.log was written ───────────────────────────────────
  const logPath = path.join(__dirname, 'logs', 'access.log');
  const logExists = fs.existsSync(logPath);
  assert('logs/access.log exists', logExists);
  if (logExists) {
    const logContent = fs.readFileSync(logPath, 'utf8');
    assert('access.log has entries', logContent.length > 0);
  }

  // ── Clean up ─────────────────────────────────────────────────────────
  try { fs.unlinkSync(dummyApk); } catch (_) {}
  
  console.log(`\n📊  Results: ${passed} passed, ${failed} failed\n`);

  if (serverInstance) serverInstance.close();
  process.exit(failed > 0 ? 1 : 0);
})();
