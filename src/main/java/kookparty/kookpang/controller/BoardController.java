package kookparty.kookpang.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dao.BoardDAO;
import kookparty.kookpang.dto.BoardDTO;
import kookparty.kookpang.dto.BoardDTO.Image;
import kookparty.kookpang.dto.BoardDTO.Comment;
import kookparty.kookpang.dto.UserDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** key=board */
public class BoardController implements Controller {
    private final BoardDAO dao = new BoardDAO();

    // ===================== 유틸 =====================
    private static String t(String s){ return (s==null) ? "" : s.trim(); }
    private static int parseInt(String s, int dv){ try { return Integer.parseInt(s); } catch(Exception e){ return dv; } }
    private static Long parseLongObj(String s){ try { return Long.parseLong(s); } catch(Exception e){ return null; } }

    /** 세션에서 로그인 사용자 ID 꺼내기 (UserDTO 또는 Map 지원) */
    private static Long uid(HttpServletRequest req){
        HttpSession session = req.getSession(false);
        if(session == null) return null;
        Object u = session.getAttribute("loginUser");
        if(u == null) return null;
        try{
            if(u instanceof UserDTO dto) return dto.getUserId();
            if(u instanceof Map<?,?> m){
                Object v = m.get("userId");
                if(v instanceof Number n) return n.longValue();
                if(v != null) return Long.valueOf(v.toString());
            }
            // POJO with getUserId()
            return ((Number)u.getClass().getMethod("getUserId").invoke(u)).longValue();
        }catch(Exception e){
            return null;
        }
    }

    private static List<Image> parseImages(String[] urls){
        List<Image> list = new ArrayList<>();
        if(urls == null) return list;
        int order = 0;
        for(String u : urls){
            if(u == null || u.isBlank()) continue;
            Image im = new Image();
            im.setImageUrl(u.trim());
            im.setImageOrder(order++);
            list.add(im);
        }
        return list;
    }

    // ===================== /front (JSP forward/redirect) =====================

