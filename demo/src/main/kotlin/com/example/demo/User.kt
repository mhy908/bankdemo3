package com.example.demo

import jakarta.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY: MySQL의 AUTO_INCREMENT와 연동
    val uid: Long = 0,

    @Column(nullable = false, unique = true)
    val id: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false)
    val name: String,

    @Column(nullable = false, unique = true)
    val email: String
)