package com.db.awmd.challenge.repository;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;

import lombok.Synchronized;

@Component
@Scope(value = "singleton", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AccountTransInMemory implements AccountsTrans {

	@Autowired
	AccountsService accountService;

	@Override
	@Synchronized
	public boolean transferAmount(Account from, Account to, BigDecimal amount) {
		to.setBalance(to.getBalance().add(amount));
		from.setBalance(from.getBalance().subtract(amount));
		return true;
	}

}
