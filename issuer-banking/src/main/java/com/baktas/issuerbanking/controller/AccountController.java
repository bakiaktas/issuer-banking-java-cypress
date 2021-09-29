package com.baktas.issuerbanking.controller;

import com.baktas.issuerbanking.model.dto.AccountResDto;
import com.baktas.issuerbanking.service.account.AccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/accounts")
@RequiredArgsConstructor
@Tag(name = "Account", description = "The Account API")
public class AccountController {
    private final AccountService accountService;

    @GetMapping(value = "/{accountId}", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
    public AccountResDto getBalance(@PathVariable long accountId) {
        return AccountResDto.builder().balance(accountService.findById(accountId).getBalance()).build();
    }
}