"""Bridge mixin `this` to Object where javac cannot see the runtime merge.

In a mixin class, `this instanceof ClientWorld` / `this == mc.player` is legal at
runtime (Mixin merges the class into the target), but javac sees two provably
unrelated classes. Casting through Object reproduces the same bytecode semantics.
Only the exact lines javac flagged are rewritten.
"""
import os
import re
from collections import defaultdict

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
BS = chr(92)
ERR_RE = re.compile(r'^(.+?):(\d+): error: (.*)$', re.M)

WANT = ('incomparable types:', 'bad operand types for binary operator',
        'incompatible types:')


def transform(line):
    orig = line
    # (BipedEntityModel<?>)this  ->  (BipedEntityModel<?>)(Object)this
    line = re.sub(r'\(([A-Za-z_$][A-Za-z0-9_$.]*(?:<[^()]*>)?)\)this\b(?!\s*\.)',
                  r'(\1)(Object)this', line)
    line = re.sub(r'\bthis instanceof\b', '(Object)this instanceof', line)
    line = re.sub(r'\bthis(\s*)(==|!=)', r'(Object)this\1\2', line)
    line = re.sub(r'(==|!=)(\s*)this\b(?!\s*\.)', r'\1\2(Object)this', line)
    line = line.replace('this.getClass()', '((Object)this).getClass()')
    return line


def main():
    log = open('out/javac.log', encoding='utf-8', errors='replace').read()
    targets = defaultdict(set)
    for f, l, msg in ERR_RE.findall(log):
        if not msg.startswith(WANT):
            continue
        if any(k in msg for k in ('Mixin', 'mixins', 'Overlay', 'incomparable',
                                  'bad operand types', 'cannot be converted to HandledScreen')):
            targets[f.replace(BS, '/')].add(int(l))

    n = 0
    for path, lines in targets.items():
        if not os.path.isfile(path):
            continue
        src = open(path, encoding='utf-8', errors='replace').read().splitlines()
        dirty = False
        for ln in sorted(lines):
            i = ln - 1
            if i >= len(src) or 'this' not in src[i]:
                continue
            new = transform(src[i])
            if new != src[i]:
                src[i] = new
                dirty = True
                n += 1
        if dirty:
            open(path, 'w', encoding='utf-8', newline='').write('\n'.join(src) + '\n')
    print('patched %d lines in %d files' % (n, len(targets)))


if __name__ == '__main__':
    main()
