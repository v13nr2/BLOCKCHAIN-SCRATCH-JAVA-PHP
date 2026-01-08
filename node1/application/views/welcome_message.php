<!doctype html>
<html lang="id">
<head>
  <meta charset="utf-8" />
  <meta name="viewport" content="width=device-width,initial-scale=1" />
  <title>Borneolink — Blockchain Explorer</title>
  <!-- Tailwind Play CDN (for quick prototype) -->
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    /* Palm-oil harvest theme colors */
    :root{
      --palm-green:#2f6f3b;
      --palm-gold:#d4a017;
      --palm-brown:#6b3f1a;
      --card-bg:linear-gradient(135deg, rgba(244,208,63,0.06), rgba(47,111,59,0.03));
    }
    .theme-accent{ background: linear-gradient(90deg,var(--palm-gold),var(--palm-green)); }
  </style>
</head>
<body class="bg-gradient-to-b from-green-50 to-yellow-50 min-h-screen font-sans text-slate-800">
  <div class="max-w-7xl mx-auto p-6">
    <header class="flex items-center justify-between mb-6">
      <div class="flex items-center gap-4">
        <div class="w-12 h-12 rounded-xl theme-accent flex items-center justify-center text-white text-lg font-bold shadow">BL</div>
        <div>
          <h1 class="text-2xl font-semibold">Borneolink Explorer</h1>
          <p class="text-sm text-slate-600">Dashboard eksplorasi blockchain — corak panen kelapa sawit</p>
        </div>
      </div>
      <div class="flex items-center gap-3">
        <div class="text-right">
          <div class="text-xs text-slate-500">Node Status</div>
          <div class="text-sm font-medium text-green-700">Online</div>
        </div>
        <button id="refreshBtn" class="px-4 py-2 rounded-md bg-white border shadow-sm hover:shadow-md">Refresh</button>
      </div>
    </header>

    <!-- Summary cards -->
    <section class="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
      <div class="p-4 rounded-2xl shadow-sm" style="background:var(--card-bg)">
        <div class="text-xs text-slate-500">Total Blocks</div>
        <div id="totalBlocks" class="text-2xl font-bold">—</div>
        <div class="text-sm text-slate-600 mt-1">Termasuk genesis.</div>
      </div>

      <div class="p-4 rounded-2xl shadow-sm" style="background:var(--card-bg)">
        <div class="text-xs text-slate-500">Latest Block Hash</div>
        <div id="latestHash" class="text-sm font-mono truncate">—</div>
        <div class="text-sm text-slate-600 mt-1">Hash paling akhir.</div>
      </div>

      <div class="p-4 rounded-2xl shadow-sm" style="background:var(--card-bg)">
        <div class="text-xs text-slate-500">Total Transactions (est)</div>
        <div id="totalTx" class="text-2xl font-bold">—</div>
        <div class="text-sm text-slate-600 mt-1">Estimasi jumlah transaksi dari semua blok.</div>
      </div>
    </section>

    <!-- Controls -->
    <section class="flex items-center gap-3 mb-4">
      <input id="search" type="text" placeholder="Cari hash / index / miner..." class="flex-1 p-2 rounded-md border" />
      <select id="perPage" class="p-2 rounded-md border">
        <option>10</option>
        <option selected>25</option>
        <option>50</option>
      </select>
      <button id="exportBtn" class="px-4 py-2 rounded-md bg-yellow-500 text-white font-medium">Export CSV</button>
    </section>

    <!-- Table -->
    <section class="bg-white rounded-2xl shadow overflow-hidden">
      <div class="p-4 border-b flex items-center justify-between">
        <div class="font-semibold">Block List</div>
        <div class="text-sm text-slate-500">Menampilkan blok contoh (genesis -> latest)</div>
      </div>
      <div class="overflow-auto">
        <table class="min-w-full text-sm">
          <thead class="bg-slate-50">
            <tr>
              <th class="p-3 text-left">#</th>
              <th class="p-3 text-left">Hash</th>
              <th class="p-3 text-left">Prev Hash</th>
              <th class="p-3 text-left">Timestamp</th>
              <th class="p-3 text-left">Tx Count</th>
              <th class="p-3 text-left">Miner</th>
              <th class="p-3 text-left">Action</th>
            </tr>
          </thead>
          <tbody id="blocksTable" class="divide-y"></tbody>
        </table>
      </div>
      <div class="p-4 flex items-center justify-between">
        <div class="text-sm text-slate-600">Showing <span id="showingRange">—</span></div>
        <div class="flex items-center gap-2">
          <button id="prevBtn" class="px-3 py-1 rounded-md border">Prev</button>
          <button id="nextBtn" class="px-3 py-1 rounded-md border">Next</button>
        </div>
      </div>
    </section>

    <!-- Block detail modal -->
    <div id="modal" class="fixed inset-0 bg-black/40 hidden items-center justify-center p-4">
      <div class="bg-white rounded-xl max-w-3xl w-full p-6 shadow-lg">
        <div class="flex items-start justify-between mb-4">
          <h3 id="modalTitle" class="text-lg font-semibold">Block #</h3>
          <button id="closeModal" class="text-slate-500">Close</button>
        </div>
        <pre id="modalContent" class="font-mono text-sm bg-slate-50 p-4 rounded">—</pre>
      </div>
    </div>

    <footer class="mt-6 text-sm text-slate-500">© Borneolink Explorer — corak panen kelapa sawit</footer>
  </div>

<script>
// Utility: convert ArrayBuffer to hex
function bufToHex(buffer){
  const bytes = new Uint8Array(buffer);
  return Array.from(bytes).map(b=>b.toString(16).padStart(2,'0')).join('');
}

// Deterministic genesis
const GENESIS_HASH = '0000000000000000000000000000000000000000000000000000000000000000';

