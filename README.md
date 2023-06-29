
# InsideSoftwaresSecurityCommons

Projeto por manter as configurações, tratamento e padrões da Autenticação e Validação de acesso de todos os projetos que importa.
* Versão disponivel: 1.0-SNAPSHOT

## Framework Utilizado

* [Spring Boot]('https://spring.io/projects/spring-boot')
  * Versão: 3.1.1
* [Java]('https://www.java.com/pt-BR/')
  * Versão: 17 ou superior

## Usado pelos projetos

Esse projeto é usado pelas seguintes projetos:


## Build do projeto

  * Realizar o clone do projeto
  * Na pasta do clone rodar o seguinte comando ``` mvn clean install ``` projeto ira buildar

## Utilização em demais projetos

  * Obrigatorio importa no projeto.
  ```
  <dependency>
      <groupId>br.com.insidesoftwares.securitycommons</groupId>
      <artifactId>security</artifactId>
      <version>1.0-SNAPSHOT</version>
  </dependency>
  ```

  * Importa no pom do projeto Spring para integração de segurança com Azure Active Directory:
  ```
  <dependency>
      <groupId>br.com.insidesoftwares.securitycommons</groupId>
      <artifactId>azure-active-directory</artifactId>
      <version>1.0-SNAPSHOT</version>
  </dependency>
  ```
  *  Importa no pom do projeto Spring para integração de segurança com Keycloak
  ```
  <dependency>
      <groupId>br.com.insidesoftwares.securitycommons</groupId>
      <artifactId>keycloak</artifactId>
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
    @EnableMethodSecurity
    @ComponentScan(basePackages = {
        "br.com.insidesoftwares.authenticator.controller"
    })
    @RequiredArgsConstructor
    public class SecurityConfiguration {}
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