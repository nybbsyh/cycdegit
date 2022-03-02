package cn.App.service.DevUserService;

import cn.App.entity.devUser;
import cn.App.entity.info;
import cn.App.entity.version;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DevUserService {
    /*登录*/
    devUser selsectDevUserByCode(@Param("devCode") String devCode);
    /*查看所有软件*/
    List<info> selectListInfo( Integer categoryLevel3, Integer categoryLevel2,Integer categoryLevel1,String softwareName,Integer flatformId,Integer statusName ,Integer begin , Integer pageSize);
    /*查看所有记录数*/
    Integer getcount(Integer categoryLevel3, Integer categoryLevel2,Integer categoryLevel1,String softwareName,Integer flatformId,Integer statusName);
    /*新增*/
    Integer addSoftware(info infos);
    /*判断安装包是否存在*//*SELECT * FROM app_info WHERE APKName='com.momocorp.o2jamu'*/
    info findApk(String APKName);
    /*修改之前的查看*/
    info selectinfoid(Integer id);
    /*修改*/
    Integer updateinfoid(info infos);
    /*更新*/
    Integer UpdateappId(Integer id);
    /*上架下架*/
    Integer modifystate(Integer id,Integer status);
    /*查询状态信息*/
    info findstatus(Integer id);
    /*删除图片*/
    Integer Updateimg(info infos);



}
