package com.example.demo.ref;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author zhuwei
 * @Date 2020/7/30 9:51
 * @Description: 通过文件编辑器示例展示软引用的方式方式
 */
public class FileEditor {
    private static class FileData {
        private Path filePath;
        private SoftReference<byte[]> dataRef;

        public FileData(Path filePath) {
            this.filePath=filePath;
            this.dataRef=new SoftReference<byte[]>(new byte[0]);
        }
        public Path getPath() {
            return filePath;
        }

        public byte[] getData() throws IOException {
            byte[] dataArray = dataRef.get();
            if(dataArray == null || dataArray.length == 0) {
                dataArray = readFile();
                dataRef = new SoftReference<byte[]>(dataArray);
                dataArray = null;
            }
            return dataRef.get();
        }

        private byte[] readFile() throws IOException {
            return Files.readAllBytes(filePath);
        }
    }

    private FileData currentFileData;
    private Map<Path,FileData> openedFiles = new HashMap<>();

    public void switchTo(String filePath) {
        Path path = Paths.get(filePath).toAbsolutePath();
        if(openedFiles.containsKey(path)) {
            currentFileData = openedFiles.get(path);
        } else {
            currentFileData = new FileData(path);
            openedFiles.put(path,currentFileData);
        }
    }

    public void useFile() throws IOException {
        if(currentFileData != null) {
            System.out.println(String.format("",currentFileData.getPath(),currentFileData.getData().length));
        }
    }

    public static void main(String[] args) {
        FileEditor fe = new FileEditor();
        try {
            for(int i=0;i<100;i++) {
                fe.switchTo("src");
                fe.useFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
