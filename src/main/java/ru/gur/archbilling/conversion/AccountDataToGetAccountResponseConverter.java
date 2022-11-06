package ru.gur.archbilling.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.gur.archbilling.service.data.AccountData;
import ru.gur.archbilling.web.account.response.GetAccountResponse;

@Component
public class AccountDataToGetAccountResponseConverter implements Converter<AccountData, GetAccountResponse> {

    @Override
    public GetAccountResponse convert(final AccountData source) {
        return GetAccountResponse.builder()
                .id(source.getId())
                .balance(source.getBalance())
                .created(source.getCreated())
                .updated(source.getUpdated())
                .build();
    }
}
