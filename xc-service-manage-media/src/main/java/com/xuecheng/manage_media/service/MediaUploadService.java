package com.xuecheng.manage_media.service;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.domain.media.response.MediaCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.config.RabbitMQConfig;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/15
 * @Description:com.xuecheng.manage_media.service
 * @version:1.0
 */
@Service
public class MediaUploadService
{

    @Value("${xc-service-manage-media.mq.routingkey-media-video}")
    private String routingkey_media_video;

    @Value("${xc-service-manage-media.upload-location}")
    private String uploadPath;

    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 上传文件注册
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    public ResponseResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt)
    {
        String filePath = getFilePath(fileMd5, fileExt);
        File file = new File(filePath);

        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(fileMd5);
        if(file.exists()&&mediaFileOptional.isPresent())
        {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_EXIST);
        }

        boolean fileFolder = createFileFolder(fileMd5);
        if(!fileFolder)
        {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 分块检查
     * @param fileMd5
     * @param chunk
     * @param chunkSize
     * @return
     */
    public CheckChunkResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize)
    {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath+chunk);
        if(file.exists())
        {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, true);
        }
        else {
            return new CheckChunkResult(MediaCode.CHUNK_FILE_EXIST_CHECK, false);
        }
    }

    /**
     * 上传分块
     * @param file
     * @param chunk
     * @param fileMd5
     * @return
     */
    public ResponseResult uploadChunk(MultipartFile file, Integer chunk, String fileMd5)
    {
        if(file==null)
        {
            ExceptionCast.cast(MediaCode.UPLOAD_FILE_REGISTER_FAIL);
        }
        createChunkFileFolder(fileMd5);
        File chunkFile = new File(getChunkFileFolderPath(fileMd5) + chunk);
        InputStream in=null;
        OutputStream out=null;
        try
        {
            in=file.getInputStream();
            out = new FileOutputStream(chunkFile);
            IOUtils.copy(in, out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                out.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            try
            {
                in.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 合并分块
     * @param fileMd5
     * @param fileName
     * @param fileSize
     * @param mimetype
     * @param fileExt
     * @return
     */
    public ResponseResult mergeChunk(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt)
    {
        //获取块文件的路径
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath);
        if(!file.exists())
        {
            file.mkdirs();
        }

        //合并文件的路径
        String mergefilePath = getFilePath(fileMd5, fileExt);
        File mergeFile = new File(mergefilePath);
        //创建合并文件  如果存在则先删除
        if(mergeFile.exists())
            mergeFile.delete();
        try
        {
            mergeFile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //获取块文件列表(排好序)
        List<File> chunkFileList = getChunkFileList(file);

        //合并文件
        mergeFile= mergeFiles(mergeFile, chunkFileList);

        //校验文件
        boolean chechFileMd5 = chechFileMd5(fileMd5, mergeFile);
        if(!chechFileMd5)
            ExceptionCast.cast(MediaCode.MERGE_FILE_CHECKFAIL);

        //将文件信息保存到数据库
        MediaFile mediaFile = new MediaFile();
        mediaFile.setFileId(fileMd5);
        mediaFile.setFileName(fileMd5+"."+fileExt);
        mediaFile.setFileOriginalName(fileName);
        //文件路径保存相对路径
        mediaFile.setFilePath(getFileFolderRelativePath(fileMd5,fileExt));
        mediaFile.setFileSize(fileSize);
        mediaFile.setUploadTime(new Date());
        mediaFile.setMimeType(mimetype);
        mediaFile.setFileType(fileExt);
        //状态为上传成功
        mediaFile.setFileStatus("301002");
        MediaFile save = mediaFileRepository.save(mediaFile);

        //发送消息到消息队列处理文件
        HashMap<String, String> map = new HashMap<>();
        map.put("mediaId", fileMd5);
        String msg = JSON.toJSONString(map);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EX_MEDIA_PROCESSTASK,routingkey_media_video,msg);

        return new ResponseResult(CommonCode.SUCCESS);
    }


    /**
     * 根据fileMd5得到文件的路径
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getFilePath(String fileMd5,String fileExt)
    {
        return uploadPath+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/"+fileMd5+"."+fileExt;
    }

    /**
     * 得到文件目录的相对路径
     * @param fileMd5
     * @param fileExt
     * @return
     */
    private String getFileFolderRelativePath(String fileMd5,String fileExt)
    {
        return fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
    }

    /**
     * 得到文件所在目录
     * @param fileMd5
     * @return
     */
    private String getFileFolderPath(String fileMd5)
    {
        return uploadPath+fileMd5.substring(0,1)+"/"+fileMd5.substring(1,2)+"/"+fileMd5+"/";
    }

    /**
     * 创建文件目录
     * @param fileMd5
     * @return
     */
    private boolean createFileFolder(String fileMd5)
    {
        String fileFolderPath = getFileFolderPath(fileMd5);
        File file = new File(fileFolderPath);
        if(!file.exists())
        {
            boolean mkdirs = file.mkdirs();
            return mkdirs;
        }
        return true;
    }

    /***
     * 得到分块文件的存储目录
     * @param fileMd5
     * @return
     */
    private String getChunkFileFolderPath(String fileMd5)
    {
        return getFileFolderPath(fileMd5) +"/" + "chunks" + "/";
    }

    /**
     * 创建分块文件的目录
     * @param fileMd5
     * @return
     */
    private boolean createChunkFileFolder(String fileMd5)
    {
        String chunkFileFolderPath = getChunkFileFolderPath(fileMd5);
        File file = new File(chunkFileFolderPath);
        if(!file.exists())
        {
           return  file.mkdirs();
        }
        return true;
    }

    /**
     * 获取块文件的列表
     * @param chunkFile
     */
    private List<File> getChunkFileList(File chunkFile)
    {
        File[] files = chunkFile.listFiles();
        List<File> list = Arrays.asList(files);

        Collections.sort(list, new Comparator<File>()
        {
            @Override
            public int compare(File o1, File o2)
            {
                if(Integer.parseInt(o1.getName())>Integer.parseInt(o2.getName()))
                    return 1;
                return -1;
            }
        });
        return list;
    }

    /**
     * 合并文件
     * @param mergeFile
     * @param chunkFileList
     */
    private File mergeFiles(File mergeFile, List<File> chunkFileList)
    {
        FileOutputStream outputStream=null;
        try
        {
            outputStream = new FileOutputStream(mergeFile);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        byte[] bytes = new byte[1024];
        int len=0;
        for(File file:chunkFileList)
        {
            InputStream inputStream=null;
            try
            {
                inputStream=new FileInputStream(file);

                while ((len=inputStream.read(bytes))!=-1)
                {
                    outputStream.write(bytes,0,len);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }finally
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        try
        {
            outputStream.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return mergeFile;
    }

    /**
     * 校验文件
     * @param fileMd5
     * @param mergeFile
     * @return
     */
    private boolean chechFileMd5(String fileMd5, File mergeFile)
    {
        FileInputStream fileInputStream=null;
        try
        {
            fileInputStream = new FileInputStream(mergeFile);

            //得到文件的md5串
            String md5 = DigestUtils.md5DigestAsHex(fileInputStream);
            if(md5.equalsIgnoreCase(fileMd5))
                return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileInputStream.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
}
