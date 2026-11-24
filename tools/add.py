import json, sys, os, shutil, difflib

ZORUNLU = ["sair", "kategori", "orijinalMetin", "sadelestirme",
           "yorum", "sairBilgisi", "kaynak"]

def norm(s):
    return "".join(c for c in s.lower() if c.isalnum())

hedef, yeni = sys.argv[1], sys.argv[2]
yeni_sair_izni = "--yeni-sair" in sys.argv

data = json.load(open(hedef, encoding="utf-8-sig"))
eklenecek = json.load(open(yeni, encoding="utf-8-sig"))

shutil.copy(hedef, os.path.join("tools", "yedek_" + os.path.basename(hedef)))

mevcut_metinler = {norm(d["orijinalMetin"]) for d in data}
mevcut_sairler = {d["sair"] for d in data}
sonraki_id = max(d["id"] for d in data) + 1
eklenen = 0

for e in eklenecek:
    eksik = [k for k in ZORUNLU if not str(e.get(k, "")).strip()]
    if eksik:
        print("ATLANDI (eksik alan:", eksik, ")", e.get("orijinalMetin", "")[:40])
        continue
    if norm(e["orijinalMetin"]) in mevcut_metinler:
        print("ATLANDI (zaten var):", e["orijinalMetin"][:40])
        continue
    if e["sair"] not in mevcut_sairler and not yeni_sair_izni:
        yakin = difflib.get_close_matches(e["sair"], mevcut_sairler, n=3, cutoff=0.6)
        print("ATLANDI (bilinmeyen sair:", e["sair"], "| yakin:", yakin, ")")
        continue
    e["id"] = sonraki_id
    sonraki_id += 1
    data.append(e)
    mevcut_metinler.add(norm(e["orijinalMetin"]))
    eklenen += 1

with open(hedef, "w", encoding="utf-8") as f:
    json.dump(data, f, ensure_ascii=False, indent=2)
print(eklenen, "kayit eklendi. Toplam:", len(data))