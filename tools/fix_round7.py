"""ModuleSearchPanel: var3 is reassigned each do/while iteration but captured by
lambdas. Snapshot to a final local. Java-only, no behaviour change.
"""
import os

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
p = 'src/main/java/com/larpclient/ModuleSearchPanel.java'
s = open(p, encoding='utf-8', errors='replace').read()

FIXES = [
    ("""               var3 = (ControlSettings.Type_h)var9.next();
               var4 = () -> var3.settings().stream().anyMatch(Setting::isVisible);""",
     """               final ControlSettings.Type_h var3f = (ControlSettings.Type_h)var9.next();
               var3 = var3f;
               var4 = () -> var3f.settings().stream().anyMatch(Setting::isVisible);"""),
    ("""               String var5 = var3.title().toLowerCase(Locale.ROOT);
               ModuleSearchPanel.Type_w var6 = new ModuleSearchPanel.Type_w(() -> (this.V.contains(var3.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.V.remove(var3.title())) {
                     this.V.add(var3.title());""",
     """               String var5 = var3f.title().toLowerCase(Locale.ROOT);
               ModuleSearchPanel.Type_w var6 = new ModuleSearchPanel.Type_w(() -> (this.V.contains(var3f.title()) ? "Hide " : "Show ") + var5, () -> {
                  if (!this.V.remove(var3f.title())) {
                     this.V.add(var3f.title());"""),
]

for old, new in FIXES:
    n = s.count(old)
    if n != 1:
        raise SystemExit('expected 1 match, got %d for:\n%s' % (n, old))
    s = s.replace(old, new)
    print('  OK  patched %d-line block' % len(old.splitlines()))

open(p, 'w', encoding='utf-8', newline='').write(s)
print('done')
