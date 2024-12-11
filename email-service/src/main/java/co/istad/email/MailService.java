package co.istad.email;

public interface MailService {

    MailResponse send(MailRequest<?> mailRequest);

}
