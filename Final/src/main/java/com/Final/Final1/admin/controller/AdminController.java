package com.Final.Final1.admin.controller;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.Final.Final1.admin.model.AdminDAO;
import com.Final.Final1.admin.service.AdminService;
import com.Final.Final1.board.model.BoardDTO;
import com.Final.Final1.board.model.PageUtil;
import com.Final.Final1.admin.model.AdminDTO;


@Controller
public class AdminController {

	

	@Autowired
	AdminService adminService;
	
	// 회원관리페이지는 메인컨트롤에 있음
	
//	@RequestMapping(value = "/admin/memlist", method = RequestMethod.GET)
//	public ModelAndView adminMemList(AdminDTO dto, HttpSession session, Map<String, Object> map) {
//		ModelAndView mv = new ModelAndView();
//		mv.addObject("map", adminService.adminMemList(map));
//		mv.setViewName("/admin/memlist");
//		return mv;
//	}
	
	
	// 게시판관리페이지
	@RequestMapping("/admin/board")
	public String adminBoard(AdminDTO dto) {
		return "/admin/admin_BoardMng";
	}
	
	// 게시판관리에 공지사항페이지
	@RequestMapping("/admin/board_mng")
	public String adminBoardMng() {
		return "/admin/admin_BoardMng";
	}
	
	// 게시판 회원 게시판 

	// 게시판관리에 공지사항 작성페이지
	@RequestMapping("/admin/write")
	public String adminBoardWrite() {
		return "/admin/admin_WriteForm";
	}
	
	// 게시판관리에 공지사항 작성했을때
//		@RequestMapping(value = "/adminBoard", method = RequestMethod.POST)
//		public ModelAndView adminBoard(AdminDTO dto, HttpSession session, Map<String, Object> map, HttpServletRequest request) {
//			ModelAndView mv = new ModelAndView();
//			adminService.adminBoard(dto);
//			
//			
//			
//			
//			return "/admin/admin_BoardMng";
//		}
	
	
	
	// 게시판관리에 회원게시글페이지

	@RequestMapping(value = "/admin/board_mem", method = RequestMethod.GET)
	public ModelAndView adminBoardMem(HttpServletRequest req, @RequestParam(defaultValue="1")int curPage,
			@RequestParam(defaultValue ="all")String search_option) {
		AdminDTO dto = new AdminDTO();
		String select = req.getParameter("select");
		dto.setSelect(select);
		
		ModelAndView mv = new ModelAndView();
		
		int userBoardCount = adminService.userBoardCount();
		PageUtil page_info = new PageUtil(userBoardCount, curPage);
		int start = page_info.getPageBegin();
		int end = page_info.getPageEnd();
		
		List<AdminDTO> list = adminService.userBoard(start, end, select );
		mv.setViewName("/admin/admin_BoardMem");
		
		mv.addObject("count", userBoardCount);
		mv.addObject("map",list);
		mv.addObject("page_info", page_info);
		mv.addObject("search_option", dto.getSelect());
		
		
		
		return mv;
	}	
		

	
//	@RequestMapping(value = "/admin/board_mem", method = RequestMethod.GET)
//	public ModelAndView adminBoardMem(AdminDTO dto, HttpSession session, Map<String, Object> map, HttpServletRequest request) {
//		ModelAndView mv = new ModelAndView();
//		int userBoardCount = adminService.userBoardCount(dto);
//		mv.addObject("count", userBoardCount);
//		mv.addObject("map", adminService.userBoard(map));
//		mv.setViewName("/admin/admin_BoardMem");
//		return mv;
//	}
	
	// 게시판관리 게시글 선택삭제
	@ResponseBody
	@RequestMapping(value = "/boardMemDelete", method = RequestMethod.POST)
		//public String boardDelete(HttpServletRequest request) {
		public String boardDelete(int[] valueArr) {
		int[] boardDeleteMsg = valueArr;
		int size = boardDeleteMsg.length;
		for(int i=0; i<size; i++) {
			adminService.boardMemDelete(boardDeleteMsg[i]);
		}
		return "redirect:/admin/admin_BoardMem";
	}
	
}
