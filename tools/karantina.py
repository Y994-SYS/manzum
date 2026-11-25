import json, sys, os, shutil

KAR = os.path.join("tools", "karantina.json")

def oku(p):
    if os.path.exists(p):
        return json.load(open(p, encoding="utf-8-sig"))
    return []

def yaz(p, d):
    with open(p, "w", encoding="utf-8") as f:
        json.dump(d, f, ensure_ascii=False, indent=2)

hedef, mod = sys.argv[1], sys.argv[2]
idler = {int(x) for x in sys.argv[3:]}

ana, kar = oku(hedef), oku(KAR)
shutil.copy(hedef, os.path.join("tools", "yedek_" + os.path.basename(hedef)))

if mod == "al":
    giden = [d for d in ana if d["id"] in idler]
    ana = [d for d in ana if d["id"] not in idler]
    kar += giden
elif mod == "geri":
    giden = [d for d in kar if d["id"] in idler]
    kar = [d for d in kar if d["id"] not in idler]
    ana += giden
    ana.sort(key=lambda d: d["id"])
else:
    sys.exit("mod: al veya geri")

bulunamayan = idler - {d["id"] for d in giden}
if bulunamayan:
    print("Bulunamadi:", sorted(bulunamayan))

yaz(hedef, ana)
yaz(KAR, kar)
print(len(giden), "kayit tasindi. Ana:", len(ana), "| Karantina:", len(kar))