package com.mvpmatch.vendingmachine.controller;

import com.mvpmatch.vendingmachine.VendingMachineAbstractTest;
import com.mvpmatch.vendingmachine.dto.ProductRequest;
import com.mvpmatch.vendingmachine.dto.UpdateUserRequest;
import com.mvpmatch.vendingmachine.dto.view.ProductView;
import com.mvpmatch.vendingmachine.dto.view.UserView;
import com.mvpmatch.vendingmachine.util.JsonUtils;
import com.mvpmatch.vendingmachine.utils.RequestBuilderHelper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class ProductControllerTest extends VendingMachineAbstractTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RequestBuilderHelper requestBuilderHelper;

    @Autowired
    private JsonUtils jsonUtils;

    @Test
    @WithAnonymousUser
    public void testCreateProductAsAnonymous_ReturnUnauthorized() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/products", ProductRequest.builder().build()))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testCreateProductAsBuyer_ReturnForbidden() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/products", ProductRequest.builder().build()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testCreateProductAsSeller_Success() throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .productName("ProductName1")
                .cost(100)
                .amountAvailable(10)
                .build();

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/products", productRequest))
                .andExpect(status().isOk())
                .andReturn();

        ProductView productView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), ProductView.class);
        assertEquals(productRequest.getProductName(), productView.getProductName());
        assertEquals(productRequest.getCost(), productView.getCost());
        assertEquals(productRequest.getAmountAvailable(), productView.getAmountAvailable());
    }

    @ParameterizedTest
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    @ValueSource(ints = {0, 2, 12, 133})
    public void testCreateProductWithInvalidCost_ReturnBadRequest(Integer cost) throws Exception {
        ProductRequest productRequest = ProductRequest.builder()
                .productName("ProductName1")
                .cost(cost)
                .amountAvailable(10)
                .build();

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.postRequest(
                        "/api/products", productRequest))
                .andExpect(status().isBadRequest())
                .andReturn();

        Map<String, Map<String, String>> responseError = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), HashMap.class);
        assertEquals("Invalid price", responseError.get("errors").get("cost"));
    }


    @Test
    @WithAnonymousUser
    public void testDeleteProductAsAnonymous_ReturnUnauthorized() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testDeleteProductAsSeller_Success() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testDeleteProductFromAnotherSeller_ReturnNotFound() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/products/4")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testDeleteNonExistingProduct_ReturnNotFound() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .delete("/api/products/1234")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testViewProducts_Success() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        List<ProductView> productViews = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), List.class);
        assertEquals(5, productViews.size());
    }

    @Test
    @Sql("/test_data.sql")
    @WithAnonymousUser
    public void testViewProductsAsAnonymous_ReturnUnauthorized() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testViewProduct_Success() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        ProductView productView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), ProductView.class);
        assertEquals("product2", productView.getProductName());
        assertEquals(2, productView.getId());
        assertEquals(20, productView.getCost());
        assertEquals(0, productView.getAmountAvailable());
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testViewNonExistingProduct_ReturnNotFound() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products/2222")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account_2")
    public void testViewSoftDeletedProduct_ReturnNotFound() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(MockMvcRequestBuilders
                        .get("/api/products/6")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("buyer_account")
    public void testUpdatesProductAsBuyer_ReturnForbidden() throws Exception {
        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.putRequest("/api/products/1", ProductRequest.builder().build()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testUpdateProductFromAnotherSeller_ReturnNotFound() throws Exception {
        ProductRequest updateProductRequest = ProductRequest.builder()
                .cost(10)
                .amountAvailable(0)
                .productName("productName")
                .build();

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.putRequest("/api/products/4", updateProductRequest))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    @Sql("/test_data.sql")
    @WithUserDetails("seller_account")
    public void testUpdateProductAsSeller_Success() throws Exception {
        ProductRequest updateProductRequest = ProductRequest.builder()
                .cost(10)
                .amountAvailable(0)
                .productName("productName")
                .build();

        MvcResult requestResult = this.mockMvc
                .perform(requestBuilderHelper.putRequest("/api/products/2", updateProductRequest))
                .andExpect(status().isOk())
                .andReturn();

        ProductView productView = jsonUtils.fromJson(requestResult.getResponse().getContentAsString(), ProductView.class);
        assertEquals(updateProductRequest.getCost(), productView.getCost());
        assertEquals(updateProductRequest.getAmountAvailable(), productView.getAmountAvailable());
        assertEquals(updateProductRequest.getProductName(), productView.getProductName());
    }

}
