package com.eomcs.lms.web;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.eomcs.lms.dao.BoardDao;
import com.eomcs.lms.dao.LessonDao;
import com.eomcs.lms.domain.Board;
import com.eomcs.lms.domain.Member;

@Controller
@RequestMapping("/board")
public class BoardController  {

  BoardDao boardDao;
  LessonDao lessonDao;

  public BoardController(BoardDao boardDao, LessonDao lessonDao) {
    this.boardDao = boardDao;
    this.lessonDao = lessonDao;
  }
  
  @RequestMapping("list")
  public ModelAndView list(Model model)
      throws Exception {  //예외처리는 프론트 컨트롤러에게 넘김
    
      ModelAndView mv = new ModelAndView();   
      List<Board> list = boardDao.findAll();
      mv.addObject("list", list);
      mv.setViewName("board/list");
      return mv;
  }

  @RequestMapping("detail")
  public String detail(int no, Model model) throws Exception {  
    
    Board board = boardDao.findByNo(no);
    model.addAttribute("board", board);
    return "/board/detail";
  }
  
  @RequestMapping("form")
  public String form(HttpSession session, Model model)
      throws Exception {  //예외처리는 프론트 컨트롤러에게 넘김
    
    Member loginUser = (Member) session.getAttribute("loginUser");
    
    List<Map<String, Object>> lessons;
    
    lessons = lessonDao.findByParticipantNo(loginUser.getNo());
    model.addAttribute("lessons", lessons);
    
    return "/board/form";
  }
  
  @RequestMapping("add")
  public String add(Board board, HttpSession session) throws Exception {  //예외처리는 프론트 컨트롤러에게 넘김

    //★★파라미터에 Board를 넣어버림. request.getParameter 노 쓸모
    
    Member loginUser = 
        (Member) session.getAttribute("loginUser");

    board.setWriterNo(loginUser.getNo());  

    boardDao.insert(board);

    return "redirect:list";
  }


  @RequestMapping("update")
  public String update(Board board)
      throws Exception {  //예외처리는 프론트 컨트롤러에게 넘김

      boardDao.update(board);
      return "redirect:list";
  }


  @RequestMapping("delete")
  public String delete(int no, Model model) throws Exception {  //예외처리는 프론트 컨트롤러에게 넘김
    
    model.addAttribute("count", boardDao.delete(no));
    return "/board/delete"; //여기선 상대경로 ㄴㄴ 항상 루트로 시작해
  }
  
}
