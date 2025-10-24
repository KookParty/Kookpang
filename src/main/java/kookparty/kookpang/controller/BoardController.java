package kookparty.kookpang.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import kookparty.kookpang.dao.BoardDAO;
import kookparty.kookpang.dto.BoardDTO;
import kookparty.kookpang.dto.BoardDTO.Image;
import kookparty.kookpang.dto.BoardDTO.Comment;
import kookparty.kookpang.dto.UserDTO;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/** key=board */
public class BoardController implements Controller {
    private final BoardDAO dao = new BoardDAO();

    // ===================== 유틸 =====================
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static String t(String s){ return (s==null) ? "" : s.trim(); }
    private static int parseInt(String s, int dv){ try { return Integer.parseInt(s); } catch(Exception e){ return dv; } }
    private static Long parseLongObj(String s){ try { return Long.parseLong(s); } catch(Exception e){ return null; } }

    /** 세션에서 로그인 사용자 ID 꺼내기 */
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

    private static String iso(LocalDateTime dt){
        return (dt == null) ? null : dt.format(FMT);
    }

    /** 리스트용 간단 맵 (LocalDateTime → String) */
    private static Map<String,Object> mapPostLite(BoardDTO d){
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("postId", d.getPostId());
        m.put("userId", d.getUserId());
        m.put("category", d.getCategory());
        m.put("title", d.getTitle());
        m.put("content", d.getContent());
    m.put("viewCount", d.getViewCount());
    m.put("commentCount", d.getCommentCount());
        m.put("createdAt", iso(d.getCreatedAt()));
        m.put("nickname", d.getNickname());
        return m;
    }

    /** 상세뷰용 맵 (이미지/댓글 포함, LocalDateTime → String) */
    private static Map<String,Object> mapPostFull(BoardDTO d){
        Map<String,Object> m = mapPostLite(d);
        // images
        List<Map<String,Object>> imgs = new ArrayList<>();
        if (d.getImages() != null){
            for (Image im : d.getImages()){
                Map<String,Object> mi = new LinkedHashMap<>();
                mi.put("imageId", im.getImageId());
                mi.put("postId", im.getPostId());
                mi.put("imageUrl", im.getImageUrl());
                mi.put("imageOrder", im.getImageOrder());
                imgs.add(mi);
            }
        }
        m.put("images", imgs);

        // comments
        List<Map<String,Object>> cmts = new ArrayList<>();
        if (d.getComments() != null){
            for (Comment c : d.getComments()){
                Map<String,Object> mc = new LinkedHashMap<>();
                mc.put("commentId", c.getCommentId());
                mc.put("postId", c.getPostId());
                mc.put("userId", c.getUserId());
                mc.put("content", c.getContent());
                mc.put("createdAt", iso(c.getCreatedAt()));
                mc.put("nickname", c.getNickname());
                cmts.add(mc);
            }
        }
        m.put("comments", cmts);
        return m;
    }

    private static List<Map<String,Object>> mapComments(List<Comment> cs){
        List<Map<String,Object>> out = new ArrayList<>();
        if(cs != null){
            for(Comment c : cs){
                Map<String,Object> mc = new LinkedHashMap<>();
                mc.put("commentId", c.getCommentId());
                mc.put("postId", c.getPostId());
                mc.put("userId", c.getUserId());
                mc.put("content", c.getContent());
                mc.put("createdAt", iso(c.getCreatedAt()));
                mc.put("nickname", c.getNickname());
                out.add(mc);
            }
        }
        return out;
    }

    // ===================== /front (JSP forward/redirect) =====================

