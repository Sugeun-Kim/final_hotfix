package com.Final.Final1.team.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.Final.Final1.team.model.TeamlistDTO;
import com.Final.Final1.team.service.Teamlistservice;

@Controller
public class Teamlistcontroller {
	
	@Autowired
	Teamlistservice teamlistservice;
	
	//��Ϻҷ�����
	//selectbox
	@RequestMapping(value="/teamlist")
	public ModelAndView teamlist(@RequestParam Map<String, Object> map, @RequestParam(defaultValue = "all") String search_option, 
			@RequestParam(defaultValue = " ") String keyword) {
		
			System.out.println(map.toString());
			
		
			ModelAndView mv = new ModelAndView();
			mv.setViewName("/team/teamlist");
			
			System.out.println("else��");
			List<Map<String, Object>> taglist = teamlistservice.taglist(map, search_option, keyword); //�±� ��ü
			List<TeamlistDTO> teamlist = teamlistservice.list(map, search_option, keyword); //����� ��ü
			List<Map<String, Object>> tags = teamlistservice.tags(map); //�������
			
			
			Map<String, Object> map2 = new HashMap<>();
			
			
			map2.put("teamlist", teamlist); //����� ��ü
			map2.put("taglist", taglist); //�±� ��ü
			map2.put("tags", tags);
			
			
			mv.addObject("map",map2);

			return mv;
	}
	
	//�±� �˻����� �� ->�� �ؿ� �±׵��� �����ؼ� �˻����� ��
//	@RequestMapping(value="/teamlist2")
//	public ModelAndView teamlist2(@RequestParam Map<String, Object> map, @RequestParam(defaultValue = "") String tagname) {
//		
//			System.out.println("tt2");
//			System.out.println("tagname ="+ tagname);
//		
//			ModelAndView mv = new ModelAndView();
//			mv.setViewName("/team/teamlist");
//			
//			
//			
//			List<Map<String, Object>> click_taglist = teamlistservice.click_taglist(map, tagname);
//			mv.addObject("clcik_taglist", click_taglist); //Ŭ������ ��
//			
//			System.out.println("click_taglist"+click_taglist.toString());
//			
//			return mv;
//
//	}
	

	//�������
	//�±׼��� -> ������ �±׵� insert
	@RequestMapping(value="/teammake" , method= RequestMethod.POST)
	public ModelAndView teammake2(@RequestParam Map<String, Object> map) {
			
			ModelAndView mv = new ModelAndView();
			mv.setViewName("redirect:/teamlist");
			//�� �μ�Ʈ
			int teammake = teamlistservice.teammake(map);
			mv.addObject("teammake", teammake); 
			
			return mv;

	}
	
	@RequestMapping(value="/teamjoin" , method= RequestMethod.POST)
	public ModelAndView teamjoin(@RequestParam Map<String, Object> map, HttpSession session) {
			
			ModelAndView mv = new ModelAndView();
			mv.setViewName("redirect:/teamlist");
			
			int teammake = teamlistservice.teammake(map);
			mv.addObject("teammake", teammake); 
			
			return mv;

	}
}
