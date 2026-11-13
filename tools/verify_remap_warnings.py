#!/usr/bin/env python3
"""Verify every 'Cannot remap' warned member exists in its target class
in the INTERMEDIARY namespace (what runtime actually uses).

Inputs:
  /tmp/larp-build2.log                       - fresh build warnings
  ~/.gradle/caches/fabric-loom/1.21.11/net.fabricmc.yarn.1_21_11.1.21.11+build.6-v2/mappings.tiny  (official/intermediary/named)
  ~/.gradle/caches/fabric-loom/1.21.11/intermediary-v2.tiny  (official/intermediary)

Output: per (member, target) verdicts + summary.
"""
import re, sys, collections, os

HOME = r"C:\Users\admin\.gradle\caches\fabric-loom\1.21.11"
YARN = os.path.join(HOME, "net.fabricmc.yarn.1_21_11.1.21.11+build.6-v2", "mappings.tiny")
INTER = os.path.join(HOME, "intermediary-v2.tiny")
LOG = r"C:\Users\admin\AppData\Local\Temp\larp-build2.log"

warn_re = re.compile(r"Cannot remap (\S+) because it does not exist in any of the targets \[([^\]]+)\]")

# ---- parse warnings -> unique (member, set of named targets) ----
pairs = set()
for line in open(LOG, encoding="utf-8", errors="replace"):
    m = warn_re.search(line)
    if m:
        member = m.group(1)
        targets = [t.strip() for t in m.group(2).split(",")]
        for t in targets:
            pairs.add((member, t))
print(f"distinct (member,target) pairs: {len(pairs)}")

# ---- yarn tiny: named class -> intermediary class; named member -> intermediary member ----
# yarn build.6 mappings.tiny header: tiny 2 0 official intermediary named (check)
with open(YARN, encoding="utf-8") as f:
    header = f.readline().strip().split("\t")
print("yarn header:", header)
ncols = {n: i for i, n in enumerate(header[3:])}
# data lines: <type> <official> <intermediary> [named...] -> +1 offset for row type
ICOL, NCOL = 2, 3

yarn_class = {}          # named -> intermediary
yarn_members = collections.defaultdict(dict)  # (intermediaryClass) {namedMember -> intermediaryMember}
cur_icls = None
for line in open(YARN, encoding="utf-8"):
    line = line.rstrip("\r\n")
    indented = line.startswith("\t")
    s = line.lstrip("\t")
    if not indented and s.startswith("c\t"):
        p = s.split("\t")
        if len(p) > NCOL:
            cur_icls = p[ICOL]
            yarn_class[p[NCOL]] = p[ICOL]
        continue
    if indented and (s.startswith("m\t") or s.startswith("f\t")) and not s.startswith("c\t"):
        p = s.split("\t")
        if p[0] in ("m", "f") and len(p) > NCOL:
            yarn_members[cur_icls][p[NCOL]] = p[ICOL]

# ---- intermediary tiny: per class member names ----
inter_members = collections.defaultdict(set)
cur = None
for line in open(INTER, encoding="utf-8"):
    line = line.rstrip("\r\n")
    s = line.lstrip("\t")
    if not line.startswith("\t") and s.startswith("c\t"):
        cur = s.split("\t")[2]
        continue
    if line.startswith("\t") and (s.startswith("m\t") or s.startswith("f\t")):
        inter_members[cur].add(s.split("\t")[-1])

# ---- build superclass map (named names) from the named merged MC jar ----
import zipfile, struct
NAMED_JAR = None
import glob as _g
for cand in _g.glob(r"C:\Users\admin\Desktop\workspace\LarpClient-2.2.6-SRC\.gradle\loom-cache\minecraftMaven\net\minecraft\minecraft-merged-*\*\*.jar"):
    NAMED_JAR = cand  # any of these is the named merged jar for this mapping
    break
print("named jar:", NAMED_JAR)

