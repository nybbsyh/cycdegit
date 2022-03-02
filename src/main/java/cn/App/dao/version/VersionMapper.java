package cn.App.dao.version;

import cn.App.entity.info;
import cn.App.entity.version;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface VersionMapper {
    /*查看版本信息*/
    List<version> findmassion(Integer id);
    /*新增版本信息*/
    Integer addVersion(version vers);
    /*查看最新版本*/
    List<version> findVersion(Integer aid);
    /*查看最新版本细节*/
    version findbanben(@Param("aid") Integer aid,@Param("vid") Integer vid);
    /*修改最新版本*/
    Integer updateBanBen(version vers);
    /*删除*/
    Integer Deleat(Integer id);
    /*删除info*/
    Integer DeleteVersion(Integer id);
    /*删除安装包*/
    Integer UpdateApk(version vers);

}

