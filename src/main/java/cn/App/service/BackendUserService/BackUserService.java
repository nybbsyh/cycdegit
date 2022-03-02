package cn.App.service.BackendUserService;

import cn.App.entity.backendUser;
import cn.App.entity.info;

public interface BackUserService {
    /*登录*/
    backendUser login(String code);
    /*审核通过状态改变*/
    boolean updateStatus(info ins);
}
