package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.VendingMachineAbstractTest;
import com.mvpmatch.vendingmachine.dto.DepositCoinRequest;
import com.mvpmatch.vendingmachine.dto.ProductRequest;
import com.mvpmatch.vendingmachine.dto.view.CoinsView;
import com.mvpmatch.vendingmachine.dto.view.ProductView;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.util.JsonUtils;
import com.mvpmatch.vendingmachine.utils.RequestBuilderHelper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class OperationControllerTest extends VendingMachineAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JsonUtils jsonUtils;

    @Autowired
    private RequestBuilderHelper requestBuilderHelper;


    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testResetDeposit_ReturnEmptyList() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/reset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CoinsView coinsView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), CoinsView.class);
        assertEquals(0, coinsView.getCoins().size());
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_20")
    public void testResetDeposit_ReturnCoinList() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/reset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        CoinsView coinsView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), CoinsView.class);
        assertEquals(1, coinsView.getCoins().size());
        assertEquals(2, coinsView.getCoins().get(0).getAmount());
        assertEquals(10, coinsView.getCoins().get(0).getValue());
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_50")
    public void testResetDeposit_ReturnBadRequest() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/reset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testResetDeposit_ReturnForbidden() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/reset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithAnonymousUser
    public void testResetDeposit_ReturnUnauthorized() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/reset")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_20")
    public void testDepositCoin_ReturnUpdatedDeposit() throws Exception {
        DepositCoinRequest depositCoinRequest = new DepositCoinRequest(50);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/deposit", depositCoinRequest))
                .andExpect(status().isOk())
                .andReturn();

        UserView userView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), UserView.class);
        assertEquals(70, userView.getDeposit());
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_20")
    public void testDepositInvalidCoin_ReturnBadRequest() throws Exception {
        DepositCoinRequest depositCoinRequest = new DepositCoinRequest(75);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/deposit", depositCoinRequest))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testDepositAsSeller_ReturnForbidden() throws Exception {
        DepositCoinRequest depositCoinRequest = new DepositCoinRequest(10);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/deposit", depositCoinRequest))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithAnonymousUser
    public void testDepositAsAnonymous_ReturnUnauthorized() throws Exception {
        DepositCoinRequest depositCoinRequest = new DepositCoinRequest(10);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/deposit", depositCoinRequest))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }


}
