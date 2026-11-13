"""Last repair pass: all 14 remaining javac errors, zero prebuilt classes needed.

Notable: `this.Z` in AutoTrap/AutoWeb is a dangling inherited-field reference
present already in the v3 jar. Type_k declares that field as `rotation`
(ModeSetting, modes "Visible"/"Crosshair"); the decompiled subclasses still use
the stale name. Rebuilding from source with `rotation` fixes the reference.
"""
import os

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
os.chdir(ROOT)
B = 'src/main/java/com/larpclient/'

# The exact EntityModelLayer key built by EntityModelLayers.registerMain("player_slim").
# EntityModelLayer is a record, so an equal instance resolves in LoadedEntityModels' map.
# Direct construction is required: MC 1.21.11 declares TWO static fields named
# PLAYER_SLIM (EntityModelLayer and EquipmentModelData), which javac cannot disambiguate.
LAYER = ('new net.minecraft.client.render.entity.model.EntityModelLayer('
         'net.minecraft.util.Identifier.ofVanilla("player_slim"), "main")')

FIXES = [
    # Raw BipedEntityModel gives the method reference no target shape -> typed local.
    ('CosmeticPlayerModel.java',
     '      this.M = new BipedEntityModel(var1, RenderLayers::entityTranslucent);',
     '      java.util.function.Function<net.minecraft.util.Identifier, net.minecraft.client.render.RenderLayer> larpLayerFn'
     ' = RenderLayers::entityTranslucent;\n'
     '      this.M = new BipedEntityModel(var1, larpLayerFn);', 1),
    # Duplicate PLAYER_SLIM static fields; construct the record key directly.
    ('AccountWalkOnAnimation.java',
     'var3.getModelPart(EntityModelLayers.PLAYER_SLIM)',
     'var3.getModelPart(' + LAYER + ')', 1),
    ('r0.java',
     'var1.getModelPart(EntityModelLayers.PLAYER_SLIM)',
     'var1.getModelPart(' + LAYER + ')', 1),
    # Stale inherited field name -> real declaration in BlockPlacementController.Type_k.
    ('module/AutoTrap.java', 'this.Z', 'this.rotation', 5),
    ('module/AutoWeb.java', 'this.Z', 'this.rotation', 2),
    # Raw List sort: comparingInt infers Comparator<Vec3i>; thenComparing needs BlockPos.
    ('module/AutoTrap.java',
     'Comparator.comparingInt(Vec3i::getY)\n               .thenComparing(',
     'Comparator.<BlockPos>comparingInt(Vec3i::getY)\n               .thenComparing(', 1),
    # forceMainThread(Packet<T>, T, ...): raw Packet erases the capture mismatch.
    ('module/Backtrack.java',
     'NetworkThreadUtils.forceMainThread(var0, var3, var4.getPacketApplyBatcher());',
     'NetworkThreadUtils.forceMainThread((net.minecraft.network.packet.Packet)var0, var3, var4.getPacketApplyBatcher());', 1),
    # Stream.map capture conversion -> unchecked widening via List<?>.
    ('module/Removals.java',
     'VanillaEffectFilters.V.stream().map(var0 -> (Setting<?>)this.L.get(var0)).toList()',
     '(List<Setting<?>>)(List<?>)VanillaEffectFilters.V.stream().map(this.L::get).toList()', 1),
    ('module/Removals.java',
     'VanillaEffectFilters.T.stream().map(var0 -> (Setting<?>)this.L.get(var0)).toList()',
     '(List<Setting<?>>)(List<?>)VanillaEffectFilters.T.stream().map(this.L::get).toList()', 1),
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
        print('  OK  %-24s %d x %s' % (f, n, old[:48]))
    open(p, 'w', encoding='utf-8', newline='').write(s)

if failed:
    print('\n!!! MISSED PATTERNS:')
    for x in failed:
        print('   ', x)
print('done')
