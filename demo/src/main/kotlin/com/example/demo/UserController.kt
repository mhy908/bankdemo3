package com.example.demo

import jakarta.servlet.http.HttpSession
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.domain.AbstractPersistable_
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController {
    @Autowired
    private val userRepository: UserRepository? = null

    data class LoginRequest(val id: String, val password: String)

    @PostMapping("/login")
    fun getUserbyid(
        @RequestBody request: LoginRequest,
        session: HttpSession
    ): ResponseEntity<User>{
        val user: User? = userRepository!!.findUser(request.id)

        if(user==null || !BCryptPasswordEncoder().matches(request.password, user.password)){
            return ResponseEntity.notFound().build()
        }
        else{
            session.setAttribute("loginned", true)
            session.setAttribute("id", user.id)
            return ResponseEntity.ok(user)
        }
    }

}