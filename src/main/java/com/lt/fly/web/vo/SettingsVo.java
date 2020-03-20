package com.lt.fly.web.vo;

import com.lt.fly.entity.Settings;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class SettingsVo {
    private Long id;
    private String dataKey;
    private String dataValue;
    private Long createTime;
    private String createUsername;
    private Long modifyTime;
    private String modifyUsername;


    public SettingsVo(Settings obj) {
        BeanUtils.copyProperties(obj,this);
        this.createUsername = obj.getCreateUser().getUsername();
        this.modifyUsername = obj.getModifyUser().getUsername();
    }

    public static List<SettingsVo> tovo (List<Settings> settings){
        List<SettingsVo> list = new ArrayList<>();
        for (Settings item :
                settings) {
            list.add(new SettingsVo(item));
        }
        return list;
    }
}
