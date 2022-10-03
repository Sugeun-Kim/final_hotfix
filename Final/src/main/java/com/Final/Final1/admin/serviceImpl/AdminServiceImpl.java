package com.Final.Final1.admin.serviceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Final.Final1.admin.model.AdminDAO;
import com.Final.Final1.admin.model.AdminDTO;
import com.Final.Final1.admin.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService{


	@Autowired
	AdminDAO adminDao;
	
	@Override
	public List<Map<String, Object>> adminMemList( Map<String, Object> map) {
		return adminDao.adminMemList(map);
	}

	@Override
	public int userCount(AdminDTO dto) {
		return adminDao.userCount(dto);
	}

	@Override
	public List<AdminDTO> userBoard(int start, int end, String select) {
		return adminDao.userBoard(start, end, select);
	}

	@Override
	public int userBoardCount() {
		return adminDao.userBoardCount();
	}


	
	
//	@Override
//	public List<Map<String, Object>> userBoard(Map<String, Object> map) {
//		return adminDao.userBoard(map);
//	}
//
//	@Override
//	public int userBoardCount(AdminDTO dto) {
//		return adminDao.userBoardCount(dto);
//	}

	@Override
	public void boardMemDelete(int no) {
		adminDao.boardMemDelete(no);
	}

	@Override
	public List<Map<String, Object>> adminBoard(Map<String, Object> map) {
		return adminDao.adminBoard(map);
	}


	

	
	

}

