package com.mvpmatch.vendingmachine.integration;

import com.mvpmatch.vendingmachine.VendingMachineAbstractTest;
import com.mvpmatch.vendingmachine.dto.AuthRequest;
import com.mvpmatch.vendingmachine.dto.UpdateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.AuthView;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.util.JsonUtils;
import com.mvpmatch.vendingmachine.utils.RequestBuilderHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class UserControllerIntegrationTest extends VendingMachineAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private RequestBuilderHelper requestBuilderHelper;

    private String authGetToken(String username, String password) throws Exception {
        AuthRequest successAuthRequest = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest("/api/login", successAuthRequest))
                .andExpect(status().isOk())
                .andReturn();

        return jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), AuthView.class).getJwt();
    }

    @Test
    @Sql("/test_data.sql")
    public void testLogin_ThenDeleteUser_ThenGetUser_ReturnUnauthorized() throws Exception {
        String jwtToken = authGetToken("buyer_account", "buyer_pass");

        MvcResult deleteRequestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/users/current")
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();

        MvcResult getRequestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/users/current")
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testLogin_ThenUpdateUsername_ThenLoginWithNewUsername_Success() throws Exception {
        String oldUsername = "buyer_account";
        String newUsername = "buyer_account_2";

        String oldJwtToken = authGetToken(oldUsername, "buyer_pass");

        UpdateUserRequest updateUserRequest = UpdateUserRequest.builder().username(newUsername).build();

        MvcResult updateRequestResult = this.mockMvc
                .perform(requestBuilderHelper.putRequest("/api/users/current", updateUserRequest)
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", oldJwtToken)))
                .andExpect(status().isOk())
                .andReturn();

        String newJwtToken = authGetToken(newUsername, "buyer_pass");
        MvcResult getRequestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/users/current")
                        .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", newJwtToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();


        UserView userView = jsonUtils.fromJson(getRequestResult.getResponse().getContentAsString(), UserView.class);
        assertEquals(newUsername, userView.getUsername());
    }

}
