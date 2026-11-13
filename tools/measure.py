"""Fast javac measurement against the Loom-resolved, access-widened classpath.

Iteratively drops files with parse errors (Vineflower round-trip failures) so javac
can enter attribution, then reports the real semantic errors by category and file.
"""
import os
import re
import subprocess
import sys
from collections import Counter

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
CP = open('compile-cp.txt', encoding='utf-8').read().strip()
PARSE_KW = (
    'expected', 'illegal start', 'reached end of file', 'not a statement',
    'class, interface, enum, or record', 'illegal character', 'unclosed',
    'invalid method declaration', 'not allowed here', 'illegal escape',
    'unclosed string literal', 'malformed', 'extraneous input',
    'lambda expression not expected here',
)


def sources():
    out = []
    for dp, dn, fn in os.walk('src/main/java'):
        for f in fn:
            if f.endswith('.java'):
                out.append(os.path.join(dp, f).replace('\\', '/'))
    return sorted(out)


def main():
    src = sources()
    dropped = []
    log = ''
    for it in range(10):
        os.makedirs('out/jc', exist_ok=True)
        with open('out/sources.txt', 'w', encoding='utf-8') as fh:
            fh.write('\n'.join(src))
        cmd = ['javac', '-J-Xmx2G', '-nowarn', '-proc:none', '-encoding', 'UTF-8',
               '--release', '21', '-Xmaxerrs', '100000', '-cp', CP,
               '-d', 'out/jc', '@out/sources.txt']
        p = subprocess.run(cmd, capture_output=True, text=True, errors='replace')
        log = (p.stdout or '') + (p.stderr or '')
        open('out/javac.log', 'w', encoding='utf-8').write(log)
        errs = re.findall(r'^(.+?):(\d+): error: (.*)$', log, re.M)
        bad = set()
        for f, line, msg in errs:
            if any(k in msg for k in PARSE_KW):
                bad.add(f.replace('\\', '/'))
        print('iter %d: %d sources / %d errors / %d parse-broken' % (it, len(src), len(errs), len(bad)))
        if not bad:
            break
        dropped.extend(sorted(bad))
        bad = set(os.path.normcase(os.path.abspath(b)) for b in bad)
        src = [s for s in src if os.path.normcase(os.path.abspath(s)) not in bad]

    errs = re.findall(r'^(.+?):(\d+): error: (.*)$', log, re.M)
    cats, files = Counter(), Counter()
    for f, line, msg in errs:
        cats[msg.split(',')[0][:120]] += 1
        files[f.replace('\\', '/')] += 1
    print('=== %d semantic errors, %d parse-broken files ===' % (len(errs), len(dropped)))
    print('-- parse-broken:')
    for b in dropped:
        print('   ', b)
    print('-- top messages:')
    for m, n in cats.most_common(30):
        print('   %5d  %s' % (n, m))
    print('-- top files:')
    for f, n in files.most_common(30):
        print('   %5d  %s' % (n, f))
    open('out/broken.txt', 'w', encoding='utf-8').write('\n'.join(dropped))
    return len(errs)


if __name__ == '__main__':
    sys.exit(0 if main() == 0 else 1)
