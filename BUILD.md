# LarpClient 2.2.6 — Build Instructions

Rebuildable Fabric mod project reconstructed from the deobfuscated (pass-4) sources
of `LARP-CRACKED-BY-CIG.jar` (LarpClient 2.2.6).

- MC `1.21.11` / yarn `1.21.11+build.6` / Loom `1.16.3` / Loader `0.19.5` / Java 21
- `build/libs/larp-client-2.2.6.jar` is the deliverable.

## Build

```bash
./gradlew clean build -x test
```

Expect `BUILD SUCCESSFUL` and ~459 `Cannot remap` warnings from Loom's mixin
annotation remapper. **These warnings are verified benign** (see below).

## No refmap — annotation strings are literal

This mod ships **without a refmap** (`refmap=None` in both mixin configs), so
Mixin resolves every annotation string (`@Mixin targets`, `@Inject method`,
`@At` targets) **literally in the runtime (intermediary) namespace**.

The deobf pass had renamed those strings to yarn names (and left inner-class
tokens intermediary), producing hybrid names that exist in no namespace — this
was the cause of the original runtime crash:

```
@Mixin target net/minecraft/network/PacketApplyBatcher$class_11981 was not found
```

### Fix: `tools/fix_mixin_strings.py`

Restores all 929 annotation string values from the pristine original jar:

1. `tools/DumpMixinStrings.java` (ASM) dumps every annotation string value of a
   jar to TSV: `simpleName, fullClassName, ordinal, annotationDesc, elementPath, value`.
   Run it on the original jar -> `tools/mixin-orig.tsv`, on the v4 deobf jar ->
   `tools/mixin-v4.tsv`, on the built jar -> verification dump.
2. The script builds a class map from the two mixin configs (positional join,
   173 + 21 = 194 classes) and joins rows on `(mapped class, ordinal)`.
3. Each mixin source file gets its string literals restored positionally from
   `src-final` while preserving the non-literal compile fixes.

Verified result: **929/929 annotation values in the built jar are byte-identical
to the original jar** (re-check after `clean build` with
`DumpMixinStrings` + join on `(class, ordinal)`).

## Why the `Cannot remap` warnings are benign

The source uses **intermediary member names** (`field_1752`, `method_8532`,
`comp_4056`, ...) inside `@Shadow`/`@Inject`/`@At`, while `@Mixin(SomeClass.class)`
class literals are yarn-named at remap time. Loom therefore tries to remap each
member name from *named -> intermediary*, cannot find e.g. `field_1752` in
`MinecraftClient` (yarn namespace), and warns.

Verification (`tools/verify_remap_warnings.py`): every distinct
`(member, target)` pair from the warnings (323 pairs, 316 distinct names) was
checked against the **1.21.11 intermediary** mapping (`intermediary-v2.tiny`),
walking superclass **and** interface chains of the target class (parsed from the
named merged jar in `.gradle/loom-cache`). Result: **323/323 resolve, 0
failures** — the only non-intermediary name among the warned ones is `<init>`,
which never needs remapping. At runtime the annotation strings resolve exactly
as they did in the original mod.

## Layout

- `src/main/java/com/larpclient/` — deobfuscated sources (mixin handlers renamed)
- `src/main/resources/larpclient.accesswidener` — access widener, `intermediary` header
- `libs/` — nested jars carried over from the original mod
- `tools/` — deobf/build pipeline scripts (string dumpers, fixers, `measure.py`)
