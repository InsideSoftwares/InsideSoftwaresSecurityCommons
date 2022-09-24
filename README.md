
# InsideSoftwaresSecurityCommons


Projeto por manter as configurações, tratamento e padrões da Autenticação e Validação de acesso de todos os projetos que importa.
* Versão disponivel: 1.0-SNAPSHOT

## Framework Utilizado

* [Spring Boot]('https://spring.io/projects/spring-boot')
  * Versão: 2.7.3
* [Java]('https://www.java.com/pt-BR/')
  * Versão: 17 ou superior

## Usado pelos projetos

Esse projeto é usado pelas seguintes projetos:

- [InsideSoftwaresAuthenticator]('https://github.com/InsideSoftwares/InsideSoftwaresAuthenticator') 
- [InsideSoftwaresAccessControl]('https://github.com/InsideSoftwares/access_control_back')

## Build do projeto

  * Realizar o clone do projeto
  * Na pasta do clone rodar o seguinte comando ``` mvn clean install ``` projeto ira buildar

## Utilização em demais projetos

  * Importa no pom do projeto Spring: 
  ```
    <dependency>
        <groupId>br.com.insidesoftwares</groupId>
        <artifactId>securitycommons</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
  ```

  * Será necessário criar uma configuração(@Bean) no projeto para carregar as mensagens dos erros, como mostrado logo abaixo:
  ```
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(
                "classpath:messages_security",
        );
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(10);
        messageSource.setFallbackToSystemLocale(false);
        return messageSource;
    }
  ```
  * Será necessário também criar uma classe de configuração para ativar as regras e acesso autenticado da aplicação:
  ```
    @Configuration
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    @ComponentScan(basePackages = {
            "br.com.insidesoftwares.authenticator.controller"
    })
    @RequiredArgsConstructor
    public class SecurityConfiguration {

        private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;
        private final AuthenticationFilter authenticationFilter;
        private final CorsFilter corsFilter;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return WebSecurityConfig.securityFilterChain(
                    httpSecurity, accessDeniedExceptionHandler, authenticationFilter, corsFilter
            );
        }

        @Bean
        public GrantedAuthorityDefaults grantedAuthorityDefaults() {
            return WebSecurityConfig.grantedAuthorityDefaults();
        }

    }
  ```
  * Ao importa e ativar e obrigatorio a configuração do redis para o funcionamento da autenticação no Projeto:
  ```
    spring:
    data:
        redis:
        repositories:
            enabled: false
    redis:
        port: 26379
        passowrd: '{cipher}5e954244f702180bb7165fdbf3d89aa7779da7ffa541934a2474dd6844d72065' #Senha Criptografada
        sentinel:
        database: 0
        master: mymaster
        nodes: localhost
        passowrd: '{cipher}5e954244f702180bb7165fdbf3d89aa7779da7ffa541934a2474dd6844d72065' #Senha Criptografada

    jwt:
    secret: '{cipher}ce6f091a2b60096ead2c6968e3ec634fd87e863b82e69b3bcc35a4f314a9cf4fc5cc249e6b1a80c9ff6a2e988b8593757f0bdd43cdea45687d433b955033b6bce8bdca1e1fe859f376fff307f7fda93f9368741e259ad2defb1672b1e4fb4017c35f71da3aa177818f760582327a26ee' #Segredo Criptografado
    valid: 60 #Sets token validity time in minutes 
  ``` 
  * As configuração de cors não são obrigatorias, porém para alterar o comportamento default da aplicação necessário realizar a configuração abaixo no properties:
  ```
    cors-security:
      allowOrigin: '*'
      allowMethods: 'GET,POST,DELETE,PUT'
      allowHeaders: '*'
      allowCredentials: 'true' 
      maxAge: '1800' 
  ``` 