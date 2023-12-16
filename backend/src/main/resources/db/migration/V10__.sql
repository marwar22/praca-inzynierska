CREATE TABLE password_reset_token
(
    id                  BIGINT                      NOT NULL,
    application_user_id BIGINT                      NOT NULL,
    token               VARCHAR(255)                NOT NULL,
    expiration_date     TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_password_reset_token PRIMARY KEY (id)
);

ALTER TABLE password_reset_token
    ADD CONSTRAINT uc_password_reset_token_application_user UNIQUE (application_user_id);

ALTER TABLE password_reset_token
    ADD CONSTRAINT FK_PASSWORD_RESET_TOKEN_ON_APPLICATION_USER FOREIGN KEY (application_user_id) REFERENCES application_user (id);