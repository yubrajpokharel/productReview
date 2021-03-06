package com.product.review.controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.product.review.model.Category;
import com.product.review.model.Editor;
import com.product.review.model.ProductBrand;
import com.product.review.service.CategoryService;
import com.product.review.service.ProductBrandService;

/**
 * Handles requests for the application home page.
 */
@Controller
@EnableTransactionManagement
public class ProductBrandController {

	@Autowired
	private ProductBrandService productBrandService;
	
	@Autowired
	private CategoryService categoryService;

	@InitBinder
	protected void initBinder(WebDataBinder binder){
	    binder.registerCustomEditor(Category.class, new Editor(categoryService));
	}
		
	@CrossOrigin(origins = "http://localhost:3000/dashboard")
	@RequestMapping(value="/api/brand", method=RequestMethod.GET)
	public @ResponseBody List<ProductBrand> getAllBrands(){
		List<ProductBrand> test=(List<ProductBrand>) productBrandService.findAll();
		return test;
	}
	
	@RequestMapping(value = "/auth/admin/brand", method = RequestMethod.GET)
	public String getAllBrand(Model model) {
		List<ProductBrand> brand= (List<ProductBrand>) productBrandService.findAll();
		if(brand==null){
			//model.addAttribute("brand",brand.add(new ProductBrand()) );
		}else
		model.addAttribute("brand",brand);
		return "brand";
	}

	@RequestMapping(value = "/auth/admin/brand/add", method = RequestMethod.GET)
	public String addBrand(Model model) {
		ProductBrand productBrand=new ProductBrand();
		//List<Product>product=new ArrayList<Product>();
		model.addAttribute("productBrand",productBrand);
		//model.addAttribute("product",product);
		Map<Category,String>category=new HashMap<Category,String>();
		for(Category c:categoryService.findAll()){
			category.put(c,c.getCategoryName());
		}
		model.addAttribute("catz",category);
		return "add-brand";
	}

	@RequestMapping(value = "/auth/admin/brand/add", method = RequestMethod.POST)
	public String saveBrand(@ModelAttribute ProductBrand productBrand) {
		System.out.println("I was here");
		productBrandService.create(productBrand);
		return "redirect:/auth/admin/brand";
	}

	@RequestMapping(value = "/auth/admin/brand/edit", params = { "id" }, method = RequestMethod.GET)
	public String editBrand(@RequestParam("id") long id, Model model) {
        
		model.addAttribute("productBrand", productBrandService.findOne(id));
		Set<String>category=new HashSet<String>();
		for(Category c:categoryService.findAll()){
			category.add(c.getCategoryName());
		}
		model.addAttribute("catz",category);
		return "add-brand";
	}

	@RequestMapping(value = "/auth/admin/brand/edit", method = RequestMethod.POST)
	public String saveEditedBrand(@RequestParam(value = "_submit", required = false) String submit,
			@RequestParam(value = "_cancel", required = false) String cancel, @ModelAttribute ProductBrand productBrand) {
		System.out.println(productBrand.getBrandId());
		if (cancel != null) {
			return "redirect:/auth/admin/brand";
		} else {
			productBrandService.update(productBrand);
		}
		return "redirect:/auth/admin/brand";
	}

	@RequestMapping(value = "/auth/admin/brand/remove", params = { "id" }, method = RequestMethod.GET)
	public String deleteBrand(@RequestParam("id") long id, Model model) {
		productBrandService.delete(productBrandService.findOne(id));
		return "redirect:/auth/admin/brand";
	}
}
