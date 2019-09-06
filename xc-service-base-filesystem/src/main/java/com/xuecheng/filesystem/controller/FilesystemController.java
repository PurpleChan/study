package com.xuecheng.filesystem.controller;

import com.xuecheng.filesystem.service.FilesystemService;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/1
 * @Description:com.xuecheng.filesystem.controller
 * @version:1.0
 */
@RestController
public class FilesystemController
{
    @Autowired
    private FilesystemService filesystemService;

    @PostMapping("/filesystem/upload")
    public UploadFileResult upload(@RequestParam("file") MultipartFile multipartFile, String filetag, String businesskey, String metadata)
    {
        return filesystemService.upload(multipartFile,filetag,businesskey,metadata);
    }
}
