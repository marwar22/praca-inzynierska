CREATE SEQUENCE IF NOT EXISTS application_user_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS knockout_bracket_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS match_in_knockout_bracket_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS match_result_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS match_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS tournament_group_seq START WITH 1 INCREMENT BY 50;

CREATE SEQUENCE IF NOT EXISTS tournament_seq START WITH 1 INCREMENT BY 50;

CREATE TABLE application_user
(
    id                BIGINT       NOT NULL,
    username          VARCHAR(20)  NOT NULL,
    first_name        VARCHAR(255) NOT NULL,
    last_name         VARCHAR(255) NOT NULL,
    email             VARCHAR(255) NOT NULL,
    password          VARCHAR(255),
    last_login        TIMESTAMP WITHOUT TIME ZONE,
    created_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_application_user PRIMARY KEY (id)
);

CREATE TABLE application_user_role
(
    application_user_id BIGINT       NOT NULL,
    role                VARCHAR(255) NOT NULL
);

CREATE TABLE knockout_bracket
(
    id                BIGINT NOT NULL,
    number_of_players BIGINT,
    CONSTRAINT pk_knockout_bracket PRIMARY KEY (id)
);

CREATE TABLE match
(
    id                  BIGINT NOT NULL,
    match_result_id     BIGINT,
    first_player_id     BIGINT,
    second_player_id    BIGINT,
    tournament_id       BIGINT NOT NULL,
    last_modified_by_id BIGINT,
    created_date_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_match PRIMARY KEY (id)
);

CREATE TABLE match_in_knockout_bracket
(
    id                                BIGINT NOT NULL,
    match_id                          BIGINT NOT NULL,
    stage                             BIGINT NOT NULL,
    bracket_position                  BIGINT,
    next_match_in_knockout_bracket_id BIGINT,
    knockout_bracket_id               BIGINT,
    CONSTRAINT pk_match_in_knockout_bracket PRIMARY KEY (id)
);

CREATE TABLE match_result
(
    id        BIGINT  NOT NULL,
    winner_id BIGINT  NOT NULL,
    walkover  BOOLEAN NOT NULL,
    scratch   BOOLEAN NOT NULL,
    CONSTRAINT pk_match_result PRIMARY KEY (id)
);

CREATE TABLE match_result_played_set_result
(
    match_result_id     BIGINT  NOT NULL,
    first_player_score  SMALLINT,
    second_player_score SMALLINT,
    set_number          INTEGER NOT NULL,
    CONSTRAINT pk_match_result_played_set_result PRIMARY KEY (match_result_id, set_number)
);

CREATE TABLE match_result_set_result
(
    match_result_id     BIGINT  NOT NULL,
    first_player_score  SMALLINT,
    second_player_score SMALLINT,
    set_number          INTEGER NOT NULL,
    CONSTRAINT pk_match_result_set_result PRIMARY KEY (match_result_id, set_number)
);

CREATE TABLE tournament
(
    id                  BIGINT       NOT NULL,
    name                VARCHAR(255) NOT NULL,
    number_of_players   BIGINT       NOT NULL,
    number_of_groups    BIGINT       NOT NULL,
    sets_to_win         BIGINT       NOT NULL,
    knockout_bracket_id BIGINT,
    start_date          TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date            TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    organizer_id        BIGINT       NOT NULL,
    created_date_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_date_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_tournament PRIMARY KEY (id)
);

CREATE TABLE tournament_group
(
    id            BIGINT NOT NULL,
    tournament_id BIGINT,
    group_number  INTEGER,
    CONSTRAINT pk_tournament_group PRIMARY KEY (id)
);

CREATE TABLE tournament_group_match
(
    match_id            BIGINT NOT NULL,
    tournament_group_id BIGINT NOT NULL
);

CREATE TABLE tournament_group_player
(
    player_id           BIGINT NOT NULL,
    tournament_group_id BIGINT NOT NULL
);

CREATE TABLE tournament_player
(
    player_id     BIGINT NOT NULL,
    tournament_id BIGINT NOT NULL
);

ALTER TABLE application_user
    ADD CONSTRAINT uc_application_user_email UNIQUE (email);

ALTER TABLE application_user
    ADD CONSTRAINT uc_application_user_username UNIQUE (username);

ALTER TABLE match_in_knockout_bracket
    ADD CONSTRAINT uc_match_in_knockout_bracket_match UNIQUE (match_id);

