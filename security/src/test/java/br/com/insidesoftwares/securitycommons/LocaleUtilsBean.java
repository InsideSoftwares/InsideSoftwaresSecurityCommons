package br.com.insidesoftwares.securitycommons;

import br.com.insidesoftwares.commons.specification.LocaleService;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class LocaleUtilsBean implements LocaleService {

    @Override
    public Locale getLocale(){
        return LocaleContextHolder.getLocale(LocaleContextHolder.getLocaleContext());
    }

    @Override
    public String getMessage(String code, Object... args){
        return "Message";
    }

}