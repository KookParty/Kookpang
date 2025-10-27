package kookparty.kookpang.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kookparty.kookpang.dao.BoardDAO;
import kookparty.kookpang.dao.OrderDAO;
import kookparty.kookpang.dao.OrderDAOImpl;
import kookparty.kookpang.dto.BoardDTO;
import kookparty.kookpang.dto.OrderDTO;
import kookparty.kookpang.dto.UserDTO;

import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MyPageController implements Controller {
    
    private final BoardDAO boardDAO = new BoardDAO();
    //private final LikesDAO likesDAO = new LikesDAO();
    private final OrderDAO orderDAO = new OrderDAOImpl();
    
    private static final DateTimeFormatter ISO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    public Object getMyPosts(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession s = req.getSession(false);
            if (s == null || s.getAttribute("loginUser") == null) {
                return Map.of("ok", false, "msg", "로그인이 필요합니다.");
            }
            
            UserDTO user = (UserDTO) s.getAttribute("loginUser");
            int page = parseIntParam(req.getParameter("page"), 1);
            int size = parseIntParam(req.getParameter("size"), 10);
            
            List<BoardDTO> posts = boardDAO.listByUser(user.getUserId(), page, size);
            int total = boardDAO.countByUser(user.getUserId());
            
            List<Map<String, Object>> postList = posts.stream()
                .map(this::mapPost)
                .collect(Collectors.toList());
            
            return Map.of(
                "ok", true,
                "posts", postList,
                "total", total,
                "page", page,
                "size", size
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", e.getMessage());
        }
    }
    /*
    public Object getLikedRecipes(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession s = req.getSession(false);
            if (s == null || s.getAttribute("loginUser") == null) {
                return Map.of("ok", false, "msg", "로그인이 필요합니다.");
            }
            
            UserDTO user = (UserDTO) s.getAttribute("loginUser");
            int page = parseIntParam(req.getParameter("page"), 1);
            int size = parseIntParam(req.getParameter("size"), 10);
            
            List<Map<String, Object>> recipes = likesDAO.findLikedRecipesByUser(user.getUserId(), page, size);
            int total = likesDAO.countLikedRecipesByUser(user.getUserId());
            
            return Map.of(
                "ok", true,
                "recipes", recipes,
                "total", total,
                "page", page,
                "size", size
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", e.getMessage());
        }
    }
	*/
	
    public Object getMyOrders(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession s = req.getSession(false);
            if (s == null || s.getAttribute("loginUser") == null) {
                return Map.of("ok", false, "msg", "로그인이 필요합니다.");
            }
            
            UserDTO user = (UserDTO) s.getAttribute("loginUser");
            
            List<OrderDTO> orders = orderDAO.selectByUserId(user.getUserId());
            
            List<Map<String, Object>> orderList = orders.stream()
                .map(this::mapOrder)
                .collect(Collectors.toList());
            
            return Map.of(
                "ok", true,
                "orders", orderList,
                "total", orders.size()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", e.getMessage());
        }
    }
    /*
    public Object getMyPageSummary(HttpServletRequest req, HttpServletResponse resp) {
        try {
            HttpSession s = req.getSession(false);
            if (s == null || s.getAttribute("loginUser") == null) {
                return Map.of("ok", false, "msg", "로그인이 필요합니다.");
            }
            
            UserDTO user = (UserDTO) s.getAttribute("loginUser");
            
            int postCount = boardDAO.countByUser(user.getUserId());
            int likedRecipeCount = likesDAO.countLikedRecipesByUser(user.getUserId());
            List<OrderDTO> orders = orderDAO.selectByUserId(user.getUserId());
            int orderCount = orders.size();
            
            List<BoardDTO> recentPosts = boardDAO.listByUser(user.getUserId(), 1, 3);
            List<Map<String, Object>> recentRecipes = likesDAO.findLikedRecipesByUser(user.getUserId(), 1, 3);
            List<OrderDTO> recentOrders = orders.stream().limit(3).collect(Collectors.toList());
            
            return Map.of(
                "ok", true,
                "stats", Map.of(
                    "postCount", postCount,
                    "likedRecipeCount", likedRecipeCount,
                    "orderCount", orderCount
                ),
                "recentPosts", recentPosts.stream().map(this::mapPost).collect(Collectors.toList()),
                "recentRecipes", recentRecipes,
                "recentOrders", recentOrders.stream().map(this::mapOrder).collect(Collectors.toList())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of("ok", false, "msg", e.getMessage());
        }
    }
	*/
	
    private int parseIntParam(String param, int defaultValue) {
        if (param == null || param.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(param);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private Map<String, Object> mapPost(BoardDTO d) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("postId", d.getPostId());
        m.put("title", d.getTitle());
        m.put("content", d.getContent());
        m.put("category", d.getCategory());
        m.put("viewCount", d.getViewCount());
        m.put("commentCount", d.getCommentCount());
        m.put("likeCount", d.getLikeCount());
        m.put("createdAt", d.getCreatedAt() != null ? d.getCreatedAt().format(ISO) : null);
        m.put("nickname", d.getNickname());
        return m;
    }

    private Map<String, Object> mapOrder(OrderDTO o) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("orderId", o.getOrderId());
        m.put("totalPrice", o.getTotalPrice());
        m.put("deliveryFee", o.getDeliveryFee());
        m.put("shippingAddress", o.getShippingAddress());
        m.put("orderName", o.getOrderName());
        m.put("createdAt", o.getCreatedAt());
        m.put("status", o.isStatus());
        return m;
    }
}

