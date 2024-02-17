package org.efa.backend.model.business.implementations;

import lombok.extern.slf4j.Slf4j;
import org.efa.backend.model.Mail;
import org.efa.backend.model.business.exceptions.BusinessException;
import org.efa.backend.model.business.exceptions.FoundException;
import org.efa.backend.model.business.exceptions.NotFoundException;
import org.efa.backend.model.business.interfaces.IMailBusiness;
import org.efa.backend.model.persistence.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MailBusiness implements IMailBusiness {

    @Autowired
    private MailRepository mailRepository;

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {

        log.info("Enviando mail subject={} a: {}",subject, to);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@mugiwaras.com.ar");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        try {
            emailSender.send(message);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public void sendSimpleMessageToAll(Float temperatura) {

        List<Mail> list = mailRepository.findAll();

        for (Mail mail : list) {
            sendSimpleMessage(mail.getMail(), "Temperatura!", "Temperatura umbral superada: " + temperatura);
        }
    }

    @Override
    public Mail load(long id) throws NotFoundException, BusinessException {
        Optional<Mail> r;
        try {
            r = mailRepository.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
        if (r.isEmpty()) {
            throw NotFoundException.builder().message("No se encuentra la orden con id=" + id).build();
        }
        return r.get();
    }

    @Override
    public List<Mail> list() throws BusinessException {
        try {
            return mailRepository.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public Mail add(Mail mail) throws FoundException, BusinessException {
        try {
            load(mail.getId());
            throw FoundException.builder().message("Se encontró el mail con id=" + mail.getId()).build();
        } catch (NotFoundException e) {
        }

        try {
            return mailRepository.save(mail);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }

    @Override
    public void delete(long id) throws NotFoundException, BusinessException {
        load(id);
        try {
            mailRepository.deleteById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw BusinessException.builder().ex(e).build();
        }
    }
}
