ALTER TABLE ranking_for_knockout_stage_participation
    ADD stage INTEGER;

ALTER TABLE tournament_scoring
    RENAME COLUMN id TO tournament_id;

ALTER TABLE ranking_for_knockout_stage_participation
    ADD CONSTRAINT pk_ranking_for_knockout_stage_participation PRIMARY KEY (tournament_id, stage);