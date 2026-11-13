import java.io.*; import java.util.*; import java.util.zip.*;
import org.objectweb.asm.ClassReader;
public class ParseAll {
  public static void main(String[] a) throws Exception {
    ZipFile z = new ZipFile(a[0]); int ok=0, bad=0, maj=0;
    Enumeration<? extends ZipEntry> e = z.entries();
    while (e.hasMoreElements()) {
      ZipEntry en = e.nextElement();
      if (!en.getName().endsWith(".class")) continue;
      byte[] d = z.getInputStream(en).readAllBytes();
      try { ClassReader cr = new ClassReader(d); cr.getClassName(); ok++;
            if (cr.readShort(6) > maj) maj = cr.readShort(6); }
      catch (Throwable t) { bad++; System.out.println("BAD " + en.getName() + ": " + t); }
    }
    System.out.println("parsed=" + ok + " bad=" + bad + " maxMajor=" + maj);
    if (bad != 0) System.exit(1);
  }
}
