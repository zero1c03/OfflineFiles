package model;

import java.io.File;
import java.util.Date;

/**
 * Created by Weber on 2016/9/30.
 */

public class FileSearchModel {
    File fileName;
    String fileType;
    Long fileSize;
    Date fileModifiedDate;
    Date fileCreationTime;

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

    public Date getFileModifiedDate() {
        return fileModifiedDate;
    }

    public void setFileModifiedDate(Date fileModifiedDate) {
        this.fileModifiedDate = fileModifiedDate;
    }

    public Date getFileCreationTime() {
        return fileCreationTime;
    }

    public void setFileCreationTime(Date fileCreationTime) {
        this.fileCreationTime = fileCreationTime;
    }
}

