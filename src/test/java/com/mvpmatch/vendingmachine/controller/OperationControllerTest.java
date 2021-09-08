package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.VendingMachineAbstractTest;
import com.mvpmatch.vendingmachine.dto.BuyProductsRequest;
import com.mvpmatch.vendingmachine.dto.DepositCoinRequest;
import com.mvpmatch.vendingmachine.dto.ProductRequest;
import com.mvpmatch.vendingmachine.dto.view.BuyView;
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

import java.util.HashMap;
import java.util.Map;

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

    @Test
    @Sql("/test_data.sql")
    @WithAnonymousUser
    public void testBuyAsAnonymous_ReturnUnauthorized() throws Exception {
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(1, 1);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/buy", buyProductsRequest))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testBuyAsSeller_ReturnForbidden() throws Exception {
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(1, 1);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/buy", buyProductsRequest))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_50")
    public void testBuyAsBuyer_ReturnInsufficientFunds() throws Exception {
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(5, 2);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/buy", buyProductsRequest))
                .andExpect(status().isBadRequest())
                .andReturn();


        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Insufficient funds", responseError.get("errors").get("exception"));
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_50")
    public void testBuyAsBuyer_ReturnInsufficientAmount() throws Exception {
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(7, 8);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/buy", buyProductsRequest))
                .andExpect(status().isBadRequest())
                .andReturn();


        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Insufficient amount", responseError.get("errors").get("exception"));
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_50")
    public void testBuyAsBuyer_ReturnBadRequestNonExistingProduct() throws Exception {
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(10, 2);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/buy", buyProductsRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Product doesn't exist", responseError.get("errors").get("exception"));
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_50")
    public void testBuyAsBuyer_ReturnBadRequestInsufficientChange() throws Exception {
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(7, 1);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/buy", buyProductsRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Insufficient coins", responseError.get("errors").get("exception"));
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account_50")
    public void testBuyAsBuyer_Success() throws Exception {
        BuyProductsRequest buyProductsRequest = new BuyProductsRequest(5, 1);

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/buy", buyProductsRequest))
                .andExpect(status().isOk())
                .andReturn();

        BuyView buyView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), BuyView.class);
        assertEquals(30, buyView.getSpent());
        assertEquals(30, buyView.getProducts().getCost());
        assertEquals(5, buyView.getProducts().getId());
        assertEquals(1, buyView.getChange().size());
        assertEquals(10, buyView.getChange().get(0).getValue());
        assertEquals(2, buyView.getChange().get(0).getAmount());
    }


}
