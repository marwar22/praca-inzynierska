CREATE TABLE ranking_for_knockout_stage_participation
(
    tournament_id BIGINT NOT NULL,
    ranking       SMALLINT
);

CREATE TABLE tournament_scoring
(
    id                                   BIGINT   NOT NULL,
    group_points_for_win                 SMALLINT NOT NULL,
    group_points_for_loss                SMALLINT NOT NULL,
    group_points_for_walkover            SMALLINT NOT NULL,
    ranking_for_tournament_participation SMALLINT NOT NULL,
    ranking_for_match_win                SMALLINT NOT NULL,
    ranking_for_match_loss               SMALLINT NOT NULL,
    ranking_for_match_walkover           SMALLINT NOT NULL,
    ranking_for_tournament_win           SMALLINT NOT NULL,
    CONSTRAINT pk_tournament_scoring PRIMARY KEY (id)
);

ALTER TABLE tournament_scoring
    ADD CONSTRAINT FK_TOURNAMENT_SCORING_ON_ID FOREIGN KEY (id) REFERENCES tournament (id);

ALTER TABLE ranking_for_knockout_stage_participation
    ADD CONSTRAINT fk_rankingforknockoutstageparticipation_on_tournamentscoring FOREIGN KEY (tournament_id) REFERENCES tournament_scoring (id);