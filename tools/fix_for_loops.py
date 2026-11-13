"""Repair Vineflower's raw-collection enhanced-for loops.

Vineflower erases generic collection declarations to raw List/Set/Deque, then emits
    for (BlockPos var19 : var3) { ... }
which javac rejects ("Object cannot be converted to BlockPos"). Casting the iterable
to Iterable<T> restores the element type without changing the emitted bytecode
(the checkcast to T happens either way). Only lines javac actually complained about
are touched.
"""
import os
import re
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
BS = chr(92)

BOX = {'int': 'Integer', 'long': 'Long', 'double': 'Double', 'float': 'Float',
       'boolean': 'Boolean', 'byte': 'Byte', 'short': 'Short', 'char': 'Character'}
FOR_RE = re.compile(r'^(\s*)for\s*\(\s*(.+?)\s+([A-Za-z_$][A-Za-z0-9_$]*)\s*:\s*(.+?)\s*\)(\s*\{?)\s*$')
ERR_RE = re.compile(r'^(.+?):(\d+): error: (.*)$', re.M)


def main():
    log = open('out/javac.log', encoding='utf-8', errors='replace').read()
    targets = defaultdict(set)   # file -> line numbers
    for f, l, msg in ERR_RE.findall(log):
        if 'Object cannot be converted to' not in msg and 'List<Object> cannot be converted' not in msg:
            continue
        targets[f.replace(BS, '/')].add(int(l))

    changed = 0
    skipped = []
    for path, lines in targets.items():
        if not os.path.isfile(path):
            continue
        src = open(path, encoding='utf-8', errors='replace').read().splitlines()
        dirty = False
        for ln in sorted(lines):
            i = ln - 1
            if i >= len(src):
                continue
            m = FOR_RE.match(src[i])
            if not m:
                skipped.append((path, ln, src[i].strip()[:110]))
                continue
            indent, typ, var, expr, tail = m.groups()
            boxed = BOX.get(typ, typ)
            # Nested/parameterized types would need a different treatment; leave them.
            if '<' in typ or ' ' in typ.strip() or '[]' in typ and typ not in BOX and '<' in typ:
                skipped.append((path, ln, 'type=' + typ))
                continue
            if '[]' in typ:
                # e.g. float[] : Iterable<float[]> is legal
                pass
            if expr.startswith('(Iterable<'):
                continue
            src[i] = '%sfor (%s %s : (Iterable<%s>) (%s))%s' % (
                indent, typ, var, boxed, expr, tail)
            dirty = True
            changed += 1
        if dirty:
            open(path, 'w', encoding='utf-8', newline='').write('\n'.join(src) + '\n')
    print('patched %d enhanced-for loops in %d files' % (changed, len(targets)))
    print('skipped %d:' % len(skipped))
    for s in skipped[:20]:
        print('   ', s)


if __name__ == '__main__':
    main()
