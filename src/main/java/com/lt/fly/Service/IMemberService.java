package com.lt.fly.Service;

import com.lt.fly.entity.Member;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.web.req.MemberAdd;
import com.lt.fly.web.req.MemberAddByClient;
import com.lt.fly.web.req.MemberEdit;
import com.lt.fly.web.req.MemberEditByClient;

public interface IMemberService {

    Member add(MemberAddByClient req) throws ClientErrorException;

    Member edit(Long id, MemberEditByClient req) throws  ClientErrorException;
}