    /** GET /front?key=board&methodName=list */
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp){
        // 목록 데이터는 AJAX로 가져가므로 여기서는 필터값만 세팅하고 JSP forward
        req.setAttribute("category", t(req.getParameter("category")));
        req.setAttribute("q", t(req.getParameter("q")));
        return new ModelAndView("/boards/board.jsp");
    }

    /** GET /front?key=board&methodName=view&postId= */
    public ModelAndView view(HttpServletRequest req, HttpServletResponse resp){
        Long id = parseLongObj(req.getParameter("postId"));
        if(id != null){
            dao.incView(id);
            BoardDTO d = dao.findById(id);
            req.setAttribute("post", d); // JSP에서는 post가 있으면 보기 모드로 렌더
        }
        return new ModelAndView("/boards/board-write.jsp");
    }

    /** GET /front?key=board&methodName=writeForm */
    public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp){
        if (uid(req) == null) {
            // ModelAndView 클래스 수정 없이 redirect 사용 (두 번째 인자 true)
            return new ModelAndView(req.getContextPath()+"/front?key=user&methodName=loginForm", true);
        }
        return new ModelAndView("/boards/board-write.jsp");
    }

    // ===================== /ajax (JSON 반환) =====================

    /** GET /ajax?key=board&methodName=listData&category=&q=&page=&size= */
    public Object listData(HttpServletRequest req, HttpServletResponse resp){
        String category = t(req.getParameter("category"));
        String q = t(req.getParameter("q"));
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 10);

        List<BoardDTO> rows = dao.list(category, q, page, size);
        int total = dao.count(category, q);

        Map<String,Object> res = new HashMap<>();
        res.put("ok", true);
        res.put("rows", rows);
        res.put("total", total);
        res.put("page", page);
        res.put("size", size);
        return res;
    }

    /** GET /ajax?key=board&methodName=postData&postId= */
    public Object postData(HttpServletRequest req, HttpServletResponse resp){
        Long id = parseLongObj(req.getParameter("postId"));
        Map<String,Object> res = new HashMap<>();
        if(id == null){
            res.put("ok", false);
            res.put("error", "invalid");
            return res;
        }
        res.put("ok", true);
        res.put("post", dao.findById(id));
        return res;
    }

    /** POST /ajax?key=board&methodName=save  (등록/수정 공용) */
    public Object save(HttpServletRequest req, HttpServletResponse resp){
        Long userId = uid(req);
        if(userId == null) return Map.of("ok", false, "error", "login-required");

        String postIdStr = req.getParameter("postId");
        String category  = t(req.getParameter("category"));
        String title     = t(req.getParameter("title"));
        String contentH  = t(req.getParameter("content"));      // HTML
        String contentT  = t(req.getParameter("contentText"));  // 순수 텍스트(프론트에서 추가 전송)

        // 내용 검증: 빈 HTML(<br>, &nbsp;)도 빈값으로 취급
        String contentForCheck =
                (contentT == null || contentT.isBlank())
                        ? contentH.replaceAll("\\<[^>]*>", "")
                                  .replace('\u00A0',' ')
                                  .trim()
                        : contentT;

        if (title.isBlank() || contentForCheck.isBlank()){
            return Map.of("ok", false, "error", "invalid");
        }

        BoardDTO d = new BoardDTO();
        d.setUserId(userId);
        d.setCategory(category.isBlank() ? "free" : category);
        d.setTitle(title);
        d.setContent(contentH); // 저장은 HTML 그대로

        List<Image> images = parseImages(req.getParameterValues("imageUrl"));

        long newId;
        if(postIdStr == null || postIdStr.isBlank()){
            newId = dao.insertPost(d, images);
            return Map.of("ok", newId > 0, "postId", newId);
        } else {
            Long pid = parseLongObj(postIdStr);
            if(pid == null) return Map.of("ok", false, "error", "invalid");
            d.setPostId(pid);
            boolean ok = dao.updatePost(d, images); // BoardDAO가 boolean 반환하도록 가정
            return Map.of("ok", ok, "postId", d.getPostId());
        }
    }

    /** POST /ajax?key=board&methodName=delete */
    public Object delete(HttpServletRequest req, HttpServletResponse resp){
        Long userId = uid(req);
        if(userId == null) return Map.of("ok", false, "error", "login-required");
        Long postId = parseLongObj(req.getParameter("postId"));
        if(postId == null) return Map.of("ok", false, "error", "invalid");
        boolean ok = dao.deletePost(postId, userId);
        return Map.of("ok", ok);
    }

    /** POST /ajax?key=board&methodName=addComment */
    public Object addComment(HttpServletRequest req, HttpServletResponse resp){
        Long userId = uid(req);
        if(userId == null) return Map.of("ok", false, "error", "login-required");
        Long postId = parseLongObj(req.getParameter("postId"));
        String content = t(req.getParameter("content"));
        if(postId == null || content.isBlank()) return Map.of("ok", false, "error", "invalid");

        long cid = dao.addComment(postId, userId, content);
        List<Comment> list = dao.listComments(postId);
        Map<String,Object> res = new HashMap<>();
        res.put("ok", true);
        res.put("commentId", cid);
        res.put("comments", list);
        return res;
    }

    /** POST /ajax?key=board&methodName=delComment */
    public Object delComment(HttpServletRequest req, HttpServletResponse resp){
        Long userId = uid(req);
        if(userId == null) return Map.of("ok", false, "error", "login-required");
        Long cid = parseLongObj(req.getParameter("commentId"));
        Long postId = parseLongObj(req.getParameter("postId"));
        if(cid == null || postId == null) return Map.of("ok", false, "error", "invalid");

        boolean ok = dao.delComment(cid, userId, postId);
        List<Comment> list = dao.listComments(postId);
        Map<String,Object> res = new HashMap<>();
        res.put("ok", ok);
        res.put("comments", list);
        return res;
    }
}
