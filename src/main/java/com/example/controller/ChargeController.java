package com.example.controller;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.model.ChargeRequest;
import com.example.model.Event;
import com.example.model.Purchase;
import com.example.model.User;
import com.example.model.ChargeRequest.Currency;
import com.example.repository.UserRepository;
import com.example.service.EventService;
import com.example.service.PurchaseService;
import com.example.service.StripeService;
import com.example.service.UserService;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;

import net.glxn.qrgen.javase.QRCode;

@Controller
public class ChargeController {
 
    @Autowired
    private StripeService paymentsService;
    
	@Autowired
	private PurchaseService purchaseService;

	@Autowired
	private EventService eventService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private UserRepository userRepository;
 	
	@RequestMapping(value = "/charge/{eventId}", method = RequestMethod.POST)
	public ModelAndView charge(@PathVariable int eventId, ChargeRequest chargeRequest) throws StripeException {
		ModelAndView modelAndView = new ModelAndView();
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUserByEmail(auth.getName());
		Event event = eventService.getEvent(eventId);
		User seller = userService.findUserByEmail(event.getEmail());
		
		chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.USD);
        Charge charge = paymentsService.charge(chargeRequest);
        
        double price1 = charge.getAmount();
        double price2 = price1/100;
        double pay = (price1-30)/100;
        modelAndView.addObject("id", charge.getId());
        modelAndView.addObject("status", charge.getStatus());
        modelAndView.addObject("chargeAmount", price2);      
        modelAndView.addObject("balance_transaction", charge.getBalanceTransaction());
        modelAndView.addObject("userName", user.getName() +" " + user.getLastName());
		
        
		Purchase purchase = purchaseService.createPurchase(user, event, price2, charge.getId());
		double pricefee = round(0.05 * pay, 2);
		//TODO add price to seller balance, (-5% from them +$0.30 on user purchase) 
		seller.setBalance(seller.getBalance() + (pay-pricefee));
		userRepository.save(seller);	
		modelAndView.addObject("purchase", purchase);
		
		File file = QRCode.from(charge.getId()).file();
		File qrcode = new File("images/"+charge.getId()+".png");
		file.renameTo(qrcode);
		modelAndView.addObject("qrCode", qrcode);
		System.out.println(qrcode.getPath());
		
		modelAndView.setViewName("purchase");
		
		return modelAndView;
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/*
    @PostMapping("/charge/{eventId}")
    public String charge(ChargeRequest chargeRequest, Model model)
      throws StripeException {
        chargeRequest.setDescription("Example charge");
        chargeRequest.setCurrency(Currency.USD);
        Charge charge = paymentsService.charge(chargeRequest);
        model.addAttribute("id", charge.getId());
        model.addAttribute("status", charge.getStatus());
        model.addAttribute("chargeId", charge.getId());
        model.addAttribute("balance_transaction", charge.getBalanceTransaction());

        
        return "result";
    } */
 
    @ExceptionHandler(StripeException.class)
    public String handleError(Model model, StripeException ex) {
        model.addAttribute("error", ex.getMessage());
        return "result";
    }
}
