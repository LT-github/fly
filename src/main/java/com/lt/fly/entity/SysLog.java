package com.lt.fly.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_sys_Log")
public class SysLog extends BasicEntity{

    @Column
    private String userAction;
}
