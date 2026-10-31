
export default async function handler(req, res) {
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET,POST,DELETE,OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');
  if (req.method === 'OPTIONS') return res.status(200).end();
  if (!globalThis._chat) globalThis._chat = [];
  let store = globalThis._chat;
  const now = Date.now();
  store = store.filter(m => now - m.ts < 86400000);
  globalThis._chat = store;
  if (req.method === 'POST') {
    let body = req.body;
    if (!body || typeof body === 'string') { try { body = JSON.parse(body || '{}'); } catch(e){ body = {}; } }
    const {from,to,text,image,time,type} = body;
    if (!to) return res.status(400).json({error:'missing'});
    const msg = {id: Date.now().toString()+Math.random(), from, to, text, image: image||'', time: time||new Date().toLocaleTimeString(), ts: now, type: type||'msg'};
    store.push(msg); globalThis._chat = store;
    return res.status(200).json({success:true});
  }
  if (req.method === 'GET') {
    const forId = req.query.for;
    if (!forId) return res.json([]);
    return res.status(200).json(store.filter(m => String(m.to) === String(forId)));
  }
  if (req.method === 'DELETE') {
    const forId = req.query.for;
    if (forId) globalThis._chat = store.filter(m => String(m.to) !== String(forId));
    return res.json({success:true});
  }
  return res.json([]);
}
