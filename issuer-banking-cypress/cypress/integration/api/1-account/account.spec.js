describe("The Account Api", () => {
  it("givenAccountIdWhenBalanceThenResultIsOk", () => {
    cy.request({
      method: "GET",
      url: `/v1/accounts/${Cypress.config().env.account.accountId}`,
    }).then((res) => {
      expect(res).to.have.property("status", 200);
      expect(res.body).to.not.be.null;
      expect(res.body).to.have.property("balance", 456.45);
    });
  });

  it("givenNotExistingAccountIdWhenBalanceThenResultNotFoundException", () => {
    cy.request({
      method: "GET",
      url: "/v1/accounts/1",
      failOnStatusCode: false,
    })
      .its("body")
      .should("include", {
        code: "1002",
        reason: "ENTITY_NOT_FOUND",
        message: "Account not found with by id: 1",
      });
  });
});
