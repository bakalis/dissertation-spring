package com.bakalis.spring.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bakalis.models.ContentsTuple;
import com.bakalis.spring.services.*;

@Controller
public class MappingController {
	
	@Autowired
	ContentsService contentsService;
	@Autowired 
	TransactionsService transactionsService;
	@Autowired
	ErrorLoggingService errorService;
	
	@GetMapping("/")
	public String rootURL(){
		return "redirect:/index";
	}
	
	// Get all the Contents of the Warehouse
	@GetMapping("/index")
	public String index(Model model){
		ArrayList<ContentsTuple> contents = contentsService.getAllContents();
		model.addAttribute("contents", contents);
		errorService.passErrorMessageToModel(model);
		return "index";
	}
	
	
	//Get the Filtered by the Search mechanic Contents of the Warehouse
	@PostMapping("/index")
	public String getSearchedContents(@RequestParam String searchBar, @RequestParam String searchFilter, Model model){
		ArrayList<ContentsTuple> contents = contentsService.getSearchedContents(searchBar, searchFilter);
		model.addAttribute("contents", contents);
		errorService.passErrorMessageToModel(model);
		return "index";
	}
	
	
	
	//Get the Entry Page
	@GetMapping("/entry") 
	public String getEntry(Model model)
	{
		model.addAttribute("categories", contentsService.getCategories());
		model.addAttribute("clients", contentsService.getClients());
		errorService.passErrorMessageToModel(model);
		return "entry";
	}
	
	//Add a new entry to the Database
	@PostMapping("/entry")
	public String addEntry(@RequestParam String productId, @RequestParam String productName,
			@RequestParam String category, @RequestParam String client,
			@RequestParam String quantity, @RequestParam String code, RedirectAttributes redirectAttributes)
	{
		transactionsService.manageEntry(productId, productName, category, client, quantity, code);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/index";
	}	
	
	
	// Get the Retrievals Page
	@GetMapping("/retrieval")
	public String getRetrieval(Model model){
		model.addAttribute("categories", contentsService.getCategories());
		model.addAttribute("clients", contentsService.getClients());
		errorService.passErrorMessageToModel(model);
		return "retrieval";
	}
	
	
	//Add a new Retrieval to the Database
	@PostMapping("/retrieval")
	public String addRetrieval(@RequestParam String productId, @RequestParam String productName,
			@RequestParam String category, @RequestParam String client,
			@RequestParam String quantity, @RequestParam String code, RedirectAttributes redirectAttributes)
	{
		transactionsService.manageRetrieval(productId, productName, category, client, quantity, code);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/index";
	}	
	
	
	
	//Get the View Clients Page
	@GetMapping("/clients")
	public String getClients(Model model){
		model.addAttribute("clients", contentsService.getClients());
		errorService.passErrorMessageToModel(model);
		return "clients";
	}
	
	
	//Get the clients based on the Search Mechanic
	@PostMapping("/clients")
	public String getSearchedClients(@RequestParam String searchBar, Model model){
		model.addAttribute("clients", contentsService.getSearchedClients(searchBar));
		errorService.passErrorMessageToModel(model);
		return "clients";
	}
	
	//Get the Add Client page
	@GetMapping("/addClient")
	public String getAddClient(Model model){
		errorService.passErrorMessageToModel(model);
		return "addClient";
	}
	
	
	//Add a new Client to the Database
	@PostMapping("/addClient")
	public String addClient(@RequestParam String clientId, @RequestParam String clientName, RedirectAttributes redirectAttributes){
		transactionsService.addClient(clientId, clientName);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/clients";
	}
	
	
	//Delete a Client from the Database when the Remove link is pressed
	@GetMapping("/clients/{deleteId}")
	public String deleteClient(@PathVariable String deleteId, RedirectAttributes redirectAttributes){
		transactionsService.deleteClient(deleteId);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/clients";
	}
	
	//Get the addClient Page with the Client to edit
	@GetMapping("/addClient/{editedId}")
	public String getEditClient(@PathVariable String editedId, Model model){
		model.addAttribute("client", contentsService.getEditedClient(editedId));
		errorService.passErrorMessageToModel(model);
		return "addClient";	
	}
	
	//Post the Edited Client to the Database
	@PostMapping("/addClient/{editedId}")
	public String editClient(@PathVariable String editedId, @RequestParam String clientName, RedirectAttributes redirectAttributes){
		transactionsService.editClient(editedId, clientName);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/clients";
	}
	
	//Get the View Categories Page
	@GetMapping("/categories")
	public String getCategories(Model model){
		model.addAttribute("categories", contentsService.getCategories());
		errorService.passErrorMessageToModel(model);
		return "categories";
	}
	//Get the categories based on the Search Mechanic
	@PostMapping("/categories")
	public String getSearchedCategories(@RequestParam String searchBar, Model model){
		model.addAttribute("categories", contentsService.getSearchedCategories(searchBar));
		errorService.passErrorMessageToModel(model);
		return "categories";
	}
	
	//Get the Add Category page
	@GetMapping("/addCategory")
	public String getAddCategory(Model model){
		errorService.passErrorMessageToModel(model);
		return "addCategory";
	}
	
	//Add a new Category to the Database
	@PostMapping("/addCategory")
	public String addCategory(@RequestParam String categoryId, @RequestParam String categoryName,
			RedirectAttributes redirectAttributes){
		transactionsService.addCategory(categoryId, categoryName);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/categories";
	}
	
	//Delete a Category from the Database when the Remove link is pressed
	@GetMapping("/categories/{deleteId}")
	public String deleteCategory(@PathVariable String deleteId, RedirectAttributes redirectAttributes){
		transactionsService.deleteCategory(deleteId);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/categories";
	}
	
	//Get the addCategory Page with the Category to edit
	@GetMapping("/addCategory/{editedId}")
	public String getEditCategory(@PathVariable String editedId, Model model){
		model.addAttribute("category", contentsService.getEditedCategory(editedId));
		errorService.passErrorMessageToModel(model);
		return "addCategory";	
	}
	
	//Post the Edited Category to the Database
	@PostMapping("/addCategory/{editedId}")
	public String editCategory(@PathVariable String editedId, @RequestParam String categoryName,
			RedirectAttributes redirectAttributes){
		System.out.println(editedId);
		transactionsService.editCategory(editedId, categoryName);
		errorService.passErrorMessageToRedirectAttributes(redirectAttributes);
		return "redirect:/categories";
	}
	
	//Get the Past Transactions Page
	@GetMapping("/transactions")
	public String getTransactions(Model model){
		model.addAttribute("transactions", contentsService.getPastTransactions());
		errorService.passErrorMessageToModel(model);
		return "transactions";
	}
}
