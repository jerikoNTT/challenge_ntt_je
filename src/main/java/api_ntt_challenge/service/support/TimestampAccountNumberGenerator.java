package api_ntt_challenge.service.support;

import org.springframework.stereotype.Component;

@Component
public class TimestampAccountNumberGenerator implements AccountNumberGenerator {

    @Override
    public String generate() {
        return "ACC-" + System.currentTimeMillis();
    }
}
