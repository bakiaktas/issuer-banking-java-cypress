describe("The Transaction Api", () => {
  const failOnStatusCode = Cypress.config().failOnStatusCode;
  const env = Cypress.env();
  const reqDto = env.transaction.reqDto;

  it("givenTransactionReqDtoForPaymentWhenTransactionResDtoThenResultIsOk", () => {
    cy.request({
      method: "POST",
      url: "/v1/transactions/",
      body: reqDto,
      failOnStatusCode,
    }).then((res) => {
      expect(res.body).to.not.be.null;
      expect(res).to.have.property("status", parseInt(`${env.httpStatus.ok}`));
      expect(res.body).to.have.property("transactionId");
    });
  });

  it("givenTransactionReqDtoWithNotExistingAccountIdWhenTransactionResDtoThenResultNotFoundException", () => {
    cy.request({
      method: "POST",
      url: "/v1/transactions/",
      body: { ...reqDto, accountId: env.account.notExistingAccountId },
      failOnStatusCode,
    })
      .its("body")
      .should("include", {
        code: `${env.error.code.entityNotFound}`,
        reason: `${env.error.reason.entityNotFound}`,
        message: `${env.error.message.entityNotFound}`.replace(
          "%s",
          `${env.account.notExistingAccountId}`
        ),
      });
  });

  it("givenTransactionReqDtoWithNotPositiveAmountWhenTransactionResDtoThenResultNotValidException", () => {
    cy.request({
      method: "POST",
      url: "/v1/transactions/",
      body: { ...reqDto, amount: 0 },
      failOnStatusCode,
    })
      .its("body")
      .should("include", {
        code: `${env.error.code.paymentAmountNotValid}`,
        reason: `${env.error.reason.notValid}`,
        message: `${env.error.message.paymentAmountNotValid}`,
      });
  });

  it("givenTransactionReqDtoWithNotEmptyTransactionIdWhenTransactionResDtoThenResultNotValidException", () => {
    cy.request({
      method: "POST",
      url: "/v1/transactions/",
      body: { ...reqDto, transactionId: "xyz" },
      failOnStatusCode,
    })
      .its("body")
      .should("include", {
        code: `${env.error.code.paymentTransactionIdNotValid}`,
        reason: `${env.error.reason.notValid}`,
        message: `${env.error.message.paymentTransactionIdNotValid}`,
      });
  });
});
