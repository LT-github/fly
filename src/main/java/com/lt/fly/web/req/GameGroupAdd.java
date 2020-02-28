package com.lt.fly.web.req;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class GameGroupAdd {

    @NotNull(message = "名称不能为空")
    private String name;			 //玩法组名称

    @NotNull(message = "描述不能为空")
    private String description;			//玩法描述

    @NotNull(message = "彩种不能为空")
    private String type; 			//彩种

    @NotNull(message = "玩法拼音不能为空")
    private String pingYinName;	//玩法的拼音

}
