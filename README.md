# **OpenLarpClient**

by one guy with jadx
buildable source of the LarpClient client · MC 1.21.11 · Fabric

sibling repo of [OpenKuroPlus](https://github.com/ITotallyNeedAJob/OpenKuroPlus). same energy, worse budget.

---

## what is this

LarpClient (by Tobias), a **$5 client** that larps as premium. recovered from `LARP-CRACKED-BY-CIG.jar` — yes, that is the actual shipped filename. the crack was somebody else's crime, we just made it compile.

**fun facts from the rebuild:**

- the original ships with **no refmap**. at five dollars you get annotation strings resolved literally at runtime and you will like it.
- obfuscated with Zelix KlassMaster. we answered with 929 positional joins and a spreadsheet.
- 1463 classes, 194 mixins, and every single annotation string restored **byte-for-byte** against the original jar.
- gradle yells `Cannot remap` **459 times** per build. we audited every single one. all 323 targets resolve. that audit is the most expensive thing in this repo.
- compiled at class file version **65**. not 69. five-dollar client.

## how to build

Requires **JDK 21**. it's the same java the mod ships with, so you have no excuse.

```
./gradlew build
```

the 459 warnings are normal. do not read them. we already read them for you (see [BUILD.md](BUILD.md)).

Output: `build/libs/larp-client-2.2.6.jar`

## how to run

1. Fabric Loader **0.19.5+** for MC 1.21.11
2. Fabric API (0.141.6 works, others probably do too)
3. jar goes in `mods/`. you know the rest.

## credits

- **CIG** — for cracking the jar before we even got here. legends.
- **Tobias** — for the five dollars of code. we got what we paid for, plus the source.
- **Zelix KlassMaster** — for `q240a4660874b1b31`. never change.
- **Loom** — for the 459 warnings. someone had to say it.

license follows the original mod metadata: **`LicenseRef-LarpClient-EULA`** (yes, a $5 client ships an EULA. lawyer-maxxing.)
