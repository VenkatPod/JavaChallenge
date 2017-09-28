package com.db.awmd.challenge;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.regex.Matcher;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.NotValidTransferAmountException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;
import com.db.awmd.challenge.web.AccountsTransController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AccountsTransControllerTest {

	private MockMvc mockMvc;

	@Mock
	private AccountsService accountsService;

	@Mock
	private NotificationService notifyService;

	@Autowired
	@InjectMocks
	private AccountsTransController transController;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Rule
	public ExpectedException exp = ExpectedException.none();

	Account mockFromAcc;

	Account mockToAcc;

	@Before
	public void setup() {
		mockFromAcc = new Account("Id-123", new BigDecimal("1000"));
		when(accountsService.getAccount("Id-123")).thenReturn(mockFromAcc);
		mockToAcc = new Account("Id-124", new BigDecimal("100"));
		when(accountsService.getAccount("Id-124")).thenReturn(mockToAcc);
		this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
	}

	@Test
	public void transferAmountTest() throws Exception {

		this.mockMvc
				.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON).content(
						"{\"fromAccountId\":\"Id-123\",\"toAccountId\":\"Id-124\",\"amountTransfer\":\"100.00\"}"))
				.andExpect(status().isOk());
		assertThat(mockFromAcc.getBalance().compareTo(BigDecimal.valueOf(900)) == 0);
		assertThat(mockToAcc.getBalance().compareTo(BigDecimal.valueOf(200)) == 0);
		verify(notifyService, times(2)).notifyAboutTransfer(any(Account.class), any(String.class));
	}

	@Test
	public void transferAmount_NegativeAMT_Test() throws Exception {

		this.mockMvc
				.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON).content(
						"{\"fromAccountId\":\"Id-123\",\"toAccountId\":\"Id-124\",\"amountTransfer\":-100.00}"))
				.andExpect(status().isBadRequest());
		verify(notifyService, times(0)).notifyAboutTransfer(any(Account.class), any(String.class));
	}

	@Test
	public void transferAmount_LessFromAccount_Test() throws Exception {
		try {
			this.mockMvc.perform(post("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON)
					.content("{\"fromAccountId\":\"Id-123\",\"toAccountId\":\"Id-124\",\"amountTransfer\":2000.00}"));
		} catch (NestedServletException e) {
			assertThat(e.getMessage().equalsIgnoreCase("Doesn't have enough funds in your account"));
		}
	}

}
