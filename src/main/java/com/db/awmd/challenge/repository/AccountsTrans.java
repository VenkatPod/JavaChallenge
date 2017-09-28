package com.db.awmd.challenge.repository;

import java.math.BigDecimal;

import com.db.awmd.challenge.domain.Account;


public interface AccountsTrans {
	
	 boolean transferAmount(Account from, Account to, BigDecimal amount);


}
