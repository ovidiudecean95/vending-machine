package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.VendingMachineAbstractTest;
import com.mvpmatch.vendingmachine.dto.UpdateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.util.JsonUtils;
import com.mvpmatch.vendingmachine.utils.RequestBuilderHelper;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;


@AutoConfigureMockMvc
public class UserControllerTest extends VendingMachineAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private RequestBuilderHelper requestBuilderHelper;

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testViewCurrentUser_Success() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        UserView userView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), UserView.class);
        assertEquals("buyer_account", userView.getUsername());
    }

    @Test
    @WithAnonymousUser
    public void testViewCurrentUserAsAnonymous_ReturnUnauthorized() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testDeleteCurrentUser_Success() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @WithAnonymousUser
    public void testDeleteCurrentUserAsAnonymous_ReturnUnauthorized() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/users/current")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testUpdateCurrentUser_Success() throws Exception {
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().username("buyer_account_2").build();

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.putRequest("/api/users/current", updateUserRequest))
                .andExpect(status().isOk())
                .andReturn();

        UserView userView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), UserView.class);
        assertEquals(updateUserRequest.getUsername(), userView.getUsername());
    }

    @Test
    @WithAnonymousUser
    public void testUpdateCurrentUserAsAnonymous_ReturnUnauthorized() throws Exception {
        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().username("andrei12").build();

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.putRequest("/api/users/current", updateUserRequest))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

}
