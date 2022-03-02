package cn.App.dao.backendUser;

import cn.App.entity.backendUser;
import cn.App.entity.info;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BackUserMapper {
    /*登录*/
    backendUser login(String code);
    /*查看审核列表*/
    /*查看软件列表并分页*/
    List<info> selectListInfo(@Param("categoryLevel3") Integer categoryLevel3, @Param("categoryLevel2") Integer categoryLevel2, @Param("categoryLevel1") Integer categoryLevel1, @Param("softwareName") String softwareName, @Param("flatformId") Integer flatformId, @Param("begin") Integer begin , @Param("pageSize") Integer pageSize);
     /*审核通过状态改变*/
    Integer updateStatus(info ins);


}
