package com.example.clothingstore.controllers;

import com.example.clothingstore.dao.BasketDao;
import com.example.clothingstore.dao.CustomerDao;
import com.example.clothingstore.entities.Clothes;
import com.example.clothingstore.entities.Customer;
import com.example.clothingstore.entities.Orders;
import com.example.clothingstore.services.ClothesService;
import com.example.clothingstore.services.CustomerService;
import com.example.clothingstore.services.OrdersService;
import com.example.clothingstore.services.impl.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

@Controller
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    private final ClothesService clothesService;

    private final OrdersService ordersService;

    private final EmailSenderService senderService;

    public static long CURRENT_CUSTOMER_ID,CURRENT_CLOTHES_ID;
    public static Customer CURRENT_CUSTOMER_TO_REGISTRATION;

    public Integer numberToCheckEmail = 0;
    @GetMapping("/")
    public String start(Model model){
        model.addAttribute("customer", new Customer());

        return "log-in";
    }

    @RequestMapping(value = "/log_in", method = RequestMethod.GET)
    public String getLog(@ModelAttribute("customer") Customer customer, Model model){
        if(customerService.authorizedCustomer(customer.getUsername(), customer.getPassword())){
            //model.addAttribute("mensClothes", clothesService.clothesByGender("MEN"));
            //System.out.println(clothesService.clothesByGender("MEN"));
            model.addAttribute("kind",new Clothes());

            CURRENT_CUSTOMER_ID = customerService.getIdByUsername(customer.getUsername());
            System.out.println(CURRENT_CUSTOMER_ID + " Id user");
            return "intro-new";
        } else {
            model.addAttribute("message","Bad login or password");
            return "log-in";
        }
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String getMainPage(Model model){
        model.addAttribute("kind",new Clothes());

        return "intro-new";
    }




    @RequestMapping(value = "/choice", method = RequestMethod.GET)
    public String getChoice(@ModelAttribute("kind") Clothes clothes, Model model){
        System.out.println(clothes.getGender() + " Choice is ");
        model.addAttribute("clothesByChoice", clothesService.clothesByGender(clothes.getGender()));
        System.out.println(clothesService.clothesByGender(clothes.getGender()));
        if(clothes.getGender().equals("MEN")){
            System.out.println("here men");
            model.addAttribute("title","Men`s Clothing");
        } if(clothes.getGender().equals("WOMEN")){
            System.out.println("here wom");
            model.addAttribute("title","Women`s Clothing");
        } if(clothes.getGender().equals("KIDS")){
            System.out.println("here wom");
            model.addAttribute("title","Children's clothes");
        }
        return "main";
    }

    @RequestMapping(value = "/descriptionItem" , method = RequestMethod.GET)
    public String getDescriptionByName(@ModelAttribute("name") String name, Model model){

        model.addAttribute("allDescriptionAboutItem", clothesService.getClothesByName(name));
        CURRENT_CLOTHES_ID = clothesService.getClothesByName(name).getId();
        System.out.println(CURRENT_CLOTHES_ID + " Id Clothes");
        model.addAttribute("order", new Orders());

        return "about-item-prepare";
    }

    @RequestMapping(value = "/description" , method = RequestMethod.GET)
    public String getDescription(@ModelAttribute("item") Clothes clothes, Model model){
        model.addAttribute("allDescriptionAboutItem", clothesService.clothesById(clothes.getId()));
        //System.out.println(customerService.getCustomerById(1).getClothes());
        CURRENT_CLOTHES_ID = clothes.getId();
        System.out.println(CURRENT_CLOTHES_ID + " Id Clothes");
        model.addAttribute("order", new Orders());
        return "about-item-prepare";
    }

    @RequestMapping(value = "/makeOrder", method = RequestMethod.POST)
    public String makeOrder(@ModelAttribute("order") Orders order, Model model){
        System.out.println(order.getQuantity());

        model.addAttribute("clothesByChoice", clothesService.clothesByGender(clothesService.getClothesById(CURRENT_CLOTHES_ID).getGender()));
        System.out.println(CURRENT_CUSTOMER_ID);
//        System.out.println(clothesService.clothesByGender(clothesService.getClothesById(CURRENT_CLOTHES_ID).getGender()));
        ordersService.save(ordersService.createOrderEntityToMakeOrder(order.getQuantity()));

        return "main";
    }

    @RequestMapping(value = "/showBasket", method = RequestMethod.GET)
    public String showAllPurchases(Model model){
        model.addAttribute("stuff", ordersService.getOrdersByIdCustomer(CURRENT_CUSTOMER_ID));
//        System.out.println(ordersService.getOrdersByIdCustomer(CURRENT_CUSTOMER_ID));
        List<Orders> ordersForBasket = ordersService.getOrdersByIdCustomer(CURRENT_CUSTOMER_ID);
        List<Clothes> clothesForBasket ;
        try{
            clothesForBasket =  customerService.getCustomerById(CURRENT_CUSTOMER_ID).getClothes();
        }catch (NullPointerException e){
            clothesForBasket = new ArrayList<>();
        }

        model.addAttribute("infoStuff", clothesForBasket);

//        System.out.println("**********");
//        System.out.println(ordersForBasket);
//        System.out.println(clothesForBasket);
        model.addAttribute("basketCurrentPerson", ordersService.getBasketFromTwoList(ordersForBasket,clothesForBasket));
        Double totalPrice = ordersService.getBasketFromTwoList(ordersForBasket,clothesForBasket).stream().mapToDouble(BasketDao::getPrice).sum();
        model.addAttribute("totalPrice", totalPrice + 30);
        List<BasketDao> basketDaoList = new ArrayList<>();
        model.addAttribute("basketToFinalOrder", basketDaoList);
        model.addAttribute("name", new String());

        return "basket-restart";
    }


    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model){
        model.addAttribute("unregisteredCustomer", new Customer());
        return "registration";
    }

    @RequestMapping(value = "/tryToRegister", method = RequestMethod.GET)
    public String getRegister(@Valid @ModelAttribute("unregisteredCustomer") Customer customer, BindingResult result,Model model){
        Map<String, String> mapFields = new HashMap<>();
        int i = 1;
        if(result.hasErrors()){
            model.addAttribute("messageError", "Your data is incorrect");
            for (FieldError error : result.getFieldErrors()) {
                String fieldName = error.getField();
                String errorMessage = error.getDefaultMessage();

                // Отримання назви поля в класі Customer
                String fieldDisplayName = getFieldDisplayName(Customer.class, fieldName);

                System.out.println("Невалідне поле: " + fieldDisplayName);
//                System.out.println(customer.getPassword());
                mapFields.put(fieldDisplayName, fieldDisplayName + "Style");
                System.out.println("Повідомлення про помилку: " + errorMessage);
                i++;
            }

            for(Map.Entry<String, String> el : mapFields.entrySet()){
                String currentFieldName = el.getKey();
                String currentFieldAttribute = el.getValue();

                if(mapFields.containsKey(currentFieldName)){
                    model.addAttribute(currentFieldAttribute, "border-color: red;");
                }
            }
            return "registration";
        } else {
            model.addAttribute("number", new String());
            CURRENT_CUSTOMER_TO_REGISTRATION = customer;
            Random random = new Random();
            numberToCheckEmail = random.nextInt(9000 - 1000 + 1) + 1000;
            senderService.sendEmail(customer.getEmail(),"Code to confirm registration",String.valueOf(numberToCheckEmail));
            model.addAttribute("emailToSend", customer.getEmail());

            return "confirm-register";
        }
    }

    @RequestMapping(value = "/sendLetterAgain", method = RequestMethod.GET)
    public String  sendLetter(@ModelAttribute("emailToSend") String emailCustomer){
        Random random = new Random();
        numberToCheckEmail = random.nextInt(9000 - 1000 + 1) + 1000;
        System.out.println(emailCustomer);
        senderService.sendEmail(emailCustomer,"Code to confirm registration",String.valueOf(numberToCheckEmail));



        return "confirm-register";
    }



    @RequestMapping(value = "/getConfirmRegister", method = RequestMethod.GET)
    public String getConfirmRegister(@ModelAttribute("number") String number,@ModelAttribute("emailToSend") String emailCustomer,Model model){
//        System.out.println(number);
//        System.out.println(numberToCheckEmail);
        System.out.println(emailCustomer);
        if(number.equals(String.valueOf(numberToCheckEmail))){
            System.out.println("Successfully");
            CURRENT_CUSTOMER_TO_REGISTRATION.setDateRegistration(LocalDate.now());
            System.out.println(CURRENT_CUSTOMER_TO_REGISTRATION);
            customerService.save(CURRENT_CUSTOMER_TO_REGISTRATION);
            CURRENT_CUSTOMER_ID = customerService.getIdByUsername(CURRENT_CUSTOMER_TO_REGISTRATION.getUsername());
            model.addAttribute("customer", new Customer());
            return "intro-new";
        } else {
            System.out.println("Error");

            model.addAttribute("errorConfirmPassword", "this code is fail try again");

            return "confirm-register";
        }
    }




    private String getFieldDisplayName(Class<?> entityClass, String fieldName) {
        Field[] fields = entityClass.getDeclaredFields();

        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field.getName();
            }
        }

        return fieldName;
    }
}
