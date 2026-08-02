<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=!messagesPerField.existsError("username", "password") displayInfo=false; section>
    <#if section = "header">
        Reservae - Entrar
    <#elseif section = "form">
        <div class="reservae-page">
            <header class="reservae-navbar">
                <div class="reservae-navbar-logo-area">
                    <#assign appBaseUrl = "http://localhost:4200">

                    <#if client?? && client.baseUrl?? && client.baseUrl?has_content>
                        <#assign appBaseUrl = client.baseUrl>
                    </#if>

                    <a href="${appBaseUrl}/inicio" class="reservae-logo-link" aria-label="Voltar ao Reservae">
                        <img src="${url.resourcesPath}/img/logo.png" alt="Reservae" class="reservae-logo-img">
                    </a>
                </div>

                <nav class="reservae-navbar-menu" aria-label="Menu principal">
                    <#assign appBaseUrl = "http://localhost:4200">

                    <#if client?? && client.baseUrl?? && client.baseUrl?has_content>
                        <#assign appBaseUrl = client.baseUrl>
                    </#if>

                    <a href="${appBaseUrl}/inicio" class="reservae-navbar-item is-active">
                        <svg viewBox="0 0 24 24" aria-hidden="true">
                            <path d="M3 11.5 12 4l9 7.5"
                                  fill="none"
                                  stroke="currentColor"
                                  stroke-width="2"
                                  stroke-linecap="round"
                                  stroke-linejoin="round"/>
                            <path d="M6.5 10.5V20h11v-9.5"
                                  fill="none"
                                  stroke="currentColor"
                                  stroke-width="2"
                                  stroke-linejoin="round"/>
                        </svg>
                        <span>Início</span>
                    </a>

                    <a href="${appBaseUrl}/shows" class="reservae-navbar-item">
                        <svg viewBox="0 0 24 24" aria-hidden="true">
                            <path d="M4 8a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v2a2 2 0 0 0 0 4v2a2 2 0 0 1-2 2H6a2 2 0 0 1-2-2v-2a2 2 0 0 0 0-4V8Z"
                                  fill="none"
                                  stroke="currentColor"
                                  stroke-width="2"
                                  stroke-linejoin="round"/>
                            <path d="M9 8v8M15 8v8"
                                  stroke="currentColor"
                                  stroke-width="1.8"
                                  stroke-linecap="round"/>
                        </svg>
                        <span>Eventos</span>
                    </a>

                    <a href="${appBaseUrl}/club-vip" class="reservae-navbar-item">
                        <svg viewBox="0 0 24 24" aria-hidden="true">
                            <path d="M5 5h14v14M5 5v14"
                                  fill="none"
                                  stroke="currentColor"
                                  stroke-width="2"
                                  stroke-linecap="round"
                                  stroke-linejoin="round"/>
                            <path d="M9 9h6M9 13h4"
                                  stroke="currentColor"
                                  stroke-width="1.8"
                                  stroke-linecap="round"/>
                        </svg>
                        <span>Club VIP</span>
                    </a>

                    <a href="${appBaseUrl}/suporte" class="reservae-navbar-item">
                        <svg viewBox="0 0 24 24" aria-hidden="true">
                            <path d="M5 6.5A2.5 2.5 0 0 1 7.5 4h9A2.5 2.5 0 0 1 19 6.5v7A2.5 2.5 0 0 1 16.5 16H10l-4 4v-4.2A2.5 2.5 0 0 1 5 13.5v-7Z"
                                  fill="none"
                                  stroke="currentColor"
                                  stroke-width="2"
                                  stroke-linejoin="round"/>
                        </svg>
                        <span>Suporte</span>
                    </a>
                </nav>

                <div class="reservae-navbar-actions">
                    <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                        <a href="${url.registrationUrl}" class="reservae-register-pill">
                            Registrar-se
                        </a>
                    <#else>
                        <a href="${appBaseUrl}/inicio" class="reservae-register-pill">
                            Registrar-se
                        </a>
                    </#if>
                </div>
            </header>

            <main class="reservae-main">
                <div class="reservae-bg-glow reservae-bg-glow-one" aria-hidden="true"></div>
                <div class="reservae-bg-glow reservae-bg-glow-two" aria-hidden="true"></div>

                <section class="reservae-card">
                    <div class="reservae-card-header">
                        <h1>Bem-vindo de volta</h1>
                    </div>

                    <#if messagesPerField.existsError("username", "password")>
                        <div class="reservae-alert" role="alert" aria-live="polite">
                            ${kcSanitize(messagesPerField.getFirstError("username", "password"))?no_esc}
                        </div>
                    </#if>

                    <form id="kc-form-login" class="reservae-form" action="${url.loginAction}" method="post">
                        <#if !usernameHidden??>
                            <div class="reservae-field">
                                <label for="username">
                                    <#if !realm.loginWithEmailAllowed>
                                        Usuário
                                    <#elseif !realm.registrationEmailAsUsername>
                                        Usuário ou e-mail
                                    <#else>
                                        E-mail
                                    </#if>
                                </label>

                                <div class="reservae-input-container">
                                    <svg class="reservae-field-icon" viewBox="0 0 24 24" aria-hidden="true">
                                        <path d="M4 6.5h16v11H4zM4.5 7l7.5 6 7.5-6"
                                              fill="none"
                                              stroke="currentColor"
                                              stroke-width="2"
                                              stroke-linecap="round"
                                              stroke-linejoin="round"/>
                                    </svg>

                                    <input
                                            id="username"
                                            name="username"
                                            type="text"
                                            value="${(login.username!'')}"
                                            placeholder="seu@email.com"
                                            autocomplete="username"
                                            autofocus
                                            required
                                            aria-invalid="<#if messagesPerField.existsError('username', 'password')>true<#else>false</#if>"
                                    />
                                </div>
                            </div>
                        </#if>

                        <div class="reservae-field">
                            <label for="password">Senha</label>

                            <div class="reservae-input-container">
                                <svg class="reservae-field-icon" viewBox="0 0 24 24" aria-hidden="true">
                                    <rect x="5" y="10" width="14" height="10" rx="2"
                                          fill="none"
                                          stroke="currentColor"
                                          stroke-width="2"/>
                                    <path d="M8 10V7a4 4 0 0 1 8 0v3"
                                          fill="none"
                                          stroke="currentColor"
                                          stroke-width="2"
                                          stroke-linecap="round"/>
                                </svg>

                                <input
                                        id="password"
                                        name="password"
                                        type="password"
                                        placeholder="••••••••"
                                        autocomplete="current-password"
                                        required
                                        aria-invalid="<#if messagesPerField.existsError('username', 'password')>true<#else>false</#if>"
                                />

                                <button
                                        class="reservae-password-toggle"
                                        type="button"
                                        data-password-toggle
                                        aria-label="Mostrar senha"
                                        aria-controls="password"
                                >
                                    <svg class="reservae-eye-icon" data-eye-visible viewBox="0 0 24 24"
                                         aria-hidden="true">
                                        <path d="M2.5 12s3.5-6 9.5-6 9.5 6 9.5 6-3.5 6-9.5 6-9.5-6-9.5-6Z"
                                              fill="none"
                                              stroke="currentColor"
                                              stroke-width="2"/>
                                        <circle cx="12" cy="12" r="2.5"
                                                fill="none"
                                                stroke="currentColor"
                                                stroke-width="2"/>
                                    </svg>

                                    <svg class="reservae-eye-icon is-hidden" data-eye-hidden viewBox="0 0 24 24"
                                         aria-hidden="true">
                                        <path d="M3 3l18 18M10.6 6.2A9.8 9.8 0 0 1 12 6c6 0 9.5 6 9.5 6a16 16 0 0 1-3.1 3.7M6.1 6.1C3.7 7.8 2.5 12 2.5 12S6 18 12 18c1.2 0 2.3-.2 3.3-.6"
                                              fill="none"
                                              stroke="currentColor"
                                              stroke-width="2"
                                              stroke-linecap="round"/>
                                    </svg>
                                </button>
                            </div>
                        </div>

                        <div class="reservae-options">
                            <#if realm.rememberMe && !usernameHidden??>
                                <label class="reservae-remember">
                                    <input id="rememberMe" name="rememberMe" type="checkbox"
                                           <#if login.rememberMe??>checked</#if> />
                                    <span>Lembrar-me</span>
                                </label>
                            <#else>
                                <span></span>
                            </#if>

                            <#if realm.resetPasswordAllowed>
                                <a href="${url.loginResetCredentialsUrl}" class="reservae-link">
                                    Esqueci minha senha
                                </a>
                            </#if>
                        </div>

                        <button id="kc-login" name="login" type="submit" class="reservae-submit">
                            Entrar
                        </button>
                    </form>

                    <#if realm.password && realm.registrationAllowed && !registrationDisabled??>
                        <div class="reservae-registration">
                            <span>Ainda não tem conta?</span>
                            <a href="${url.registrationUrl}" class="reservae-register-link">
                                Criar conta
                            </a>
                        </div>
                    </#if>
                </section>
            </main>
        </div>
    </#if>
</@layout.registrationLayout>