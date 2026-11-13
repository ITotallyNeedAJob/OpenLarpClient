"""Restore runtime-correct mixin annotation strings (exact, bytecode-grounded).

Why: the shipped mod has NO refmap, so Mixin resolves @Mixin(targets=...),
@Inject(method=...), @At(target=...), @Accessor/@Invoker(value=...) strings
literally in the runtime namespace. The original jar is the authority: its
annotation values are pure intermediary (net/minecraft/class_NNNN, method_NNNN).

The deobfuscation pipeline renamed those strings to yarn for readability but was
incomplete -- inner-class tokens stayed intermediary, producing hybrids like
`net/minecraft/network/PacketApplyBatcher$class_11981` that exist in NO namespace.
Mixin then fails in PREPARE: "@Mixin target ... was not found".

Method:
  1. DumpMixinStrings.java emits (class, ordinal, annotation, element, value)
     for the original jar and the v4 jar.
  2. The two mixin configs list the same 194 classes positionally; that gives an
     exact original-class -> v4-class map (including renamed inner classes).
  3. Join on (mapped class, ordinal) -> (v4 value, original value) pairs.
  4. Restore every mixin source file's string literals positionally from the
     pristine decompiled tree (this undoes an earlier, coarser attempt while
     preserving all non-literal compile fixes).
  5. Replace those literals with the original jar's values, scoped by source
     file so identical literals in different mixins cannot cross-contaminate.
"""
import collections
import json
import os
import re
import zipfile

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
ORIG_JAR = 'C:/Users/admin/Downloads/workspace-01a1d043-extracted/idk-smth/LARP-CRACKED-BY-CIG.jar'
V4_JAR = 'C:/Users/admin/Downloads/workspace-01a1d043-extracted/work/deliverables/LarpClient-2.2.6-deobf-v4.jar'
PRISTINE = 'C:/Users/admin/Downloads/workspace-01a1d043-extracted/work/src-final'
SRC = 'src/main/java'
LIT = re.compile(r'"(?:\\.|[^"\\])*"')


def unesc(s):
    return (s.replace('\\t', '\t').replace('\\n', '\n')
             .replace('\\r', '\r').replace('\\\\', '\\'))


def load_tsv(p):
    """(fullClass, ordinal) -> value."""
    d = {}
    for line in open(p, encoding='utf-8'):
        f = line.rstrip('\n').split('\t')
        if len(f) < 6:
            continue
        d[(f[1], int(f[2]))] = unesc(f[5])
    return d


def class_map():
    zo, zv = zipfile.ZipFile(ORIG_JAR), zipfile.ZipFile(V4_JAR)
    m = {}
    for cfg in ('larpclient.mixins.json', 'fake-spawner.mixins.json'):
        a, b = json.loads(zo.read(cfg)), json.loads(zv.read(cfg))
        la, lb = a['client'] + a.get('common', []), b['client'] + b.get('common', [])
        assert len(la) == len(lb), cfg
        pa, pb = a['package'].replace('.', '/'), b['package'].replace('.', '/')
        for x, y in zip(la, lb):
            m[pa + '/' + x.replace('.', '/')] = pb + '/' + y.replace('.', '/')
    return m


def java_literal(s):
    return '"' + s.replace('\\', '\\\\').replace('"', '\\"') + '"'


def main():
    cm = class_map()
    o, v = load_tsv('tools/mixin-orig.tsv'), load_tsv('tools/mixin-v4.tsv')
    # map original rows onto v4 class names
    om = {}
    for (c, i), val in o.items():
        if c in cm:
            om[(cm[c], i)] = val
    keys = sorted(set(v) & set(om))
    print('join keys: v4=%d orig=%d matched=%d' % (len(v), len(om), len(keys)))

    # v4 class -> source file (nested classes live in the outer file)
    def src_file(v4cls):
        outer = v4cls.split('$')[0]
        p = os.path.join(SRC, *outer.split('/')) + '.java'
        return p if os.path.exists(p) else None

    # per-file: old literal -> new literal
    per_file = collections.defaultdict(dict)
    conflicts, nofile = [], []
    for k in keys:
        a, b = v[k], om[k]
        if a == b:
            continue
        p = src_file(k[0])
        if not p:
            nofile.append(k[0])
            continue
        prev = per_file[p].get(a)
        if prev is not None and prev != b:
            conflicts.append((p, a, prev, b))
        per_file[p][a] = b
    print('files to patch:', len(per_file))
    print('conflicts:', len(conflicts), conflicts[:4])
    print('no source file:', len(set(nofile)), sorted(set(nofile))[:6])

    # restore literals from pristine, then apply exact replacements
    patched, restore_fail = 0, []
    for p in sorted(per_file):
        rel = os.path.relpath(p, SRC).replace('\\', '/')
        q = os.path.join(PRISTINE, *rel.split('/'))
        cur = open(p, encoding='utf-8', errors='replace').read()
        pri = open(q, encoding='utf-8', errors='replace').read()
        parts = LIT.split(cur)
        plit = LIT.findall(pri)
        clit = LIT.findall(cur)
        if len(plit) != len(clit):
            restore_fail.append(rel)
            continue
        # rebuild with pristine literals, preserving all non-literal text
        text = parts[0]
        for i, lit in enumerate(plit):
            text += lit + parts[i + 1]
        # exact replacements
        for old, new in per_file[p].items():
            lo, ln = java_literal(old), java_literal(new)
            if lo in text:
                text = text.replace(lo, ln)
            else:
                restore_fail.append(rel + ' :: missing ' + old[:70])
        if text != cur:
            open(p, 'w', encoding='utf-8', newline='').write(text)
            patched += 1
    print('source files rewritten:', patched)
    print('restore issues:', len(restore_fail))
    for x in restore_fail[:15]:
        print('   ', x)


if __name__ == '__main__':
    main()
