"""Last 3 errors: a reassigned local captured by a nested lambda.

Bytecode reuses the local slot; Java requires effectively-final captures.
Snapshot it into a final local right before the lambda. No behaviour change.
"""
import os

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
p = 'src/main/java/com/larpclient/account/MicrosoftAccounts.java'
s = open(p, encoding='utf-8', errors='replace').read()

FIXES = [
    ("""                  var5.execute(() -> {
                     if (!this.current(var6, var1)) {
                        discard(var8);
                     } else {
""",
     """                  final Object var8final = var8;
                  var5.execute(() -> {
                     if (!this.current(var6, var1)) {
                        discard(var8final);
                     } else {
"""),
    ("""                           var4.accept((T)var8);
                        } catch (RuntimeException var8x) {
                           discard(var8);
                           this.failed(var2, var8x);
""",
     """                           var4.accept((T)var8final);
                        } catch (RuntimeException var8x) {
                           discard(var8final);
                           this.failed(var2, var8x);
"""),
]

for old, new in FIXES:
    n = s.count(old)
    if n != 1:
        raise SystemExit('expected exactly 1 match, got %d for:\n%s' % (n, old))
    s = s.replace(old, new)
    print('  OK  patched block (%d lines)' % len(old.splitlines()))

open(p, 'w', encoding='utf-8', newline='').write(s)
print('done')
