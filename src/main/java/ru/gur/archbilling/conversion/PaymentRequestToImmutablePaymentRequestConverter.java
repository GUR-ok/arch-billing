package ru.gur.archbilling.conversion;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.gur.archbilling.service.immutable.ImmutablePaymentRequest;
import ru.gur.archbilling.web.account.request.PaymentRequest;

@Component
public class PaymentRequestToImmutablePaymentRequestConverter implements Converter<PaymentRequest, ImmutablePaymentRequest> {

    @Override
    public ImmutablePaymentRequest convert(final PaymentRequest source) {
        return ImmutablePaymentRequest.builder()
                .id(source.getId())
                .amount(source.getAmount())
                .build();
    }
}