// Generate 100 blocks (genesis + 99)
async function generateBlocks(count=100){
  const blocks = [];
  let prev = GENESIS_HASH;
  const baseTime = Date.now() - (count * 60*60*1000); // some hours ago
  for(let i=0;i<count;i++){
    const index = i; // genesis = 0
    const timestamp = new Date(baseTime + i*60000).toISOString();
    // create a payload to hash deterministically
    const payload = `${prev}|${index}|${timestamp}|borneolink:${i}`;
    const data = new TextEncoder().encode(payload);
    const digest = await crypto.subtle.digest('SHA-256', data);
    const hash = bufToHex(digest);
    const txCount = Math.floor(Math.abs((hash.charCodeAt(0) + i) % 12));
    const miner = i===0 ? 'genesis' : `miner-${(i%7)+1}`;
    const block = {
      index,
      hash,
      prevHash: prev,
      timestamp,
      txCount,
      miner
    };
    blocks.push(block);
    prev = hash;
  }
  // ensure first block is genesis with predefined hash
  blocks[0].hash = GENESIS_HASH;
  blocks[0].prevHash = '—';
  blocks[0].miner = 'genesis';
  return blocks;
}

let ALL_BLOCKS = [];
let currentPage = 1;
let perPage = 25;
let filtered = [];

function renderSummary(blocks){
  document.getElementById('totalBlocks').textContent = blocks.length;
  document.getElementById('latestHash').textContent = blocks[blocks.length-1].hash;
  const totalTx = blocks.reduce((s,b)=>s+b.txCount,0);
  document.getElementById('totalTx').textContent = totalTx;
}

function renderTable(page=1){
  const start = (page-1)*perPage;
  const end = start + perPage;
  const pageItems = filtered.slice(start,end);
  const tbody = document.getElementById('blocksTable');
  tbody.innerHTML = '';
  for(const b of pageItems){
    const tr = document.createElement('tr');
    tr.innerHTML = `
      <td class="p-3">${b.index}</td>
      <td class="p-3"><div class="font-mono truncate max-w-[260px]">${b.hash}</div></td>
      <td class="p-3"><div class="font-mono truncate max-w-[260px]">${b.prevHash}</div></td>
      <td class="p-3">${new Date(b.timestamp).toLocaleString()}</td>
      <td class="p-3">${b.txCount}</td>
      <td class="p-3">${b.miner}</td>
      <td class="p-3"><button data-index="${b.index}" class="viewBtn px-3 py-1 rounded bg-green-600 text-white text-xs">View</button></td>
    `;
    tbody.appendChild(tr);
  }
  const showingStart = filtered.length ? start+1 : 0;
  const showingEnd = Math.min(end, filtered.length);
  document.getElementById('showingRange').textContent = `${showingStart} - ${showingEnd} of ${filtered.length}`;
  // attach view handlers
  document.querySelectorAll('.viewBtn').forEach(btn=>{
    btn.addEventListener('click', (e)=>{
      const idx = Number(e.currentTarget.dataset.index);
      openModal(ALL_BLOCKS.find(x=>x.index===idx));
    });
  });
}

function openModal(block){
  document.getElementById('modalTitle').textContent = `Block #${block.index}`;
  document.getElementById('modalContent').textContent = JSON.stringify(block, null, 2);
  document.getElementById('modal').classList.remove('hidden');
}

function closeModal(){
  document.getElementById('modal').classList.add('hidden');
}

function applySearch(q){
  if(!q) filtered = ALL_BLOCKS.slice();
  else{
    const s = q.toLowerCase();
    filtered = ALL_BLOCKS.filter(b=>
      String(b.index).includes(s) || b.hash.includes(s) || (b.miner||'').toLowerCase().includes(s)
    );
  }
  currentPage = 1;
  renderTable(currentPage);
}

function exportCSV(){
  const rows = ['index,hash,prevHash,timestamp,txCount,miner'];
  for(const b of filtered){
    rows.push(`${b.index},${b.hash},${b.prevHash},${b.timestamp},${b.txCount},${b.miner}`);
  }
  const blob = new Blob([rows.join('\n')], {type:'text/csv'});
  const url = URL.createObjectURL(blob);
  const a = document.createElement('a');
  a.href = url; a.download = 'borneolink_blocks.csv';
  a.click();
  URL.revokeObjectURL(url);
}

// Init
(async function(){
  ALL_BLOCKS = await generateBlocks(100);
  filtered = ALL_BLOCKS.slice();
  renderSummary(ALL_BLOCKS);
  renderTable(1);
})();

// UI bindings
document.getElementById('perPage').addEventListener('change', (e)=>{
  perPage = Number(e.target.value);
  currentPage = 1;
  renderTable(currentPage);
});

document.getElementById('search').addEventListener('input', (e)=>{
  applySearch(e.target.value.trim());
});

document.getElementById('prevBtn').addEventListener('click', ()=>{
  if(currentPage>1){ currentPage--; renderTable(currentPage); }
});

document.getElementById('nextBtn').addEventListener('click', ()=>{
  const maxPage = Math.ceil(filtered.length / perPage);
  if(currentPage < maxPage){ currentPage++; renderTable(currentPage); }
});

document.getElementById('closeModal').addEventListener('click', closeModal);

document.getElementById('modal').addEventListener('click', (e)=>{ if(e.target.id==='modal') closeModal(); });

document.getElementById('exportBtn').addEventListener('click', exportCSV);

document.getElementById('refreshBtn').addEventListener('click', async ()=>{
  // regenerate (simulate refresh)
  ALL_BLOCKS = await generateBlocks(100);
  filtered = ALL_BLOCKS.slice();
  renderSummary(ALL_BLOCKS);
  renderTable(1);
});
</script>
</body>
</html>