def parse_super(data):
    """Return (superclass, [interfaces]) internal names from raw class bytes."""
    if data[0:4] != b"\xca\xfe\xba\xbe":
        return (None, [])
    n = struct.unpack_from(">H", data, 8)[0]  # constant_pool_count
    off = 10
    utf8 = {}
    cls_names = {}
    idx = 1
    while idx < n:
        tag = data[off]
        if tag == 1:
            ln = struct.unpack_from(">H", data, off + 1)[0]
            utf8[idx] = data[off + 3: off + 3 + ln]
            off += 3 + ln
        elif tag in (7,):        # Class
            cls_names[idx] = struct.unpack_from(">H", data, off + 1)[0]
            off += 3
        elif tag in (9, 10, 11, 12, 17, 18):
            off += 5
        elif tag in (3, 4):      # Integer / Float: 4-byte payload
            off += 5
        elif tag in (8, 16, 19, 20):  # String/MethodType/Module/Package: 2-byte payload
            off += 3
        elif tag in (5, 6):
            off += 9; idx += 1  # long/double take two slots
        elif tag == 15:
            off += 4
        else:
            return (None, [])
        idx += 1
    off += 4  # access_flags + this_class
    sup = struct.unpack_from(">H", data, off)[0]
    off += 2
    nic = struct.unpack_from(">H", data, off)[0]  # interfaces_count
    off += 2
    ifaces = []
    for _ in range(nic):
        i = struct.unpack_from(">H", data, off)[0]
        ifaces.append(utf8.get(cls_names.get(i, 0), b"").decode("utf-8", "replace"))
        off += 2
    supname = utf8.get(cls_names.get(sup, 0), b"").decode("utf-8", "replace") if sup else None
    return (supname, ifaces)

zf = zipfile.ZipFile(NAMED_JAR)
parsed = {}
def supers(named_cls):
    """Return (super, [interfaces]) cached."""
    if named_cls in parsed:
        return parsed[named_cls]
    path = named_cls + ".class"
    try:
        r = parse_super(zf.read(path))
    except KeyError:
        r = (None, [])
    parsed[named_cls] = r
    return r

# ---- verify ----
ok, fail, unknown_class = [], [], []
for member, named_target in sorted(pairs):
    itarget = yarn_class.get(named_target)
    if itarget is None:
        unknown_class.append((member, named_target))
        continue
    if member == "<init>":
        ok.append((member, named_target, "constructor - never needs remapping"))
        continue
    # member is already intermediary? check target class and ancestor chain (BFS: supers + interfaces)
    if member in inter_members[itarget]:
        ok.append((member, named_target, "found in intermediary target"))
        continue
    found_parent = None
    seen = {named_target}
    queue = [named_target]
    while queue and not found_parent:
        c = queue.pop(0)
        s, ifs = supers(c)
        for anc in ([s] if s else []) + ifs:
            if not anc or anc in seen:
                continue
            seen.add(anc)
            ia = yarn_class.get(anc)
            if ia and member in inter_members[ia]:
                found_parent = anc
                break
            queue.append(anc)
    if found_parent:
        ok.append((member, named_target, f"inherited from {found_parent}"))
        continue
    # member might be a yarn name -> map via yarn
    im = yarn_members.get(itarget, {}).get(member)
    if im and im in inter_members[itarget]:
        ok.append((member, named_target, f"yarn name -> {im}"))
        continue
    fail.append((member, named_target))

print(f"\nOK: {len(ok)}  FAIL: {len(fail)}  unknown-class: {len(unknown_class)}")
if fail:
    print("\n--- members NOT found in intermediary target class ---")
    for m, t in fail:
        print(f"  {m}  in  {t}")
if unknown_class:
    print("\n--- target classes not in yarn mappings ---")
    for m, t in unknown_class:
        print(f"  {m}  target {t}")
# show a few OK samples with reasons
print("\n--- sample OK reasons ---")
seen = set()
for m, t, r in ok:
    k = r.split(" -> ")[0]
    if k not in seen:
        seen.add(k)
        print(f"  {m} in {t}: {r}")
    if len(seen) >= 5:
        break
