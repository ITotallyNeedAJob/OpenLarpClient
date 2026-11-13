"""Last lambda-capture fix: var18 is reassigned in a do/while loop but captured
by lambdas. Snapshot it into final locals (per iteration + after the loop).
Java-only change; same values, same bytecode semantics.
"""
import os

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
p = 'src/main/java/com/larpclient/ModuleSettingsPanel.java'
s = open(p, encoding='utf-8', errors='replace').read()

FIXES = [
    ("""               var18 = (ControlSettings.Type_h)var12.next();
               if (!var18.collapsed()) {""",
     """               final ControlSettings.Type_h var18f = (ControlSettings.Type_h)var12.next();
               var18 = var18f;
               if (!var18.collapsed()) {"""),
    ("""                  boolean var2x = this.e.get(var18.title()).Q;
                  if (!this.j.remove(var18.title())) {
                     this.j.add(var18.title());""",
     """                  boolean var2x = this.e.get(var18f.title()).Q;
                  if (!this.j.remove(var18f.title())) {
                     this.j.add(var18f.title());"""),
    ("""                     this.FAsVoid(this.e.get(var18.title()));""",
     """                     this.FAsVoid(this.e.get(var18f.title()));"""),
    ("""               this.Z.put(var5, () -> var18.settings().stream().anyMatch(Setting::isVisible));
            } while (!this.j.contains(var18.title()));

            this.AAsVoid(new SectionHeaderWidget(var18.title(), () -> var18.settings().stream().anyMatch(Setting::isVisible), var18.chips()));""",
     """               this.Z.put(var5, () -> var18f.settings().stream().anyMatch(Setting::isVisible));
            } while (!this.j.contains(var18.title()));

            final ControlSettings.Type_h var18g = var18;
            this.AAsVoid(new SectionHeaderWidget(var18.title(), () -> var18g.settings().stream().anyMatch(Setting::isVisible), var18.chips()));"""),
]

for old, new in FIXES:
    n = s.count(old)
    if n != 1:
        raise SystemExit('expected 1 match, got %d for:\n%s' % (n, old))
    s = s.replace(old, new)
    print('  OK  patched %d-line block' % len(old.splitlines()))

open(p, 'w', encoding='utf-8', newline='').write(s)
print('done')
