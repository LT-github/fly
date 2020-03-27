package com.lt.fly.entity;

import com.lt.fly.utils.IdWorker;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * 信息传输.非对称
 */
@Data
@Entity
@Table(name = "t_security")
public class Security extends BasicEntity{

    @Column(length = 2000)
    private String publicKey;

    @Column(length = 2000)
    private String privateKey;

}
