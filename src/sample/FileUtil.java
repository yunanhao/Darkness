package sample;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class FileUtil {

    @Test
    public void fid() {
        String path = "C:\\Users\\Master\\Downloads";
        File key = new File(path, "key");
        File value = new File(path, "value");
        File serch = new File(path, "serch");
        HashMap<String, String> kv = new HashMap<>();
        try {
            Scanner k = new Scanner(key);
            Scanner v = new Scanner(value);
            Scanner s = new Scanner(serch);
            while (k.hasNextLine()) {
                kv.put(k.nextLine(), v.nextLine());
            }
            while (s.hasNextLine()) {
                System.out.println(kv.get(s.nextLine()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(kv);
    }

    @Test
    public void rename() {
        String path = "C:\\Users\\Master\\Downloads\\CNY 2020-SpecExport\\新建文件夹\\Android-Splash Screen";
        String target = "Android Splash Screen.png";
        String result = "christmas.png";
//        int matchMode = 0;
        rename(new File(path), target, result);
    }

    private void rename(File file, String target, String result) {
        if (file == null) return;
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files == null) return;
            for (File value : files) {
                System.out.println(value.getName());
                rename(value, target, result);
            }
        } else if (file.getName().contains(target)) {
            File dst = new File(file.getParentFile(), result);
            System.out.println(dst.getPath());
            boolean flag = file.renameTo(dst);
            System.out.println(flag);
        }
    }
}
