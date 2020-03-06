package com.lt.fly.Service.impl;

import com.lt.fly.Service.IOddService;
import com.lt.fly.dao.IOddRepository;
import com.lt.fly.entity.Odd;
import com.lt.fly.exception.ClientErrorException;
import com.lt.fly.utils.HttpResult;
import com.lt.fly.web.query.OddFind;
import com.lt.fly.web.vo.OddVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OddServiceImpl implements IOddService {

    @Autowired
    private IOddRepository iOddRepository;

//    @Override
//    public Object findOne(OddFind query) {
//        if(null == query.getOddGroupId()){
//            List<Odd> odds = iOddRepository.findByBetGroupId(query.getBetGroupId());
//            if (null != odds)
//                return OddVo.tovo(odds);
//        }else{
//            Odd odd = iOddRepository.findOne(query.getBetGroupId(),query.getOddGroupId());
//            if(null != odd) {
//                return new OddVo(odd);
//            }
//        }
//        return null;
//    }

    @Override
    public OddVo findOneByOddGroupId(OddFind query) throws ClientErrorException {
        Odd odd = iOddRepository.findOne(query.getBetGroupId(),query.getOddGroupId());
        if(null == odd) {
           return null;
        }
        return new OddVo(odd);
    }
}
