package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.AccountsTransService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsTransTest {
	
	  @Autowired
	  private AccountsTransService accountsTransService;
	  
	  @Mock
	  private Account mockAcc;
	  
	  @Test
	  public void transferMoney_Mock()throws Exception{
		  when(mockAcc.getAccountId()).thenReturn("1");
		  when(mockAcc.getBalance()).thenReturn(new BigDecimal(1000));
		  assertThat(this.accountsTransService.transferAmount(mockAcc,mockAcc,new BigDecimal(100))).isTrue();
	  }
	  
	  @Test
	  public void transferMoney_failFromAccBalLess()throws Exception{
		  when(mockAcc.getAccountId()).thenReturn("1");
		  when(mockAcc.getBalance()).thenReturn(new BigDecimal(99));
		  assertThat(this.accountsTransService.transferAmount(mockAcc,mockAcc,new BigDecimal(100))).isFalse();
	  }
	  
	  @Test
	  public void transferMoney_failNegativeBal()throws Exception{
		  when(mockAcc.getAccountId()).thenReturn("1");
		  when(mockAcc.getBalance()).thenReturn(new BigDecimal(99));
		  assertThat(this.accountsTransService.transferAmount(mockAcc,mockAcc,new BigDecimal(-1))).isFalse();
	  }
	
	

}
