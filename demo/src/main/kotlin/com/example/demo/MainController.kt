package com.example.demo

import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*

@Controller // This means that this class is a Controller
class MainController {
    @Autowired
    private val userRepository: UserRepository? = null

    @Autowired
    private val transferService: TransferService? = null

    @GetMapping("/")
    fun init(
        model: Model,
        session: HttpSession
    ): String {
        if(session.getAttribute("loginned")==true){
            model.addAttribute("loginned", true)
            val userID: String = session.getAttribute("id")!!.toString()
            val user: User? = userRepository!!.findUser(userID)
            model.addAttribute("id", user!!.id)
            model.addAttribute("name", user.name)
            model.addAttribute("email", user.email)
            model.addAttribute("balance", user.balance)
        }
        return "init"
    }

    @GetMapping("/login")
    fun login(
        model: Model,
        session: HttpSession
    ): String {
        return "login"
    }

    @GetMapping("/signup")
    fun signup(
        model: Model,
        session: HttpSession
    ): String {
        return "signup"
    }
    @PostMapping("/signup") // Map ONLY POST Requests
    fun register(
        @RequestParam id: String,
        @RequestParam password: String,
        @RequestParam name: String,
        @RequestParam email: String,
        model: Model,
        session: HttpSession
    ): String {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        if((userRepository!!.findUser(id))!=null){
            model.addAttribute("error", true)
            return "signup"
        }
        model.addAttribute("error", false)

        userRepository.addUser(id, BCryptPasswordEncoder().encode(password), name, email)
        return "redirect:/login"
    }

    @GetMapping("/logout")
    fun logout(
        model: Model,
        session: HttpSession
    ): String{
        session.invalidate()
        return "redirect:/"
    }

    @GetMapping("/admin")
    fun admin(
        model: Model,
        session: HttpSession
    ): String{
        if(session.getAttribute("id")!="admin"){
            return "redirect:/"
        }
        model.addAttribute("users", userRepository!!.allUser())
        return "admin"
    }

    @GetMapping("/transfer")
    fun transfer(
        @RequestParam msg: Char?,
        model: Model,
        session: HttpSession
    ): String{
        if(session.getAttribute("id")==null){
            return "redirect:/"
        }
        val id: String = session.getAttribute("id").toString()
        val user: User? = userRepository!!.findUser(id)
        if(user==null){
            return "redirect:/"
        }
        model.addAttribute("balance", user.balance)
        if(msg!=null){
            if(msg=='Y'){
                model.addAttribute("message", "Success")
                model.addAttribute("success", true)
            }
            if(msg=='N'){
                model.addAttribute("message", "Failed")
                model.addAttribute("success", false)
            }
        }
        return "transfer"
    }

    @PostMapping("/transfer")
    fun processTransfer(
        @RequestParam toID: String,
        @RequestParam amount: Long,
        model: Model,
        session: HttpSession
    ): String {
        if(session.getAttribute("id")==null){
            return "redirect:/"
        }
        val id: String = session.getAttribute("id").toString()
        val fromUser: User? = userRepository!!.findUser(id)
        if(fromUser==null){
            return "redirect:/"
        }
        val success: Boolean = transferService!!.transfer(id, toID, amount)
        if (success) {
            return "redirect:/transfer?msg=Y"
        } else {
            return "redirect:/transfer?msg=N"
        }
    }
}