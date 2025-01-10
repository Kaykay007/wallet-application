//package com.korede.wallet.controller;
//
//import com.korede.wallet.entity.Account;
//import com.korede.wallet.entity.User;
//import com.korede.wallet.model.enums.AccountStatus;
//import com.korede.wallet.repository.AccountRepository;
//import com.korede.wallet.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//import org.testcontainers.containers.PostgreSQLContainer;
//
//import java.math.BigDecimal;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@SpringJUnitConfig
//@SpringBootTest
//@AutoConfigureMockMvc
//@Transactional
//public class WalletControllerIntegrationTest {
//    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:latest")
//            .withDatabaseName("test_db")
//            .withUsername("sa")
//            .withPassword("password");
//
//    @DynamicPropertySource
//    static void configureDatabase(DynamicPropertyRegistry registry) {
//        postgresContainer.start();
//        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
//        registry.add("spring.datasource.username", postgresContainer::getUsername);
//        registry.add("spring.datasource.password", postgresContainer::getPassword);
//    }
//
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @BeforeEach
//    public void setUp() {
////        User user = new User();
////        user.setId(3L);
////        user.setUsername("testUser");
////        user.setEmail("test@example.com");
////        user.setHashedPassword("hashedPassword");
////        userRepository.save(user);
////
////        Account account = new Account();
////        account.setId(1L);
////        account.setBalance(BigDecimal.valueOf(10615));
////        accountRepository.save(account);
//    }
//
//    @Test
//    public void testGetBalance_HappyPath() throws Exception {
//        mockMvc.perform(get("/api/v1/wallet/balance?userId=3")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data").value(10615))
//                .andExpect(jsonPath("$.message").value("Balance retrieved successfully"));
//    }
//
//    @Test
//    public void testGetTransactionHistory_HappyPath() throws Exception {
//        mockMvc.perform(get("/api/v1/wallet/transactions?userId=3&page=0&size=1")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data.totalRecords").value(15L))
//                .andExpect(jsonPath("$.message").value("Transaction history retrieved successfully"));
//    }
//
//    @Test
//    public void testGetTransactionHistory_NoAccountsFound() throws Exception {
//        mockMvc.perform(get("/api/wallet/transactions?userId=999&page=0&size=10")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isForbidden());
//    }
//
//    @Test
//    public void testTransfer_HappyPath() throws Exception {
//        String jsonRequest = "{ \"amount\": 50.0, \"destinationAccountNumber\": \"160520250110\", \"currency\": \"USD\" }";
//
//        mockMvc.perform(post("/api/v1/wallet/transfer?userId=3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Transfer successful"));
//    }
//
//    @Test
//    public void testTransfer_InsufficientBalance() throws Exception {
//        String jsonRequest = "{ \"amount\": 100050.0, \"destinationAccountNumber\": \"160520250110\", \"currency\": \"USD\" }";
//
//        mockMvc.perform(post("/api/wallet/transfer?userId=3")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(jsonRequest))
//                .andExpect(status().isForbidden());
//    }
//
//}
