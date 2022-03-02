package cn.App.service.DevUserService;

import cn.App.dao.DevUser.DevUserMapper;
import cn.App.entity.devUser;
import cn.App.entity.info;
import cn.App.entity.version;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("devUserService")
public class DevUserServiceImpl implements DevUserService{
    @Autowired
    private DevUserMapper devUserMapper;
    @Override
    public devUser selsectDevUserByCode(String devCode) {
        return devUserMapper.selsectDevUserByCode(devCode);
    }

    @Override
    public List<info> selectListInfo( Integer categoryLevel3,Integer categoryLevel2,Integer categoryLevel1,String softwareName,Integer flatformId,Integer statusName,Integer pageIndex, Integer pageSize) {
        return devUserMapper.selectListInfo(categoryLevel3,categoryLevel2,categoryLevel1,softwareName,flatformId,statusName,pageSize*(pageIndex-1),pageSize);
    }

    @Override
    public Integer getcount(Integer categoryLevel3, Integer categoryLevel2,Integer categoryLevel1,String softwareName,Integer flatformId,Integer statusName) {
        return devUserMapper.getcount(categoryLevel3, categoryLevel2,categoryLevel1,softwareName,flatformId,statusName);
    }

    @Override
    public Integer addSoftware(info infos) {
        return devUserMapper.addSoftware(infos);
    }

    @Override
    public info findApk(String APKName) {
        return devUserMapper.findApk(APKName);
    }

    @Override
    public info selectinfoid(Integer id) {
        return devUserMapper.selectinfoid(id);
    }

    @Override
    public Integer updateinfoid(info infos) {
        return devUserMapper.updateinfoid(infos);
    }

    @Override
    public Integer UpdateappId(Integer id) {
        return devUserMapper.UpdateappId(id);
    }

    @Override
    public Integer modifystate(Integer id,Integer status) {
        return devUserMapper.modifystate(id,status);
    }

    @Override
    public info findstatus(Integer id) {
        return devUserMapper.findstatus(id);
    }

    @Override
    public Integer Updateimg(info infos) {
        return devUserMapper.Updateimg(infos);
    }
}
