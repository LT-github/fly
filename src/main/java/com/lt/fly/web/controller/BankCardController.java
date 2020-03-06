package com.lt.fly.web.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lt.fly.annotation.RequiredPermission;
import com.lt.fly.annotation.UserLoginToken;
import com.lt.fly.dao.IBankCardRepository;
import com.lt.fly.entity.BankCard;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.CheckBankCardUtil;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.utils.ListFenUtils;
import com.lt.fly.utils.MyBeanUtils;
import com.lt.fly.utils.PagingList;
import com.lt.fly.web.req.BankCardAddReq;






@RestController
@RequestMapping("/bankCard")
public class BankCardController extends BaseController{

	@Autowired
	private IBankCardRepository bankCardRepository;

	@Autowired
	private IdWorker idWorker;

	//添加银行卡	
	//@RequiredPermission(value="addBank-add")
	@PostMapping("/addBankCard")
	@UserLoginToken
	public Object addBankCard(@RequestBody  BankCardAddReq req) throws ClientErrorException{

		if(req.getBank()==null)
			throw new ClientErrorException("请选择所属银行");
		if(req.getCard()==null)
			throw new ClientErrorException("请填写卡号");
		
		if(!CheckBankCardUtil.checkBankCard(req.getCard()))
			throw new ClientErrorException("银行卡格式错误！");
		
		BankCard bankCard=new BankCard();
		BeanUtils.copyProperties(req, bankCard);
		bankCard.setStatus(0);
		bankCard.setId(idWorker.nextId());
		
		bankCard.setCreateTime(System.currentTimeMillis());
		
		BankCard bank = bankCardRepository.save(bankCard);

		return HttpResult.success(bank, "添加成功");
	}

	//删除银行卡	
	@RequiredPermission(value="deleteBank-delete")
	@PostMapping("/deleteBankCard")
	@UserLoginToken
	public Object deleteBankCard(@RequestBody  BankCardAddReq req) throws ClientErrorException{

		if(req.getId()==null)
			throw new ClientErrorException("id不合法或为空");
		Optional<BankCard> op = bankCardRepository.findById(req.getId());
		if(!op.isPresent())
			throw new ClientErrorException("该银行卡不存在");			
		BankCard bankCard = op.get();
        		
		bankCardRepository.delete(bankCard);
		return HttpResult.success(null, "删除成功");
	}
	//查询银行卡	
	@RequiredPermission(value="findAllBank-find")
	@PostMapping("/findAllBankCard")
	@UserLoginToken
	public HttpResult<PagingList> findAllBankCard(@RequestBody  BankCardAddReq req) throws ClientErrorException{

		List<BankCard> bankCards = bankCardRepository.findAll();
		ListFenUtils<BankCard> pageList=new  ListFenUtils<BankCard>();
		PagingList page=new PagingList();		
		page.setCurrentPage(req.getCurrentPage());
		pageList.fen(page, bankCards);		  
		
		return HttpResult.success(page, "查询成功");
	}
	
	//修改银行卡	
		@RequiredPermission(value="updateBank-update")
		@PostMapping("/updateBankCard")
		@UserLoginToken
		public Object updateBankCard(@RequestBody  BankCardAddReq req) throws ClientErrorException{

			if(req.getId()==null)
				throw new ClientErrorException("id不合法或为空");
			
			Optional<BankCard> op = bankCardRepository.findById(req.getId());
			if(!op.isPresent())
				throw new ClientErrorException("该银行卡不存在");			
			BankCard bankCard = op.get();
			
			MyBeanUtils.copyProperties(req, bankCard);
			BankCard bank = bankCardRepository.save(bankCard);
			return HttpResult.success(bank, "修改成功");
		}
		
		//选择银行卡	
				@RequiredPermission(value="chooseBankCard")
				@PostMapping("/chooseBankCard")
				@UserLoginToken
				public Object chooseBankCard(@RequestBody  BankCardAddReq req) throws ClientErrorException{

					if(req.getId()==null)
						throw new ClientErrorException("id不合法或为空");
					if(req.getStatus()==null) 
							throw new ClientErrorException("状态不能为空");						
					
					
					if(req.getStatus()!=1)
						throw new ClientErrorException("状态异常，请联系管理员");
					
				    bankCardRepository.updateStatusAll();								
					
					Optional<BankCard> op = bankCardRepository.findById(req.getId());
					if(!op.isPresent())
						throw new ClientErrorException("该银行卡不存在");			
					BankCard bankCard = op.get();
					bankCard.setStatus(req.getStatus());
					MyBeanUtils.copyProperties(req, bankCard);
					bankCardRepository.save(bankCard);
					return HttpResult.success(null, "修改成功");
					
					
				
					}	
}
