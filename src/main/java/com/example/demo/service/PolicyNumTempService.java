package com.example.demo.service;

import com.example.demo.mapper.PolicyNumTempMapper;
import com.example.demo.model.PolicyNumTemp;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PolicyNumTempService{

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;//引入bean

    private PolicyNumTempMapper policyNumTempMapper;

    public void batchInsert(List<PolicyNumTemp> policyNumTempList){
        SqlSession session = sqlSessionTemplate.getSqlSessionFactory().openSession(ExecutorType.BATCH, false);//关闭session的自动提交
        policyNumTempMapper = session.getMapper(PolicyNumTempMapper.class);//利用反射生成mapper对象
        try {
            int i=0;
            for (PolicyNumTemp fs : policyNumTempList) {
                policyNumTempMapper.insert(fs);
                if (i % 1000 == 0 || i == policyNumTempList.size()-1) {
                    //手动每1000个一提交，提交后无法回滚
                    session.commit();
                    session.clearCache();//注意，如果没有这个动作，可能会导致内存崩溃。
                }
                i++;
            }
        }catch (Exception e) {
            //没有提交的数据可以回滚
            session.rollback();
        } finally{
            session.close();
        }
    }
}
