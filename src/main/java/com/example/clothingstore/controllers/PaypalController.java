package com.example.clothingstore.controllers;

import com.example.clothingstore.dao.BasketDao;
import com.example.clothingstore.entities.Clothes;
import com.example.clothingstore.entities.Orders;
import com.example.clothingstore.services.ClothesService;
import com.example.clothingstore.services.CustomerService;
import com.example.clothingstore.services.OrdersService;
import com.example.clothingstore.services.impl.EmailSenderService;
import com.example.clothingstore.services.impl.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static com.example.clothingstore.controllers.CustomerController.CURRENT_CUSTOMER_ID;

@Controller
public class PaypalController {

    @Autowired
    PaypalService service;

    @Autowired
    CustomerService customerService;

    @Autowired
    ClothesService clothesService;

    @Autowired
    OrdersService ordersService;

    @Autowired
    EmailSenderService emailSenderService;

    public static final String SUCCESS_URL = "pay/success";
    public static final String CANCEL_URL = "pay/cancel";

    public static Double finalSum;

    @GetMapping("/home")
    public String home() {
        return "home";
    }



//    @ModelAttribute("order") Order order
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public String payment(HttpServletRequest request) {

        //Trash
        List<String> basketCurrentPersonList;
        try{
            basketCurrentPersonList = Arrays.asList(request.getParameterValues("basketCurrentPerson"));
        }catch (NullPointerException e){
            return "basket-null";
        }
        Iterator<String> iterator = basketCurrentPersonList.iterator();
        ArrayList<BasketDao> basketDao = new ArrayList<>();

        while (iterator.hasNext()){
            BasketDao item = new BasketDao();
            String element = iterator.next();
            item.setName(element);

            element = iterator.next();
            item.setImage(element);

            element = iterator.next();
            item.setPriceByOne(Double.valueOf(element));

            element = iterator.next();
            item.setPrice(Double.valueOf(element));

            element = iterator.next();
            item.setQuantity(Integer.valueOf(element));

            System.out.println(item);
            basketDao.add(item);
        }

        finalSum = basketDao.stream().mapToDouble(BasketDao::getPrice).sum();
        System.out.println(finalSum);

        System.out.println(basketDao);
        try {
            System.out.println("Hiiiiiiii");
            Payment payment = service.createPayment(finalSum, "USD", "PAYPAL",
                    "SALE", "You have to pay", "https://spring-boot-shop-84473a91f755.herokuapp.com/" + CANCEL_URL,
                    "https://spring-boot-shop-84473a91f755.herokuapp.com/" + SUCCESS_URL);
            for(Links link:payment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    return "redirect:"+link.getHref();
                }
            }

        } catch (PayPalRESTException e) {

            e.printStackTrace();
        }
        return "redirect:/home";
    }

    @GetMapping(value = CANCEL_URL)
    public String cancelPay() {
        return "cancel";
    }

    @GetMapping(value = SUCCESS_URL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            List<Orders> ordersForBasket = ordersService.getOrdersByIdCustomer(CURRENT_CUSTOMER_ID);
            List<Clothes> clothesForBasket =  customerService.getCustomerById(CURRENT_CUSTOMER_ID).getClothes();
            Payment payment = service.executePayment(paymentId, payerId);
            System.out.println(payment.toJSON());
            if (payment.getState().equals("approved")) {
                System.out.println(customerService.getCustomerById(CURRENT_CUSTOMER_ID).getClothes());
                String message = "Total price is " + finalSum + "\nif you want to communicate with customer you might to use this email\n" +
                        customerService.getCustomerById(CURRENT_CUSTOMER_ID).getEmail() + "\nname is "
                        + customerService.getCustomerById(CURRENT_CUSTOMER_ID).getName() + "\n" + "Order list \n" +
                        basketToMessage(ordersService.getBasketFromTwoList(ordersForBasket,clothesForBasket));


                emailSenderService.sendEmail("daniilzotinjava@gmail.com","You have received new order", message);
                ordersService.deleteByCustomerId(CURRENT_CUSTOMER_ID);
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
        }
        return "redirect:/home";
    }


    public StringBuilder basketToMessage(List<BasketDao> basket){

        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("-------------------------------\n");
        for(BasketDao el : basket){
            resultMessage.append("Model " + el.getName()).append("\n");
            resultMessage.append("Quantity " + el.getQuantity()).append("\n");
            resultMessage.append("Gender " + clothesService.getClothesByName(el.getName()).getGender()).append("\n");
            resultMessage.append("Price " + el.getPrice()).append("\n");
            resultMessage.append("-------------------------------\n");
        }

        return resultMessage;
    }

}