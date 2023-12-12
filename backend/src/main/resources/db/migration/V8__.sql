ALTER TABLE ranking_for_knockout_stage_participation
    RENAME TO rating_for_knockout_stage_participation;

ALTER TABLE rating_for_knockout_stage_participation
    RENAME COLUMN tournament_id TO tournament_scoring_id;

ALTER TABLE rating_for_knockout_stage_participation
    RENAME COLUMN ranking TO rating;

ALTER TABLE tournament_scoring
    RENAME COLUMN ranking_for_match_win TO rating_for_match_win;

ALTER TABLE tournament_scoring
    RENAME COLUMN ranking_for_match_loss TO rating_for_match_loss;

ALTER TABLE tournament_scoring
    RENAME COLUMN ranking_for_match_walkover TO rating_for_match_walkover;

ALTER TABLE tournament_scoring
    RENAME COLUMN ranking_for_tournament_participation TO rating_for_tournament_participation;

ALTER TABLE tournament_scoring
    RENAME COLUMN ranking_for_tournament_win TO rating_for_tournament_win;