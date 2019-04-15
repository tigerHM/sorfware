// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   FileWatchService.java

package com.zhixin;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.*;

public class FileWatchService
{

	private WatchService watcher;

	public FileWatchService(Path path)
		throws IOException
	{
		watcher = FileSystems.getDefault().newWatchService();
		path.register(watcher, new java.nio.file.WatchEvent.Kind[] {
			StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY
		});
	}

	public void handleEvents(Path path)
		throws InterruptedException
	{
		WatchKey key;
		do
		{
			key = watcher.take();
			for (Iterator iterator = key.pollEvents().iterator(); iterator.hasNext();)
			{
				WatchEvent event = (WatchEvent)iterator.next();
				java.nio.file.WatchEvent.Kind kind = event.kind();
				if (kind != StandardWatchEventKinds.OVERFLOW)
				{
					WatchEvent e = event;
					Path context = (Path)e.context();
					String fileName = context.toString();
					String message = "";
					if (kind.name() == "ENTRY_CREATE")
					{
						message = (new StringBuilder(String.valueOf(fileName))).append("-----> create").toString();
						delFolder(path.toString());
						return;
					}
					if (kind.name() == "ENTRY_DELETE")
					{
						message = (new StringBuilder(String.valueOf(fileName))).append("-----> delete").toString();
						delFolder(path.toString());
						return;
					}
					if (kind.name() == "ENTRY_MODIFY")
					{
						message = (new StringBuilder(String.valueOf(fileName))).append("-----> modify").toString();
						delFolder(path.toString());
						return;
					}
					if (!fileName.endsWith("swp") && !fileName.endsWith("swpx"))
						System.out.println(message);
				}
			}

		} while (key.reset());
	}

	public static void main(String args[])
		throws Exception
	{
		if (!readProporty(args[0], 0).equals(getinfo()))
		{
			throw new Exception("配置信息与运行机器不符");
		} else
		{
			String watchDir = readProporty(args[0], 2);
			String delDir = readProporty(args[0], 1);
			System.out.println((new StringBuilder("Begin watching DIR: ")).append(watchDir).toString());
			(new FileWatchService(Paths.get(watchDir, new String[0]))).handleEvents((new File(delDir)).toPath());
			return;
		}
	}

	private static String readProporty(String path, int i)
	{
		List list = new ArrayList();
		try
		{
			FileReader reader = new FileReader(path);
			BufferedReader br = new BufferedReader(reader);
			String st;
			while ((st = br.readLine()) != null) 
				list.add(st);
			br.close();
			reader.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return (String)list.get(i);
	}

	public static void delFolder(String path)
	{
		try
		{
			delAllFile(path);
			String filePath = path;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			myFilePath.delete();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void delAllFile(String filepath)
		throws IOException
	{
		File file = new File(filepath);
		String tempList[] = file.list();
		for (int i = 0; i < tempList.length; i++)
		{
			File temp;
			if (filepath.endsWith(File.separator))
				temp = new File((new StringBuilder(String.valueOf(filepath))).append(tempList[i]).toString());
			else
				temp = new File((new StringBuilder(String.valueOf(filepath))).append(File.separator).append(tempList[i]).toString());
			if (temp.isFile())
				temp.delete();
			if (temp.isDirectory())
			{
				delAllFile((new StringBuilder(String.valueOf(filepath))).append("/").append(tempList[i]).toString());
				delFolder((new StringBuilder(String.valueOf(filepath))).append("/").append(tempList[i]).toString());
			}
		}

	}

	private static String getinfo()
	{
		String str = null;
		try
		{
			Process process = Runtime.getRuntime().exec(new String[] {
				"dmidecode", "-t", "1"
			});
			process.getOutputStream().close();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			for (String line = null; (line = bufferedReader.readLine()) != null;)
				if (line.contains("Serial Number"))
					str = line;

			str = str.split(":")[1].trim();
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte data[] = md5.digest(str.getBytes("UTF-8"));
			str = bytesToHexString(data);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return str;
	}

	public static String bytesToHexString(byte src[])
	{
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0)
			return null;
		for (int i = 0; i < src.length; i++)
		{
			int v = src[i] & 0xff;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2)
				stringBuilder.append(0);
			stringBuilder.append(hv);
		}

		return stringBuilder.toString();
	}
}