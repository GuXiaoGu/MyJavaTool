package main.java.org.example;

import java.io.*;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: hdd
 * @Date: 2023-7-12 - 14:13
 * @Description: main.java.org.example
 * @version: 1.0
 * 删除空白文件夹
 */

public class DeleteFolder {
    private static int deletedFoldersCount = 0;


    public static void main(String[] args) {

        String folderPath = "E:\\Recovery_20231021_161504";


        deleteEmptyFolders(folderPath);

        System.out.println("总共删除了 " + deletedFoldersCount + " 个文件夹");
    }

    /**
     * @param folderPath
     */
    public static void deleteEmptyFolders(String folderPath) {
        File folder = new File(folderPath);

        // 检查文件夹是否存在
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("指定的路径不是文件夹");
            return;
        }

        // 重置计数器
        deletedFoldersCount = 0;

        // 创建日志文件
        String logDirectoryPath = System.getProperty("user.dir") + File.separator + "logs";
        File logFile = createLogFile(logDirectoryPath);

        // 递归遍历文件夹
        deleteEmptyFoldersRecursively(folder, logFile);

        // 输出删除文件夹的信息
        if (deletedFoldersCount > 0) {
            System.out.println("总共删除了 " + deletedFoldersCount + " 个文件夹");
        } else {
            System.out.println("没有删除任何文件夹");
        }
    }

    private static void deleteEmptyFoldersRecursively(File folder, File logFile) {
        if (!folder.isDirectory()) {
            return;
        }

        // 获取文件夹中的文件和子文件夹
        File[] files = folder.listFiles();

        // 如果文件夹为空，删除空白文件夹并增加计数器，并记录日志
        if (files != null && files.length == 0) {
            boolean isDeleted = folder.delete();
            if (isDeleted) {
                deletedFoldersCount++;
                logDeletedFolder(logFile, folder.getAbsolutePath());
            }
            return;
        }

        // 递归遍历子文件夹
        for (File file : files) {
            if (file.isDirectory()) {
                deleteEmptyFoldersRecursively(file, logFile);
            }
        }
    }

    /**
     * 创建日志文件，但是目前存在bug就是会覆盖掉当天的日志文件，有空了再改
     * @param logDirectoryPath
     * @return
     */
    private static File createLogFile(String logDirectoryPath) {
        // 检查日志目录是否存在，如果不存在则创建
        File logDirectory = new File(logDirectoryPath);
        if (!logDirectory.exists()) {
            logDirectory.mkdirs();
        }

        // 获取当前日期和时间作为日志文件名
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = "delete_log_" + format.format(date.getTime()) + ".txt";

        // 创建日志文件
        File logFile = new File(logDirectory, fileName);

        try {
            if (logFile.createNewFile()) {
                System.out.println("创建日志文件: " + logFile.getAbsolutePath());
            } else {
                System.out.println("无法创建日志文件: " + logFile.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("无法创建日志文件: " + e.getMessage());
        }

        return logFile;
    }

    private static void logDeletedFolder(File logFile, String folderPath) {
        try {
            // 使用文件输出流追加模式写入日志
            FileWriter writer = new FileWriter(logFile, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            // 记录删除的文件夹和时间戳
            bufferedWriter.write("被删除的文件夹: " + folderPath);
            bufferedWriter.newLine();
            bufferedWriter.write("删除时间: " + new Date());
            bufferedWriter.newLine();
            bufferedWriter.newLine();

            bufferedWriter.close();

            System.out.println("记录日志: " + folderPath);
        } catch (IOException e) {
            System.out.println("无法记录日志: " + e.getMessage());
        }
    }
}