package com.db.awmd.challenge.web;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.TransferRequest;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.NotValidTransferAmountException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.AccountsTransService;
import com.db.awmd.challenge.service.NotificationService;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1/accounts/transfer")
@Slf4j
public class AccountsTransController {

	private AccountsService accountsService;

	@Autowired
	private AccountsTransService accountsTransService;

	private NotificationService notifyService;

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public void accountTransfer(@RequestBody @Valid TransferRequest trasReq) {
		Account fromAccount = this.accountsService.getAccount(trasReq.getFromAccountId());
		if (fromAccount == null) {
			throw new AccountNotFoundException("Account From which amount needs to be transfered not found");
		}

		Account toAccount = this.accountsService.getAccount(trasReq.getToAccountId());
		if (toAccount == null) {
			throw new AccountNotFoundException("Account to wich amount needs to be transfered not found");
		}
		if (accountsTransService.transferAmount(fromAccount, toAccount, trasReq.getAmountTransfer())) {
			notifyService.notifyAboutTransfer(fromAccount,
					"Amount - " + String.valueOf(trasReq.getAmountTransfer().longValue()) + " ,Transfered to AccNo:" + toAccount.getAccountId());
			notifyService.notifyAboutTransfer(toAccount,
					"Amount - " + String.valueOf(trasReq.getAmountTransfer().longValue()) + " ,Transfered from AccNo:" + fromAccount.getAccountId());
		}else
			throw new NotValidTransferAmountException("Doesn't have enough funds in your account");

	}

	@Autowired
	public void setAccountsService(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	@Autowired
	public void setNotifyService(NotificationService notifyService) {
		this.notifyService = notifyService;
	}
}
