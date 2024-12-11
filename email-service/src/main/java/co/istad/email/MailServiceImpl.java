package co.istad.email;

import co.istad.email.config.MailServerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final MailServerConfig mailServerConfig;

    @Override
    public MailResponse send(MailRequest<?> mailRequest) {
        mailServerConfig.send(mailRequest);
        return new MailResponse(
                mailRequest.to(),
                mailRequest.cc(),
                mailRequest.subject()
        );
    }
}
