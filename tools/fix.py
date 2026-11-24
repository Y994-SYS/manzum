import json, sys, difflib

MAP = {
    "Ahmet Paşa": "Ahmed Paşa",
    "Muhibbi (Kanuni Sultan Süleyman)": "Kanuni Sultan Süleyman (Muhibbi)",
    "Taşlıcalı Yahya Bey": "Taşlıcalı Yahya",
}

for path in sys.argv[1:]:
    data = json.load(open(path, encoding="utf-8-sig"))
    for d in data:
        d["sair"] = MAP.get(d["sair"], d["sair"])
    with open(path, "w", encoding="utf-8") as f:
        json.dump(data, f, ensure_ascii=False, indent=2)

    names = sorted({d["sair"] for d in data})
    print(path, "->", len(names), "sair")
    for i, a in enumerate(names):
        for b in names[i+1:]:
            if difflib.SequenceMatcher(None, a.lower(), b.lower()).ratio() > 0.8:
                print("  Benzer, kontrol et:", a, "|", b)