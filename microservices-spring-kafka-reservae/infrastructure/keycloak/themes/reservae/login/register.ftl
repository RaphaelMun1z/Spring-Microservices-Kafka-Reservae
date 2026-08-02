<#import "template.ftl" as layout>

<@layout.registrationLayout displayMessage=false displayInfo=false; section>
    <#if section = "header">
        Reservae - Criar conta
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

                    <a href="${appBaseUrl}/eventos" class="reservae-navbar-item">
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
                    <a href="${url.loginUrl}" class="reservae-register-pill">
                        Entrar
                    </a>
                </div>
            </header>

            <main class="reservae-main reservae-register-main">
                <div class="reservae-bg-glow reservae-bg-glow-one" aria-hidden="true"></div>
                <div class="reservae-bg-glow reservae-bg-glow-two" aria-hidden="true"></div>

                <section class="reservae-register-card">
                    <div class="reservae-card-header">
                        <h1>Crie sua Conta</h1>
                    </div>

                    <#if message?has_content>
                        <div class="reservae-alert" role="alert" aria-live="polite">
                            ${kcSanitize(message.summary)?no_esc}
                        </div>
                    </#if>

                    <form id="kc-register-form" class="reservae-register-form" action="${url.registrationAction}"
                          method="post">
                        <div class="reservae-field reservae-field-full">
                            <label for="firstName">Nome</label>
                            <input
                                    id="firstName"
                                    name="firstName"
                                    type="text"
                                    value="${(register.formData.firstName!'')}"
                                    placeholder="Nome"
                                    autocomplete="given-name"
                                    aria-invalid="<#if messagesPerField.existsError('firstName')>true<#else>false</#if>"
                            />

                            <#if messagesPerField.existsError("firstName")>
                                <span class="reservae-field-error">
                                    ${kcSanitize(messagesPerField.get("firstName"))?no_esc}
                                </span>
                            </#if>
                        </div>

                        <div class="reservae-field reservae-field-full">
                            <label for="lastName">Sobrenome</label>
                            <input
                                    id="lastName"
                                    name="lastName"
                                    type="text"
                                    value="${(register.formData.lastName!'')}"
                                    placeholder="Sobrenome"
                                    autocomplete="family-name"
                                    aria-invalid="<#if messagesPerField.existsError('lastName')>true<#else>false</#if>"
                            />

                            <#if messagesPerField.existsError("lastName")>
                                <span class="reservae-field-error">
                                    ${kcSanitize(messagesPerField.get("lastName"))?no_esc}
                                </span>
                            </#if>
                        </div>

                        <#if !realm.registrationEmailAsUsername>
                            <div class="reservae-field reservae-field-full">
                                <label for="username">Usuário</label>
                                <input
                                        id="username"
                                        name="username"
                                        type="text"
                                        value="${(register.formData.username!'')}"
                                        placeholder="Nome de usuário"
                                        autocomplete="username"
                                        aria-invalid="<#if messagesPerField.existsError('username')>true<#else>false</#if>"
                                />

                                <#if messagesPerField.existsError("username")>
                                    <span class="reservae-field-error">
                                        ${kcSanitize(messagesPerField.get("username"))?no_esc}
                                    </span>
                                </#if>
                            </div>
                        </#if>

                        <div class="reservae-field">
                            <label for="email">E-mail</label>
                            <input
                                    id="email"
                                    name="email"
                                    type="email"
                                    value="${(register.formData.email!'')}"
                                    placeholder="seu@email.com"
                                    autocomplete="email"
                                    aria-invalid="<#if messagesPerField.existsError('email')>true<#else>false</#if>"
                            />

                            <#if messagesPerField.existsError("email")>
                                <span class="reservae-field-error">
                                    ${kcSanitize(messagesPerField.get("email"))?no_esc}
                                </span>
                            </#if>
                        </div>

                        <div class="reservae-field">
                            <label for="user.attributes.document">CPF</label>
                            <input
                                    id="user.attributes.document"
                                    name="user.attributes.document"
                                    type="text"
                                    value="${(register.formData['user.attributes.document']!'')}"
                                    placeholder="000.000.000-00"
                                    inputmode="numeric"
                                    autocomplete="off"
                            />
                        </div>

                        <#if passwordRequired??>
                            <div class="reservae-field">
                                <label for="password">Senha</label>
                                <input
                                        id="password"
                                        name="password"
                                        type="password"
                                        placeholder="••••••••"
                                        autocomplete="new-password"
                                        aria-invalid="<#if messagesPerField.existsError('password', 'password-confirm')>true<#else>false</#if>"
                                />

                                <#if messagesPerField.existsError("password")>
                                    <span class="reservae-field-error">
                                        ${kcSanitize(messagesPerField.get("password"))?no_esc}
                                    </span>
                                </#if>
                            </div>

                            <div class="reservae-field">
                                <label for="password-confirm">Confirmar senha</label>
                                <input
                                        id="password-confirm"
                                        name="password-confirm"
                                        type="password"
                                        placeholder="••••••••"
                                        autocomplete="new-password"
                                        aria-invalid="<#if messagesPerField.existsError('password-confirm')>true<#else>false</#if>"
                                />

                                <#if messagesPerField.existsError("password-confirm")>
                                    <span class="reservae-field-error">
                                        ${kcSanitize(messagesPerField.get("password-confirm"))?no_esc}
                                    </span>
                                </#if>
                            </div>
                        </#if>

                        <button id="kc-register" type="submit" class="reservae-submit reservae-field-full">
                            Finalizar Cadastro
                        </button>
                    </form>

                    <div class="reservae-registration">
                        <span>Já possui uma conta?</span>
                        <a href="${url.loginUrl}" class="reservae-register-link">
                            Fazer login
                        </a>
                    </div>
                </section>
            </main>
        </div>
    </#if>
</@layout.registrationLayout>