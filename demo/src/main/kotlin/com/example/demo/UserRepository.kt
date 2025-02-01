package com.example.demo
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
interface UserRepository : CrudRepository<User?, Int?> {

    @Modifying
    @Transactional
    @Query("INSERT INTO users (id, password, name, email) VALUES (:id, :password, :name, :email)", nativeQuery = true)
    fun addUser(
        @Param("id") id: String,
        @Param("password") password: String,
        @Param("name") name: String,
        @Param("email") email: String
    )

    @Query("SELECT * FROM users WHERE id = :id", nativeQuery = true)
    fun findUser(
        @Param("id") id: String
    ): User?

    @Query("SELECT * FROM users", nativeQuery = true)
    fun allUser(): List<User>
}