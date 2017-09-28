package com.db.awmd.challenge.domain;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TransferRequest {
	@NotNull
	@NotEmpty
	private final String fromAccountId;

	@NotNull
	@NotEmpty
	private final String toAccountId;

	@NotNull
	@Min(value = 0, message = "Initial balance must be positive.")
	private final BigDecimal amountTransfer;

	@JsonCreator
	public TransferRequest(@JsonProperty("fromAccountId") String fromAccountId,
			@JsonProperty("toAccountId") String toAccountId,
			@JsonProperty("amountTransfer") @NotEmpty BigDecimal amountTransfer) {
		this.fromAccountId = fromAccountId;
		this.toAccountId = toAccountId;
		this.amountTransfer = amountTransfer;
	}

	public BigDecimal getAmountTransfer() {
		return amountTransfer;
	}

	public String getFromAccountId() {
		return fromAccountId;
	}

	public String getToAccountId() {
		return toAccountId;
	}
}
