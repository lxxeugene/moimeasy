//package com.kosa.moimeasy.transaction;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.kosa.moimeasy.moeim.entity.Moeim;
//import com.kosa.moimeasy.transaction.controller.TransactionControllerSample;
//import com.kosa.moimeasy.transaction.dto.GetTransactionResponseDTO;
//import com.kosa.moimeasy.transaction.dto.TransferRequestDTO;
//import com.kosa.moimeasy.transaction.entity.TransactionSample;
//import com.kosa.moimeasy.transaction.service.TransactionServiceSamle;
//import com.kosa.moimeasy.transaction.service.TransferService;
//import com.kosa.moimeasy.user.entity.User;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.doThrow;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//class TransactionSampleControllerTest {
//
//    private MockMvc mockMvc;
//
//    @Mock
//    private TransactionServiceSamle transactionService;
//
//    @Mock
//    private TransferService transferService;
//
//    @InjectMocks
//    private TransactionControllerSample transactionControllerSample;
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    // Mocked Moeim and User objects
//    private Moeim mockMoeim;
//    private User mockUser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        mockMvc = MockMvcBuilders.standaloneSetup(transactionControllerSample).build();
//
//        // Initialize mock User
//        mockUser = new User();
//        mockUser.setUserId(1L);
//        mockUser.setUserName("Mock User");
//        // 다른 필요한 필드들 설정
//        // 예: mockUser.setEmail("mockuser@example.com");
//        // 필요한 경우 다른 필드도 설정
//
//        // Initialize mock Moeim
//        mockMoeim = new Moeim();
//        mockMoeim.setMoeimId(10L);
//        mockMoeim.setUser(mockUser);
//        mockMoeim.setMoeimName("Mock Moeim");
//        mockMoeim.setMoeimCode("123456");
//        mockMoeim.setAccountNumber("123-456-7890");
//        mockMoeim.setBalance(10000.0);
//        // 다른 필요한 필드들 설정
//    }
//
//    @Test
//    @DisplayName("POST /api/v1/transaction/pay 정상적으로 이체가 되는 경우")
//    void testExecuteTransferSuccess() throws Exception {
//        TransferRequestDTO requestDTO = new TransferRequestDTO();
//        requestDTO.setUserId(mockUser.getUserId());
//        requestDTO.setMoeimId(mockMoeim.getMoeimId());
//        requestDTO.setTransferAmount(1000.0);
//        // 필요한 필드 값들 설정
//        // 예: requestDTO.setContent("Payment for services");
//        // requestDTO.setCategoryName("Services");
//
//        // 정상적인 케이스: 예외 없음
//        mockMvc.perform(post("/api/v1/transaction/pay")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true));
//    }
//
//    @Test
//    @DisplayName("POST /api/v1/transaction/pay 예외 발생 시 failResponse 반환")
//    void testExecuteTransferFail() throws Exception {
//        TransferRequestDTO requestDTO = new TransferRequestDTO();
//        requestDTO.setUserId(mockUser.getUserId());
//        requestDTO.setMoeimId(mockMoeim.getMoeimId());
//        requestDTO.setTransferAmount(1000.0);
//        // 필요한 필드 값들 설정
//
//        // TransferService에서 예외 발생을 mocking
//        doThrow(new RuntimeException("Test Exception"))
//                .when(transferService).executeTransfer(any(TransferRequestDTO.class));
//
//        mockMvc.perform(post("/api/v1/transaction/pay")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDTO)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(false));
//    }
//
//    @Test
//    @DisplayName("GET /api/v1/transaction/{moeimId} 모임 아이디로 거래내역 조회")
//    void testGetAllTransaction() throws Exception {
//        Long moeimId = mockMoeim.getMoeimId();
//
//        // Mock Transaction 데이터
//        TransactionSample t1 = TransactionSample.builder()
//                .id(1L)
//                .moeim(mockMoeim)
//                .user(mockUser)
//                .transferAmount(500.0)
//                .depositAmount(1000.0)
//                .withdrawalAmount(0.0)
//                .content("Deposit Transaction 1")
//                .transactionType(1)
//                .categoryName("Food")
//                .build();
//
//        TransactionSample t2 = TransactionSample.builder()
//                .id(2L)
//                .moeim(mockMoeim)
//                .user(mockUser)
//                .transferAmount(300.0)
//                .depositAmount(0.0)
//                .withdrawalAmount(300.0)
//                .content("Withdrawal Transaction 1")
//                .transactionType(0)
//                .categoryName("Travel")
//                .build();
//
//        when(transactionService.findByMoeimId(moeimId)).thenReturn(List.of(t1, t2));
//
//        mockMvc.perform(get("/api/v1/transaction/{moeimId}", moeimId))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data[0].id").value(t1.getId()))
//                .andExpect(jsonPath("$.data[0].content").value(t1.getContent()))
//                .andExpect(jsonPath("$.data[1].id").value(t2.getId()))
//                .andExpect(jsonPath("$.data[1].content").value(t2.getContent()));
//    }
//
//    @Test
//    @DisplayName("GET /api/v1/transaction/{moeimId}/{idx} 거래내역 페이징 조회")
//    void testGetTransactionList() throws Exception {
//        Long moeimId = mockMoeim.getMoeimId();
//        int idx = 1;
//
//        // Mock ResponseDTO 데이터
//        GetTransactionResponseDTO responseDTO = new GetTransactionResponseDTO();
//        responseDTO.setId(1L);
//        responseDTO.setContent("Deposit Transaction 1");
//        responseDTO.setCategoryName("Food");
//        // 필요한 필드 값 설정
//
//        when(transactionService.getTransactionList(moeimId, idx)).thenReturn(List.of(responseDTO));
//
//        mockMvc.perform(get("/api/v1/transaction/{moeimId}/{idx}", moeimId, idx))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data[0].id").value(responseDTO.getId()))
//                .andExpect(jsonPath("$.data[0].content").value(responseDTO.getContent()));
//    }
//
//    @Test
//    @DisplayName("GET /api/v1/transaction/category/{moeimId}?year={year}&month={month} 카테고리별 소비내역 조회")
//    void testGetTransactionListByCategory() throws Exception {
//        Long moeimId = mockMoeim.getMoeimId();
//        int year = 2023;
//        int month = 12;
//
//        // Mock Transaction 데이터
//        TransactionSample t1 = TransactionSample.builder()
//                .id(1L)
//                .moeim(mockMoeim)
//                .user(mockUser)
//                .transferAmount(1500.0)
//                .depositAmount(0.0)
//                .withdrawalAmount(1500.0)
//                .content("Travel Expense")
//                .transactionType(0)
//                .categoryName("Travel")
//                .build();
//
//        TransactionSample t2 = TransactionSample.builder()
//                .id(2L)
//                .moeim(mockMoeim)
//                .user(mockUser)
//                .transferAmount(2000.0)
//                .depositAmount(2000.0)
//                .withdrawalAmount(0.0)
//                .content("Food Expense")
//                .transactionType(1)
//                .categoryName("Food")
//                .build();
//
//        when(transactionService.getTransactionListByCategory(moeimId, year, month))
//                .thenReturn(List.of(t1, t2));
//
//        mockMvc.perform(get("/api/v1/transaction/category/{moeimId}", moeimId)
//                        .param("year", String.valueOf(year))
//                        .param("month", String.valueOf(month)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.success").value(true))
//                .andExpect(jsonPath("$.data").isArray())
//                .andExpect(jsonPath("$.data[0].categoryName").value("Travel"))
//                .andExpect(jsonPath("$.data[1].categoryName").value("Food"));
//    }
//}
