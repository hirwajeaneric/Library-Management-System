-- Create the user_borrow_summary view
CREATE VIEW user_borrow_summary AS
SELECT
    u.id AS user_id,
    u.username,
    COUNT(br.id) AS borrowed_count
FROM users u
         LEFT JOIN borrow_records br
                   ON u.id = br.user_id
                       AND br.status = 'BORROWED'
GROUP BY u.id, u.username;

-- Create the overdue_books view
CREATE VIEW overdue_books AS
SELECT
    br.id AS record_id,
    u.id AS user_id,
    u.username,
    b.id AS book_id,
    b.title,
    br.due_date
FROM borrow_records br
         JOIN users u ON br.user_id = u.id
         JOIN books b ON br.book_id = b.id
WHERE br.status = 'BORROWED'
  AND br.due_date < CURRENT_TIMESTAMP;