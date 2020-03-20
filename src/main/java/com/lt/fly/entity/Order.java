package com.lt.fly.entity;


import com.lt.fly.utils.GlobalConstant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "t_order")
@Setter
@Getter
public class Order extends BasicEntity{

	//彩种
	@Column(name = "lottery_type")
	private String lotteryType;

	// 下单的期号
	@Column(name = "issue_number")
	private Long issueNumber;

	// 结果类型 0 未开奖 1已开奖
	@Column(name = "result_type")
	private Integer resultType;

	// 0未结算  1已结算 2已撤销
	@Column(name = "status")
	private Integer status = GlobalConstant.OrderStatus.NOTClEARING.getCode();

	// 玩法下注内容
	@Column(name = "bets_content")
	private String betsContent;

	//单注金额
	@Column(name = "single_betting")
	private Double singleBetting;
	
	// 总点
	@Column(name = "total_money")
	private Double totalMoney;

	// 开奖结果 0=输 1=赢
	@Column(name = "lottery_result")
	private Integer lotteryResult;

	//输赢的金额
	@Column(name = "battle_result")
	private Double battleResult;
	
	//中奖明细
	@Column(name = "exchange_detail")
	private String exchangeDetail;
	
	//下注的注数
	@Column(name = "bets_count")
	private Integer betsCount;
	
	// 关联下注组
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="bet_group_id")
	private BetGroup betGroup;

	//这个注单的赔率
	@Column
	private Double betOdd;

	//某些玩法注单特殊的赔率
	@Column
	private String specificOdd;
	
}
