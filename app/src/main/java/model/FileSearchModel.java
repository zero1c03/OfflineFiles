package model;

import java.io.File;
import java.util.Date;

import static utils.SimpleUtils.formatCalculatedSize;

/**
 * Created by Weber on 2016/9/30.
 */

public class FileSearchModel {
    File fileName;
    String fileType;
    Long fileSize;
    Date fileDate;
    Date fileTime;

    public File getFileName() {
        return fileName;
    }

    public void setFileName(File fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Date getFileDate() {
        return fileDate;
    }

    public void setFileDate(Date fileDate) {
        this.fileDate = fileDate;
    }

    public Date getFileTime() {
        return fileTime;
    }

    public void setFileTime(Date fileTime) {
        this.fileTime = fileTime;
    }
}

