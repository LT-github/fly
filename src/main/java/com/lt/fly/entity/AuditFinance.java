package com.lt.fly.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
    *     充值财务记录
 * @author Administrator
 *
 */

@Entity
@Table(name = "t_audit_finance")
@DiscriminatorValue("AuditFinance")
@Setter
@Getter
public class AuditFinance extends Finance{


}