    /** GET /front?key=board&methodName=list */
    public ModelAndView list(HttpServletRequest req, HttpServletResponse resp){
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
            req.setAttribute("post", d); // JSP에서 보기/수정 전환에 사용
        }
        return new ModelAndView("/boards/board-write.jsp");
    }

    /** GET /front?key=board&methodName=writeForm */
    public ModelAndView writeForm(HttpServletRequest req, HttpServletResponse resp){
        if (uid(req) == null) {
            return new ModelAndView(req.getContextPath()+"/front?key=user&methodName=loginForm", true);
        }
        return new ModelAndView("/boards/board-write.jsp");
    }

    // ===================== /ajax (JSON 반환: LocalDateTime → String 변환 적용) =====================

    /** GET /ajax?key=board&methodName=listData&category=&q=&page=&size= */
    public Object listData(HttpServletRequest req, HttpServletResponse resp){
        String category = t(req.getParameter("category"));
        String q = t(req.getParameter("q"));
        int page = parseInt(req.getParameter("page"), 1);
        int size = parseInt(req.getParameter("size"), 10);
        String sort = t(req.getParameter("sort"));
        if (sort.isBlank()) sort = "latest";

        List<BoardDTO> rows = dao.list(category, q, page, size, sort);
        List<Map<String,Object>> mapped = new ArrayList<>();
        for (BoardDTO d : rows) mapped.add(mapPostLite(d));

        int total = dao.count(category, q);

        Map<String,Object> res = new LinkedHashMap<>();
        res.put("ok", true);
        res.put("rows", mapped);
        res.put("total", total);
        res.put("page", page);
        res.put("size", size);
        res.put("sort", sort);
        return res;
    }

    /** GET /ajax?key=board&methodName=postData&postId= */
    public Object postData(HttpServletRequest req, HttpServletResponse resp){
        Long id = parseLongObj(req.getParameter("postId"));
        if(id == null){
            return Map.of("ok", false, "error", "invalid");
        }
        BoardDTO d = dao.findById(id);
        return Map.of("ok", true, "post", d==null ? null : mapPostFull(d));
    }

    /** POST /ajax?key=board&methodName=save  (등록/수정 공용) */
    public Object save(HttpServletRequest req, HttpServletResponse resp){
        Long userId = uid(req);
        if (userId == null) return Map.of("ok", false, "error", "login-required");

        String postIdStr = req.getParameter("postId");
        String category  = t(req.getParameter("category"));
        String title     = req.getParameter("title");
        String contentH  = req.getParameter("content");          // HTML
        String contentT  = t(req.getParameter("contentText"));   // 순수 텍스트 (선택)

        // NPE 방지 및 로깅
        String safeTitle = (title == null) ? "" : title;
        String safeContentH = (contentH == null) ? "" : contentH;
        System.out.println("Board.save >> title=" + safeTitle + " | content.len=" + safeContentH.length());

        // 텍스트로 검증
        String contentForCheck;
        if (contentT != null && !contentT.isBlank()) {
            contentForCheck = contentT;
        } else {
            // contentH가 null일 수 있으니 안전 처리
            String raw = (contentH == null) ? "" : contentH;
            contentForCheck = raw.replaceAll("\\<[^>]*>", "")
                                 .replace('\u00A0',' ')
                                 .trim();
        }

        if (safeTitle.isBlank() || contentForCheck.isBlank()) {
            return Map.of("ok", false, "error", "invalid");
        }

        BoardDTO d = new BoardDTO();
        d.setUserId(userId);
        d.setCategory(category.isBlank() ? "free" : category);
        d.setTitle(safeTitle);
        d.setContent(safeContentH); // 저장은 HTML 그대로

        // imageUrl 여러 개 수신
        List<Image> images = parseImages(req.getParameterValues("imageUrl"));

        if (postIdStr == null || postIdStr.isBlank()){
            long newId = dao.insertPost(d, images);
            return Map.of("ok", newId > 0, "postId", newId);
        } else {
            Long pid = parseLongObj(postIdStr);
            if(pid == null) return Map.of("ok", false, "error", "invalid");
            d.setPostId(pid);
            boolean ok = dao.updatePost(d, images);
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
        return Map.of("ok", true, "commentId", cid, "comments", mapComments(dao.listComments(postId)));
    }

    /** POST /ajax?key=board&methodName=delComment */
    public Object delComment(HttpServletRequest req, HttpServletResponse resp){
        Long userId = uid(req);
        if(userId == null) return Map.of("ok", false, "error", "login-required");
        Long cid = parseLongObj(req.getParameter("commentId"));
        Long postId = parseLongObj(req.getParameter("postId"));
        if(cid == null || postId == null) return Map.of("ok", false, "error", "invalid");

        boolean ok = dao.delComment(cid, userId, postId);
        return Map.of("ok", ok, "comments", mapComments(dao.listComments(postId)));
    }

    /** POST /ajax?key=board&methodName=uploadImage  (multipart file upload via front controller) */
    /** POST /ajax?key=board&methodName=uploadImage  (multipart file upload via front controller) */
    public Object uploadImage(HttpServletRequest req, HttpServletResponse resp){
        try {
            // === 아주 간단한 프린트 디버깅 ===
            System.out.println("==== [uploadImage] ====");
            System.out.println("Content-Type: " + req.getContentType());

            Part filePart = req.getPart("file");
            System.out.println("filePart = " + filePart);
            if (filePart != null) {
                System.out.println("name=" + filePart.getName());
                System.out.println("filename=" + filePart.getSubmittedFileName());
                System.out.println("size=" + filePart.getSize());
                System.out.println("contentType=" + filePart.getContentType());
            } else {
                System.out.println("filePart가 null 입니다. (name=\"file\" 파트를 못 찾음)");
            }
            System.out.println("=======================");
            // ===============================

            if (filePart == null) return Map.of("ok", false, "msg", "no-file");

            String submitted = filePart.getSubmittedFileName();
            String ext = "";
            if (submitted != null && submitted.contains(".")) {
                ext = submitted.substring(submitted.lastIndexOf('.'));
            }

            String uploads = req.getServletContext().getRealPath("/uploads");
            Path up = Paths.get(uploads);
            if (!Files.exists(up)) Files.createDirectories(up);

            String newName = System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 6) + ext;
            Path target = up.resolve(newName);

            try (InputStream in = filePart.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            String url = req.getContextPath() + "/uploads/" + newName;
            return Map.of("ok", true, "url", url);

        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", e.getClass().getName() + ":" + e.getMessage());
        }
    }
}
