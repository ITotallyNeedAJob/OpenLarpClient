"""Final mechanical source repairs before the prebuilt fallback.

Every edit here is compile-time-only or restores a name Vineflower visibly lost.
Assertions make any missed pattern loud instead of silent.
"""
import os
import re
import shutil

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
B = 'src/main/java/com/larpclient/'

# (relative path, old, new, expected occurrences)
FIXES = [
    # Vineflower collapsed an int flag into a boolean; all uses are boolean-context.
    ('module/Aura.java',
     'int var421 = com.larpclient.module.y.sAsDouble',
     'boolean var421 = com.larpclient.module.y.sAsDouble', 1),
    # Lost local declaration: bytecode local is var2xx, decompiler declared var2x.
    ('update/UpdateService.java',
     'UpdateStore.Receipt var2x;',
     'UpdateStore.Receipt var2xx;', 1),
    # Lost local declaration for the two-way flag.
    ('ModuleMigrationNames.java',
     '               label54: {',
     '               boolean var29;\n               label54: {', 1),
    # Typo: the declared local is var7x.
    ('InterfaceSettingsPanel.java',
     'var9.setValue(Boolean.valueOf(var7.S));',
     'var9.setValue(Boolean.valueOf(var7x.S));', 1),
    # ScheduledExecutorService schedule/submit overloads are ambiguous for a method ref.
    ('PreferenceSaveQueue.java',
     'this.M.schedule(this::kAsVoid, 250L, TimeUnit.MILLISECONDS)',
     'this.M.schedule((Runnable)this::kAsVoid, 250L, TimeUnit.MILLISECONDS)', 1),
    ('PreferenceSaveQueue.java',
     'this.M.submit(this::kAsVoid)',
     'this.M.submit((Runnable)this::kAsVoid)', 1),
    # Stream<BooleanSetting> -> List<Setting<?>> needs an upcast at the element.
    ('module/Removals.java',
     '.map(this.L::get).toList()',
     '.map(var0 -> (Setting<?>)this.L.get(var0)).toList()', 2),
    # Raw Comparator makes the lambda parameter Object.
    ('module/b.java',
     'Comparator.comparingDouble(entity -> var1.squaredDistanceTo(entity))',
     'Comparator.comparingDouble(entity -> var1.squaredDistanceTo((Entity)entity))', 1),
    # Setting<?> setValue(capture) cannot take Object; raw receiver erases to Object.
    ('ModuleConfigCodec.java',
     '(var0, var1x) -> var0.setValue(var1x)',
     '(var0, var1x) -> ((com.larpclient.module.Setting)var0).setValue(var1x)', 1),
    # byte flag vs boolean field in bytecode both ints.
    ('AutoTunnel.java',
     'if (var1 != this.h) {',
     'if ((var1 != 0) != this.h) {', 1),
    # Raw Map.Entry.getKey() returns Object.
    ('mixin/compat/ClientPlayNetworkHandlerOnItemPickupAnimationMixin.java',
     'ItemStack var4 = var3.getKey() == -1',
     'ItemStack var4 = ((Integer)var3.getKey()) == -1', 1),
    # A local int named `m` shadows class m -> qualify the nested enum.
    ('ShopPurchaseController.java',
     'm.Type_i.SAFE', 'com.larpclient.m.Type_i.SAFE', 1),
    ('ShopPurchaseController.java',
     'm.Type_i.UNREADABLE', 'com.larpclient.m.Type_i.UNREADABLE', 2),
    # A local float named `a` shadows class a -> qualify the nested enum.
    ('module/p.java', 'a.Type_b.YAW', 'com.larpclient.module.a.Type_b.YAW', 2),
    ('module/p.java', 'a.Type_b.PITCH', 'com.larpclient.module.a.Type_b.PITCH', 2),
]

by_file = {}
for f, old, new, n in FIXES:
    by_file.setdefault(f, []).append((old, new, n))

failed = []
for f, edits in by_file.items():
    p = B + f
    s = open(p, encoding='utf-8', errors='replace').read()
    for old, new, n in edits:
        got = s.count(old)
        if got != n:
            failed.append('%s: expected %d of %r, found %d' % (f, n, old[:55], got))
            continue
        s = s.replace(old, new)
        print('  OK  %-58s %d x %s' % (f, n, old[:50]))
    open(p, 'w', encoding='utf-8', newline='').write(s)

# --- duplicate local variable names (Vineflower reused a name in one method) ---
log = open('out/javac.log', encoding='utf-8', errors='replace').read()
BS = chr(92)
dup = {}
for f, l, m in re.findall(r'^(.+?):(\d+): error: (.*)$', log, re.M):
    mm = re.match(r'variable (\w+) is already defined in method', m)
    if mm:
        dup.setdefault(f.replace(BS, '/'), []).append((int(l), mm.group(1)))

os.makedirs('out/dup-backup', exist_ok=True)
for p, items in dup.items():
    shutil.copyfile(p, os.path.join('out/dup-backup', os.path.basename(p)))
    lines = open(p, encoding='utf-8', errors='replace').read().splitlines(keepends=True)
    for line_no, name in sorted(items, reverse=True):
        i = line_no - 1
        # Vineflower indents method bodies by 3 spaces; the first bare "   }"
        # after the declaration closes the enclosing method.
        end = len(lines)
        for j in range(i + 1, len(lines)):
            if lines[j].rstrip('\r\n') == '   }':
                end = j
                break
        new = name + '_r2'
        word = re.compile(r'\b' + re.escape(name) + r'\b')
        count = 0
        for j in range(i, end):
            lines[j], k = word.subn(new, lines[j])
            count += k
        print('  DUP %-46s L%d %s -> %s (%d refs)' % (os.path.basename(p), line_no, name, new, count))
    open(p, 'w', encoding='utf-8', newline='').write(''.join(lines))

if failed:
    print('\n!!! MISSED PATTERNS:')
    for f in failed:
        print('   ', f)
print('done')
