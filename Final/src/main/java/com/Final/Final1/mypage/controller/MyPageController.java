package com.Final.Final1.mypage.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.Final.Final1.board.model.BoardDTO;
import com.Final.Final1.board.model.MyCommentListDTO;
import com.Final.Final1.board.model.MyWriterListDTO;
import com.Final.Final1.mypage.model.MypageDAO;
import com.Final.Final1.mypage.model.MypageDTO;
import com.Final.Final1.mypage.service.MypageService;

//留덉씠 �럹�씠吏� 而⑦듃濡ㅻ윭
@Controller
public class MyPageController {

	@Inject
	MypageDAO mypageDao;
	@Autowired
	MypageService mypageService;
	
	// 마이 페이지
	@RequestMapping("/fileTest")
	public String fileTest(MypageDTO dto, @RequestParam MultipartFile file, HttpSession session) {
		
		String name = (String)session.getAttribute("User_id");
		System.out.println(name);
		dto.setUser_id(name); // 불러온 세션값을 dto에 설정
		
		String fileRealName = file.getOriginalFilename(); //파일명을 얻어낼 수 있는 메서드!
		long size = file.getSize(); //파일 사이즈
		
		System.out.println("파일명 : "  + fileRealName);
		System.out.println("용량크기(byte) : " + size);
		//서버에 저장할 파일이름 fileextension으로 .jsp이런식의  확장자 명을 구함
		String fileExtension = fileRealName.substring(fileRealName.lastIndexOf("."),fileRealName.length());
		String uploadFolder = "\\Users\\광트북\\img";
		

		/*
		  파일 업로드시 파일명이 동일한 파일이 이미 존재할 수도 있고 사용자가 
		  업로드 하는 파일명이 언어 이외의 언어로 되어있을 수 있습니다. 
		  타인어를 지원하지 않는 환경에서는 정산 동작이 되지 않습니다.(리눅스가 대표적인 예시)
		  고유한 랜덤 문자를 통해 db와 서버에 저장할 파일명을 새롭게 만들어 준다.
		 */
		
		UUID uuid = UUID.randomUUID();
		System.out.println(uuid.toString());
		String[] uuids = uuid.toString().split("-");
		
		String uniqueName = uuids[0];
		System.out.println("생성된 고유문자열" + uniqueName);
		System.out.println("확장자명" + fileExtension);
		
		
		
		
		// File saveFile = new File(uploadFolder+"\\"+fileRealName); uuid 적용 전
		
		File saveFile = new File(uploadFolder+"\\"+uniqueName + fileExtension);  // 적용 후
		try {
			System.out.println("저장완료됨");
			file.transferTo(saveFile); // 실제 파일 저장메서드(filewriter 작업을 손쉽게 한방에 처리해준다.)
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		dto.setIMG_File_name(uniqueName+fileExtension);
		dto.setIMG_URL_name(uploadFolder+"\\"+uniqueName+fileExtension);

		mypageService.updateUserPhoto(dto);
		
		
		
		return "/mypage/mypage";
	}

	
	// 마이 페이지
	@ResponseBody
	@RequestMapping("/mypage/index")
	public ModelAndView mypageIndex(MypageDTO dto,HttpSession session) {
		ModelAndView mv = new ModelAndView();
		
//		String name = (String)session.getAttribute("User_id");
//		dto.setUser_id(name);
//		
//		String photo = "\\"+mypageService.UserPhotoView(dto);
//		System.out.println(photo);
//		
//		File file = new File("C:\\Users\\광트북\\img"+photo);
//		ResponseEntity<byte[]> result = null;
	
//		try {
//			HttpHeaders header = new HttpHeaders();
//			header.add("Content-Type", Files.probeContentType(file.toPath()));
//			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
//					header, HttpStatus.OK);
//			mv.addObject("photo", result);
			mv.setViewName("/mypage/mypage");
//			
//		} catch(IOException e) {
//			e.printStackTrace();
//		}
		
		
		
		return mv;
	}
	
	@ResponseBody
	@RequestMapping("/mypage/index2")
	public ResponseEntity<byte[]> mypageIndex2(MypageDTO dto,HttpSession session) {
		String name = (String)session.getAttribute("User_id");
		dto.setUser_id(name);
		
		
		String photo = "\\"+mypageService.UserPhotoView(dto);
		System.out.println(photo);
		
		File file = new File("C:\\Users\\광트북\\img"+photo);
		ResponseEntity<byte[]> result = null;
	
		try {
			HttpHeaders header = new HttpHeaders();
			header.add("Content-Type", Files.probeContentType(file.toPath()));
			result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file),
					header, HttpStatus.OK);
			
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		
		return result;
	}
	

	// 留덉씠�럹�씠吏� �젙蹂� �닔�젙
	@RequestMapping("/mypage/setUserInfo")
	public String mypageSet() {
		return "mypage/mypage_Set";
	}

