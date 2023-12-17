package main.java.org.example;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: hdd
 * @Date: ${DATE} - ${TIME}
 * @Description: org.example
 * @version: 1.0
 *
 * 移动文件到新的文件夹，并且以年月命名
 * 自己主要是用来分类图片
 */
public class MoveFile {

    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
    public static void main(String[] args) {
        System.out.println("Hello world!");

        // 源文件夹路径
        String sourceFolderPath = "C:\\Users\\HZ\\Desktop\\新建文件夹";
        // 目标文件夹路径
        String targetFolderPath = "F:\\完完整整分类好的\\04-照片日期分类";

        // 指定的文件类型
        // String[] fileType = {".docx", ".pdf", ".txt",".doc",".xlsx"};
        // String[] fileType = {".mp3"};
        String[] fileType = {".jpg", ".mp4", ".png" ,".jpeg",".JPG",".NEF",".bmp",".gif",".MP4",".mpg",".MOV",".AAE",".avi",".TS",".PNG","mp3"};


        File folder = new File(sourceFolderPath);
        if (folder.exists()) {
            List<File> fileList = searchFiles(folder, fileType);
            System.out.println("需要移动的文件数量 - "+fileList.size());
            int i = 0;
            for (File file : fileList) {
                String modifiedTime = sdf.format(new Date(file.lastModified()));
                System.out.println(file.getAbsolutePath() + " - " + modifiedTime);
                System.out.println("移动的文件排序----"+(++i));
                moveFile(file, targetFolderPath,sdf2.format(new Date(file.lastModified())));

            }
        } else {
            System.out.println("源数据文件夹不存在！");
        }
    }


    /**
     * 移动文件到新的文件夹，会根据日期新建分类
     * @param file 文件名称
     * @param targetFolderPath 目标文件夹路径
     * @param format 新建的文件夹名称
     */
    private static void moveFile(File file, String targetFolderPath, String format) {
        String targetFileName = file.getName();
        // 新建文件夹名称
        String targetFolderName = format;
        File targetFolder = new File(targetFolderPath, targetFolderName);

        try {
            if (!targetFolder.exists()) {
                // 创建新的目标文件夹（mkdirs可以成功不加s不行）因为是多级
                boolean created=targetFolder.mkdirs();
                //判断一下是否创建成功
                if (created) {
                    System.out.println("文件夹创建成功");
                } else {
                    System.out.println("文件夹创建失败");
                }
            }

            Path sourcePath = file.toPath();
            Path targetPath = generateUniqueFileName(targetFolder.toPath(), targetFileName);
            Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("已将文件移动到目标文件夹：" + targetPath);
        } catch (IOException e) {
            System.out.println("移动文件时出现错误：" + e.getMessage());
        }
    }

    /**
     * 生成唯一的文件名，如果目标文件夹中已存在同名文件，则在文件名后面添加一个下划线和计数器
     * @param targetFolderPath 文件夹路径
     * @param targetFileName 文件夹名称
     * @return
     */
    private static Path generateUniqueFileName(Path targetFolderPath, String targetFileName) {
        int counter = 1;
        String fileName = targetFileName;
        String fileExtension = "";
        int extensionIndex = targetFileName.lastIndexOf('.');
        if (extensionIndex != -1) {
            fileName = targetFileName.substring(0, extensionIndex);
            fileExtension = targetFileName.substring(extensionIndex);
        }

        Path filePath = targetFolderPath.resolve(targetFileName);
        while (Files.exists(filePath)) {
            targetFileName = fileName + "_" + counter + fileExtension;
            filePath = targetFolderPath.resolve(targetFileName);
            counter++;
        }

        return filePath;
    }

    /**
     * 筛选满足格式的文件类型
     * @param folder 源文件夹地址
     * @param fileTypes 文件类型
     *
     * @return
     */
    private static List<File> searchFiles(File folder, String[] fileTypes) {
        List<File> fileList = new ArrayList<>();

        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // 递归调用，搜索子文件夹中的符合条件的文件
                    fileList.addAll(searchFiles(file, fileTypes));
                } else {

                    //多种类判断
                    for (String fileType : fileTypes) {
                        if (file.getName().endsWith(fileType)) {
                            fileList.add(file);
                        }
                    }
                }
            }
        }
        return fileList;
    }
}