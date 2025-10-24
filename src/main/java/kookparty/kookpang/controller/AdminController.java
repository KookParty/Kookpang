package kookparty.kookpang.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
		return new ModelAndView();
	}
}
