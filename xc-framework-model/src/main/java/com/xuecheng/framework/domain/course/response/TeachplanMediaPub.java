package com.xuecheng.framework.domain.course.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/18
 * @Description:com.xuecheng.framework.domain.course.response
 * @version:1.0
 */
@Data
@ToString
@NoArgsConstructor
@Entity
@Table(name="teachplan_media_pub")
@GenericGenerator(name="jpa-assigned" ,strategy = "assigned")
public class TeachplanMediaPub implements Serializable
{
    @Id
    @GeneratedValue(generator = "jpa-assigned")
    @Column(name="teachplan_id")
    private String teachplanId;
    
    @Column(name="media_id")
    private String mediaId;
    
    @Column(name="media_fileoriginalname")
    private String mediaFileOriginalName;
    
    @Column(name="media_url")
    private String mediaUrl;
    
    @Column(name="courseid")
    private String courseId;
    
    @Column(name="timestamp")
    private Date timestamp;
}
