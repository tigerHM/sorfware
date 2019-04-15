package com.zhixin;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.nio.file.StandardWatchEventKinds.*;
/**
 * 使用监控器监视文件改变
 * 实时监控
 * @
 */
public class FileWatchService2 {

    private WatchService watcher;

    public FileWatchService2(Path path)throws IOException {
        watcher = FileSystems.getDefault().newWatchService();
        path.register(watcher, ENTRY_CREATE,ENTRY_DELETE,ENTRY_MODIFY);
    }
    public void handleEvents(Path path) throws InterruptedException{
        while(true){
            WatchKey key = watcher.take();
            for(WatchEvent event : key.pollEvents()){
                WatchEvent.Kind kind = event.kind();

                if(kind == OVERFLOW){
                    continue;
                }

                WatchEvent e = (WatchEvent)event;
                Path context = (Path)e.context();
                String fileName = context.toString();

                String message = "";
                if(kind.name() == "ENTRY_CREATE")
                {
                    message = fileName + "-----> create";
                }else if(kind.name() == "ENTRY_DELETE" ){
                    message = fileName + "-----> delete";

                }else if(kind.name() == "ENTRY_MODIFY"){
                    message = fileName + "-----> modify";
                    delFolder(path.toString());
                }
                if (fileName.endsWith("swp")||fileName.endsWith("swpx"))
                {
                    continue;
                }else{
                    System.out.println(message);
                }
            }
            if(!key.reset()){
                break;
            }
        }
    }

    public static void main(String args[]) throws Exception{
        if(readProporty(args[0],0).equals(getinfo())){
            throw new Exception("配置信息与运行机器不符");
        }
        String watchDir = readProporty(args[0],2);
        String delDir=readProporty(args[0],1);
        System.out.println("Begin watching DIR: "+watchDir);
        new FileWatchService2(Paths.get(watchDir)).handleEvents(new File(watchDir).toPath());
    }

    //读取指定文件获取参数:ProcessorId,InstallPath,WatchPath
    private static String readProporty(String path,int i){
        List<String> list=new ArrayList<>();
        try {
            String st;
            FileReader reader =new FileReader(path);
            BufferedReader br = new BufferedReader(reader);
            while ((st=br.readLine())!=null){
                list.add(st);
            }
            br.close();
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }
        return list.get(i);
    }

    //删除指定文件夹
    public  static void delFolder(String path) {
        try {
            delAllFile(path); // 删除完里面所有内容
            String filePath = path;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //删除指定文件夹及文件下所有内容
    public static  void delAllFile(String filepath) throws IOException {
        File file = new File(filepath);
        String[] tempList = file.list();
        File temp;
        for (int i = 0; i < tempList.length; i++) {
            if (filepath.endsWith(File.separator)) {//File.separator文件分隔符
                temp = new File(filepath + tempList[i]);
            } else {
                temp = new File(filepath + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(filepath + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(filepath + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }
    //获取Serial Number进行识别运行机器:使用MD5加密通过与配置文件比对
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
            //MD5加密
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] data=md5.digest(str.getBytes("UTF-8"));
            str= bytesToHexString(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }

    //获取CPU序列号信息:window系统
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

    //将数组转换为字符串
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
