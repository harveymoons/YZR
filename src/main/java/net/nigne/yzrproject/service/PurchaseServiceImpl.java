package net.nigne.yzrproject.service;

import java.util.List;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import net.nigne.yzrproject.domain.Criteria;
import net.nigne.yzrproject.domain.MemberVO;
import net.nigne.yzrproject.domain.PurchaseVO;
import net.nigne.yzrproject.persistence.PurchaseDAO;

@Service
public class PurchaseServiceImpl implements PurchaseService {
	
	@Autowired
	private PurchaseDAO dao;
	
	@Transactional(rollbackFor = { Exception.class })
	@Override
	public long getTotalCount(String member_id) {
		// TODO Auto-generated method stub
		return dao.getTotalCount(member_id);
	}


	@Transactional(rollbackFor = { Exception.class })
	@Override
	public List<PurchaseVO> getListPage(String member_id, Criteria criteria) {
		// TODO Auto-generated method stub
		return dao.getListPage(member_id, criteria);
	}

	@Transactional(rollbackFor = { Exception.class })
	@Override
	public void delete_Store(int no) {
		// TODO Auto-generated method stub
		dao.delete_Store(no);
	}
	@Transactional(rollbackFor={Exception.class})
	@Override
	public void payPersist(PurchaseVO vo) throws Exception{
		dao.payPersist(vo);
	}

	@Transactional(readOnly=true)
	@Override
	public MemberVO getUser_Info(String member_id) {
		return dao.getUser_Info(member_id);
	}

}
