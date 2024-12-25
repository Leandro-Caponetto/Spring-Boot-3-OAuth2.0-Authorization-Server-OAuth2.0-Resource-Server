# Spring Boot 3 OAuth2.0 Authorization Server

Este proyecto es una implementación de un **Authorization Server** basado en **OAuth2.0** utilizando Spring Boot 3. Incluye soporte para **OpenID Connect (OIDC)** y validación de tokens JWT.

## Características

- Configuración de un Authorization Server compatible con OAuth2.0.
- Soporte para OpenID Connect (OIDC).
- Validación de tokens JWT.
- Redirección personalizada para la autenticación.

## Requisitos Previos

1. **Java 17** o superior.
2. **Maven** o **Gradle** para la construcción del proyecto.
3. **Spring Boot 3.x**.

## Configuración

### Dependencias

Incluye las siguientes dependencias en el archivo `pom.xml` o `build.gradle`:

#### Maven

```xml
<dependencies>
    <!-- Spring Security OAuth2 Authorization Server -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-authorization-server</artifactId>
    </dependency>

    <!-- JWT Support -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
    </dependency>

    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
</dependencies>
```
#### Gradel

```text
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-authorization-server'
    implementation 'org.springframework.boot:spring-boot-starter-oauth2-resource-server'
    implementation 'org.springframework.boot:spring-boot-starter-web'
}

```

#### Configuration

```java
package com.tutorial.authorization_server.config;

@Configuration
@Slf4j
public class AuthorizationSecurityConfig {

    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http)
            throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
                .with(authorizationServerConfigurer, (authorizationServer) ->
                        authorizationServer
                                .oidc(Customizer.withDefaults())
                )
                .authorizeHttpRequests((authorize) ->
                        authorize
                                .anyRequest().authenticated()
                )
                // Redirect to the login page when not authenticated from the
                // authorization endpoint
                .exceptionHandling((exceptions) -> exceptions
                        .defaultAuthenticationEntryPointFor(
                                new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
                        )
                );

        return http.build();
    }
    
}

```

#### application.properties

```properties
server.port=9000
logging.level.org.springframework.security=TRACE
logging.level.org.springframework.security.oauth2=TRACE

```

#### Esta versión incluye el endpoint solicitado como parte de la sección **Endpoints Disponibles**.

````url
http://localhost:9000/.well-known/oauth-authorization-server

````
````json
{
"issuer": "http://localhost:9000",
"authorization_endpoint": "http://localhost:9000/oauth2/authorize",
"device_authorization_endpoint": "http://localhost:9000/oauth2/device_authorization",
"token_endpoint": "http://localhost:9000/oauth2/token",
"token_endpoint_auth_methods_supported": [
"client_secret_basic",
"client_secret_post",
"client_secret_jwt",
"private_key_jwt",
"tls_client_auth",
"self_signed_tls_client_auth"
],
"jwks_uri": "http://localhost:9000/oauth2/jwks",
"response_types_supported": [
"code"
],
"grant_types_supported": [
"authorization_code",
"client_credentials",
"refresh_token",
"urn:ietf:params:oauth:grant-type:device_code",
"urn:ietf:params:oauth:grant-type:token-exchange"
],
"revocation_endpoint": "http://localhost:9000/oauth2/revoke",
"revocation_endpoint_auth_methods_supported": [
"client_secret_basic",
"client_secret_post",
"client_secret_jwt",
"private_key_jwt",
"tls_client_auth",
"self_signed_tls_client_auth"
],
"introspection_endpoint": "http://localhost:9000/oauth2/introspect",
"introspection_endpoint_auth_methods_supported": [
"client_secret_basic",
"client_secret_post",
"client_secret_jwt",
"private_key_jwt",
"tls_client_auth",
"self_signed_tls_client_auth"
],
"code_challenge_methods_supported": [
"S256"
],
"tls_client_certificate_bound_access_tokens": true
}
````

#### url OAuth Debugger

````url
https://oauthdebugger.com/

````

# OAuth 2.0 Debugger

Este archivo describe cómo utilizar el **OAuth 2.0 Debugger** para probar y depurar solicitudes OAuth 2.0. Esta herramienta es ideal para validar flujos de autorización, generación de tokens y configuración de PKCE.

---

## Información Básica

- **Authorize URI**:  
  `http://localhost:9000/oauth2/authorize`

- **Redirect URI**:  
  `https://oauthdebugger.com/debug`

- **Client ID**:  
  `client`

- **Scope**:  
  `openid`

- **State**:  
  `ow7busqivt`

- **Nonce**:  
  `6z1yptr8y1`

---

## Parámetros para el Flujo de Código de Autorización (Authorization Code Flow)

1. **Response Type**:  
   `code`

2. **Use PKCE?**:
    - Método: `SHA-256`
    - **Code Verifier**:  
      `ZICJ0OS2xhNmXqhQOjpW6dBkEqfrolcOgiDxHMOWCTb`
    - **Code Challenge**:  
      `KdsuVpFHDQwDY_vpsJC-Rfz8Ae5LN1HKTmmvhh1uo0E`

3. **Token URI**:  
   `http://localhost:9000/oauth2/token`

4. **Response Mode**:  
   `form_post`

---

## Ejemplo de URL Construida para Autorizar

```plaintext
http://localhost:9000/oauth2/authorize
?client_id=client
&redirect_uri=https://oauthdebugger.com/debug
&scope=openid
&response_type=code
&response_mode=form_post
&code_challenge_method=S256
&code_challenge=KdsuVpFHDQwDY_vpsJC-Rfz8Ae5LN1HKTmmvhh1uo0E
&state=ow7busqivt
&nonce=6z1yptr8y1
````

