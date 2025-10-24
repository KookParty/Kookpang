package kookparty.kookpang.controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.service.OrderService;
import kookparty.kookpang.service.OrderServiceImpl;
import kookparty.kookpang.service.ProductService;
import kookparty.kookpang.service.ProductServiceImpl;
import kookparty.kookpang.service.RecipeService;
import kookparty.kookpang.service.RecipeServiceImpl;

public class AdminController implements Controller {
	ProductService productService = new ProductServiceImpl();
	OrderService orderService = new OrderServiceImpl();
	RecipeService recipeService = RecipeServiceImpl.getInstance();
	
	public ModelAndView adminPage(HttpServletRequest request, HttpServletResponse response) {
		
		return new ModelAndView("/admin/admin-main.jsp");
	}
	
	public ModelAndView productList(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ProductDTO> productList = productService.selectAll();
			request.setAttribute("list", productList);
		} catch (SQLException e) {
			request.setAttribute("errMsg", "잘못된 접근입니다?");
			e.printStackTrace();
		}
		return new ModelAndView("/admin/products.jsp");
	}
	
	public ModelAndView deleteProducts(HttpServletRequest request, HttpServletResponse response) {
		String contextPath = request.getContextPath();
		String[] items = request.getParameterValues("selectedItems");
		if(items == null || items.length == 0) {
			request.setAttribute("errMsg", "선택된 항목이 없습니다.");
		    return new ModelAndView("/common/error.jsp");
		}
		try {
			int result = productService.deleteProduct(items);
			if(result == 0) {
				request.setAttribute("errMsg", "삭제에 실패했습니다.");
				return new ModelAndView("/common/error.jsp");
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("errMsg", "삭제에 실패했습니다.");
			return new ModelAndView("/common/error.jsp");
		}
		
		return new ModelAndView(contextPath + "/front?key=admin&methodName=productList", true);
	}
}
