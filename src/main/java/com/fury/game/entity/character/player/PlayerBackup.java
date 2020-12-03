package com.fury.game.entity.character.player;

import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.fury.Stopwatch;
import com.fury.engine.task.executor.GameExecutorManager;
import com.fury.game.GameSettings;
import com.fury.game.system.files.Resources;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class PlayerBackup {

    private static final int TIME = 1800000;// Every 30 mins
    private static final String ACCESS_TOKEN = "QCg5P_qy8QAAAAAAAAAADIay1OtbE0FkmKCsvWe9RQ2a9wQkhyYzMNGCMgpH3w-R";
    private static Stopwatch timer = new Stopwatch().reset();

    public static void sequence() {
        if (timer.elapsed(TIME)) {
            if (!backup()) {
                System.err.println("Error backing up player saves");
            }
            timer.reset();
        }
    }

    public static boolean backup() {
        return backup(false);
    }

    public static boolean backup(boolean noisy) {
        if(!GameSettings.HOSTED)
            return true;

        GameExecutorManager.backgroundExecutor.execute(() -> {

            try {
                File file = new File(Resources.getSaveDirectory("backup") + "/", new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + ".zip");
                file.getParentFile().mkdirs();

                zipDir(file.getAbsolutePath(), Resources.getSaveDirectory("saves"), noisy);

//			if(GameSettings.HOSTED && GameSettings.DROPBOX)
//				upload(file.getAbsolutePath(), "/" + year + "/" + month + "/" + day + "/" + file.getName());

                /*File[] files = new File(Resources.getSaveDirectory("backup")).listFiles();

                if (files.length >= GameSettings.LOCAL_BACKUP_MAX) {
                    Arrays.sort(files, (f1, f2) -> Long.valueOf(f1.lastModified()).compareTo(f2.lastModified()));

                    for (int i = GameSettings.LOCAL_BACKUP_MAX; i < files.length; i++)
                        files[i].delete();
                }*/

                System.out.println("Backup complete: " + new SimpleDateFormat("dd hh-mm").format(new Date()));

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return true;
    }

    public static void upload(String location, String dir) throws DbxException, IOException {
        DbxRequestConfig config = new DbxRequestConfig("frontier_server/" + (GameSettings.GAME_VERSION / 10));
        DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

        ListFolderResult result = client.files().listFolder("");
        while (true) {
            if (!result.getHasMore())
                break;
            result = client.files().listFolderContinue(result.getCursor());
        }

        try (InputStream in = new FileInputStream(location)) {
            FileMetadata metadata = client.files().uploadBuilder(dir)
                    .uploadAndFinish(in);
        }
    }


    private static void zipDir(String zipFileName, String dir, boolean noisy) throws Exception {
        File dirObj = new File(dir);
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipFileName));
        addDir(dirObj, out, noisy);
        out.close();
    }

    static void addDir(File dirObj, ZipOutputStream out, boolean noisy) throws IOException {
        File[] files = dirObj.listFiles();
        byte[] tmpBuf = new byte[1024];

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                addDir(files[i], out, noisy);
                continue;
            }
            FileInputStream in = new FileInputStream(files[i].getAbsolutePath());
            String name = files[i].getAbsolutePath().substring(new File(GameSettings.SAVES).getAbsolutePath().length() + 1, files[i].getAbsolutePath().length());
            if(noisy)
                System.out.println(files[i].getAbsolutePath() + " " + new File(GameSettings.SAVES).getAbsolutePath() + " " + name);
            out.putNextEntry(new ZipEntry(name));
            int len;
            while ((len = in.read(tmpBuf)) > 0)
                out.write(tmpBuf, 0, len);
            out.closeEntry();
            in.close();
        }
    }
}
