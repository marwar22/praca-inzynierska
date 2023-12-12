CREATE SEQUENCE IF NOT EXISTS tournament_scoring_seq START WITH 1 INCREMENT BY 50;

ALTER TABLE tournament_scoring
    RENAME COLUMN tournament_id TO id;

ALTER TABLE tournament
    ADD tournament_scoring_id BIGINT;

UPDATE tournament
    SET tournament_scoring_id = tournament_scoring.id
FROM tournament tournament2 INNER JOIN tournament_scoring
    ON tournament2.id = tournament_scoring.id;



ALTER TABLE tournament
    ALTER COLUMN tournament_scoring_id SET NOT NULL;

ALTER TABLE tournament
    ADD CONSTRAINT uc_tournament_tournament_scoring UNIQUE (tournament_scoring_id);

ALTER TABLE tournament
    ADD CONSTRAINT FK_TOURNAMENT_ON_TOURNAMENT_SCORING FOREIGN KEY (tournament_scoring_id) REFERENCES tournament_scoring (id);