package com.kosa.moimeasy.transaction;

import com.kosa.moimeasy.common.exception.NotExistMoeimAccountException;
import com.kosa.moimeasy.moeim.entity.Moeim;
import com.kosa.moimeasy.moeim.repository.MoeimRepository;
import com.kosa.moimeasy.transaction.dto.GetTransactionByCategoryResponseDTO;
import com.kosa.moimeasy.transaction.dto.GetTransactionResponseDTO;
import com.kosa.moimeasy.transaction.entity.Transaction;
import com.kosa.moimeasy.transaction.repository.TransactionRepository;
import com.kosa.moimeasy.transaction.service.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private MoeimRepository moeimRepository;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registTransactionInfo_savesTransaction() {
        // Given
        Transaction transaction = Transaction.builder()
                .content("Test Content")
                .transactionType(1)
                .depositAmount(1000.0)
                .withdrawalAmount(0.0)
                .build();

        // When
        transactionService.registTransactionInfo(transaction);

        // Then
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void findByMoeimId_returnsTransactions() {
        // Given
        Long moeimId = 1L;
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder().content("Transaction 1").build(),
                Transaction.builder().content("Transaction 2").build()
        );
        when(transactionRepository.findByMoeim_Id(moeimId)).thenReturn(transactions);

        // When
        List<Transaction> result = transactionService.findByMoeimId(moeimId);

        // Then
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findByMoeim_Id(moeimId);
    }

    @Test
    void findTransactionOne_returnsCorrectCategory() {
        // Given
        Long moeimId = 1L;
        Moeim moeim = new Moeim();
        moeim.setMoeimId(moeimId);
        moeim.setAccountNumber("1234567890");
        moeim.setBalance(1000);

        Transaction transaction = Transaction.builder()
                .categoryName("음료")
                .build();

        when(moeimRepository.findAccountNumberById(moeimId)).thenReturn(Optional.of(moeim));
        when(transactionRepository.findFirstByMoeim(moeim)).thenReturn(transaction);

        // When
        int category = transactionService.findTransactionOne(moeimId);

        // Then
        assertEquals(2, category); // "음료" 카테고리 번호는 2
    }


    @Test
    void findTransactionOne_throwsExceptionIfNoMoeim() {
        // Given
        Long moeimId = 1L;
        when(moeimRepository.findAccountNumberById(moeimId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(NotExistMoeimAccountException.class, () -> transactionService.findTransactionOne(moeimId));
    }

    @Test
    void getTransactionList_returnsPagedTransactions() {
        // Given
        Long moeimId = 1L;
        int idx = 0;
        List<Transaction> transactions = Arrays.asList(
                Transaction.builder().content("Transaction 1").build(),
                Transaction.builder().content("Transaction 2").build()
        );
        Page<Transaction> page = new PageImpl<>(transactions);
        when(transactionRepository.findByMoeim(eq(moeimId), any(Pageable.class))).thenReturn(page);

        // When
        List<GetTransactionResponseDTO> result = transactionService.getTransactionList(moeimId, idx);

        // Then
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findByMoeim(eq(moeimId), any(Pageable.class));
    }

    @Test
    void getTransactionListByCategory_returnsStatistics() {
        // Given
        Long moeimId = 1L;
        int year = 2024;
        int month = 12;

        Transaction transaction1 = Transaction.builder()
                .categoryName("음료")
                .withdrawalAmount(500)
                .build();

        Transaction transaction2 = Transaction.builder()
                .categoryName("회식")
                .withdrawalAmount(1000)
                .build();

        when(transactionRepository.findByMoeimAndYearAndMonth(eq(moeimId), eq(year), eq(month)))
                .thenReturn(Arrays.asList(transaction1, transaction2));

        // When
        List<GetTransactionByCategoryResponseDTO> result = transactionService.getTransactionListByCategory(moeimId, year, month);

        // Then
        assertEquals(2, result.size());
        assertEquals("회식", result.get(0).getCategory());
        assertEquals(66.7, result.get(0).getRate(), 0.1); // 1000/1500 = 66.7%
        verify(transactionRepository, times(1)).findByMoeimAndYearAndMonth(eq(moeimId), eq(year), eq(month));
    }
}
