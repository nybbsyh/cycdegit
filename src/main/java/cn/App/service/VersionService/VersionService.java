package cn.App.service.VersionService;

import cn.App.entity.version;

import java.util.List;

public interface VersionService {
    /*查看版本信息*/
    List<version> findmassion(Integer id);
    /*新增版本信息*/
    Integer addVersion(version vers);
    /*查看最新版本*/
    List<version> findVersion(Integer vid);
    /*查看最新版本细节*/
    version findbanben(Integer aid,Integer vid);
    /*修改最新版本*/
    Integer updateBanBen(version vers);
    /*删除*/
    boolean Deleat(Integer id);
    /*删除info*/
    Integer DeleteVersion(Integer id);
    /*删除安装包*/
    Integer UpdateApk(version vers);
}
