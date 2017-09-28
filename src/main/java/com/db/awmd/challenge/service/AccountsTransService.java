package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsTrans;

import lombok.Synchronized;

@Service
public class AccountsTransService {

@Autowired
private AccountsTrans accountTrans;

public boolean transferAmount(Account fromAcc, Account toAcc,BigDecimal amountToTransfer) {
	if(fromAcc.getBalance().compareTo(amountToTransfer)>0
			&& amountToTransfer.compareTo(BigDecimal.ZERO)>0){
		return this.accountTrans.transferAmount(fromAcc, toAcc, amountToTransfer);
	}
	return false;
}

}
