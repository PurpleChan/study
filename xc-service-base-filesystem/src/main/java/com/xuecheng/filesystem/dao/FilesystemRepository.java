package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Auther:http://www.chenzi.com
 * @Date:2019/8/1
 * @Description:com.xuecheng.filesystem.dao
 * @version:1.0
 */
public interface FilesystemRepository extends MongoRepository<FileSystem,String>
{

}
