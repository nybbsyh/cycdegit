package cn.App.service.BackendUserService;

import cn.App.dao.backendUser.BackUserMapper;
import cn.App.entity.backendUser;
import cn.App.entity.info;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("backUserService")
public class BackUserServiceImpl implements BackUserService{
    @Autowired
    private BackUserMapper backUserMapper;
    @Override
    public backendUser login(String code) {
        return backUserMapper.login(code);
    }

    @Override
    public boolean updateStatus(info ins) {
        return backUserMapper.updateStatus(ins)>0;
    }
}
