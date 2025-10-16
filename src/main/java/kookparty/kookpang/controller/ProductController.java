package kookparty.kookpang.controller;

import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kookparty.kookpang.dto.ProductDTO;
import kookparty.kookpang.service.ProductService;
import kookparty.kookpang.service.ProductServiceImpl;

public class ProductController implements Controller {
	ProductService productService = new ProductServiceImpl();
	
	public ModelAndView moveIngredients(HttpServletRequest request, HttpServletResponse response) {
		ServletContext application = request.getServletContext();
		String contextPath = application.getContextPath();
		return new ModelAndView(contextPath + "/orders/ingredients.jsp", true);
	}
	
	public List<ProductDTO> selectAll(HttpServletRequest request, HttpServletResponse response){
		List<ProductDTO> list = null;
		try {
			list = productService.selectAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
}
