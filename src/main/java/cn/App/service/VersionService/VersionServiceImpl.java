package cn.App.service.VersionService;

import cn.App.dao.version.VersionMapper;
import cn.App.entity.version;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service(" versionService")
public   class VersionServiceImpl implements VersionService {
    @Autowired
    private VersionMapper versionMapper;
    @Override
    public List<version> findmassion(Integer id) {
        return versionMapper.findmassion(id);
    }

    @Override
    public Integer addVersion(version vers) {
        return versionMapper.addVersion(vers);
    }

    @Override
    public List<version> findVersion( Integer vid) {
        return versionMapper.findVersion(vid);
    }

    @Override
    public version findbanben(Integer aid, Integer vid) {
        return versionMapper.findbanben(aid,vid);
    }

    @Override
    public Integer updateBanBen(version vers) {
        return versionMapper.updateBanBen(vers);
    }

    @Override
    public boolean Deleat(Integer id) {
        return versionMapper.Deleat(id)>0;
    }

    @Override
    public Integer DeleteVersion(Integer id) {
        return versionMapper.DeleteVersion(id);
    }

    @Override
    public Integer UpdateApk(version vers) {
        return  versionMapper.UpdateApk(vers);
    }

}
