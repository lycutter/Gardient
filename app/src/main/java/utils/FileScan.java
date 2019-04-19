package utils;

import entity.DirectoryInfo;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileScan {

    /**
     * 根据提供的String类型的路径信息返回所含的子文件夹信息，父文件夹信息等
     */
    public DirectoryInfo getFileDirectory(String filePathString) {
        DirectoryInfo directoryInfo = new DirectoryInfo();
        directoryInfo.directoryName = new ArrayList<String>();
        directoryInfo.childDirectoryContain = new ArrayList<String>();
        directoryInfo.currentDirectory = filePathString;


        if (directoryInfo.currentDirectory.equals("/sdcard")) {
        } else {
            String path = directoryInfo.currentDirectory;
            directoryInfo.fatherDirectory = (new File(path)).getParent();
        }

        getFileList(new File(filePathString), directoryInfo);
        return directoryInfo;
    }

    private void getFileList(File currentFile, DirectoryInfo directoryInfo) {
        if (currentFile.isDirectory()) {
            File[] childFileGroup = currentFile.listFiles();   //当前目录包含的所有文件
            if (childFileGroup == null)  //权限判断
                return;
            for (int i = 0; i < childFileGroup.length; i++) {
                String childFileString = childFileGroup[i].getAbsolutePath();   //获取子文件夹中单个文件
                File childFile = new File(childFileString);
                String childFileName = childFileString.substring(childFileString.lastIndexOf("/") + 1); //从路径中截取名字
                if (!childFileName.startsWith(".") && childFile.isDirectory() && (childFile != null)) { //如果是目录
                    File[] childChildfiles = childFile.listFiles();
                    directoryInfo.directoryName.add(childFileName);
                    directoryInfo.childDirectoryContain.add("(" + childChildfiles.length + ")");
                } else if (!childFileName.startsWith(".") && childFile.isFile() && (childFile != null)) { //如果是文件
                    directoryInfo.directoryName.add(childFileName);
                    directoryInfo.childDirectoryContain.add("");
                }

            }
        }
    }
}
