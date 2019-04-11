package com.zhixin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *功能:实现对文件监控
 * 参数1:指定配置文件的路径
 * @
 */
public class FileWatchService {
    public static void main(String[] args) throws Exception {
        //获取监控文件路径
        File file1=new File(readProporty(args[0],2).split(":")[1]);
        Path path= file1.toPath();
        //指定保护文件路径
        String path2=readProporty(args[0],1).split(":")[1];
        //检查文件路径
        if (!cherkFile(file1)||!cherkFile(new File(path2))){
            throw new IOException("该配置文件中文件或文件夹不存在");
        }
        //核对该代码运行的机器是否是指定机器.
        if(readProporty(args[0],0).equals(getCPUinfo())){
            throw new Exception("CPU信息与配置文件不符");
        }
        try {
            //获取文件属性
            BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class);
            //当发现文件被修改删除当前的项目文件
            if(!attr.creationTime().equals(attr.lastModifiedTime())){
                    //对保护文件删除
                delFolder(path2);
            }
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //检查文件路径
    public static boolean cherkFile(File file){
        boolean flag=false;
        if (!file.exists()) {
            return flag;
        }
        return true;
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

    //获取CPU序列号信息
    private static String getCPUinfo(){
        String str=null;
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[] { "wmic", "cpu", "get", "ProcessorId" });
            process.getOutputStream().close();
            Scanner sc = new Scanner(process.getInputStream());
            String serial = sc.next();
            //使用MD5加密
            MessageDigest md5=MessageDigest.getInstance("MD5");
            byte[] data=md5.digest(serial.getBytes("UTF-8"));
            str= bytesToHexString(data);
        } catch (Exception e){
            e.printStackTrace();
        }
        return str;
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

    //实现MD5加密后字符数组的转换为字32位的字符串
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
