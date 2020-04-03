package com.lt.fly.web.vo;

import com.lt.fly.entity.SysLog;
import com.lt.fly.utils.MyBeanUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class LogVo {

    private Long id;

    private String username;

    private String userAction;

    private Long createTime;

    public LogVo() {
    }

    public LogVo(SysLog obj) {
        MyBeanUtils.copyProperties(obj,this);
        this.username = obj.getCreateUser().getNickname();
    }

    public static List<LogVo> tovo(List<SysLog> sysLogs){
        List<LogVo> logVos = new ArrayList<>();
        sysLogs.forEach(sysLog -> {
            logVos.add(new LogVo(sysLog));
        });
        return logVos;
    }
}
