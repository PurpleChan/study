package com.xuecheng.filesystem.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.filesystem.dao.FilesystemRepository;
import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.FileSystemCode;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.csource.fastdfs.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/1
 * @Description:com.xuecheng.filesystem.service
 * @version:1.0
 */
@Service
public class FilesystemService
{
    @Value("${xuecheng.fastdfs.connect_timeout_in_seconds}")
    private int connect_timeout_in_seconds;

    @Value("${xuecheng.fastdfs.network_timeout_in_seconds}")
    private int network_timeout_in_seconds;

    @Value("${xuecheng.fastdfs.charset}")
    private String charset;

    @Value("${xuecheng.fastdfs.tracker_servers}")
    private String tracker_servers;

    @Autowired
    private FilesystemRepository filesystemRepository;

    //加载fastdfs的配置
    private void initFastdfs()
    {
        try
        {
            ClientGlobal.initByTrackers(tracker_servers);
            ClientGlobal.setG_connect_timeout(connect_timeout_in_seconds);
            ClientGlobal.setG_network_timeout(network_timeout_in_seconds);
            ClientGlobal.setG_charset(charset);
        }
        catch (Exception e)
        {
 //           e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
    }

    /**
     * 上传文件到fastdfs服务器
     * @param multipartFile
     * @return
     */
    private String dfsUpload(MultipartFile multipartFile)
    {
        try
        {
            initFastdfs();

            byte[] fileBytes = multipartFile.getBytes();
            String originalFilename = multipartFile.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf('.') + 1);

            //创建fastdfs所需的trackerclient   trackerservice  storageservice   storageclient
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer ts = trackerClient.getConnection();
            StorageServer ss = trackerClient.getStoreStorage(ts);
            StorageClient1 storageClient1 = new StorageClient1(ts, ss);

            //上传文件   得到文件id
            String fileId = storageClient1.upload_appender_file1(fileBytes, suffix, null);
            return fileId;
        }
        catch (Exception e)
        {
//            e.printStackTrace();
            ExceptionCast.cast(FileSystemCode.FS_UPLOADFILE_SERVERFAIL);
        }
        return null;
    }

    /**
     * 图片上传并保存到数据库
     * @param multipartFile
     * @param filetag
     * @param businesskey
     * @param metadata
     * @return
     */
    public UploadFileResult upload(MultipartFile multipartFile, String filetag, String businesskey, String metadata)
    {
        String fileId = dfsUpload(multipartFile);

        FileSystem fileSystem = new FileSystem();
        fileSystem.setFileId(fileId);
        fileSystem.setFilePath(fileId);
        fileSystem.setFileName(multipartFile.getOriginalFilename());
        fileSystem.setFileSize(multipartFile.getSize());
        fileSystem.setFileType(multipartFile.getContentType());
        fileSystem.setBusinesskey(businesskey);
        fileSystem.setFiletag(filetag);
        if(metadata!=null&&!metadata.equals(""))
        {
            Map metadata1 = JSON.parseObject(metadata, Map.class);
            fileSystem.setMetadata(metadata1);
        }

        filesystemRepository.save(fileSystem);

        return new UploadFileResult(CommonCode.SUCCESS, fileSystem);
    }

}
