package com.example.demo

import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import

@Import(TestcontainersConfiguration::class)
@SpringBootTest
class DemoApplicationTests {
    @Autowired
    private lateinit var transferService: TransferService

    @Mock
    private lateinit var userRepository: UserRepository

    @Test
    fun `transfer should return true when successful`() {
        `when`(userRepository.findUserwithlock("1")).thenReturn(1)
        `when`(userRepository.findUserwithlock("2")).thenReturn(1)
        `when`(userRepository.changeBalance("1", -200)).thenReturn(1)
        `when`(userRepository.changeBalance("2", 200)).thenReturn(1)

        val result = transferService.transfer("1", "2", 200)

        assert(result==false)
    }
}