	// 수근 코드 
		@RequestMapping("/mypage/writer")
		public ModelAndView mypageWriter(HttpServletRequest req, MyWriterListDTO Post_writer, HttpSession session) {
			
			MyWriterListDTO dto = new MyWriterListDTO();
			String keyword = req.getParameter("keyword");
			dto.setKeyword(keyword);
			
			int count = mypageService.count(req.getParameter("keyword"));
			
			
			
			
			ModelAndView mv = new ModelAndView();
	
			//세션 값 불러옴
			String name = (String)session.getAttribute("User_nickname");
			Post_writer.setPost_writer(name); // 불러온 세션값을 dto에 설정
			//로그인한 유저가 해결요청한 게시글을 뽑아옴
			List<BoardDTO> list = mypageService.myRequestlist(Post_writer);
			mv.setViewName("/mypage/mypage_writer");
			mv.addObject("list", list);
			mv.addObject("count", count);
			return mv;
		}
	

	
	//광모형 코드 
	// 留덉씠�럹�씠吏� �옉�꽦�븳湲�
//	@RequestMapping("/mypage/writer")
//	public ModelAndView mypageWriter(MyWriterListDTO dto, HttpSession session) {
//		ModelAndView mv = new ModelAndView();
		//세션 값 불러옴
//		String name = (String)session.getAttribute("User_nickname");
//		dto.setPost_writer(name); // 불러온 세션값을 dto에 설정
		//로그인한 유저가 해결요청한 게시글을 뽑아옴
//		List<BoardDTO> list = mypageService.myRequestlist(dto);
//		mv.setViewName("/mypage/mypage_writer");
//		mv.addObject("list", list);
//		System.out.println(list);
//		return mv;
//	}
	
	
	

	// 나의 작성 댓글
	@RequestMapping("/mypage/comments")
	public ModelAndView mypagecomments(MyCommentListDTO dto, HttpSession session) {
		
		ModelAndView mv = new ModelAndView();
		//세션 값 불러옴
		String name = (String)session.getAttribute("User_nickname");
		dto.setComment_writer(name); // 불러온 세션값을 dto에 설정

		//로그인한 유저가 해결요청한 게시글을 뽑아옴
		List<BoardDTO> list = mypageService.myCommentlist(dto);
		mv.setViewName("/mypage/mypage_comments");
		mv.addObject("list", list);
				
		return mv;
	}


	

	// 나의 결제 내역
	@RequestMapping(value = "/mypage/pay", method = RequestMethod.GET)
	public ModelAndView mainIndex(MypageDTO dto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

//		session.setAttribute("User_point", dto.getUser_point());

		mv.setViewName("/mypage/mypage_pay");

		return mv;
	}

	// 유저 닉네임 변경
	@RequestMapping(value = "/mypage/setok", method = RequestMethod.POST)
	public ModelAndView mypagesetok(MypageDTO dto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		mypageService.UserSetNickname(dto);
		session.setAttribute("User_nickname", dto.getUser_nickname());

		mv.setViewName("redirect:/mypage/setUserInfo");

		return mv;
	}
	// 유저 폰번호 변경
	@RequestMapping(value = "/mypage/UserSetPhone_num", method = RequestMethod.POST)
	public ModelAndView UserSetPhone_num(MypageDTO dto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		mypageService.UserSetPhone_num(dto);
		session.setAttribute("Phone_num", dto.getPhone_num());

		mv.setViewName("redirect:/mypage/setUserInfo");

		return mv;
	}
	// 유저 이메일변경
	@RequestMapping(value = "/mypage/UserSetEmail_address", method = RequestMethod.POST)
	public ModelAndView UserSetEmail_address(MypageDTO dto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		mypageService.UserSetEmail_address(dto);
		session.setAttribute("Email_address", dto.getEmail_address());
		mv.setViewName("redirect:/mypage/setUserInfo");

		return mv;
	}
	// 유저 자기소개
	@RequestMapping(value = "/mypage/UserSetUser_introduce", method = RequestMethod.POST)
	public ModelAndView UserSetUser_introduce(MypageDTO dto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		mypageService.UserSetUser_introduce(dto);
		session.setAttribute("User_introduce", dto.getUser_introduce());

		mv.setViewName("redirect:/mypage/setUserInfo");

		return mv;
	}
	
	// 유저 자기소개
	@RequestMapping(value = "/mypage/UserContactSet", method = RequestMethod.POST)
	public ModelAndView UserContactSet(MypageDTO dto, HttpSession session) {
		ModelAndView mv = new ModelAndView();

		mypageService.UserContactSet(dto);
		session.setAttribute("User_contact1", dto.getUser_contact1());
		session.setAttribute("User_contact2", dto.getUser_contact2());
		session.setAttribute("User_contact3", dto.getUser_contact3());
		mv.setViewName("redirect:/mypage/setUserInfo");

		return mv;
	}
	
	
	
	
	// 유저 탈퇴
	@RequestMapping(value="/UserDelete", method = RequestMethod.POST)
	public ModelAndView UserDelete(MypageDTO dto, HttpSession session, Model model) {
		String userid = (String)session.getAttribute("User_id");
		ModelAndView mv = new ModelAndView();
		mypageService.UserDelete(userid);
		session.invalidate();
		mv.setViewName("redirect:/MainPage");
		//mv.addObject("msg","완료2");
		return mv;
	}
	
	

}
