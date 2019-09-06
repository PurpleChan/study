package com.xuecheng.manage_media_process.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.MediaFileProcess_m3u8;
import com.xuecheng.framework.utils.HlsVideoUtil;
import com.xuecheng.framework.utils.Mp4VideoUtil;
import com.xuecheng.manage_media_process.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/16
 * @Description:com.xuecheng.manage_media_process.mq
 * @version:1.0
 */
@Component
public class MediaProcessTask
{
    @Autowired
    private MediaFileRepository mediaFileRepository;

    @Value("${xc-service-manage-media.video-location}")
    private String fileDirectoryPath;

    @Value("${xc-service-manage-media.ffmpeg-path}")
    private String ffmpeg;

    @RabbitListener(queues ={"${xc-service-manage-media.mq.queue-media-video-processor}"} ,containerFactory = "customContainerFactory")
    public void receiveMediaProcessTask(String msg)
    {
        //解析消息
        Map map = JSON.parseObject(msg, Map.class);
        String mediaId = (String)map.get("mediaId");

        //查询数据库得到文件信息
        Optional<MediaFile> mediaFileOptional = mediaFileRepository.findById(mediaId);
        if(!mediaFileOptional.isPresent())
            return;
        MediaFile mediaFile = mediaFileOptional.get();

        //不处理非avi的文件
        if(!StringUtils.equals(mediaFile.getFileType(),"avi"))
        {
            mediaFile.setProcessStatus("303004");
            mediaFileRepository.save(mediaFile);
            return;
        }
        else {
            mediaFile.setProcessStatus("303001");
            mediaFileRepository.save(mediaFile);
        }

        //生成mp4文件
        String filePath =fileDirectoryPath+ mediaFile.getFilePath()+mediaFile.getFileName();
        Mp4VideoUtil mp4VideoUtil= new Mp4VideoUtil(ffmpeg, filePath, mediaFile.getFileId() + ".mp4", fileDirectoryPath + mediaFile.getFilePath());
        String mp4Result = mp4VideoUtil.generateMp4();
        if(StringUtils.isEmpty(mp4Result)||!StringUtils.equals(mp4Result,"success"))
        {
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(mp4Result);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }


        //生成m3u8文件
        HlsVideoUtil hlsVideoUtil = new HlsVideoUtil(ffmpeg, fileDirectoryPath + mediaFile.getFilePath() + mediaFile.getFileId() + ".mp4", mediaFile.getFileId() + ".m3u8", fileDirectoryPath + mediaFile.getFilePath() + "hls/");
        String hlsResult = hlsVideoUtil.generateM3u8();
        if(StringUtils.isEmpty(hlsResult)||!StringUtils.equals(hlsResult,"success"))
        {
            mediaFile.setProcessStatus("303003");
            MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
            mediaFileProcess_m3u8.setErrormsg(hlsResult);
            mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
            mediaFileRepository.save(mediaFile);
            return;
        }
        List<String> ts_list = hlsVideoUtil.get_ts_list();
        MediaFileProcess_m3u8 mediaFileProcess_m3u8 = new MediaFileProcess_m3u8();
        mediaFile.setProcessStatus("303002");
        mediaFileProcess_m3u8.setErrormsg(hlsResult);
        mediaFileProcess_m3u8.setTslist(ts_list);
        mediaFile.setMediaFileProcess_m3u8(mediaFileProcess_m3u8);
        mediaFile.setFileUrl(mediaFile.getFilePath()+"hls/"+mediaFile.getFileId()+".m3u8");
        mediaFileRepository.save(mediaFile);
    }
}
