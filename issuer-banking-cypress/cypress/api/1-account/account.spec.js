describe("The Account Api", () => {
  const env = Cypress.env();
  it("givenAccountIdWhenBalanceThenResultIsOk", () => {
    cy.request({
      method: "GET",
      url: `/v1/accounts/${env.account.accountId}`,
    }).then((res) => {
      expect(res.body).to.not.be.null;
      expect(res).to.have.property("status", parseInt(`${env.httpStatus.ok}`));
      expect(res.body.balance).equal(parseFloat(`${env.account.balance}`));
    });
  });

  it("givenNotExistingAccountIdWhenBalanceThenResultNotFoundException", () => {
    cy.request({
      method: "GET",
      url: `/v1/accounts/${env.account.notExistingAccountId}`,
      failOnStatusCode: Cypress.config().failOnStatusCode,
    })
      .its("body")
      .should("include", {
        code: env.error.code.entityNotFound,
        reason: env.error.reason.entityNotFound,
        message: `${env.error.message.entityNotFound}`.replace(
          "%s",
          `${env.account.notExistingAccountId}`
        ),
      });
  });
});
