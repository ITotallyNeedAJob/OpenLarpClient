/** Dump every annotation string value in a jar's mixin classes.
 *  Columns: simpleName, fullClassName, ordinalWithinClass, annotationDesc,
 *  elementPath, value.  Joining original vs deobfuscated on
 *  (mapped fullClassName, ordinal) recovers the intermediary annotation
 *  strings the deobfuscation pipeline mangled.
 */
import java.io.*;
import java.util.*;
import java.util.zip.*;
import org.objectweb.asm.*;
import org.objectweb.asm.tree.*;

public class DumpMixinStrings {
    static PrintWriter out;
    static String cls;
    static int ordinal;

    public static void main(String[] a) throws Exception {
        out = new PrintWriter(new BufferedWriter(new FileWriter(a[1])));
        try (ZipFile z = new ZipFile(a[0])) {
            List<String> names = new ArrayList<>();
            Enumeration<? extends ZipEntry> e = z.entries();
            while (e.hasMoreElements()) {
                ZipEntry en = e.nextElement();
                if (en.getName().endsWith(".class")) names.add(en.getName());
            }
            Collections.sort(names);
            for (String n : names) {
                byte[] d = z.getInputStream(z.getEntry(n)).readAllBytes();
                ClassNode cn = new ClassNode();
                new ClassReader(d).accept(cn, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                cls = cn.name;
                ordinal = 0;
                String simple = n.substring(n.lastIndexOf('/') + 1, n.length() - 6);
                visitAnns(simple, cn.visibleAnnotations, cn.invisibleAnnotations);
                for (FieldNode f : cn.fields)
                    visitAnns(simple, f.visibleAnnotations, f.invisibleAnnotations);
                for (MethodNode m : cn.methods)
                    visitAnns(simple, m.visibleAnnotations, m.invisibleAnnotations);
            }
        }
        out.close();
    }

    static void visitAnns(String simple, List<AnnotationNode> a, List<AnnotationNode> b) {
        if (a != null) for (AnnotationNode n : a) visit(simple, n.desc, "", n);
        if (b != null) for (AnnotationNode n : b) visit(simple, n.desc, "", n);
    }

    static void visit(String simple, String desc, String path, AnnotationNode n) {
        if (n.values == null) return;
        for (int i = 0; i < n.values.size(); i += 2) {
            String key = (String) n.values.get(i);
            Object v = n.values.get(i + 1);
            emit(simple, desc, path + "/" + key, v, 0);
        }
    }

    static void emit(String simple, String desc, String path, Object v, int idx) {
        if (v instanceof String s) {
            out.println(simple + "\t" + cls + "\t" + ordinal + "\t" + desc + "\t"
                    + path + "[" + idx + "]\t" + esc(s));
            ordinal++;
        } else if (v instanceof List<?> l) {
            for (int i = 0; i < l.size(); i++) emit(simple, desc, path, l.get(i), i);
        } else if (v instanceof AnnotationNode an) {
            for (int i = 0; i < an.values.size(); i += 2) {
                String k = (String) an.values.get(i);
                emit(simple, desc, path + "/" + an.desc + "#" + k, an.values.get(i + 1), i);
            }
        }
        // Type / primitive values are handled by Loom; only strings matter.
    }

    static String esc(String s) {
        return s.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r");
    }
}
