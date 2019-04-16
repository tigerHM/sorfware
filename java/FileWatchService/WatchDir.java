package com.zhixin.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static java.nio.file.StandardWatchEventKinds.*;
import static java.nio.file.LinkOption.*;

/**
 * 	�ݹ����Ŀ¼
 * @author 
 *
 */
public class WatchDir {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private boolean trace = false;

	@SuppressWarnings("unchecked")
	static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	/**
	 * Register the given directory with the WatchService
	 */
	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		if (trace) {
			Path prev = keys.get(key);
			if (prev == null) {
				System.out.format("register: %s\n", dir);
			} else {
				if (!dir.equals(prev)) {
					System.out.format("update: %s -> %s\n", prev, dir);
				}
			}
		}
		keys.put(key, dir);
	}

	/**
	 * Register the given directory, and all its sub-directories, with the
	 * WatchService.
	 */
	private void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	/**
	 * Creates a WatchService and registers the given directory
	 */
	WatchDir(Path dir) throws IOException {
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();

		System.out.format("Scanning %s ...\n", dir);
		registerAll(dir);
		System.out.println("Done.");

		this.trace = true;
	}

	/**
	 * Process all events for keys queued to the watcher
	 */
	void processEvents() {
		for (;;) {

			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}

			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}

			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();

				if (kind == OVERFLOW) {
					continue;
				}

				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = dir.resolve(name);
				String fileName=name.toString();
				// print out event
				System.out.format("%s: %s\n", event.kind().name(), child);

				
				String message = "";
                if(kind.name() == "ENTRY_CREATE")
                {
                    message = fileName + "-----> create";
                    delFolder(dir.toString());
                    System.out.println("ִ���˱�������");
                }else if(kind.name() == "ENTRY_DELETE" ){
                    message = fileName + "-----> delete";
                    delFolder(dir.toString());
                    System.out.println("ִ���˱�������");
                }else if(kind.name() == "ENTRY_MODIFY"){
                    message = fileName + "-----> modify";
                    delFolder(dir.toString());
                    System.out.println("ִ���˱�������");
                }
                if (fileName.endsWith("swp")||fileName.endsWith("swpx"))
                {
                    continue;
                }else{
                    System.out.println(message);
                }
			}

			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);

				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}

	static void usage() {
		System.err.println("usage: java WatchDir dir");
		System.exit(-1);
	}

	public static void main(String[] args) throws Exception {
		// parse arguments
		if (args.length == 0 || args.length > 1)
			usage();

		// register directory and process its events
		if (!readProporty(args[0], 0).equals(getinfo())) {
			throw new Exception("������Ϣ�����л�������");
		}
		String watchDir = readProporty(args[0], 1);
		new WatchDir(new File(watchDir).toPath()).processEvents();
	}


	// ��ȡָ���ļ���ȡ����:ProcessorId,InstallPath,WatchPath
	private static String readProporty(String path, int i) {
		List<String> list = new ArrayList<>();
		try {
			String st;
			FileReader reader = new FileReader(path);
			BufferedReader br = new BufferedReader(reader);
			while ((st = br.readLine()) != null) {
				list.add(st);
			}
			br.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list.get(i);
	}
	public  static void delFolder(String path) {
        try {
            delAllFile(path); // ɾ����������������
            String filePath = path;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // ɾ�����ļ���
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //ɾ��ָ���ļ��м��ļ�����������
    public static  void delAllFile(String filepath) throws IOException {
        File file = new File(filepath);
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (filepath.endsWith(File.separator)) {//File.separator�ļ��ָ���
                temp = new File(filepath + tempList[i]);
            } else {
                temp = new File(filepath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(filepath + "/" + tempList[i]);// ��ɾ���ļ���������ļ�
                delFolder(filepath + "/" + tempList[i]);// ��ɾ�����ļ���
            }
        }
    }
    
  //��ȡCPU���к���Ϣ:windowϵͳ
    private static String getCPUinfo(){
        String str=null;
        BufferedReader bufferedReader;
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String property = sc.next();
            String serial = sc.next();

            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] data=md5.digest(serial.getBytes("UTF-8"));
            str= bytesToHexString(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
    
  //��ȡLinux��Serial Number����ʶ�����л���:ʹ��MD5����ͨ���������ļ��ȶ�
    private static String getinfo(){
        String str=null;
        BufferedReader bufferedReader;
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[] { "dmidecode", "-t", "1"});
            process.getOutputStream().close();
            bufferedReader = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            String line = null;
            while ((line=bufferedReader.readLine())!=null){
                if (line.contains("Serial Number")) {
                    str=line;
                }
            }
            str=str.split(":")[1].trim();
            //MD5����
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] data=md5.digest(str.getBytes("UTF-8"));
            str= bytesToHexString(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
    
  //������ת��Ϊ�ַ���
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
}
