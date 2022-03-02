package cn.App.dao.DevUser;

import cn.App.entity.devUser;
import cn.App.entity.info;
import cn.App.entity.version;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface DevUserMapper {
    /*登录*/
    devUser selsectDevUserByCode(@Param("devCode") String devCode);

    /*查看软件列表并分页*/
    List<info> selectListInfo(@Param("categoryLevel3") Integer categoryLevel3,@Param("categoryLevel2") Integer categoryLevel2,@Param("categoryLevel1") Integer categoryLevel1,@Param("softwareName") String softwareName,@Param("flatformId") Integer flatformId,@Param("statusName") Integer statusName,@Param("begin") Integer begin ,@Param("pageSize") Integer pageSize);

    /*查看所有记录数*/
    Integer getcount(@Param("categoryLevel3") Integer categoryLevel3,@Param("categoryLevel2") Integer categoryLevel2,@Param("categoryLevel1") Integer categoryLevel1,@Param("softwareName") String softwareName,@Param("flatformId") Integer flatformId,@Param("statusName") Integer statusName);
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
    /*查询状态信息*/
    info findstatus(Integer id);
    /*上架下架*/
    Integer modifystate(@Param("id") Integer id,@Param("status")Integer status);
    /*删除图片*/
    Integer Updateimg(info infos);


}
