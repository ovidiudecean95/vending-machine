package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.VendingMachineAbstractTest;
import com.mvpmatch.vendingmachine.dto.AuthRequest;
import com.mvpmatch.vendingmachine.dto.CreateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.AuthView;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.util.JsonUtils;
import com.mvpmatch.vendingmachine.utils.RequestBuilderHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class TestAuthenticationController extends VendingMachineAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private RequestBuilderHelper requestBuilderHelper;

    @Test
    public void testRegisterSuccess() throws Exception {
        CreateUserRequest goodRequest =
                CreateUserRequest.builder()
                        .username("andrew.dan")
                        .password("abc123DC")
                        .role("BUYER")
                        .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/register", goodRequest))
                .andExpect(status().isOk())
                .andReturn();

        UserView userView = jsonUtils.fromJson(createResult.getResponse().getContentAsString(), UserView.class);
        assertEquals(goodRequest.getUsername(), userView.getUsername());
        assertEquals(goodRequest.getRole(), userView.getRole());
        assertNotNull(userView.getId());
    }

    @Test
    public void testRegisterFailBlankUsername() throws Exception {
        CreateUserRequest blankUsernameRequest = CreateUserRequest.builder()
                .username(null)
                .password("abc123DC")
                .role("BUYER")
                .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/register", blankUsernameRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(createResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Username must not be blank", responseError.get("errors").get("username"));
    }

    @Test
    public void testRegisterFailUsernameShort() throws Exception {
        CreateUserRequest shortUsernameRequest = CreateUserRequest.builder()
                .username("ab")
                .password("abc123DC")
                .role("BUYER")
                .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/register", shortUsernameRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(createResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Username length should have 6-20 characters", responseError.get("errors").get("username"));
    }

    @Test
    public void testRegisterFailIncorrectRole() throws Exception {
        CreateUserRequest incorrectRoleRequest = CreateUserRequest.builder()
                .username("andrew.dan")
                .password("abc123DC")
                .role("wrong_role")
                .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/register", incorrectRoleRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(createResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Role doesn't exist", responseError.get("errors").get("role"));
    }

    @Test
    public void testRegisterFailPasswordShort() throws Exception {
        CreateUserRequest shortPasswordRequest = CreateUserRequest.builder()
                .username("andrew.dan")
                .password("abc")
                .role("SELLER")
                .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/register", shortPasswordRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(createResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Password length should have 8-20 characters", responseError.get("errors").get("password"));
    }

    @Test @Sql("/test_data.sql")
    public void testRegisterFailExistingUsername() throws Exception {
        CreateUserRequest existingUsernameRequest = CreateUserRequest.builder()
                .username("buyer_account")
                .password("abc123DC")
                .role("BUYER")
                .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/register", existingUsernameRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(createResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Username already exists", responseError.get("errors").get("username"));
    }

    @Test @Sql("/test_data.sql")
    public void testLoginSuccess() throws Exception {
        AuthRequest successAuthRequest = AuthRequest.builder()
                .username("buyer_account")
                .password("buyer_pass")
                .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/login", successAuthRequest))
                .andExpect(status().isOk())
                .andReturn();

        AuthView authView = jsonUtils.fromJson(createResult.getResponse().getContentAsString(), AuthView.class);
        assertNotNull(authView.getJwt());
    }


    @Test @Sql("/test_data.sql")
    public void testLoginFail() throws Exception {
        AuthRequest failAuthRequest = AuthRequest.builder()
                .username("buyer_account")
                .password("wrong_password")
                .build();

        MvcResult createResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/login", failAuthRequest))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

}