ALTER TABLE match_in_knockout_bracket
    ADD CONSTRAINT uc_match_in_knockout_bracket_next_match_in_knockout_bracket UNIQUE (next_match_in_knockout_bracket_id);

ALTER TABLE match
    ADD CONSTRAINT uc_match_match_result UNIQUE (match_result_id);

ALTER TABLE tournament_group_match
    ADD CONSTRAINT uc_tournament_group_match_match UNIQUE (match_id);

ALTER TABLE tournament
    ADD CONSTRAINT uc_tournament_knockout_bracket UNIQUE (knockout_bracket_id);

ALTER TABLE match_in_knockout_bracket
    ADD CONSTRAINT FK_MATCH_IN_KNOCKOUT_BRACKET_ON_KNOCKOUT_BRACKET FOREIGN KEY (knockout_bracket_id) REFERENCES knockout_bracket (id);

ALTER TABLE match_in_knockout_bracket
    ADD CONSTRAINT FK_MATCH_IN_KNOCKOUT_BRACKET_ON_MATCH FOREIGN KEY (match_id) REFERENCES match (id);

ALTER TABLE match_in_knockout_bracket
    ADD CONSTRAINT FK_MATCH_IN_KNOCKOUT_BRACKET_ON_NEXT_MATCH_IN_KNOCKOUT_BRACKET FOREIGN KEY (next_match_in_knockout_bracket_id) REFERENCES match_in_knockout_bracket (id);

ALTER TABLE match
    ADD CONSTRAINT FK_MATCH_ON_FIRST_PLAYER FOREIGN KEY (first_player_id) REFERENCES application_user (id);

ALTER TABLE match
    ADD CONSTRAINT FK_MATCH_ON_LAST_MODIFIED_BY FOREIGN KEY (last_modified_by_id) REFERENCES application_user (id);

ALTER TABLE match
    ADD CONSTRAINT FK_MATCH_ON_MATCH_RESULT FOREIGN KEY (match_result_id) REFERENCES match_result (id);

ALTER TABLE match
    ADD CONSTRAINT FK_MATCH_ON_SECOND_PLAYER FOREIGN KEY (second_player_id) REFERENCES application_user (id);

ALTER TABLE match
    ADD CONSTRAINT FK_MATCH_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE match_result
    ADD CONSTRAINT FK_MATCH_RESULT_ON_WINNER FOREIGN KEY (winner_id) REFERENCES application_user (id);

ALTER TABLE tournament_group
    ADD CONSTRAINT FK_TOURNAMENT_GROUP_ON_TOURNAMENT FOREIGN KEY (tournament_id) REFERENCES tournament (id);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_KNOCKOUT_BRACKET FOREIGN KEY (knockout_bracket_id) REFERENCES knockout_bracket (id);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_ORGANIZER FOREIGN KEY (organizer_id) REFERENCES application_user (id);

ALTER TABLE application_user_role
    ADD CONSTRAINT fk_application_user_role_on_application_user FOREIGN KEY (application_user_id) REFERENCES application_user (id);

ALTER TABLE match_result_played_set_result
    ADD CONSTRAINT fk_match_result_played_set_result_on_match_result FOREIGN KEY (match_result_id) REFERENCES match_result (id);

ALTER TABLE match_result_set_result
    ADD CONSTRAINT fk_match_result_set_result_on_match_result FOREIGN KEY (match_result_id) REFERENCES match_result (id);

ALTER TABLE tournament_group_match
    ADD CONSTRAINT fk_tougromat_on_match FOREIGN KEY (match_id) REFERENCES match (id);

ALTER TABLE tournament_group_match
    ADD CONSTRAINT fk_tougromat_on_tournament_group FOREIGN KEY (tournament_group_id) REFERENCES tournament_group (id);

ALTER TABLE tournament_group_player
    ADD CONSTRAINT fk_tougropla_on_application_user FOREIGN KEY (player_id) REFERENCES application_user (id);

ALTER TABLE tournament_group_player
    ADD CONSTRAINT fk_tougropla_on_tournament_group FOREIGN KEY (tournament_group_id) REFERENCES tournament_group (id);

ALTER TABLE tournament_player
    ADD CONSTRAINT fk_toupla_on_application_user FOREIGN KEY (player_id) REFERENCES application_user (id);

ALTER TABLE tournament_player
    ADD CONSTRAINT fk_toupla_on_tournament FOREIGN KEY (tournament_id) REFERENCES tournament (id);