package com.lt.fly.Service.impl;

import com.lt.fly.Service.BaseService;
import com.lt.fly.Service.IMemberService;
import com.lt.fly.dao.IHandicapRepository;
import com.lt.fly.dao.IMemberRepository;
import com.lt.fly.entity.Handicap;
import com.lt.fly.entity.Member;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.IdWorker;
import com.lt.fly.web.req.MemberAdd;
import com.lt.fly.web.req.MemberAddByClient;
import com.lt.fly.web.req.MemberEdit;
import com.lt.fly.web.req.MemberEditByClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends BaseService implements IMemberService {

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private IHandicapRepository iHandicapRepository;

    @Autowired
    IMemberRepository iMemberRepository;

    @Override
    public Member add(MemberAddByClient req) throws ClientErrorException {
        Member member = new Member();
        member.setId(idWorker.nextId());
        member.setCreateTime(System.currentTimeMillis());
        member.setCreateUser(this.getLoginUser());
        BeanUtils.copyProperties(req, member);
        if (null == req.getNickname())
            member.setNickname(req.getUsername());

        if (null != req.getHandicapId()){
            Handicap handicap = isNotNull(iHandicapRepository.findById(req.getHandicapId()),"组id查询不到实体");
            member.setHandicap(handicap);
        }

        if (null != req.getReferralCode()){
//            Long memberId =
        }

        member.setCreateTime(System.currentTimeMillis());
        member.setCreateUser(getLoginUser());

        iMemberRepository.save(member);
        return member;
    }

    @Override
    public Member edit(Long id, MemberEditByClient req) throws ClientErrorException {
        return null;
    }
}
