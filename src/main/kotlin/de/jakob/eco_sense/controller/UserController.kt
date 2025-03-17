package de.jakob.eco_sense.controller

import de.jakob.eco_sense.payload.request.LoginRequest
import de.jakob.eco_sense.payload.request.RegisterRequest
import de.jakob.eco_sense.payload.request.UserUpdateRequest
import de.jakob.eco_sense.payload.rersponse.SessionResponse
import de.jakob.eco_sense.payload.rersponse.UserResponse
import de.jakob.eco_sense.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterRequest): ResponseEntity<SessionResponse> {
        val sessionResponse = userService.registerUser(request)
        return ResponseEntity.ok(sessionResponse)
    }

    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginRequest): ResponseEntity<SessionResponse> {
        val sessionResponse = userService.loginUser(request)
        return ResponseEntity.ok(sessionResponse)
    }

    @DeleteMapping("/logout")
    fun logoutUser(@RequestHeader("Authorization") token: String): ResponseEntity<String> {
        userService.logoutUser(token)
        return ResponseEntity.ok("Successfully logged out")
    }

    @GetMapping("/me")
    fun getAuthenticatedUser(@RequestHeader("Authorization") token: String): ResponseEntity<UserResponse> {
        val userResponse = userService.getUserBySessionToken(token)
        return ResponseEntity.ok(userResponse)
    }

    @PutMapping("/me")
    fun updateUser(@RequestHeader("Authorization") token: String, @RequestBody updateRequest: UserUpdateRequest): ResponseEntity<UserResponse> {
        val updatedUser = userService.updateUser(token, updateRequest)
        return ResponseEntity.ok(updatedUser)
    }

    @DeleteMapping("/me")
    fun deleteUser(@RequestHeader("Authorization") token: String): ResponseEntity<String> {
        userService.deleteUser(token)
        return ResponseEntity.ok("User deleted successfully")
    }

    @GetMapping("/{id}")
    fun getUserProfile(@PathVariable id: String): ResponseEntity<UserResponse> {
        val userResponse = userService.getUserProfile(id)
        return ResponseEntity.ok(userResponse)
    }
}
