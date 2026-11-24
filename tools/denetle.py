import json, sys, csv, collections

data = json.load(open(sys.argv[1], encoding="utf-8-sig"))
idler = collections.Counter(d.get("id") for d in data)
metinler = collections.Counter("".join(c for c in d["orijinalMetin"].lower() if c.isalnum()) for d in data)

with open("tools/denetim.csv", "w", encoding="utf-8-sig", newline="") as f:
    w = csv.writer(f, delimiter=";")
    w.writerow(["id", "sair", "ilk_satir", "durum"])
    for d in data:
        k = d.get("kaynak", "").lower()
        durum = []
        if "doğrulama" not in k and "kaynak:" not in k:
            durum.append("KAYNAK-ZAYIF")
        if idler[d.get("id")] > 1 or d.get("id") is None:
            durum.append("ID-SORUNU")
        if metinler["".join(c for c in d["orijinalMetin"].lower() if c.isalnum())] > 1:
            durum.append("TEKRAR")
        w.writerow([d.get("id"), d["sair"], d["orijinalMetin"].split("\n")[0], ",".join(durum) or "tamam"])
print("tools/denetim.csv yazildi")