DELIMITER //

CREATE TRIGGER user_after_delete
AFTER DELETE ON user
FOR EACH ROW
BEGIN
    INSERT INTO user_deleted (
        `id`,
        `nick_name`,
        `pass_word`,
        `dorm_id`,
        `provider`,
        `role`,
        `email`,
        `image_name`
    ) VALUES (
        OLD.`id`,
        OLD.`nick_name`,
        OLD.`pass_word`,
        OLD.`dorm_id`,
        OLD.`provider`,
        OLD.`role`,
        OLD.`email`,
        OLD.`image_name`
    );
END//

DELIMITER ;


-- user의 데이터가 삭제될 때 user_deleted에 동일한 데이터를 삽입.
-- 25.04.07