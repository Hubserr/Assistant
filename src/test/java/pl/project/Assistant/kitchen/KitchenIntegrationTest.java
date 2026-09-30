package pl.project.Assistant.kitchen;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pl.project.Assistant.AbstractIntegrationTest;

import java.util.UUID;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class KitchenIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void recipeShowsIngredientAvailabilityAndIsHiddenFromOtherUsers() throws Exception {
        String tokenA = register();

        addToFridge(tokenA, """
                { "productName": "milk", "unit": "ML", "amount": 1000 }""");
        addToFridge(tokenA, """
                { "productName": "eggs", "unit": "PCS", "amount": 2 }""");

        String recipe = mockMvc.perform(post("/api/v1/kitchen/recipes")
                        .header("Authorization", "Bearer " + tokenA)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Pancakes",
                                  "servings": 2,
                                  "steps": ["Mix", "Fry"],
                                  "ingredients": [
                                    { "productName": "Milk", "amount": 500 },
                                    { "productName": "eggs", "amount": 3 },
                                    { "productName": "flour", "unit": "G", "amount": 250 }
                                  ]
                                }"""))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Integer recipeId = JsonPath.read(recipe, "$.id");

        mockMvc.perform(get("/api/v1/kitchen/recipes/{id}", recipeId)
                        .header("Authorization", "Bearer " + tokenA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ingredients", hasSize(3)))
                .andExpect(jsonPath("$.ingredients[?(@.productName == 'milk')].availability").value(contains("AVAILABLE")))
                .andExpect(jsonPath("$.ingredients[?(@.productName == 'milk')].availableAmount").value(contains(1000.0)))
                .andExpect(jsonPath("$.ingredients[?(@.productName == 'eggs')].availability").value(contains("INSUFFICIENT")))
                .andExpect(jsonPath("$.ingredients[?(@.productName == 'eggs')].availableAmount").value(contains(2.0)))
                .andExpect(jsonPath("$.ingredients[?(@.productName == 'flour')].availability").value(contains("MISSING")))
                .andExpect(jsonPath("$.ingredients[?(@.productName == 'flour')].availableAmount").value(contains(0)));

        String tokenB = register();

        mockMvc.perform(get("/api/v1/kitchen/recipes/{id}", recipeId)
                        .header("Authorization", "Bearer " + tokenB))
                .andExpect(status().isNotFound());
    }

    private String register() throws Exception {
        String email = "user-" + UUID.randomUUID() + "@test.com";
        String response = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                { "email": "%s", "password": "password123", "confirmPassword": "password123" }"""
                                .formatted(email)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        return JsonPath.read(response, "$.token");
    }

    private void addToFridge(String token, String body) throws Exception {
        mockMvc.perform(post("/api/v1/kitchen/fridge")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated());
    }
}
